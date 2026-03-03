package py.com.hidraulica.caacupe.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import py.com.hidraulica.caacupe.domain.enums.TipoMovimientoCaja;
import py.com.hidraulica.caacupe.domain.security.Usuario;

@Entity
@Table(name = "caja_movimiento")
public class CajaMovimiento extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "caja_turno_id", nullable = false)
  private CajaTurno turno;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "tipo", nullable = false, length = 20)
  private TipoMovimientoCaja tipo;

  @NotNull
  @Column(name = "monto", nullable = false, precision = 19, scale = 2)
  private BigDecimal monto = BigDecimal.ZERO;

  @Column(name = "motivo", length = 300)
  private String motivo;

  @NotNull
  @Column(name = "fecha", nullable = false)
  private OffsetDateTime fecha = OffsetDateTime.now();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "usuario_id")
  private Usuario usuario;

  public Long getId() {
    return id;
  }

  public CajaTurno getTurno() {
    return turno;
  }

  public void setTurno(CajaTurno turno) {
    this.turno = turno;
  }

  public TipoMovimientoCaja getTipo() {
    return tipo;
  }

  public void setTipo(TipoMovimientoCaja tipo) {
    this.tipo = tipo;
  }

  public BigDecimal getMonto() {
    return monto;
  }

  public void setMonto(BigDecimal monto) {
    this.monto = monto;
  }

  public String getMotivo() {
    return motivo;
  }

  public void setMotivo(String motivo) {
    this.motivo = motivo;
  }

  public OffsetDateTime getFecha() {
    return fecha;
  }

  public void setFecha(OffsetDateTime fecha) {
    this.fecha = fecha;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }
}
