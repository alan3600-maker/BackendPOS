package py.com.hidraulica.caacupe.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import py.com.hidraulica.caacupe.domain.Sucursal;
import py.com.hidraulica.caacupe.service.SucursalService;

@RestController
@RequestMapping("/api/sucursales")
public class SucursalController {

	private final SucursalService service;

	public SucursalController(SucursalService service) {
		this.service = service;
	}

	@GetMapping
	public List<Sucursal> listar(@RequestParam(value = "soloActivas", defaultValue = "true") boolean soloActivas) {
		return service.listar(soloActivas);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Sucursal crear(@RequestBody Sucursal body) {
		return service.crear(body);
	}

	@PutMapping("/{id}")
	public Sucursal actualizar(@PathVariable Long id, @RequestBody Sucursal body) {
		return service.actualizar(id, body);
	}

	@PostMapping("/{id}/activar")
	public Sucursal activar(@PathVariable Long id) {
		return service.setActivo(id, true);
	}

	@PostMapping("/{id}/desactivar")
	public Sucursal desactivar(@PathVariable Long id) {
		return service.setActivo(id, false);
	}
}
