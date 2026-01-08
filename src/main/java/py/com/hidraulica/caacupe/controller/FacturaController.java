package py.com.hidraulica.caacupe.controller;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import py.com.hidraulica.caacupe.dto.FacturaDto;
import py.com.hidraulica.caacupe.dto.FacturaEmitirDesdeVentaRequest;
import py.com.hidraulica.caacupe.dto.PageResponse;
import py.com.hidraulica.caacupe.domain.enums.EstadoFactura;
import py.com.hidraulica.caacupe.domain.enums.TipoFactura;
import py.com.hidraulica.caacupe.service.FacturaPdfService;
import py.com.hidraulica.caacupe.service.FacturaPdfService.PdfFormat;
import py.com.hidraulica.caacupe.service.FacturaService;

@RestController
@RequestMapping("/api/v1/facturas")
public class FacturaController {

  private final FacturaService service;
  private final FacturaPdfService pdfService;

  public FacturaController(FacturaService service, FacturaPdfService pdfService) {
    this.service = service;
    this.pdfService = pdfService;
  }

  // ✅ DTO para evitar LazyInitialization
  @GetMapping("/{id}")
  public FacturaDto get(@PathVariable Long id) {
    return service.getDto(id);
  }

  // (Opcional) si querés listar simple, devolvé DTOs también
  // Pero lo recomendado es usar siempre /search paginado
  @GetMapping
  public List<FacturaDto> list() {
    return service.listDto();
  }

  // ✅ Search estandarizado como Cliente/Producto/Venta
  @GetMapping("/search")
  public PageResponse<FacturaDto> search(
      @RequestParam(required = false) Long clienteId,
      @RequestParam(required = false) TipoFactura tipo,
      @RequestParam(required = false) EstadoFactura estado,
      @RequestParam(required = false) String numero,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime desde,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime hasta,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "fecha") String sortBy,
      @RequestParam(defaultValue = "desc") String dir
  ) {
    return service.searchDto(clienteId, tipo, estado, numero, desde, hasta, page, size, sortBy, dir);
  }

  @PostMapping("/emitir-desde-venta/{ventaId}")
  public FacturaDto emitirDesdeVenta(
      @PathVariable Long ventaId,
      @RequestBody @Valid FacturaEmitirDesdeVentaRequest req
  ) {
    return service.emitirDesdeVentaDto(ventaId, req);
  }

  // ✅ devuelve DTO (evita lazy)
  @PostMapping("/{id}/anular")
  public FacturaDto anular(@PathVariable Long id) {
    return service.anularDto(id);
  }

  // ✅ PDF queda igual
  @GetMapping("/{id}/pdf")
  public ResponseEntity<byte[]> pdf(
      @PathVariable Long id,
      @RequestParam(required = false, defaultValue = "TICKET") PdfFormat format
  ) {
    var factura = service.getForPdf(id);
    byte[] bytes = pdfService.generarPdf(factura, format);

    String filename = "factura-" + (factura.getNumero() != null ? factura.getNumero() : factura.getId()) + ".pdf";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(ContentDisposition.inline().filename(filename).build());

    return ResponseEntity.ok().headers(headers).body(bytes);
  }
}
