package py.com.hidraulica.caacupe.service.admin;

import py.com.hidraulica.caacupe.domain.security.Usuario;
import py.com.hidraulica.caacupe.exception.BusinessException;
import py.com.hidraulica.caacupe.exception.NotFoundException;
import py.com.hidraulica.caacupe.repository.security.RolRepository;
import py.com.hidraulica.caacupe.repository.security.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class UsuarioAdminService {

  private final UsuarioRepository usuarioRepo;
  private final RolRepository rolRepo;
  private final PasswordEncoder encoder;

  public UsuarioAdminService(UsuarioRepository usuarioRepo, RolRepository rolRepo, PasswordEncoder encoder) {
    this.usuarioRepo = usuarioRepo;
    this.rolRepo = rolRepo;
    this.encoder = encoder;
  }

  @Transactional
  public Usuario create(String username, String rawPassword, boolean activo, java.util.Set<String> roles) {
    if (usuarioRepo.existsByUsername(username)) {
      throw new BusinessException("Ya existe un usuario con username: " + username);
    }
    Usuario u = new Usuario();
    u.setUsername(username);
    u.setPasswordHash(encoder.encode(rawPassword));
    u.setActivo(activo);
    u.setRoles(roles.stream()
        .map(rn -> rolRepo.findByNombre(rn).orElseThrow(() -> new NotFoundException("Rol no existe: " + rn)))
        .collect(Collectors.toSet()));
    return usuarioRepo.save(u);
  }

  public Usuario get(Long id) {
    return usuarioRepo.findById(id).orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + id));
  }

  public org.springframework.data.domain.Page<Usuario> list(org.springframework.data.domain.Pageable pageable) {
    return usuarioRepo.findAll(pageable);
  }

  @Transactional
  public Usuario update(Long id, Boolean activo, java.util.Set<String> roles) {
    Usuario u = get(id);
    if (activo != null) u.setActivo(activo);
    if (roles != null) {
      u.setRoles(roles.stream()
          .map(rn -> rolRepo.findByNombre(rn).orElseThrow(() -> new NotFoundException("Rol no existe: " + rn)))
          .collect(Collectors.toSet()));
    }
    return usuarioRepo.save(u);
  }

  @Transactional
  public void resetPassword(Long id, String rawPassword) {
    Usuario u = get(id);
    u.setPasswordHash(encoder.encode(rawPassword));
    usuarioRepo.save(u);
  }
}
