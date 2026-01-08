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
import py.com.hidraulica.caacupe.domain.Deposito;
import py.com.hidraulica.caacupe.dto.DepositoDto;
import py.com.hidraulica.caacupe.dto.PageResponse;
import py.com.hidraulica.caacupe.service.DepositoService;

@RestController
@RequestMapping("/api/v1/depositos")
public class DepositoController {
	private final DepositoService service;

	public DepositoController(DepositoService service) {
		this.service = service;
	}

	@PostMapping
	public Deposito create(@RequestBody @Valid Deposito body) {
		return service.create(body);
	}

	@PutMapping("/{id}")
	public Deposito update(@PathVariable Long id, @RequestBody @Valid Deposito body) {
		return service.update(id, body);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}
	
	@GetMapping("/search")
	public PageResponse<DepositoDto> search(
	    @RequestParam(required = false) String q,
	    @RequestParam(defaultValue="0") int page,
	    @RequestParam(defaultValue="10") int size,
	    @RequestParam(defaultValue="id") String sortBy,
	    @RequestParam(defaultValue="desc") String dir
	){
	  return service.searchDto(q, page, size, sortBy, dir);
	}

	@GetMapping
	public List<DepositoDto> list() {
	  return service.list().stream().map(service::toDto).toList();
	}

	@GetMapping("/{id}")
	public DepositoDto get(@PathVariable Long id) {
	  return service.toDto(service.get(id));
	}
}
