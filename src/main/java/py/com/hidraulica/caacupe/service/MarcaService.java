package py.com.hidraulica.caacupe.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import py.com.hidraulica.caacupe.domain.Marca;
import py.com.hidraulica.caacupe.dto.CatalogoSearchDto;
import py.com.hidraulica.caacupe.dto.MarcaDto;
import py.com.hidraulica.caacupe.dto.PageResponse;
import py.com.hidraulica.caacupe.exception.BusinessException;
import py.com.hidraulica.caacupe.exception.NotFoundException;
import py.com.hidraulica.caacupe.repository.MarcaRepository;

@Service
public class MarcaService {

  private final MarcaRepository repo;

  public MarcaService(MarcaRepository repo) {
    this.repo = repo;
  }

  private MarcaDto toDto(Marca m) {
    return new MarcaDto(m.getId(), m.getNombre(), m.isActivo());
  }

  public Marca create(Marca entity) {
    String nombre = entity.getNombre() != null ? entity.getNombre().trim() : null;
    entity.setNombre(nombre);

    if (StringUtils.hasText(nombre) && repo.existsByNombreIgnoreCase(nombre)) {
      throw new BusinessException("Ya existe una marca con nombre: " + nombre);
    }
    return repo.save(entity);
  }

  public Marca get(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Marca no encontrada: " + id));
  }

  public Marca update(Long id, Marca body) {
    var current = get(id);
    String nombre = body.getNombre() != null ? body.getNombre().trim() : null;

    if (StringUtils.hasText(nombre) && repo.existsByNombreIgnoreCase(nombre)
        && !nombre.equalsIgnoreCase(current.getNombre())) {
      throw new BusinessException("Ya existe una marca con nombre: " + nombre);
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

  public PageResponse<MarcaDto> search(CatalogoSearchDto search, Pageable pageable) {
    String q = (search != null && StringUtils.hasText(search.getQ())) ? search.getQ().trim() : null;
    boolean incluirInactivos = search != null && Boolean.TRUE.equals(search.getIncluirInactivos());

    Page<Marca> page = repo.search(q, incluirInactivos, pageable);
    var content = page.getContent().stream().map(this::toDto).toList();
    return new PageResponse<>(content, page.getTotalElements(), page.getTotalPages(), page.getNumber(), page.getSize());
  }
}
