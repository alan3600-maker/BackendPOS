package py.com.hidraulica.caacupe.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import py.com.hidraulica.caacupe.domain.Proveedor;
import py.com.hidraulica.caacupe.dto.CatalogoSearchDto;
import py.com.hidraulica.caacupe.dto.PageResponse;
import py.com.hidraulica.caacupe.dto.ProveedorDto;
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
  public PageResponse<ProveedorDto> search(
      @PageableDefault(size = 20, sort = "id") Pageable pageable,
      CatalogoSearchDto search
  ) {
    return service.search(search, pageable);
  }
}
