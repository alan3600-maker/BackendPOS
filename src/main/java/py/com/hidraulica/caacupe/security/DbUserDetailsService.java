package py.com.hidraulica.caacupe.security;

import py.com.hidraulica.caacupe.repository.security.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class DbUserDetailsService implements UserDetailsService {

  private final UsuarioRepository usuarioRepo;

  public DbUserDetailsService(UsuarioRepository usuarioRepo) {
    this.usuarioRepo = usuarioRepo;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var u = usuarioRepo.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

    Set<GrantedAuthority> auth = new HashSet<>();
    u.getRoles().forEach(r -> {
      auth.add(new SimpleGrantedAuthority("ROLE_" + r.getNombre()));
      if (r.getPermisos() != null) {
        r.getPermisos().forEach(p -> auth.add(new SimpleGrantedAuthority("PERM_" + p.getNombre())));
      }
    });

    return org.springframework.security.core.userdetails.User
        .withUsername(u.getUsername())
        .password(u.getPasswordHash())
        .disabled(!u.isActivo())
        .authorities(auth)
        .build();
  }
}
