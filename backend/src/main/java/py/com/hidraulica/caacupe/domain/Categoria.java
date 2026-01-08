package py.com.hidraulica.caacupe.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(
    name = "categoria",
    uniqueConstraints = @UniqueConstraint(name = "uk_categoria_nombre", columnNames = "nombre")
)
public class Categoria extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(nullable = false, length = 120)
  private String nombre;

  @Column(nullable = false)
  private boolean activo = true;

  public Long getId() { return id; }

  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }

  public boolean isActivo() { return activo; }
  public void setActivo(boolean activo) { this.activo = activo; }
}
