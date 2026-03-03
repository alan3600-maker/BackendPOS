package py.com.hidraulica.caacupe.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.com.hidraulica.caacupe.domain.DocumentoVenta;
import py.com.hidraulica.caacupe.domain.Venta;
import py.com.hidraulica.caacupe.domain.enums.EstadoDocumento;
import py.com.hidraulica.caacupe.domain.enums.EstadoVenta;
import py.com.hidraulica.caacupe.domain.enums.ModalidadDocumento;
import py.com.hidraulica.caacupe.domain.enums.TipoDocumentoVenta;
import py.com.hidraulica.caacupe.dto.DocumentoVentaDto;
import py.com.hidraulica.caacupe.dto.EmitirDocumentoRequest;
import py.com.hidraulica.caacupe.exception.BusinessException;
import py.com.hidraulica.caacupe.exception.NotFoundException;
import py.com.hidraulica.caacupe.repository.DocumentoVentaRepository;
import py.com.hidraulica.caacupe.repository.VentaRepository;

@Service
@Transactional
public class DocumentoVentaService {

  private final VentaRepository ventaRepo;
  private final DocumentoVentaRepository docRepo;

  public DocumentoVentaService(VentaRepository ventaRepo, DocumentoVentaRepository docRepo) {
    this.ventaRepo = ventaRepo;
    this.docRepo = docRepo;
  }

  @Transactional(readOnly = true)
  public List<DocumentoVentaDto> listarPorVenta(Long ventaId) {
    return docRepo.findByVentaIdOrderByFechaEmisionDesc(ventaId).stream()
        .map(this::toDto)
        .toList();
  }

  public DocumentoVentaDto emitir(Long ventaId, EmitirDocumentoRequest req) {
    Venta v = ventaRepo.findDtoById(ventaId)
        .orElseThrow(() -> new NotFoundException("Venta no encontrada: " + ventaId));

    if (v.getEstado() != EstadoVenta.CONFIRMADA) {
      throw new BusinessException("Solo se puede emitir documento para una venta CONFIRMADA.");
    }

    // Anti-duplicado simple (se puede relajar después)
    if (docRepo.existsByVentaIdAndTipoDocumentoAndModalidadAndEstado(
        ventaId, req.tipoDocumento, req.modalidad, EstadoDocumento.EMITIDO
    )) {
      throw new BusinessException("Ya existe un documento EMITIDO de ese tipo/modalidad para esta venta.");
    }

    DocumentoVenta d = new DocumentoVenta();
    d.setVenta(v);
    d.setTipoDocumento(req.tipoDocumento);
    d.setModalidad(req.modalidad);
    d.setEstado(EstadoDocumento.EMITIDO);
    d.setFechaEmision(OffsetDateTime.now());

    // Snapshot cliente (solo RUC como doc, según tu modelo)
    var c = v.getCliente();
    d.setClienteNombre(c.getNombreRazonSocial());
    d.setClienteDocumento(c.getRuc() != null ? c.getRuc() : "S/D");
    // si Cliente tiene direccion, ok; si no, quitá esta línea
    // d.setClienteDireccion(c.getDireccion());

    BigDecimal total = v.getTotal() == null ? BigDecimal.ZERO : v.getTotal();
    d.setTotal(total);

    // Si es NOTA_CREDITO, exigir origen (preparado)
    if (req.tipoDocumento == TipoDocumentoVenta.NOTA_CREDITO) {
      if (req.documentoOrigenId == null) {
        throw new BusinessException("NOTA_CREDITO requiere documentoOrigenId.");
      }
      DocumentoVenta origen = docRepo.findById(req.documentoOrigenId)
          .orElseThrow(() -> new NotFoundException("Documento origen no encontrado: " + req.documentoOrigenId));
      d.setDocumentoOrigen(origen);

      // Regla mínima pro: el origen debe ser FACTURA o TICKET emitido
      if (origen.getEstado() != EstadoDocumento.EMITIDO) {
        throw new BusinessException("El documento origen debe estar EMITIDO.");
      }
      // (Opcional) verificar que el origen corresponda a la misma venta o al mismo cliente
      // Para ahora lo dejamos flexible.
    }

    asignarNumeracion(d, req);

    return toDto(docRepo.save(d));
  }

  private void asignarNumeracion(DocumentoVenta d, EmitirDocumentoRequest req) {
    if (req.modalidad == ModalidadDocumento.NO_FISCAL) {
      d.setSerie("NF");
      d.setNumero(nextNumero(req.modalidad, req.tipoDocumento));
      d.setTimbrado(null);
      d.setCdc(null);
      d.setXmlSifen(null);
      return;
    }

    if (req.modalidad == ModalidadDocumento.TIMBRADO) {
      if (req.timbrado == null || req.timbrado.isBlank())
        throw new BusinessException("TIMBRADO requiere timbrado.");
      if (req.serie == null || req.serie.isBlank())
        throw new BusinessException("TIMBRADO requiere serie.");

      d.setTimbrado(req.timbrado);
      d.setSerie(req.serie);
      d.setNumero(nextNumero(req.modalidad, req.tipoDocumento));
      return;
    }

    if (req.modalidad == ModalidadDocumento.ELECTRONICO) {
      d.setSerie("E");
      d.setNumero(nextNumero(req.modalidad, req.tipoDocumento));
      // cdc/xml se llenan cuando implementemos SIFEN
      return;
    }

    throw new BusinessException("Modalidad no soportada: " + req.modalidad);
  }

  private Long nextNumero(ModalidadDocumento modalidad, TipoDocumentoVenta tipo) {
    return docRepo.findTopByModalidadAndTipoDocumentoOrderByNumeroDesc(modalidad, tipo)
        .map(x -> x.getNumero() == null ? 1L : x.getNumero() + 1L)
        .orElse(1L);
  }

  private DocumentoVentaDto toDto(DocumentoVenta d) {
    return new DocumentoVentaDto(
        d.getId(),
        d.getVenta() != null ? d.getVenta().getId() : null,
        d.getDocumentoOrigen() != null ? d.getDocumentoOrigen().getId() : null,
        d.getTipoDocumento(),
        d.getModalidad(),
        d.getEstado(),
        d.getFechaEmision(),
        d.getSerie(),
        d.getNumero(),
        d.getTimbrado(),
        d.getCdc(),
        d.getTotal(),
        d.getClienteNombre(),
        d.getClienteDocumento()
    );
  }
}
