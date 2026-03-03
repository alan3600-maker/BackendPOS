package py.com.hidraulica.caacupe.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import py.com.hidraulica.caacupe.dto.CajaMovimientoDto;
import py.com.hidraulica.caacupe.dto.CajaMovimientoRequest;
import py.com.hidraulica.caacupe.security.SecurityUtils;
import py.com.hidraulica.caacupe.service.CajaMovimientoService;

@RestController
@RequestMapping("/api/v1/caja-movimientos")
public class CajaMovimientoController {

  private final CajaMovimientoService service;

  public CajaMovimientoController(CajaMovimientoService service) {
    this.service = service;
  }

  @GetMapping
  public List<CajaMovimientoDto> listar(@RequestParam Long turnoId) {
    return service.listar(turnoId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CajaMovimientoDto crear(@Valid @RequestBody CajaMovimientoRequest req) {
    Long usuarioId = SecurityUtils.currentUserId();
    return service.crear(req, usuarioId);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminar(@PathVariable Long id) {
    service.eliminar(id);
  }
}
