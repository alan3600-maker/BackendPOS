package py.com.hidraulica.caacupe.controller;

import java.util.List;

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
import py.com.hidraulica.caacupe.domain.Categoria;
import py.com.hidraulica.caacupe.dto.CategoriaDto;
import py.com.hidraulica.caacupe.dto.CategoriaSearchDto;
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
  public PageResponse<CategoriaDto> listar(@PageableDefault(size = 20, sort = "id") Pageable pageable,
      CategoriaSearchDto search) {
    return service.search(search, pageable);
  }

  @GetMapping("/all")
  public List<Categoria> list() {
    return service.list();
  }

  @GetMapping("/search")
  public PageResponse<CategoriaDto> search(@RequestParam(required = false) String q,
      @RequestParam(required = false) Boolean incluirInactivos, @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "desc") String dir) {
    return service.searchDto(q, incluirInactivos, page, size, sortBy, dir);
  }
}
