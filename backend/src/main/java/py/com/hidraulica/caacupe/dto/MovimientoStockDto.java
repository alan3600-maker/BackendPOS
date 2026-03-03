package py.com.hidraulica.caacupe.dto;

import java.time.OffsetDateTime;

import py.com.hidraulica.caacupe.domain.enums.TipoMovimientoStock;

public record MovimientoStockDto(
	    Long id,
	    TipoMovimientoStock tipo,
	    OffsetDateTime fecha,
	    String referenciaTipo,
	    Long referenciaId,
	    int itemsCount
	) {}
