package py.com.hidraulica.caacupe.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.com.hidraulica.caacupe.domain.Cliente;
import py.com.hidraulica.caacupe.domain.Factura;
import py.com.hidraulica.caacupe.domain.FacturaItem;
import py.com.hidraulica.caacupe.domain.Venta;
import py.com.hidraulica.caacupe.domain.enums.EstadoFactura;
import py.com.hidraulica.caacupe.domain.enums.EstadoVenta;
import py.com.hidraulica.caacupe.domain.enums.TipoFactura;
import py.com.hidraulica.caacupe.dto.FacturaDto;
import py.com.hidraulica.caacupe.dto.FacturaEmitirDesdeVentaRequest;
import py.com.hidraulica.caacupe.dto.PageResponse;
import py.com.hidraulica.caacupe.exception.BusinessException;
import py.com.hidraulica.caacupe.exception.NotFoundException;
import py.com.hidraulica.caacupe.repository.FacturaRepository;
import py.com.hidraulica.caacupe.repository.spec.FacturaSpecs;

@Service
public class FacturaService {

  private final FacturaRepository repo;
  private final VentaService ventaService;
  
  public FacturaService(FacturaRepository repo, VentaService ventaService) {
    this.repo = repo;
    this.ventaService = ventaService;
  }

  public Factura get(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Factura no encontrada: " + id));
  }

  public java.util.List<Factura> list() {
    return repo.findAll();
  }

  @Transactional(readOnly = true)
  public PageResponse<FacturaDto> searchDto(
      Long clienteId, TipoFactura tipo, EstadoFactura estado, String numero,
      OffsetDateTime desde, OffsetDateTime hasta,
      int page, int size, String sortBy, String dir
  ) {
    if (size <= 0) size = 10;
    if (page < 0) page = 0;

    var direction = "asc".equalsIgnoreCase(dir) ? Sort.Direction.ASC : Sort.Direction.DESC;
    var pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

    Specification<Factura> spec = Specification.where(FacturaSpecs.clienteId(clienteId))
        .and(FacturaSpecs.tipo(tipo))
        .and(FacturaSpecs.estado(estado))
        .and(FacturaSpecs.numeroLike(numero))
        .and(FacturaSpecs.fechaDesde(desde))
        .and(FacturaSpecs.fechaHasta(hasta));

    Page<Factura> p = repo.findAll(spec, pageable);
    var content = p.getContent().stream().map(this::toDto).toList();
    return new PageResponse<>(content, p.getTotalElements(), p.getTotalPages(), p.getNumber(), p.getSize());
  }

  @Transactional
  public Factura emitirDesdeVenta(Long ventaId, FacturaEmitirDesdeVentaRequest req) {
    Venta v = ventaService.get(ventaId);
    if (v.getEstado() != EstadoVenta.CONFIRMADA) {
      throw new BusinessException("Solo se puede facturar una venta CONFIRMADA.");
    }
    if (req == null || req.tipo == null) {
      throw new BusinessException("tipo de factura requerido.");
    }

    Factura f = new Factura();
    f.setTipo(req.tipo);
    f.setEstado(EstadoFactura.EMITIDA);
    f.setCliente(v.getCliente());
    f.setVenta(v);
    f.setFecha(OffsetDateTime.now());
    f.setSerie(req.serie);
    f.setNumero(req.numero);
    f.setTimbrado(req.timbrado);
    f.setTimbradoVencimiento(req.timbradoVencimiento);

    BigDecimal total = BigDecimal.ZERO;

    for (var vi : v.getItems()) {
      FacturaItem it = new FacturaItem();
      it.setFactura(f);
      it.setTipo(vi.getTipo());
      it.setProducto(vi.getProducto());
      it.setServicio(vi.getServicio());
      it.setDescripcion(vi.getDescripcion());
      it.setCantidad(vi.getCantidad());
      it.setPrecioUnitario(vi.getPrecioUnitario());
      it.setTotalLinea(vi.getTotalLinea());
      f.getItems().add(it);
      total = total.add(vi.getTotalLinea());
    }

    f.setTotal(total);

    // Guardar para obtener ID
    f = repo.save(f);

    // Numeración "base" (placeholder estable)
    if (f.getNumero() == null || f.getNumero().isBlank()) {
      String prefix = (req.tipo == TipoFactura.FISCAL) ? "F" : "NF";
      f.setNumero(prefix + "-" + f.getId());
      f = repo.save(f);
    }

    return f;
  }
  @Transactional
  public FacturaDto emitirDesdeVentaDto(Long ventaId, FacturaEmitirDesdeVentaRequest req) {
    Factura f = emitirDesdeVenta(ventaId, req); // tu método actual
    return toDto(f);
  }

  private FacturaDto toDto(Factura f) {
    Venta v = f.getVenta();          // si existe relación
    Cliente c = f.getCliente();      // o desde v.getCliente()

    Long clienteId = null;
    String clienteNombre = null;

    if (c != null) {
      clienteId = c.getId();
      clienteNombre = c.getNombreRazonSocial();
    } else if (v != null && v.getCliente() != null) {
      clienteId = v.getCliente().getId();
      clienteNombre = v.getCliente().getNombreRazonSocial();
    }

    return new FacturaDto(
        f.getId(),
        f.getNumero(),
        f.getTipo(),
        f.getEstado(),
        f.getFecha(),
        v != null ? v.getId() : null,
        clienteId,
        clienteNombre,
        f.getTotal()
    );
  }

  @Transactional
  public Factura anular(Long id) {
    Factura f = get(id);
    f.setEstado(EstadoFactura.ANULADA);
    return repo.save(f);
  }
  
  @Transactional(readOnly = true)
  public Factura getForPdf(Long id) {
    return repo.findForPdfById(id)
        .orElseThrow(() -> new NotFoundException("Factura no encontrada: " + id));
  }
  @Transactional(readOnly = true)
  public FacturaDto getDto(Long id) {
    var f = repo.findDetailById(id)
        .orElseThrow(() -> new NotFoundException("Factura no encontrada: " + id));
    return toDto(f);
  }

  @Transactional(readOnly = true)
  public List<FacturaDto> listDto() {
    return repo.findAll().stream().map(this::toDto).toList();
  }

  @Transactional
  public FacturaDto anularDto(Long id) {
    var f = anular(id);
    return toDto(f);
  }
}
