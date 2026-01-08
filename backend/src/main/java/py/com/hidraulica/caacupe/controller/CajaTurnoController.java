package py.com.hidraulica.caacupe.controller;

import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import py.com.hidraulica.caacupe.domain.CajaTurno;
import py.com.hidraulica.caacupe.service.CajaTurnoService;

@RestController
@RequestMapping("/api/v1/caja-turnos")
public class CajaTurnoController {

  private final CajaTurnoService service;

  public CajaTurnoController(CajaTurnoService service) {
    this.service = service;
  }

  @PostMapping("/abrir")
  @ResponseStatus(HttpStatus.CREATED)
  public CajaTurno abrir(
      @RequestParam Long cajaId,
      @RequestParam Long usuarioAperturaId,
      @RequestParam(required = false, defaultValue = "0") BigDecimal montoInicial) {
    return service.abrir(cajaId, usuarioAperturaId, montoInicial);
  }

  @PostMapping("/{turnoId}/cerrar")
  public CajaTurno cerrar(
      @PathVariable Long turnoId,
      @RequestParam Long usuarioCierreId,
      @RequestParam(required = false) BigDecimal montoFinalDeclarado,
      @RequestParam(required = false) String observacion) {
    return service.cerrar(turnoId, usuarioCierreId, montoFinalDeclarado, observacion);
  }

  @GetMapping("/abierta")
  public CajaTurno obtenerAbierta(@RequestParam Long cajaId) {
    return service.getTurnoAbierto(cajaId).orElse(null);
  }
}
