package py.com.hidraulica.caacupe.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record ArqueoCajaDto(
    Long turnoId,
    Long cajaId,
    OffsetDateTime fechaApertura,
    OffsetDateTime fechaCierre,
    BigDecimal montoInicial,
    BigDecimal totalVentas,
    BigDecimal totalIngresos,
    BigDecimal totalEgresos,
    BigDecimal saldoEfectivoEsperado,
    BigDecimal totalEsperado,
    BigDecimal totalDeclarado,
    BigDecimal diferencia,
    List<ArqueoPorMedioDto> porMedio
) {}
