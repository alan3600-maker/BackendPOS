package py.com.hidraulica.caacupe.controller;

import py.com.hidraulica.caacupe.domain.Stock;
import py.com.hidraulica.caacupe.service.StockService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks")
public class StockController {

  private final StockService service;

  public StockController(StockService service) {
    this.service = service;
  }

  @PostMapping
  public Stock create(@RequestBody @Valid Stock body) {
    return service.create(body);
  }

  @GetMapping("/{id}")
  public Stock get(@PathVariable Long id) {
    return service.get(id);
  }

  @GetMapping
  public List<Stock> list() {
    return service.list();
  }

  @PutMapping("/{id}")
  public Stock update(@PathVariable Long id, @RequestBody @Valid Stock body) {
    return service.update(id, body);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }

  @GetMapping("/by-producto-deposito")
  public Stock byProductoDeposito(@RequestParam Long productoId, @RequestParam Long depositoId) {
    return service.getByProductoDeposito(productoId, depositoId);
  }

  @GetMapping("/by-producto/{productoId}")
  public List<Stock> byProducto(@PathVariable Long productoId) {
    return service.listByProducto(productoId);
  }

  @GetMapping("/by-deposito/{depositoId}")
  public List<Stock> byDeposito(@PathVariable Long depositoId) {
    return service.listByDeposito(depositoId);
  }
}
