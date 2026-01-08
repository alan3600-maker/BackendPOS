package py.com.hidraulica.caacupe.domain.security;

import py.com.hidraulica.caacupe.domain.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "permiso", uniqueConstraints = @UniqueConstraint(name = "uk_permiso_nombre", columnNames = "nombre"))
public class Permiso extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nombre", nullable = false, length = 80)
  private String nombre;

  @Column(name = "descripcion", length = 200)
  private String descripcion;

  public Long getId() { return id; }

  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }

  public String getDescripcion() { return descripcion; }
  public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
