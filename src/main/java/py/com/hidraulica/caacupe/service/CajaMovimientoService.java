package py.com.hidraulica.caacupe.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.com.hidraulica.caacupe.domain.CajaMovimiento;
import py.com.hidraulica.caacupe.domain.CajaTurno;
import py.com.hidraulica.caacupe.domain.enums.EstadoTurnoCaja;
import py.com.hidraulica.caacupe.domain.enums.TipoMovimientoCaja;
import py.com.hidraulica.caacupe.domain.security.Usuario;
import py.com.hidraulica.caacupe.dto.CajaMovimientoDto;
import py.com.hidraulica.caacupe.dto.CajaMovimientoRequest;
import py.com.hidraulica.caacupe.exception.BusinessException;
import py.com.hidraulica.caacupe.exception.NotFoundException;
import py.com.hidraulica.caacupe.repository.CajaMovimientoRepository;
import py.com.hidraulica.caacupe.repository.CajaTurnoRepository;
import py.com.hidraulica.caacupe.repository.security.UsuarioRepository;

@Service
@Transactional
public class CajaMovimientoService {

  private final CajaMovimientoRepository repo;
  private final CajaTurnoRepository turnoRepo;
  private final UsuarioRepository usuarioRepo;

  public CajaMovimientoService(CajaMovimientoRepository repo, CajaTurnoRepository turnoRepo,
      UsuarioRepository usuarioRepo) {
    this.repo = repo;
    this.turnoRepo = turnoRepo;
    this.usuarioRepo = usuarioRepo;
  }

  public List<CajaMovimientoDto> listar(Long turnoId) {
    return repo.findByTurnoId(turnoId).stream().map(CajaMovimientoDto::of).toList();
  }

  public CajaMovimientoDto crear(CajaMovimientoRequest req, Long usuarioId) {
    CajaTurno turno = turnoRepo.findById(req.turnoId())
        .orElseThrow(() -> new NotFoundException("Turno no encontrado: " + req.turnoId()));

    if (turno.getEstado() != EstadoTurnoCaja.ABIERTA) {
      throw new BusinessException("El turno está CERRADO. No se pueden registrar movimientos.");
    }

    Usuario usuario = usuarioRepo.findById(usuarioId)
        .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + usuarioId));

    BigDecimal monto = req.monto() != null ? req.monto() : BigDecimal.ZERO;
    if (monto.signum() <= 0) {
      throw new BusinessException("El monto debe ser mayor a cero.");
    }

    CajaMovimiento m = new CajaMovimiento();
    m.setTurno(turno);
    m.setTipo(req.tipo());
    m.setMonto(monto);
    m.setMotivo(req.motivo());
    m.setUsuario(usuario);
    m.setFecha(OffsetDateTime.now());

    return CajaMovimientoDto.of(repo.save(m));
  }

  public void eliminar(Long movimientoId) {
    if (!repo.existsById(movimientoId)) {
      throw new NotFoundException("Movimiento no encontrado: " + movimientoId);
    }
    repo.deleteById(movimientoId);
  }

  public BigDecimal total(Long turnoId, TipoMovimientoCaja tipo) {
    BigDecimal v = repo.totalByTurnoAndTipo(turnoId, tipo);
    return v != null ? v : BigDecimal.ZERO;
  }
}
