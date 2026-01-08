package py.com.hidraulica.caacupe.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "stock", uniqueConstraints = @UniqueConstraint(name = "uk_stock_producto_deposito", columnNames = { "producto_id", "deposito_id" }))
public class Stock extends BaseEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "producto_id", nullable = false)
  private Producto producto;

  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "deposito_id", nullable = false)
  private Deposito deposito;

  @Column(name = "cantidad", nullable = false, precision = 19, scale = 3)
  private BigDecimal cantidad = BigDecimal.ZERO;

  @Version
  private Long version;

  public Long getId() { return id; }
  public Producto getProducto() { return producto; }
  public void setProducto(Producto v) { this.producto = v; }
  public Deposito getDeposito() { return deposito; }
  public void setDeposito(Deposito v) { this.deposito = v; }
  public BigDecimal getCantidad() { return cantidad; }
  public void setCantidad(BigDecimal v) { this.cantidad = v; }
}
