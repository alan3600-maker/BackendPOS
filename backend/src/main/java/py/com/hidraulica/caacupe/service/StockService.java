package py.com.hidraulica.caacupe.service;

import py.com.hidraulica.caacupe.domain.Stock;
import py.com.hidraulica.caacupe.exception.NotFoundException;
import py.com.hidraulica.caacupe.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService implements CrudService<Stock, Long> {

  private final StockRepository repo;

  public StockService(StockRepository repo) {
    this.repo = repo;
  }

  @Override
  public Stock create(Stock entity) {
    return repo.save(entity);
  }

  @Override
  public Stock get(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Stock no encontrado: " + id));
  }

  @Override
  public List<Stock> list() {
    return repo.findAll();
  }

  public Stock getByProductoDeposito(Long productoId, Long depositoId) {
    return repo.findByProductoIdAndDepositoId(productoId, depositoId)
        .orElseThrow(() -> new NotFoundException("Stock no encontrado. productoId=" + productoId + ", depositoId=" + depositoId));
  }

  public List<Stock> listByProducto(Long productoId) {
    return repo.findByProductoId(productoId);
  }

  public List<Stock> listByDeposito(Long depositoId) {
    return repo.findByDepositoId(depositoId);
  }

  @Override
  public Stock update(Long id, Stock entity) {
    var current = get(id);
    current.setProducto(entity.getProducto());
    current.setDeposito(entity.getDeposito());
    current.setCantidad(entity.getCantidad());
    return repo.save(current);
  }

  @Override
  public void delete(Long id) {
    if (!repo.existsById(id)) throw new NotFoundException("Stock no encontrado: " + id);
    repo.deleteById(id);
  }
}
