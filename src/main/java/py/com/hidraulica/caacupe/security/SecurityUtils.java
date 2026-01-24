package py.com.hidraulica.caacupe.security;

import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

  private SecurityUtils() {}

  public static Long currentUserId() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      throw new IllegalStateException("Usuario no autenticado");
    }

    if (auth.getPrincipal() instanceof AuthUserDetails u) {
      return u.getId();
    }

    throw new IllegalStateException("Principal no soportado: " + auth.getPrincipal());
  }
}
