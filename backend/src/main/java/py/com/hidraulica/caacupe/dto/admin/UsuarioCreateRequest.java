package py.com.hidraulica.caacupe.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public class UsuarioCreateRequest {
  @NotBlank
  private String username;

  @NotBlank
  private String password;

  private boolean activo = true;

  @NotEmpty
  private Set<String> roles;

  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }

  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }

  public boolean isActivo() { return activo; }
  public void setActivo(boolean activo) { this.activo = activo; }

  public Set<String> getRoles() { return roles; }
  public void setRoles(Set<String> roles) { this.roles = roles; }
}
