package py.com.hidraulica.caacupe.domain.security;

import py.com.hidraulica.caacupe.domain.BaseEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuario", uniqueConstraints = @UniqueConstraint(name = "uk_usuario_username", columnNames = "username"))
public class Usuario extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "username", nullable = false, length = 80)
  private String username;

  @Column(name = "password_hash", nullable = false, length = 200)
  private String passwordHash;

  @Column(name = "activo", nullable = false)
  private boolean activo = true;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "usuario_rol",
      joinColumns = @JoinColumn(name = "usuario_id"),
      inverseJoinColumns = @JoinColumn(name = "rol_id"),
      uniqueConstraints = @UniqueConstraint(name = "uk_usuario_rol", columnNames = {"usuario_id","rol_id"})
  )
  private Set<Rol> roles = new HashSet<>();

  public Long getId() { return id; }

  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }

  public String getPasswordHash() { return passwordHash; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

  public boolean isActivo() { return activo; }
  public void setActivo(boolean activo) { this.activo = activo; }

  public Set<Rol> getRoles() { return roles; }
  public void setRoles(Set<Rol> roles) { this.roles = roles; }
}
