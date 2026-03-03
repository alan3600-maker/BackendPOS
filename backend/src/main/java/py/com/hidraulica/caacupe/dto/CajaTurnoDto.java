package py.com.hidraulica.caacupe.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import py.com.hidraulica.caacupe.domain.CajaTurno;

public class CajaTurnoDto {
	public Long id;
	public Long cajaId;
	public Long usuarioAperturaId;
	public String estado;
	public OffsetDateTime fechaApertura;
	public BigDecimal montoInicial;

	public static CajaTurnoDto of(CajaTurno t) {
		CajaTurnoDto dto = new CajaTurnoDto();
		dto.id = t.getId();
		dto.cajaId = (t.getCaja() != null ? t.getCaja().getId() : null);
		dto.usuarioAperturaId = (t.getUsuarioApertura() != null ? t.getUsuarioApertura().getId() : null);
		dto.estado = t.getEstado().name();
		dto.fechaApertura = t.getFechaApertura();
		dto.montoInicial = t.getMontoInicial();
		return dto;
	}
}
