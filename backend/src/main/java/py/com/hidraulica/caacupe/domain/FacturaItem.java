package py.com.hidraulica.caacupe.domain;

import py.com.hidraulica.caacupe.domain.enums.TipoItem;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "factura_item")
public class FacturaItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonBackReference
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "factura_id", nullable = false)
  private Factura factura;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "tipo", nullable = false, length = 20)
  private TipoItem tipo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "producto_id")
  private Producto producto;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "servicio_id")
  private Servicio servicio;

  @Column(name = "descripcion", nullable = false, length = 250)
  private String descripcion;

  @Column(name = "cantidad", nullable = false, precision = 19, scale = 3)
  private BigDecimal cantidad = BigDecimal.ONE;

  @Column(name = "precio_unitario", nullable = false, precision = 19, scale = 2)
  private BigDecimal precioUnitario = BigDecimal.ZERO;

  @Column(name = "total_linea", nullable = false, precision = 19, scale = 2)
  private BigDecimal totalLinea = BigDecimal.ZERO;

  public Long getId() { return id; }

  public Factura getFactura() { return factura; }
  public void setFactura(Factura factura) { this.factura = factura; }

  public TipoItem getTipo() { return tipo; }
  public void setTipo(TipoItem tipo) { this.tipo = tipo; }

  public Producto getProducto() { return producto; }
  public void setProducto(Producto producto) { this.producto = producto; }

  public Servicio getServicio() { return servicio; }
  public void setServicio(Servicio servicio) { this.servicio = servicio; }

  public String getDescripcion() { return descripcion; }
  public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

  public BigDecimal getCantidad() { return cantidad; }
  public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }

  public BigDecimal getPrecioUnitario() { return precioUnitario; }
  public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

  public BigDecimal getTotalLinea() { return totalLinea; }
  public void setTotalLinea(BigDecimal totalLinea) { this.totalLinea = totalLinea; }
}
