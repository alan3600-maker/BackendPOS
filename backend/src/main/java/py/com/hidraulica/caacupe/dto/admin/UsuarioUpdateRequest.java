package py.com.hidraulica.caacupe.dto.admin;

import java.util.Set;

public class UsuarioUpdateRequest {
  private Boolean activo;
  private Set<String> roles;

  public Boolean getActivo() { return activo; }
  public void setActivo(Boolean activo) { this.activo = activo; }

  public Set<String> getRoles() { return roles; }
  public void setRoles(Set<String> roles) { this.roles = roles; }
}
