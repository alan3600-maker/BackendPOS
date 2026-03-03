package py.com.hidraulica.caacupe.dto.admin;

import java.util.Set;

public class UsuarioResponse {
  private Long id;
  private String username;
  private boolean activo;
  private Set<String> roles;

  public UsuarioResponse() {}

  public UsuarioResponse(Long id, String username, boolean activo, Set<String> roles) {
    this.id = id;
    this.username = username;
    this.activo = activo;
    this.roles = roles;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }

  public boolean isActivo() { return activo; }
  public void setActivo(boolean activo) { this.activo = activo; }

  public Set<String> getRoles() { return roles; }
  public void setRoles(Set<String> roles) { this.roles = roles; }
}
