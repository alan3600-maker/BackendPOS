package py.com.hidraulica.caacupe.repository;

import py.com.hidraulica.caacupe.domain.Presupuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long>, JpaSpecificationExecutor<Presupuesto> {}
