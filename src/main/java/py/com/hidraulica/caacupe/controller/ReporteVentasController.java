package py.com.hidraulica.caacupe.controller;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.YearMonth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.com.hidraulica.caacupe.service.ReporteVentasPdfService;

@RestController
@RequestMapping("/api/v1/reportes/ventas")
public class ReporteVentasController {

  private final ReporteVentasPdfService pdf;

  public ReporteVentasController(ReporteVentasPdfService pdf) {
    this.pdf = pdf;
  }

  @GetMapping(value = "/diario", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> diario(@RequestParam String fecha) {
    LocalDate d = LocalDate.parse(fecha);
    OffsetDateTime desde = ReporteVentasPdfService.startOfDay(d);
    OffsetDateTime hasta = ReporteVentasPdfService.endOfDay(d);
    byte[] bytes = pdf.pdfVentas(desde, hasta, "Reporte de ventas diario - " + fecha);
    return inline(bytes, "ventas-diario-" + fecha + ".pdf");
  }

  @GetMapping(value = "/mensual", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> mensual(@RequestParam int anio, @RequestParam int mes) {
    YearMonth ym = YearMonth.of(anio, mes);
    LocalDate start = ym.atDay(1);
    LocalDate end = ym.atEndOfMonth();
    OffsetDateTime desde = ReporteVentasPdfService.startOfDay(start);
    OffsetDateTime hasta = ReporteVentasPdfService.endOfDay(end);
    String label = ym.toString();
    byte[] bytes = pdf.pdfVentas(desde, hasta, "Reporte de ventas mensual - " + label);
    return inline(bytes, "ventas-mensual-" + label + ".pdf");
  }

  @GetMapping(value = "/anual", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> anual(@RequestParam int anio) {
    LocalDate start = LocalDate.of(anio, 1, 1);
    LocalDate end = LocalDate.of(anio, 12, 31);
    OffsetDateTime desde = ReporteVentasPdfService.startOfDay(start);
    OffsetDateTime hasta = ReporteVentasPdfService.endOfDay(end);
    byte[] bytes = pdf.pdfVentas(desde, hasta, "Reporte de ventas anual - " + anio);
    return inline(bytes, "ventas-anual-" + anio + ".pdf");
  }

  private static ResponseEntity<byte[]> inline(byte[] bytes, String filename) {
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_PDF)
        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
        .body(bytes);
  }
}
