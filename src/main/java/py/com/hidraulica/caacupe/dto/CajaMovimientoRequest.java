package py.com.hidraulica.caacupe.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import py.com.hidraulica.caacupe.domain.enums.TipoMovimientoCaja;

public record CajaMovimientoRequest(
    @NotNull Long turnoId,
    @NotNull TipoMovimientoCaja tipo,
    @NotNull BigDecimal monto,
    String motivo
) {}
