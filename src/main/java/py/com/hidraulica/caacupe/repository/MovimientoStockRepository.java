package py.com.hidraulica.caacupe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import py.com.hidraulica.caacupe.domain.MovimientoStock;
import py.com.hidraulica.caacupe.domain.enums.TipoMovimientoStock;

public interface MovimientoStockRepository
  extends JpaRepository<MovimientoStock, Long>, JpaSpecificationExecutor<MovimientoStock> {

  Optional<MovimientoStock> findFirstByReferenciaTipoAndReferenciaIdAndTipoOrderByIdDesc(
      String referenciaTipo, Long referenciaId, TipoMovimientoStock tipo);

  boolean existsByReferenciaTipoAndReferenciaId(String referenciaTipo, Long referenciaId);

  List<MovimientoStock> findByReferenciaTipoAndReferenciaId(String referenciaTipo, Long referenciaId);
  
}
