package py.com.hidraulica.caacupe.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.com.hidraulica.caacupe.domain.Caja;
import py.com.hidraulica.caacupe.domain.CajaTurno;
import py.com.hidraulica.caacupe.domain.enums.EstadoTurnoCaja;
import py.com.hidraulica.caacupe.domain.security.Usuario;
// AJUSTÁ estos imports a tus excepciones reales:
import py.com.hidraulica.caacupe.exception.NotFoundException;
import py.com.hidraulica.caacupe.repository.CajaRepository;
import py.com.hidraulica.caacupe.repository.CajaTurnoRepository;
import py.com.hidraulica.caacupe.repository.security.UsuarioRepository;

@Service
@Transactional
public class CajaTurnoService {

  private final CajaRepository cajaRepository;
  private final CajaTurnoRepository turnoRepository;
  private final UsuarioRepository usuarioRepository;

  public CajaTurnoService(CajaRepository cajaRepository,
                          CajaTurnoRepository turnoRepository,
                          UsuarioRepository usuarioRepository) {
    this.cajaRepository = cajaRepository;
    this.turnoRepository = turnoRepository;
    this.usuarioRepository = usuarioRepository;
  }

  public Optional<CajaTurno> getTurnoAbierto(Long cajaId) {
    return turnoRepository.findTopByCajaIdAndEstadoOrderByFechaAperturaDesc(
        cajaId, EstadoTurnoCaja.ABIERTA.name()
    );
  }

  public CajaTurno abrir(Long cajaId, Long usuarioId, BigDecimal montoInicial) {
    Caja caja = cajaRepository.findById(cajaId)
        .orElseThrow(() -> new NotFoundException("Caja no encontrada"));

    Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

    Optional<CajaTurno> existente =
        turnoRepository.findTopByCajaIdAndUsuarioAperturaIdAndEstadoOrderByFechaAperturaDesc(
            cajaId, usuarioId, EstadoTurnoCaja.ABIERTA.name());

    if (existente.isPresent()) {
      return existente.get();
    }

    CajaTurno t = new CajaTurno();
    t.setCaja(caja);
    t.setUsuarioApertura(usuario);
    t.setFechaApertura(OffsetDateTime.now());
    t.setMontoInicial(montoInicial != null ? montoInicial : BigDecimal.ZERO);
    t.setEstado(EstadoTurnoCaja.ABIERTA);

    return turnoRepository.save(t);
  }

  public CajaTurno cerrar(Long turnoId, Long usuarioId, BigDecimal montoFinalDeclarado, String observacion) {
    CajaTurno t = turnoRepository.findById(turnoId)
        .orElseThrow(() -> new NotFoundException("Turno no encontrado"));

    if (EstadoTurnoCaja.CERRADA.name().equals(t.getEstado())) {
      return t;
    }

    Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

    t.setUsuarioCierre(usuario);
    t.setMontoCierreDeclarado(montoFinalDeclarado != null ? montoFinalDeclarado : BigDecimal.ZERO);
    t.setObservacion(observacion);
    t.setEstado(EstadoTurnoCaja.CERRADA);
    t.setFechaCierre(OffsetDateTime.now());

    return turnoRepository.save(t);
  }
}
