package py.com.hidraulica.caacupe.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import py.com.hidraulica.caacupe.domain.Caja;

public interface CajaRepository extends JpaRepository<Caja, Long> {
  List<Caja> findBySucursalIdAndActivoTrueOrderByNombreAsc(Long sucursalId);
  List<Caja> findBySucursalIdOrderByNombreAsc(Long sucursalId);
}
