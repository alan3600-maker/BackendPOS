package py.com.hidraulica.caacupe.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import py.com.hidraulica.caacupe.domain.enums.EstadoVenta;

public record VentaDto(
    Long id,
    OffsetDateTime fecha,
    EstadoVenta estado,
    Long clienteId,
    String clienteNombre,
    BigDecimal total
) {}
