package py.com.hidraulica.caacupe.controller;

import py.com.hidraulica.caacupe.domain.OrdenTrabajo;
import py.com.hidraulica.caacupe.domain.enums.EstadoOrdenTrabajo;
import py.com.hidraulica.caacupe.dto.OrdenTrabajoRequest;
import py.com.hidraulica.caacupe.service.OrdenTrabajoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes-trabajo")
public class OrdenTrabajoController {

  private final OrdenTrabajoService service;

  public OrdenTrabajoController(OrdenTrabajoService service) {
    this.service = service;
  }

  @PostMapping
  public OrdenTrabajo create(@RequestBody @Valid OrdenTrabajoRequest req) {
    return service.create(req);
  }

  @GetMapping("/{id}")
  public OrdenTrabajo get(@PathVariable Long id) {
    return service.get(id);
  }

  @GetMapping
  public List<OrdenTrabajo> list() {
    return service.list();
  }

  @GetMapping("/search")
  public Page<OrdenTrabajo> search(@RequestParam(required = false) Long clienteId,
                                   @RequestParam(required = false) EstadoOrdenTrabajo estado,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime desde,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime hasta,
                                   Pageable pageable) {
    return service.search(clienteId, estado, desde, hasta, pageable);
  }

  @PutMapping("/{id}")
  public OrdenTrabajo update(@PathVariable Long id, @RequestBody @Valid OrdenTrabajoRequest req) {
    return service.update(id, req);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }

  @PostMapping("/{id}/estado")
  public OrdenTrabajo cambiarEstado(@PathVariable Long id, @RequestParam EstadoOrdenTrabajo value) {
    return service.cambiarEstado(id, value);
  }

  @PostMapping("/{id}/consumir-repuestos")
  public OrdenTrabajo consumirRepuestos(@PathVariable Long id) {
    return service.consumirRepuestos(id);
  }

  @PostMapping("/{id}/revertir-consumo")
  public OrdenTrabajo revertirConsumo(@PathVariable Long id) {
    return service.revertirConsumo(id);
  }
}
