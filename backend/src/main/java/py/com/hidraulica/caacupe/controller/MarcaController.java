package py.com.hidraulica.caacupe.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import py.com.hidraulica.caacupe.domain.Marca;
import py.com.hidraulica.caacupe.dto.CatalogoSearchDto;
import py.com.hidraulica.caacupe.dto.MarcaDto;
import py.com.hidraulica.caacupe.dto.PageResponse;
import py.com.hidraulica.caacupe.service.MarcaService;

@RestController
@RequestMapping("/api/v1/marcas")
public class MarcaController {

  private final MarcaService service;

  public MarcaController(MarcaService service) {
    this.service = service;
  }

  @PostMapping
  public Marca create(@RequestBody @Valid Marca body) {
    return service.create(body);
  }

  @GetMapping("/{id}")
  public Marca get(@PathVariable Long id) {
    return service.get(id);
  }

  @PutMapping("/{id}")
  public Marca update(@PathVariable Long id, @RequestBody @Valid Marca body) {
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
  public PageResponse<MarcaDto> search(
      @PageableDefault(size = 20, sort = "id") Pageable pageable,
      CatalogoSearchDto search
  ) {
    return service.search(search, pageable);
  }
}
