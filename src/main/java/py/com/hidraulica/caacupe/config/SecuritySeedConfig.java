package py.com.hidraulica.caacupe.config;

import py.com.hidraulica.caacupe.domain.security.Permiso;
import py.com.hidraulica.caacupe.domain.security.Rol;
import py.com.hidraulica.caacupe.domain.security.Usuario;
import py.com.hidraulica.caacupe.repository.security.PermisoRepository;
import py.com.hidraulica.caacupe.repository.security.RolRepository;
import py.com.hidraulica.caacupe.repository.security.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class SecuritySeedConfig {

  @Bean
  ApplicationRunner seedSecurityData(
      PermisoRepository permisoRepo,
      RolRepository rolRepo,
      UsuarioRepository usuarioRepo,
      PasswordEncoder encoder,
      @Value("${app.seed.admin-username:admin}") String adminUsername,
      @Value("${app.seed.admin-password:admin123}") String adminPassword
  ) {
    return args -> seed(permisoRepo, rolRepo, usuarioRepo, encoder, adminUsername, adminPassword);
  }

  @Transactional
  void seed(PermisoRepository permisoRepo, RolRepository rolRepo, UsuarioRepository usuarioRepo,
            PasswordEncoder encoder, String adminUsername, String adminPassword) {

    // Permisos base (podemos ampliar)
    List<String> permisos = List.of(
        "CLIENTE_CRUD",
        "PRODUCTO_CRUD",
        "SERVICIO_CRUD",
        "STOCK_VER",
        "STOCK_MOVIMIENTO",
        "VENTA_CRUD",
        "VENTA_CONFIRMAR",
        "FACTURA_EMITIR",
        "OT_CRUD"
    );

    for (String p : permisos) {
      permisoRepo.findByNombre(p).orElseGet(() -> {
        Permiso pe = new Permiso();
        pe.setNombre(p);
        pe.setDescripcion(p);
        return permisoRepo.save(pe);
      });
    }

    var allPerms = permisoRepo.findAll().stream().collect(Collectors.toSet());
    var cajaPerms = permisoRepo.findAll().stream()
        .filter(p -> Set.of("CLIENTE_CRUD","PRODUCTO_CRUD","SERVICIO_CRUD","STOCK_VER","STOCK_MOVIMIENTO","VENTA_CRUD","VENTA_CONFIRMAR","FACTURA_EMITIR").contains(p.getNombre()))
        .collect(Collectors.toSet());
    var tallerPerms = permisoRepo.findAll().stream()
        .filter(p -> Set.of("OT_CRUD","STOCK_VER").contains(p.getNombre()))
        .collect(Collectors.toSet());

    Rol admin = rolRepo.findByNombre("ADMIN").orElseGet(() -> {
      Rol r = new Rol();
      r.setNombre("ADMIN");
      r.setPermisos(allPerms);
      return rolRepo.save(r);
    });
    admin.setPermisos(allPerms);
    rolRepo.save(admin);

    Rol cajero = rolRepo.findByNombre("CAJERO").orElseGet(() -> {
      Rol r = new Rol();
      r.setNombre("CAJERO");
      r.setPermisos(cajaPerms);
      return rolRepo.save(r);
    });
    cajero.setPermisos(cajaPerms);
    rolRepo.save(cajero);

    Rol taller = rolRepo.findByNombre("TALLER").orElseGet(() -> {
      Rol r = new Rol();
      r.setNombre("TALLER");
      r.setPermisos(tallerPerms);
      return rolRepo.save(r);
    });
    taller.setPermisos(tallerPerms);
    rolRepo.save(taller);

    // Admin user inicial
    Usuario adminUser = usuarioRepo.findByUsername(adminUsername).orElseGet(() -> {
      Usuario u = new Usuario();
      u.setUsername(adminUsername);
      u.setPasswordHash(encoder.encode(adminPassword));
      u.setActivo(true);
      u.getRoles().add(admin);
      return usuarioRepo.save(u);
    });
    // Asegurar rol ADMIN
    if (adminUser.getRoles().stream().noneMatch(r -> "ADMIN".equals(r.getNombre()))) {
      adminUser.getRoles().add(admin);
      usuarioRepo.save(adminUser);
    }
  }
}
