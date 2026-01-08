package py.com.hidraulica.caacupe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import py.com.hidraulica.caacupe.domain.Sucursal;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

	List<Sucursal> findByActivoTrue();
}
