package py.com.hidraulica.caacupe.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import py.com.hidraulica.caacupe.domain.Caja;
import py.com.hidraulica.caacupe.service.CajaService;

@RestController
@RequestMapping("/api/v1/cajas")
public class CajaController {

  private final CajaService service;

  public CajaController(CajaService service) {
    this.service = service;
  }

  @GetMapping
  public List<Caja> listar(@RequestParam Long sucursalId) {
    return service.listarPorSucursal(sucursalId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Caja crear(@RequestParam Long sucursalId, @RequestBody Caja body) {
    return service.crear(sucursalId, body);
  }

  @PutMapping("/{id}")
  public Caja actualizar(@PathVariable Long id, @RequestBody Caja body) {
    return service.actualizar(id, body);
  }

  @PostMapping("/{id}/activar")
  public Caja activar(@PathVariable Long id) {
    return service.setActivo(id, true);
  }

  @PostMapping("/{id}/desactivar")
  public Caja desactivar(@PathVariable Long id) {
    return service.setActivo(id, false);
  }
}
