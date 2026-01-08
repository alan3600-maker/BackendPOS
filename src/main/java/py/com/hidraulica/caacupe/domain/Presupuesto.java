package py.com.hidraulica.caacupe.domain;

import py.com.hidraulica.caacupe.domain.enums.EstadoPresupuesto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "presupuesto")
public class Presupuesto extends BaseEntity {
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
  private EstadoPresupuesto estado = EstadoPresupuesto.BORRADOR;

  @Column(name = "observacion", length = 500)
  private String observacion;

  @Column(name = "total", nullable = false, precision = 19, scale = 2)
  private BigDecimal total = BigDecimal.ZERO;

  @JsonManagedReference
  @OneToMany(mappedBy = "presupuesto", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PresupuestoItem> items = new ArrayList<>();

  public Long getId() { return id; }
  public Cliente getCliente() { return cliente; }
  public void setCliente(Cliente cliente) { this.cliente = cliente; }
  public OffsetDateTime getFecha() { return fecha; }
  public void setFecha(OffsetDateTime fecha) { this.fecha = fecha; }
  public EstadoPresupuesto getEstado() { return estado; }
  public void setEstado(EstadoPresupuesto estado) { this.estado = estado; }
  public String getObservacion() { return observacion; }
  public void setObservacion(String observacion) { this.observacion = observacion; }
  public BigDecimal getTotal() { return total; }
  public void setTotal(BigDecimal total) { this.total = total; }
  public List<PresupuestoItem> getItems() { return items; }
}
