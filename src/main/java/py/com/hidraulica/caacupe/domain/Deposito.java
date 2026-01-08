package py.com.hidraulica.caacupe.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "deposito")
public class Deposito extends BaseEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(name = "nombre", nullable = false, length = 120)
  private String nombre;

  public Long getId() { return id; }
  public String getNombre() { return nombre; }
  public void setNombre(String v) { this.nombre = v; }
}
