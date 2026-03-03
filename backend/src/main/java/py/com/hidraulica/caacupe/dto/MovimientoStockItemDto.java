package py.com.hidraulica.caacupe.dto;

import java.math.BigDecimal;

public record MovimientoStockItemDto(
		  Long productoId,
		  String productoSku,
		  String productoDescripcion,
		  Long depositoId,
		  String depositoNombre,
		  BigDecimal cantidad
		) {}