package py.com.hidraulica.caacupe.controller;

import java.time.OffsetDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import py.com.hidraulica.caacupe.domain.enums.TipoMovimientoStock;
import py.com.hidraulica.caacupe.dto.MovimientoStockDto;
import py.com.hidraulica.caacupe.dto.MovimientoStockRequest;
import py.com.hidraulica.caacupe.dto.PageResponse;
import py.com.hidraulica.caacupe.service.MovimientoStockService;

@RestController
@RequestMapping("/api/v1/movimientos-stock")
public class MovimientoStockController {
  private final MovimientoStockService service;
  public MovimientoStockController(MovimientoStockService service) { this.service = service; }

  @PostMapping
  public MovimientoStockDto create(@RequestBody @Valid MovimientoStockRequest req) {
    return service.createDto(req); // 👈 hacé que createDto exista y devuelva DTO
  }

  @GetMapping("/{id}")
  public MovimientoStockDto get(@PathVariable Long id) {
    return service.getDto(id);
  }

  @GetMapping("/search")
  public PageResponse<MovimientoStockDto> search(
      @RequestParam(required = false) TipoMovimientoStock tipo,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime desde,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime hasta,
      @RequestParam(required = false) String referenciaTipo,
      @RequestParam(required = false) Long referenciaId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "fecha") String sortBy,
      @RequestParam(defaultValue = "desc") String dir
  ) {
    return service.searchDto(tipo, desde, hasta, referenciaTipo, referenciaId, page, size, sortBy, dir);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) { service.delete(id); }
}
