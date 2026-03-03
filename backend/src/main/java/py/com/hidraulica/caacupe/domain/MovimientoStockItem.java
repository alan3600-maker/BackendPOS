package py.com.hidraulica.caacupe.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "movimiento_stock_item")
public class MovimientoStockItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonBackReference
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "movimiento_id", nullable = false)
  private MovimientoStock movimiento;

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

  public Long getId() { return id; }
  public MovimientoStock getMovimiento() { return movimiento; }
  public void setMovimiento(MovimientoStock v) { this.movimiento = v; }
  public Producto getProducto() { return producto; }
  public void setProducto(Producto v) { this.producto = v; }
  public Deposito getDeposito() { return deposito; }
  public void setDeposito(Deposito v) { this.deposito = v; }
  public BigDecimal getCantidad() { return cantidad; }
  public void setCantidad(BigDecimal v) { this.cantidad = v; }
}
