package py.com.hidraulica.caacupe.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import py.com.hidraulica.caacupe.domain.Categoria;
import py.com.hidraulica.caacupe.dto.CatalogoSearchDto;
import py.com.hidraulica.caacupe.dto.CategoriaDto;
import py.com.hidraulica.caacupe.dto.PageResponse;
import py.com.hidraulica.caacupe.service.CategoriaService;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

  private final CategoriaService service;

  public CategoriaController(CategoriaService service) {
    this.service = service;
  }

  @PostMapping
  public Categoria create(@RequestBody @Valid Categoria body) {
    return service.create(body);
  }

  @GetMapping("/{id}")
  public Categoria get(@PathVariable Long id) {
    return service.get(id);
  }

  @PutMapping("/{id}")
  public Categoria update(@PathVariable Long id, @RequestBody @Valid Categoria body) {
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
  public PageResponse<CategoriaDto> search(
      @PageableDefault(size = 20, sort = "id") Pageable pageable,
      CatalogoSearchDto search
  ) {
    return service.search(search, pageable);
  }
}
