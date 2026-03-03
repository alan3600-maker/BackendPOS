package py.com.hidraulica.caacupe.controller.admin;

import py.com.hidraulica.caacupe.dto.admin.UsuarioCreateRequest;
import py.com.hidraulica.caacupe.dto.admin.UsuarioResponse;
import py.com.hidraulica.caacupe.dto.admin.UsuarioUpdateRequest;
import py.com.hidraulica.caacupe.service.admin.UsuarioAdminService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/usuarios")
public class UsuarioAdminController {

	private final UsuarioAdminService service;

	public UsuarioAdminController(UsuarioAdminService service) {
		this.service = service;
	}

	@GetMapping
	public Page<UsuarioResponse> list(Pageable pageable) {
		return service.list(pageable).map(u -> new UsuarioResponse(u.getId(), u.getUsername(), u.isActivo(),
				u.getRoles().stream().map(r -> r.getNombre()).collect(Collectors.toSet())));
	}

	@PostMapping
	public UsuarioResponse create(@Valid @RequestBody UsuarioCreateRequest req) {
		var u = service.create(req.getUsername(), req.getPassword(), req.isActivo(), req.getRoles());
		return new UsuarioResponse(u.getId(), u.getUsername(), u.isActivo(),
				u.getRoles().stream().map(r -> r.getNombre()).collect(Collectors.toSet()));
	}

	@PutMapping("/{id}")
	public UsuarioResponse update(@PathVariable Long id, @RequestBody UsuarioUpdateRequest req) {
		var u = service.update(id, req.getActivo(), req.getRoles());
		return new UsuarioResponse(u.getId(), u.getUsername(), u.isActivo(),
				u.getRoles().stream().map(r -> r.getNombre()).collect(Collectors.toSet()));
	}

	@PostMapping("/{id}/reset-password")
	public ResponseEntity<?> resetPassword(@PathVariable Long id, @RequestBody Map<String, String> body) {
		service.resetPassword(id, body.getOrDefault("password", ""));
		return ResponseEntity.ok().build();
	}
}
