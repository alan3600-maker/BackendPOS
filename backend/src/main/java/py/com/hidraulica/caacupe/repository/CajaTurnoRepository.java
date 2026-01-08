package py.com.hidraulica.caacupe.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import py.com.hidraulica.caacupe.domain.CajaTurno;
import py.com.hidraulica.caacupe.domain.enums.EstadoTurnoCaja;

public interface CajaTurnoRepository extends JpaRepository<CajaTurno, Long> {

	Optional<CajaTurno> findTopByCajaIdAndEstadoOrderByFechaAperturaDesc(Long cajaId, String name);

	Optional<CajaTurno> findTopByCajaIdAndUsuarioAperturaIdAndEstadoOrderByFechaAperturaDesc(Long cajaId,
			Long usuarioId, String name);
  
}
