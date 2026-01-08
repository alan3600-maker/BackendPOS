package py.com.hidraulica.caacupe.service;

import py.com.hidraulica.caacupe.domain.*;
import py.com.hidraulica.caacupe.domain.enums.EstadoPresupuesto;
import py.com.hidraulica.caacupe.domain.enums.TipoItem;
import py.com.hidraulica.caacupe.dto.PresupuestoRequest;
import py.com.hidraulica.caacupe.dto.VentaRequest;
import py.com.hidraulica.caacupe.exception.BusinessException;
import py.com.hidraulica.caacupe.exception.NotFoundException;
import py.com.hidraulica.caacupe.repository.PresupuestoRepository;
import py.com.hidraulica.caacupe.repository.ClienteRepository;
import py.com.hidraulica.caacupe.repository.ProductoRepository;
import py.com.hidraulica.caacupe.repository.ServicioRepository;
import py.com.hidraulica.caacupe.repository.spec.PresupuestoSpecs;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
public class PresupuestoService {

  private final PresupuestoRepository repo;
  private final ClienteRepository clienteRepo;
  private final ProductoRepository productoRepo;
  private final ServicioRepository servicioRepo;
  private final VentaService ventaService;

  public PresupuestoService(PresupuestoRepository repo,
                            ClienteRepository clienteRepo,
                            ProductoRepository productoRepo,
                            ServicioRepository servicioRepo,
                            VentaService ventaService) {
    this.repo = repo;
    this.clienteRepo = clienteRepo;
    this.productoRepo = productoRepo;
    this.servicioRepo = servicioRepo;
    this.ventaService = ventaService;
  }

  public Presupuesto get(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Presupuesto no encontrado: " + id));
  }

  public java.util.List<Presupuesto> list() {
    return repo.findAll();
  }

  public Page<Presupuesto> search(Long clienteId, EstadoPresupuesto estado, OffsetDateTime desde, OffsetDateTime hasta, Pageable pageable) {
    Specification<Presupuesto> spec = Specification.where(PresupuestoSpecs.clienteId(clienteId))
        .and(PresupuestoSpecs.estado(estado))
        .and(PresupuestoSpecs.fechaDesde(desde))
        .and(PresupuestoSpecs.fechaHasta(hasta));
    return repo.findAll(spec, pageable);
  }

  @Transactional
  public Presupuesto create(PresupuestoRequest req) {
    Presupuesto p = new Presupuesto();
    var cliente = clienteRepo.findById(req.clienteId)
        .orElseThrow(() -> new NotFoundException("Cliente no encontrado: " + req.clienteId));
    p.setCliente(cliente);
    p.setFecha(req.fecha != null ? req.fecha : OffsetDateTime.now());
    p.setObservacion(req.observacion);
    p.setEstado(EstadoPresupuesto.BORRADOR);

    if (req.items == null || req.items.isEmpty()) throw new BusinessException("El presupuesto debe tener items.");

    BigDecimal total = BigDecimal.ZERO;

    for (var it : req.items) {
      PresupuestoItem item = new PresupuestoItem();
      item.setPresupuesto(p);
      item.setTipo(it.tipo);

      if (it.tipo == TipoItem.PRODUCTO) {
        if (it.productoId == null) throw new BusinessException("PRODUCTO requiere productoId.");
        var prod = productoRepo.findById(it.productoId).orElseThrow(() -> new NotFoundException("Producto no encontrado: " + it.productoId));
        item.setProducto(prod);
        item.setDescripcion(it.descripcion != null && !it.descripcion.isBlank() ? it.descripcion : prod.getDescripcion());
      } else {
        if (it.servicioId == null) throw new BusinessException("SERVICIO requiere servicioId.");
        var srv = servicioRepo.findById(it.servicioId).orElseThrow(() -> new NotFoundException("Servicio no encontrado: " + it.servicioId));
        item.setServicio(srv);
        item.setDescripcion(it.descripcion != null && !it.descripcion.isBlank() ? it.descripcion : srv.getDescripcion());
      }

      item.setCantidad(it.cantidad);
      item.setPrecioUnitario(it.precioUnitario);
      var totalLinea = it.precioUnitario.multiply(it.cantidad);
      item.setTotalLinea(totalLinea);

      p.getItems().add(item);
      total = total.add(totalLinea);
    }

    p.setTotal(total);
    return repo.save(p);
  }

  @Transactional
  public Presupuesto update(Long id, PresupuestoRequest req) {
    Presupuesto p = get(id);
    if (p.getEstado() != EstadoPresupuesto.BORRADOR) throw new BusinessException("Solo se puede editar un presupuesto en BORRADOR.");

    p.getItems().clear();
    var cliente = clienteRepo.findById(req.clienteId)
        .orElseThrow(() -> new NotFoundException("Cliente no encontrado: " + req.clienteId));
    p.setCliente(cliente);
    p.setFecha(req.fecha != null ? req.fecha : p.getFecha());
    p.setObservacion(req.observacion);

    BigDecimal total = BigDecimal.ZERO;

    for (var it : req.items) {
      PresupuestoItem item = new PresupuestoItem();
      item.setPresupuesto(p);
      item.setTipo(it.tipo);

      if (it.tipo == TipoItem.PRODUCTO) {
        if (it.productoId == null) throw new BusinessException("PRODUCTO requiere productoId.");
        var prod = productoRepo.findById(it.productoId).orElseThrow(() -> new NotFoundException("Producto no encontrado: " + it.productoId));
        item.setProducto(prod);
        item.setDescripcion(it.descripcion != null && !it.descripcion.isBlank() ? it.descripcion : prod.getDescripcion());
      } else {
        if (it.servicioId == null) throw new BusinessException("SERVICIO requiere servicioId.");
        var srv = servicioRepo.findById(it.servicioId).orElseThrow(() -> new NotFoundException("Servicio no encontrado: " + it.servicioId));
        item.setServicio(srv);
        item.setDescripcion(it.descripcion != null && !it.descripcion.isBlank() ? it.descripcion : srv.getDescripcion());
      }

      item.setCantidad(it.cantidad);
      item.setPrecioUnitario(it.precioUnitario);
      var totalLinea = it.precioUnitario.multiply(it.cantidad);
      item.setTotalLinea(totalLinea);

      p.getItems().add(item);
      total = total.add(totalLinea);
    }

    p.setTotal(total);
    return repo.save(p);
  }

  @Transactional
  public void delete(Long id) {
    Presupuesto p = get(id);
    if (p.getEstado() != EstadoPresupuesto.BORRADOR) throw new BusinessException("No se puede borrar un presupuesto que no sea BORRADOR.");
    repo.delete(p);
  }

  @Transactional
  public Presupuesto aprobar(Long id) {
    Presupuesto p = get(id);
    if (p.getEstado() != EstadoPresupuesto.BORRADOR) throw new BusinessException("Solo BORRADOR se puede aprobar.");
    p.setEstado(EstadoPresupuesto.APROBADO);
    return repo.save(p);
  }

  @Transactional
  public Venta convertirAVenta(Long id) {
    Presupuesto p = get(id);
    if (p.getEstado() == EstadoPresupuesto.CONVERTIDO) throw new BusinessException("Este presupuesto ya fue convertido.");
    if (p.getItems().isEmpty()) throw new BusinessException("Presupuesto sin items.");

    VentaRequest vr = new VentaRequest();
    vr.clienteId = p.getCliente().getId();
    vr.fecha = OffsetDateTime.now();
    vr.observacion = "Generado desde Presupuesto #" + p.getId();
    vr.items = new java.util.ArrayList<>();

    for (var it : p.getItems()) {
      VentaRequest.Item vi = new VentaRequest.Item();
      vi.tipo = it.getTipo();
      vi.descripcion = it.getDescripcion();
      vi.cantidad = it.getCantidad();
      vi.precioUnitario = it.getPrecioUnitario();
      if (it.getTipo() == TipoItem.PRODUCTO) {
        vi.productoId = it.getProducto() != null ? it.getProducto().getId() : null;
        // depositoId se completa al crear/editar la venta antes de confirmar
      } else {
        vi.servicioId = it.getServicio() != null ? it.getServicio().getId() : null;
      }
      vr.items.add(vi);
    }

    Venta venta = ventaService.create(vr);
    p.setEstado(EstadoPresupuesto.CONVERTIDO);
    repo.save(p);
    return venta;
  }
}
