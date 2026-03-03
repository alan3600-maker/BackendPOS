package py.com.hidraulica.caacupe.controller;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import py.com.hidraulica.caacupe.domain.enums.EstadoVenta;
import py.com.hidraulica.caacupe.dto.PageResponse;
import py.com.hidraulica.caacupe.dto.VentaDto;
import py.com.hidraulica.caacupe.dto.VentaRequest;
import py.com.hidraulica.caacupe.service.VentaService;

@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {

  private final VentaService service;

  public VentaController(VentaService service) {
    this.service = service;
  }

  @PostMapping
  public VentaDto create(@RequestBody @Valid VentaRequest req) {
    return service.createDto(req);
  }

  @GetMapping("/{id}")
  public VentaDto get(@PathVariable Long id) {
    return service.getDto(id);
  }

  // OPCIONAL: yo recomiendo NO exponer /ventas (list) para front.
  // Si igual lo querés mantener:
  @GetMapping
  public List<VentaDto> list() {
    return service.listDto();
  }

  @GetMapping("/search")
  public PageResponse<VentaDto> search(
      @RequestParam(required = false) Long clienteId,
      @RequestParam(required = false) EstadoVenta estado,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime desde,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime hasta,
      @RequestParam(required = false) String q,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "desc") String dir
  ) {
    return service.searchDto(clienteId, estado, desde, hasta, q, page, size, sortBy, dir);
  }

  @PutMapping("/{id}")
  public VentaDto update(@PathVariable Long id, @RequestBody @Valid VentaRequest req) {
    return service.updateDto(id, req);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }

  @PostMapping("/{id}/confirmar")
  public VentaDto confirmar(@PathVariable Long id, @RequestParam Long cajaId) {
    return service.confirmarDto(id, cajaId);
  }

  @PostMapping("/{id}/anular")
  public VentaDto anular(@PathVariable Long id) {
    return service.anularDto(id);
  }
}
