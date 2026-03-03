package py.com.hidraulica.caacupe.dto;

import java.time.OffsetDateTime;

import py.com.hidraulica.caacupe.domain.enums.EstadoVenta;

public class VentaSearchDto {
	private Long clienteId;

	private EstadoVenta estado;

	private OffsetDateTime desde;

	private OffsetDateTime hasta;

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	public EstadoVenta getEstado() {
		return estado;
	}

	public void setEstado(EstadoVenta estado) {
		this.estado = estado;
	}

	public OffsetDateTime getDesde() {
		return desde;
	}

	public void setDesde(OffsetDateTime desde) {
		this.desde = desde;
	}

	public OffsetDateTime getHasta() {
		return hasta;
	}

	public void setHasta(OffsetDateTime hasta) {
		this.hasta = hasta;
	}
}
