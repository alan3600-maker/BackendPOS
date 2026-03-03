package py.com.hidraulica.caacupe.domain;

import py.com.hidraulica.caacupe.domain.enums.EstadoVenta;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venta")
public class Venta extends BaseEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "cliente_id", nullable = false)
  private Cliente cliente;

  @NotNull
  @Column(name = "fecha", nullable = false)
  private OffsetDateTime fecha = OffsetDateTime.now();

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "estado", nullable = false, length = 30)
  private EstadoVenta estado = EstadoVenta.BORRADOR;

  @Column(name = "observacion", length = 500)
  private String observacion;

  @Column(name = "total", nullable = false, precision = 19, scale = 2)
  private BigDecimal total = BigDecimal.ZERO;

  @JsonManagedReference
  @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<VentaItem> items = new ArrayList<>();

  @JsonManagedReference
  @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Cobro> cobros = new ArrayList<>();
  
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "caja_turno_id", nullable = false)
  private CajaTurno turno;

  public CajaTurno getTurno() {
	return turno;
}
  public void setTurno(CajaTurno turno) {
	this.turno = turno;
  }
  public void setId(Long id) {
	this.id = id;
  }
  public void setItems(List<VentaItem> items) {
	this.items = items;
  }
  public void setCobros(List<Cobro> cobros) {
	this.cobros = cobros;
  }
  public Long getId() { return id; }
  public Cliente getCliente() { return cliente; }
  public void setCliente(Cliente cliente) { this.cliente = cliente; }
  public OffsetDateTime getFecha() { return fecha; }
  public void setFecha(OffsetDateTime fecha) { this.fecha = fecha; }
  public EstadoVenta getEstado() { return estado; }
  public void setEstado(EstadoVenta estado) { this.estado = estado; }
  public String getObservacion() { return observacion; }
  public void setObservacion(String observacion) { this.observacion = observacion; }
  public BigDecimal getTotal() { return total; }
  public void setTotal(BigDecimal total) { this.total = total; }
  public List<VentaItem> getItems() { return items; }
  public List<Cobro> getCobros() { return cobros; }
}
