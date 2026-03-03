package py.com.hidraulica.caacupe.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "orden_trabajo_repuesto")
public class OrdenTrabajoRepuesto {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonBackReference
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "orden_trabajo_id", nullable = false)
  private OrdenTrabajo ordenTrabajo;

  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "producto_id", nullable = false)
  private Producto producto;

  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "deposito_id", nullable = false)
  private Deposito deposito;

  @Column(name = "cantidad", nullable = false, precision = 19, scale = 3)
  private BigDecimal cantidad = BigDecimal.ONE;

  public Long getId() { return id; }
  public OrdenTrabajo getOrdenTrabajo() { return ordenTrabajo; }
  public void setOrdenTrabajo(OrdenTrabajo ordenTrabajo) { this.ordenTrabajo = ordenTrabajo; }
  public Producto getProducto() { return producto; }
  public void setProducto(Producto producto) { this.producto = producto; }
  public Deposito getDeposito() { return deposito; }
  public void setDeposito(Deposito deposito) { this.deposito = deposito; }
  public BigDecimal getCantidad() { return cantidad; }
  public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }
}
