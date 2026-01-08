package py.com.hidraulica.caacupe.domain;

import py.com.hidraulica.caacupe.domain.enums.TipoMovimientoStock;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movimiento_stock")
public class MovimientoStock extends BaseEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "tipo", nullable = false, length = 30)
  private TipoMovimientoStock tipo;

  @NotNull
  @Column(name = "fecha", nullable = false)
  private OffsetDateTime fecha = OffsetDateTime.now();

  @Column(name = "referencia_tipo", length = 60)
  private String referenciaTipo;

  @Column(name = "referencia_id")
  private Long referenciaId;

  @JsonManagedReference
  @OneToMany(mappedBy = "movimiento", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MovimientoStockItem> items = new ArrayList<>();

  public Long getId() { return id; }
  public TipoMovimientoStock getTipo() { return tipo; }
  public void setTipo(TipoMovimientoStock v) { this.tipo = v; }
  public OffsetDateTime getFecha() { return fecha; }
  public void setFecha(OffsetDateTime v) { this.fecha = v; }
  public String getReferenciaTipo() { return referenciaTipo; }
  public void setReferenciaTipo(String v) { this.referenciaTipo = v; }
  public Long getReferenciaId() { return referenciaId; }
  public void setReferenciaId(Long v) { this.referenciaId = v; }
  public List<MovimientoStockItem> getItems() { return items; }
}
