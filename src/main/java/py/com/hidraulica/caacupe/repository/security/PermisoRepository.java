package py.com.hidraulica.caacupe.repository.security;

import py.com.hidraulica.caacupe.domain.security.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermisoRepository extends JpaRepository<Permiso, Long> {
  Optional<Permiso> findByNombre(String nombre);
}
