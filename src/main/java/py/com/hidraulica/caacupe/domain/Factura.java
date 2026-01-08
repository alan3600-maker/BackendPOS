package py.com.hidraulica.caacupe.domain;

import py.com.hidraulica.caacupe.domain.enums.EstadoFactura;
import py.com.hidraulica.caacupe.domain.enums.TipoFactura;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "factura")
public class Factura extends BaseEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "tipo", nullable = false, length = 20)
  private TipoFactura tipo;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "estado", nullable = false, length = 20)
  private EstadoFactura estado = EstadoFactura.EMITIDA;

  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "cliente_id", nullable = false)
  private Cliente cliente;

  // Relación opcional con venta confirmada
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "venta_id")
  private Venta venta;

  @NotNull
  @Column(name = "fecha", nullable = false)
  private OffsetDateTime fecha = OffsetDateTime.now();

  // Numeración interna (NO fiscal) o numeración fiscal (serie + número)
  @Column(name = "serie", length = 20)
  private String serie;

  @Column(name = "numero", length = 40, unique = true)
  private String numero;

  // Datos base para Paraguay (timbrado): se dejan opcionales como "base"
  @Column(name = "timbrado", length = 30)
  private String timbrado;

  @Column(name = "timbrado_vencimiento")
  private LocalDate timbradoVencimiento;

  @Column(name = "total", nullable = false, precision = 19, scale = 2)
  private BigDecimal total = BigDecimal.ZERO;

  @JsonManagedReference
  @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<FacturaItem> items = new ArrayList<>();

  public Long getId() { return id; }

  public TipoFactura getTipo() { return tipo; }
  public void setTipo(TipoFactura tipo) { this.tipo = tipo; }

  public EstadoFactura getEstado() { return estado; }
  public void setEstado(EstadoFactura estado) { this.estado = estado; }

  public Cliente getCliente() { return cliente; }
  public void setCliente(Cliente cliente) { this.cliente = cliente; }

  public Venta getVenta() { return venta; }
  public void setVenta(Venta venta) { this.venta = venta; }

  public OffsetDateTime getFecha() { return fecha; }
  public void setFecha(OffsetDateTime fecha) { this.fecha = fecha; }

  public String getSerie() { return serie; }
  public void setSerie(String serie) { this.serie = serie; }

  public String getNumero() { return numero; }
  public void setNumero(String numero) { this.numero = numero; }

  public String getTimbrado() { return timbrado; }
  public void setTimbrado(String timbrado) { this.timbrado = timbrado; }

  public LocalDate getTimbradoVencimiento() { return timbradoVencimiento; }
  public void setTimbradoVencimiento(LocalDate timbradoVencimiento) { this.timbradoVencimiento = timbradoVencimiento; }

  public BigDecimal getTotal() { return total; }
  public void setTotal(BigDecimal total) { this.total = total; }

  public List<FacturaItem> getItems() { return items; }
}
