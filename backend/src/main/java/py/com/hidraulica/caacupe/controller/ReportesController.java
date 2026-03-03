package py.com.hidraulica.caacupe.controller;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import py.com.hidraulica.caacupe.dto.ReporteCobrosPorMedioDto;
import py.com.hidraulica.caacupe.dto.ReporteVentasPorCajeroDto;
import py.com.hidraulica.caacupe.dto.ReporteVentasPorDiaDto;
import py.com.hidraulica.caacupe.service.ReportesService;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReportesController {

  private final ReportesService service;

  public ReportesController(ReportesService service) {
    this.service = service;
  }

  @GetMapping("/ventas-por-dia")
  public List<ReporteVentasPorDiaDto> ventasPorDia(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime desde,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime hasta
  ) {
    return service.ventasPorDia(desde, hasta);
  }

  @GetMapping("/cobros-por-medio")
  public List<ReporteCobrosPorMedioDto> cobrosPorMedio(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime desde,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime hasta
  ) {
    return service.cobrosPorMedio(desde, hasta);
  }

  @GetMapping("/ventas-por-cajero")
  public List<ReporteVentasPorCajeroDto> ventasPorCajero(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime desde,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime hasta
  ) {
    return service.ventasPorCajero(desde, hasta);
  }
}
