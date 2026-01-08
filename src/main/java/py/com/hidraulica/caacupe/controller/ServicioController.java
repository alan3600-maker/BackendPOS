package py.com.hidraulica.caacupe.controller;

import py.com.hidraulica.caacupe.domain.Servicio;
import py.com.hidraulica.caacupe.service.ServicioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/servicios")
public class ServicioController {

  private final ServicioService service;

  public ServicioController(ServicioService service) {
    this.service = service;
  }

  @PostMapping
  public Servicio create(@RequestBody @Valid Servicio body) {
    return service.create(body);
  }

  @GetMapping("/{id}")
  public Servicio get(@PathVariable Long id) {
    return service.get(id);
  }

  @GetMapping
  public List<Servicio> list() {
    return service.list();
  }

  @PutMapping("/{id}")
  public Servicio update(@PathVariable Long id, @RequestBody @Valid Servicio body) {
    return service.update(id, body);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }

  @GetMapping("/search")
  public Page<Servicio> search(@RequestParam(required = false) String q,
                              @RequestParam(required = false) Boolean activo,
                              Pageable pageable) {
    return service.search(q, activo, pageable);
  }

  @GetMapping("/by-codigo/{codigo}")
  public Servicio getByCodigo(@PathVariable String codigo) {
    return service.getByCodigo(codigo);
  }

  @PatchMapping("/{id}/activo")
  public Servicio setActivo(@PathVariable Long id, @RequestParam boolean value) {
    return service.setActivo(id, value);
  }
}
