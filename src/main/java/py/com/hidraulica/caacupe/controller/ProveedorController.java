package py.com.hidraulica.caacupe.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import py.com.hidraulica.caacupe.domain.Proveedor;
import py.com.hidraulica.caacupe.dto.PageResponse;
import py.com.hidraulica.caacupe.dto.ProveedorDto;
import py.com.hidraulica.caacupe.dto.ProveedorSearchDto;
import py.com.hidraulica.caacupe.service.ProveedorService;

@RestController
@RequestMapping("/api/v1/proveedores")
public class ProveedorController {

  private final ProveedorService service;

  public ProveedorController(ProveedorService service) {
    this.service = service;
  }

  @PostMapping
  public Proveedor create(@RequestBody @Valid Proveedor body) {
    return service.create(body);
  }

  @GetMapping("/{id}")
  public Proveedor get(@PathVariable Long id) {
    return service.get(id);
  }

  @PutMapping("/{id}")
  public Proveedor update(@PathVariable Long id, @RequestBody @Valid Proveedor body) {
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

  @GetMapping
  public PageResponse<ProveedorDto> listar(@PageableDefault(size = 20, sort = "id") Pageable pageable,
      ProveedorSearchDto search) {
    return service.search(search, pageable);
  }

  @GetMapping("/all")
  public List<Proveedor> list() {
    return service.list();
  }

  @GetMapping("/search")
  public PageResponse<ProveedorDto> search(@RequestParam(required = false) String q,
      @RequestParam(required = false) Boolean incluirInactivos, @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "desc") String dir) {
    return service.searchDto(q, incluirInactivos, page, size, sortBy, dir);
  }

  @GetMapping("/exists-ruc")
  public Map<String, Object> existsRuc(@RequestParam String ruc) {
    return Map.of("ruc", ruc, "exists", service.existsByRuc(ruc));
  }
}
