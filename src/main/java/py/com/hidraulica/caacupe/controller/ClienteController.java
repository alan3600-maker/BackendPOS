package py.com.hidraulica.caacupe.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import py.com.hidraulica.caacupe.domain.Cliente;
import py.com.hidraulica.caacupe.dto.ClienteDto;
import py.com.hidraulica.caacupe.dto.ClienteSearchDto;
import py.com.hidraulica.caacupe.dto.PageResponse;
import py.com.hidraulica.caacupe.service.ClienteService;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

  private final ClienteService service;

  public ClienteController(ClienteService service) {
    this.service = service;
  }

  @PostMapping
  public Cliente create(@RequestBody @Valid Cliente body) {
    return service.create(body);
  }

  @GetMapping("/{id}")
  public Cliente get(@PathVariable Long id) {
    return service.get(id);
  }
  @GetMapping
  public PageResponse<ClienteDto> listar(
      @PageableDefault(size = 20, sort = "id") Pageable pageable,
      ClienteSearchDto search
  ) {
    return service.search(search, pageable);
  }


  @GetMapping("/all")
  public List<Cliente> list() {
    return service.list();
  }

  @PutMapping("/{id}")
  public Cliente update(@PathVariable Long id, @RequestBody @Valid Cliente body) {
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
  public PageResponse<ClienteDto> search(
      @RequestParam(required = false) String q,
      @RequestParam(required = false) Boolean incluirInactivos,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "desc") String dir
  ) {
    return service.searchDto(q, incluirInactivos, page, size, sortBy, dir);
  }
  
  @GetMapping("/exists-ruc")
  public Map<String, Object> existsRuc(@RequestParam String ruc) {
    return Map.of("ruc", ruc, "exists", service.existsByRuc(ruc));
  }
}
