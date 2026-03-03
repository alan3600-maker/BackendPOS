package py.com.hidraulica.caacupe.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import py.com.hidraulica.caacupe.domain.enums.EstadoFactura;
import py.com.hidraulica.caacupe.domain.enums.TipoFactura;

public record FacturaDto(
	    Long id,
	    String numero,
	    TipoFactura tipo,
	    EstadoFactura estado,
	    OffsetDateTime fecha,
	    Long ventaId,
	    Long clienteId,
	    String clienteNombre,
	    BigDecimal total
	) {}

