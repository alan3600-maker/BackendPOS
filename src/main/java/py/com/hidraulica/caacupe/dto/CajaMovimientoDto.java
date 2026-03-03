package py.com.hidraulica.caacupe.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import py.com.hidraulica.caacupe.domain.CajaMovimiento;
import py.com.hidraulica.caacupe.domain.enums.TipoMovimientoCaja;

public record CajaMovimientoDto(
    Long id,
    Long turnoId,
    TipoMovimientoCaja tipo,
    BigDecimal monto,
    String motivo,
    OffsetDateTime fecha,
    Long usuarioId,
    String usuarioNombre
) {
  public static CajaMovimientoDto of(CajaMovimiento m) {
    return new CajaMovimientoDto(
        m.getId(),
        m.getTurno() != null ? m.getTurno().getId() : null,
        m.getTipo(),
        m.getMonto(),
        m.getMotivo(),
        m.getFecha(),
        m.getUsuario() != null ? m.getUsuario().getId() : null,
        m.getUsuario() != null ? m.getUsuario().getUsername() : null
    );
  }
}
