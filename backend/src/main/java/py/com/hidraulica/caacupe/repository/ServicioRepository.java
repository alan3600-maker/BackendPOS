package py.com.hidraulica.caacupe.repository;

import py.com.hidraulica.caacupe.domain.Servicio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {
  Page<Servicio> findByDescripcionContainingIgnoreCase(String q, Pageable pageable);
  Optional<Servicio> findByCodigo(String codigo);
  Page<Servicio> findByActivo(boolean activo, Pageable pageable);
}
