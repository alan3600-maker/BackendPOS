package py.com.hidraulica.caacupe.repository;

import py.com.hidraulica.caacupe.domain.OrdenTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrdenTrabajoRepository extends JpaRepository<OrdenTrabajo, Long>, JpaSpecificationExecutor<OrdenTrabajo> {}
