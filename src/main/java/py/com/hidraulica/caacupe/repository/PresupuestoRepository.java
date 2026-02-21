package py.com.hidraulica.caacupe.repository;

import py.com.hidraulica.caacupe.domain.Presupuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long>, JpaSpecificationExecutor<Presupuesto> {

  /**
   * Para vistas tipo "listado", necesitamos traer el cliente para evitar LazyInitializationException
   * al serializar (cliente es LAZY).
   */
  @Query("select p from Presupuesto p join fetch p.cliente c order by p.id desc")
  List<Presupuesto> findAllWithCliente();
}
