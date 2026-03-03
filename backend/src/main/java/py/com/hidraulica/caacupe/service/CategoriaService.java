package py.com.hidraulica.caacupe.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import py.com.hidraulica.caacupe.domain.Categoria;
import py.com.hidraulica.caacupe.dto.CategoriaDto;
import py.com.hidraulica.caacupe.dto.CategoriaSearchDto;
import py.com.hidraulica.caacupe.dto.PageResponse;
import py.com.hidraulica.caacupe.exception.BusinessException;
import py.com.hidraulica.caacupe.exception.NotFoundException;
import py.com.hidraulica.caacupe.repository.CategoriaRepository;

@Service
public class CategoriaService {

  private final CategoriaRepository repo;

  public CategoriaService(CategoriaRepository repo) {
    this.repo = repo;
  }

  private CategoriaDto toDto(Categoria c) {
    return new CategoriaDto(c.getId(), c.getNombre(), c.isActivo());
  }

  public Categoria create(Categoria entity) {
    String nombre = entity.getNombre() != null ? entity.getNombre().trim() : null;
    entity.setNombre(nombre);

    if (StringUtils.hasText(nombre) && repo.existsByNombreIgnoreCase(nombre)) {
      throw new BusinessException("Ya existe una categoría con nombre: " + nombre);
    }
    return repo.save(entity);
  }

  public Categoria get(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Categoría no encontrada: " + id));
  }

  public List<Categoria> list() {
    return repo.findAllByActivoTrue();
  }

  public Categoria update(Long id, Categoria body) {
    var current = get(id);
    String nombre = body.getNombre() != null ? body.getNombre().trim() : null;

    if (StringUtils.hasText(nombre) && repo.existsByNombreIgnoreCase(nombre)
        && !nombre.equalsIgnoreCase(current.getNombre())) {
      throw new BusinessException("Ya existe una categoría con nombre: " + nombre);
    }

    current.setNombre(nombre);
    return repo.save(current);
  }

  public void desactivar(Long id) {
    var current = get(id);
    current.setActivo(false);
    repo.save(current);
  }

  public void activar(Long id) {
    var current = get(id);
    current.setActivo(true);
    repo.save(current);
  }

  /**
   * Estilo "Cliente": endpoint /search con page/size/sortBy/dir.
   */
  public PageResponse<CategoriaDto> searchDto(String q, Boolean incluirInactivos, int page, int size, String sortBy,
      String dir) {
    if (size <= 0)
      size = 20;
    if (page < 0)
      page = 0;

    Set<String> allowed = Set.of("id", "nombre");
    if (!StringUtils.hasText(sortBy) || !allowed.contains(sortBy))
      sortBy = "id";

    Sort.Direction direction = "asc".equalsIgnoreCase(dir) ? Sort.Direction.ASC : Sort.Direction.DESC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

    String s = StringUtils.hasText(q) ? q.trim() : null;
    boolean inc = Boolean.TRUE.equals(incluirInactivos);

    Page<Categoria> p = repo.search(s, inc, pageable);
    var content = p.getContent().stream().map(this::toDto).toList();
    return new PageResponse<>(content, p.getTotalElements(), p.getTotalPages(), p.getNumber(), p.getSize());
  }

  public PageResponse<CategoriaDto> search(CategoriaSearchDto search, Pageable pageable) {
    String q = (search != null && StringUtils.hasText(search.getQ())) ? search.getQ().trim() : null;
    boolean incluirInactivos = search != null && Boolean.TRUE.equals(search.getIncluirInactivos());

    Page<Categoria> page = repo.search(q, incluirInactivos, pageable);
    var content = page.getContent().stream().map(this::toDto).toList();
    return new PageResponse<>(content, page.getTotalElements(), page.getTotalPages(), page.getNumber(), page.getSize());
  }
}
