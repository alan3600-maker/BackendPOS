package py.com.hidraulica.caacupe.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "servicio")
public class Servicio extends BaseEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "codigo", length = 60, unique = true)
  private String codigo;

  @NotBlank
  @Column(name = "descripcion", nullable = false, length = 250)
  private String descripcion;

  @NotNull
  @Column(name = "precio", nullable = false, precision = 19, scale = 2)
  private BigDecimal precio = BigDecimal.ZERO;

  @Column(name = "activo", nullable = false)
  private boolean activo = true;

  public Long getId() { return id; }
  public String getCodigo() { return codigo; }
  public void setCodigo(String v) { this.codigo = v; }
  public String getDescripcion() { return descripcion; }
  public void setDescripcion(String v) { this.descripcion = v; }
  public BigDecimal getPrecio() { return precio; }
  public void setPrecio(BigDecimal v) { this.precio = v; }
  public boolean isActivo() { return activo; }
  public void setActivo(boolean v) { this.activo = v; }
}
