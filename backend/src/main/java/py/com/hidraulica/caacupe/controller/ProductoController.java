package py.com.hidraulica.caacupe.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import py.com.hidraulica.caacupe.domain.Producto;
import py.com.hidraulica.caacupe.dto.PageResponse;
import py.com.hidraulica.caacupe.dto.ProductoDto;
import py.com.hidraulica.caacupe.service.ProductoService;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

	private final ProductoService service;

	public ProductoController(ProductoService service) {
		this.service = service;
	}

	@PostMapping
	public Producto create(@RequestBody @Valid Producto body) {
		return service.create(body);
	}

	@GetMapping("/{id}")
	public Producto get(@PathVariable Long id) {
		return service.get(id);
	}

	@GetMapping
	public List<Producto> list() {
		return service.list();
	}

	@PutMapping("/{id}")
	public Producto update(@PathVariable Long id, @RequestBody @Valid Producto body) {
		return service.update(id, body);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		service.desactivar(id);
	}

	@PutMapping("/{id}/activar")
	public void activar(@PathVariable Long id) {
		service.activar(id);
	}

@GetMapping("/search")
	public PageResponse<ProductoDto> search(
	    @RequestParam(required = false) String q,
	    @RequestParam(required = false) Boolean incluirInactivos,
	    @RequestParam(defaultValue = "0") int page,
	    @RequestParam(defaultValue = "10") int size,
	    @RequestParam(defaultValue = "id") String sortBy,
	    @RequestParam(defaultValue = "desc") String dir
	) {
	  return service.searchDto(q, incluirInactivos, page, size, sortBy, dir);
	}

	@GetMapping("/by-sku/{sku}")
	public Producto getBySku(@PathVariable String sku) {
		return service.getBySku(sku);
	}

}
