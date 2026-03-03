package py.com.hidraulica.caacupe.domain.security;

import py.com.hidraulica.caacupe.domain.BaseEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rol", uniqueConstraints = @UniqueConstraint(name = "uk_rol_nombre", columnNames = "nombre"))
public class Rol extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nombre", nullable = false, length = 50)
  private String nombre;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "rol_permiso",
      joinColumns = @JoinColumn(name = "rol_id"),
      inverseJoinColumns = @JoinColumn(name = "permiso_id"),
      uniqueConstraints = @UniqueConstraint(name = "uk_rol_permiso", columnNames = {"rol_id","permiso_id"})
  )
  private Set<Permiso> permisos = new HashSet<>();

  public Long getId() { return id; }

  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }

  public Set<Permiso> getPermisos() { return permisos; }
  public void setPermisos(Set<Permiso> permisos) { this.permisos = permisos; }
}
