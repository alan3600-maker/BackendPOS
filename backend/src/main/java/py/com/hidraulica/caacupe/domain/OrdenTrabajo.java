package py.com.hidraulica.caacupe.domain;

import py.com.hidraulica.caacupe.domain.enums.EstadoOrdenTrabajo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orden_trabajo")
public class OrdenTrabajo extends BaseEntity {
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
  private EstadoOrdenTrabajo estado = EstadoOrdenTrabajo.ABIERTA;

  @Column(name = "descripcion", length = 500)
  private String descripcion;

  @JsonManagedReference
  @OneToMany(mappedBy = "ordenTrabajo", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrdenTrabajoRepuesto> repuestos = new ArrayList<>();

  public Long getId() { return id; }
  public Cliente getCliente() { return cliente; }
  public void setCliente(Cliente cliente) { this.cliente = cliente; }
  public OffsetDateTime getFecha() { return fecha; }
  public void setFecha(OffsetDateTime fecha) { this.fecha = fecha; }
  public EstadoOrdenTrabajo getEstado() { return estado; }
  public void setEstado(EstadoOrdenTrabajo estado) { this.estado = estado; }
  public String getDescripcion() { return descripcion; }
  public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
  public List<OrdenTrabajoRepuesto> getRepuestos() { return repuestos; }
}
