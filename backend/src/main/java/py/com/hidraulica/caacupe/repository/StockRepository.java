package py.com.hidraulica.caacupe.repository;

import py.com.hidraulica.caacupe.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
  Optional<Stock> findByProductoIdAndDepositoId(Long productoId, Long depositoId);
  List<Stock> findByProductoId(Long productoId);
  List<Stock> findByDepositoId(Long depositoId);
}
