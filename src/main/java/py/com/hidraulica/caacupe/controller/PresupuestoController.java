package py.com.hidraulica.caacupe.controller;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import py.com.hidraulica.caacupe.domain.Presupuesto;
import py.com.hidraulica.caacupe.domain.Venta;
import py.com.hidraulica.caacupe.domain.enums.EstadoPresupuesto;
import py.com.hidraulica.caacupe.dto.PresupuestoDto;
import py.com.hidraulica.caacupe.dto.PresupuestoRequest;
import py.com.hidraulica.caacupe.service.PresupuestoService;

@RestController
@RequestMapping("/api/v1/presupuestos")
public class PresupuestoController {

  private final PresupuestoService service;

  public PresupuestoController(PresupuestoService service) {
    this.service = service;
  }

  @PostMapping
  public Presupuesto create(@RequestBody @Valid PresupuestoRequest req) {
    return service.create(req);
  }

  @GetMapping("/{id}")
  public Presupuesto get(@PathVariable Long id) {
    return service.get(id);
  }

  @GetMapping
  public List<PresupuestoDto> list() {
    return service.list();
  }

  @GetMapping("/search")
  public Page<Presupuesto> search(@RequestParam(required = false) Long clienteId,
                                  @RequestParam(required = false) EstadoPresupuesto estado,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime desde,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime hasta,
                                  Pageable pageable) {
    return service.search(clienteId, estado, desde, hasta, pageable);
  }

  @PutMapping("/{id}")
  public Presupuesto update(@PathVariable Long id, @RequestBody @Valid PresupuestoRequest req) {
    return service.update(id, req);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }

  @PostMapping("/{id}/aprobar")
  public Presupuesto aprobar(@PathVariable Long id) {
    return service.aprobar(id);
  }

  @PostMapping("/{id}/convertir-a-venta")
  public Venta convertirAVenta(@PathVariable Long id) {
    return service.convertirAVenta(id);
  }
}
