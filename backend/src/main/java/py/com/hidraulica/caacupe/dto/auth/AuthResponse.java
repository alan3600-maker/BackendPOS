package py.com.hidraulica.caacupe.dto.auth;

import java.util.Set;

public class AuthResponse {
  private String token;
  private String username;
  private Set<String> roles;
  private Set<String> permisos;

  public AuthResponse() {}

  public AuthResponse(String token, String username, Set<String> roles, Set<String> permisos) {
    this.token = token;
    this.username = username;
    this.roles = roles;
    this.permisos = permisos;
  }

  public String getToken() { return token; }
  public void setToken(String token) { this.token = token; }

  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }

  public Set<String> getRoles() { return roles; }
  public void setRoles(Set<String> roles) { this.roles = roles; }

  public Set<String> getPermisos() { return permisos; }
  public void setPermisos(Set<String> permisos) { this.permisos = permisos; }
}
