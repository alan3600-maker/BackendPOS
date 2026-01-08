package py.com.hidraulica.caacupe.domain;

import py.com.hidraulica.caacupe.domain.enums.MedioPago;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "cobro")
public class Cobro {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonBackReference
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "venta_id", nullable = false)
	private Venta venta;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "medio_pago", nullable = false, length = 30)
	private MedioPago medioPago;

	@NotNull
	@Column(name = "monto", nullable = false, precision = 19, scale = 2)
	private BigDecimal monto = BigDecimal.ZERO;

	@NotNull
	@Column(name = "fecha", nullable = false)
	private OffsetDateTime fecha = OffsetDateTime.now();

	public Long getId() {
		return id;
	}

	public Venta getVenta() {
		return venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}

	public MedioPago getMedioPago() {
		return medioPago;
	}

	public void setMedioPago(MedioPago medioPago) {
		this.medioPago = medioPago;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public OffsetDateTime getFecha() {
		return fecha;
	}

	public void setFecha(OffsetDateTime fecha) {
		this.fecha = fecha;
	}
}
