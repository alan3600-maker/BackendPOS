package py.com.hidraulica.caacupe.dto;

import java.time.OffsetDateTime;

import py.com.hidraulica.caacupe.domain.enums.EstadoVenta;

public class VentaSearchDto {
	private Long clienteId;

	private EstadoVenta estado;

	private OffsetDateTime desde;

	private OffsetDateTime hasta;
}
