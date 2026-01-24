package py.com.hidraulica.caacupe.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.com.hidraulica.caacupe.domain.Caja;
import py.com.hidraulica.caacupe.domain.CajaTurno;
import py.com.hidraulica.caacupe.domain.enums.EstadoTurnoCaja;
import py.com.hidraulica.caacupe.domain.security.Usuario;
import py.com.hidraulica.caacupe.dto.ArqueoCajaDto;
import py.com.hidraulica.caacupe.dto.ArqueoPorMedioDto;
import py.com.hidraulica.caacupe.exception.BusinessException;
import py.com.hidraulica.caacupe.exception.NotFoundException;
import py.com.hidraulica.caacupe.repository.CajaRepository;
import py.com.hidraulica.caacupe.repository.CajaTurnoRepository;
import py.com.hidraulica.caacupe.repository.CobroRepository;
import py.com.hidraulica.caacupe.repository.security.UsuarioRepository;

@Service
@Transactional
public class CajaTurnoService {

	private final CajaRepository cajaRepo;
	private final CajaTurnoRepository turnoRepo;
	private final UsuarioRepository usuarioRepo;
	private final CobroRepository cobroRepo;

	public CajaTurnoService(CajaRepository cajaRepo, CajaTurnoRepository turnoRepo, UsuarioRepository usuarioRepo,
			CobroRepository cobroRepo) {
		this.cajaRepo = cajaRepo;
		this.turnoRepo = turnoRepo;
		this.usuarioRepo = usuarioRepo;
		this.cobroRepo = cobroRepo;
	}

	public Optional<CajaTurno> getTurnoAbierto(Long cajaId) {
		return turnoRepo.findFirstByCajaIdAndEstadoOrderByFechaAperturaDesc(cajaId, EstadoTurnoCaja.ABIERTA);
	}

	public CajaTurno abrir(Long cajaId, Long usuarioId, BigDecimal montoInicial) {
		Caja caja = cajaRepo.findById(cajaId).orElseThrow(() -> new NotFoundException("Caja no encontrada: " + cajaId));

		Usuario usuario = usuarioRepo.findById(usuarioId)
				.orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + usuarioId));

		if (turnoRepo.existsByCajaIdAndEstado(cajaId, EstadoTurnoCaja.ABIERTA)) {
			throw new BusinessException("Ya existe un turno ABIERTO para esta caja.");
		}

		CajaTurno t = new CajaTurno();
		t.setCaja(caja);
		t.setUsuarioApertura(usuario);
		t.setFechaApertura(OffsetDateTime.now());
		t.setMontoInicial(montoInicial != null ? montoInicial : BigDecimal.ZERO);
		t.setEstado(EstadoTurnoCaja.ABIERTA);
		t.setMontoCierreDeclarado(BigDecimal.ZERO);

		return turnoRepo.save(t);
	}

	public CajaTurno cerrar(Long turnoId, Long usuarioId, BigDecimal montoFinalDeclarado, String observacion) {
		CajaTurno t = turnoRepo.findById(turnoId)
				.orElseThrow(() -> new NotFoundException("Turno no encontrado: " + turnoId));

		if (t.getEstado() == EstadoTurnoCaja.CERRADA) {
			return t;
		}

		Usuario usuario = usuarioRepo.findById(usuarioId)
				.orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + usuarioId));

		t.setUsuarioCierre(usuario);
		t.setMontoCierreDeclarado(montoFinalDeclarado != null ? montoFinalDeclarado : BigDecimal.ZERO);
		t.setObservacion(observacion);
		t.setEstado(EstadoTurnoCaja.CERRADA);
		t.setFechaCierre(OffsetDateTime.now());

		return turnoRepo.save(t);
	}

	public ArqueoCajaDto getArqueo(Long turnoId) {
		CajaTurno t = turnoRepo.findById(turnoId)
				.orElseThrow(() -> new NotFoundException("Turno no encontrado: " + turnoId));

		BigDecimal totalEsperado = cobroRepo.totalCobradoEnTurno(turnoId);
		if (totalEsperado == null)
			totalEsperado = BigDecimal.ZERO;

		List<ArqueoPorMedioDto> porMedio = cobroRepo.resumenPorMedioEnTurno(turnoId).stream().map(
				r -> new ArqueoPorMedioDto(r.getMedioPago(), r.getTotal() == null ? BigDecimal.ZERO : r.getTotal()))
				.toList();

		BigDecimal declarado = t.getMontoCierreDeclarado();
		if (declarado == null)
			declarado = BigDecimal.ZERO;

		BigDecimal montoInicial = t.getMontoInicial() == null ? BigDecimal.ZERO : t.getMontoInicial();
		BigDecimal diferencia = declarado.subtract(totalEsperado);

		return new ArqueoCajaDto(t.getId(), t.getCaja() != null ? t.getCaja().getId() : null, t.getFechaApertura(),
				t.getFechaCierre(), montoInicial, totalEsperado, declarado, diferencia, porMedio);
	}
}
