package py.com.hidraulica.caacupe.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import py.com.hidraulica.caacupe.domain.enums.EstadoTurnoCaja;
import py.com.hidraulica.caacupe.domain.security.Usuario;

@Entity
@Table(name = "caja_turno")
public class CajaTurno extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "caja_id", nullable = false)
	private Caja caja;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_apertura_id", nullable = false)
	private Usuario usuarioApertura;

	@Column(name = "fecha_apertura", nullable = false)
	private OffsetDateTime fechaApertura;

	@Column(name = "monto_inicial", nullable = false)
	private BigDecimal montoInicial;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_cierre_id")
	private Usuario usuarioCierre;

	@Column(name = "fecha_cierre")
	private OffsetDateTime fechaCierre;

	/**
	 * Monto declarado al cierre (por ejemplo, efectivo contado + comprobantes que
	 * queden). Para arqueo detallado, se puede ampliar con una tabla extra o un
	 * JSON.
	 */
	@Column(name = "monto_cierre_declarado",nullable=false)	
	private BigDecimal montoCierreDeclarado;

	@Column(name = "estado", nullable = false)
	private String estado;

	@Column(name = "observacion")
	private String observacion;

	public Caja getCaja() {
		return caja;
	}

	public void setCaja(Caja caja) {
		this.caja = caja;
	}

	public Usuario getUsuarioApertura() {
		return usuarioApertura;
	}

	public void setUsuarioApertura(Usuario usuarioApertura) {
		this.usuarioApertura = usuarioApertura;
	}

	public OffsetDateTime getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaApertura(OffsetDateTime fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public BigDecimal getMontoInicial() {
		return montoInicial;
	}

	public void setMontoInicial(BigDecimal montoInicial) {
		this.montoInicial = montoInicial;
	}

	public Usuario getUsuarioCierre() {
		return usuarioCierre;
	}

	public void setUsuarioCierre(Usuario usuarioCierre) {
		this.usuarioCierre = usuarioCierre;
	}

	public OffsetDateTime getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(OffsetDateTime fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public BigDecimal getMontoCierreDeclarado() {
		return montoCierreDeclarado;
	}

	public void setMontoCierreDeclarado(BigDecimal montoCierreDeclarado) {
		this.montoCierreDeclarado = montoCierreDeclarado;
	}

	public EstadoTurnoCaja getEstado() {
		return EstadoTurnoCaja.valueOf(this.estado);
	}

	public void setEstado(EstadoTurnoCaja estado) {
		this.estado = estado.name();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
}
