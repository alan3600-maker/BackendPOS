package py.com.hidraulica.caacupe.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import py.com.hidraulica.caacupe.domain.Proveedor;
import py.com.hidraulica.caacupe.dto.PageResponse;
import py.com.hidraulica.caacupe.dto.ProveedorDto;
import py.com.hidraulica.caacupe.dto.ProveedorSearchDto;
import py.com.hidraulica.caacupe.exception.BusinessException;
import py.com.hidraulica.caacupe.exception.NotFoundException;
import py.com.hidraulica.caacupe.repository.ProveedorRepository;

@Service
public class ProveedorService {

  private final ProveedorRepository repo;

  public ProveedorService(ProveedorRepository repo) {
    this.repo = repo;
  }

  private ProveedorDto toDto(Proveedor p) {
    return new ProveedorDto(p.getId(), p.getNombreRazonSocial(), p.getRuc(), p.getTelefono(), p.getDireccion(), p.isActivo());
  }

  public Proveedor create(Proveedor entity) {
    entity.setNombreRazonSocial(entity.getNombreRazonSocial() != null ? entity.getNombreRazonSocial().trim() : null);
    entity.setRuc(entity.getRuc() != null ? entity.getRuc().trim() : null);
    entity.setTelefono(entity.getTelefono() != null ? entity.getTelefono().trim() : null);
    entity.setDireccion(entity.getDireccion() != null ? entity.getDireccion().trim() : null);

    if (StringUtils.hasText(entity.getRuc()) && repo.existsByRuc(entity.getRuc())) {
      throw new BusinessException("Ya existe un proveedor con RUC: " + entity.getRuc());
    }
    return repo.save(entity);
  }

  public Proveedor get(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Proveedor no encontrado: " + id));
  }

  public Proveedor update(Long id, Proveedor body) {
    var current = get(id);

    String newRuc = body.getRuc() != null ? body.getRuc().trim() : null;
    if (StringUtils.hasText(newRuc) && repo.existsByRuc(newRuc)
        && (current.getRuc() == null || !newRuc.equals(current.getRuc()))) {
      throw new BusinessException("Ya existe un proveedor con RUC: " + newRuc);
    }

    current.setNombreRazonSocial(body.getNombreRazonSocial() != null ? body.getNombreRazonSocial().trim() : null);
    current.setRuc(newRuc);
    current.setTelefono(body.getTelefono() != null ? body.getTelefono().trim() : null);
    current.setDireccion(body.getDireccion() != null ? body.getDireccion().trim() : null);
    return repo.save(current);
  }

  public List<Proveedor> list() {
    return repo.findAllByActivoTrue();
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

  public boolean existsByRuc(String ruc) {
    return repo.existsByRuc(ruc);
  }

  /**
   * Estilo "Cliente": endpoint /search con page/size/sortBy/dir.
   */
  public PageResponse<ProveedorDto> searchDto(String q, Boolean incluirInactivos, int page, int size, String sortBy,
      String dir) {
    if (size <= 0)
      size = 20;
    if (page < 0)
      page = 0;

    Set<String> allowed = Set.of("id", "nombreRazonSocial", "ruc", "telefono");
    if (!StringUtils.hasText(sortBy) || !allowed.contains(sortBy))
      sortBy = "id";

    Sort.Direction direction = "asc".equalsIgnoreCase(dir) ? Sort.Direction.ASC : Sort.Direction.DESC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

    String s = StringUtils.hasText(q) ? q.trim() : null;
    boolean inc = Boolean.TRUE.equals(incluirInactivos);

    Page<Proveedor> p = repo.search(s, inc, pageable);
    var content = p.getContent().stream().map(this::toDto).toList();
    return new PageResponse<>(content, p.getTotalElements(), p.getTotalPages(), p.getNumber(), p.getSize());
  }

  public PageResponse<ProveedorDto> search(ProveedorSearchDto search, Pageable pageable) {
    String q = (search != null && StringUtils.hasText(search.getQ())) ? search.getQ().trim() : null;
    boolean incluirInactivos = search != null && Boolean.TRUE.equals(search.getIncluirInactivos());

    Page<Proveedor> page = repo.search(q, incluirInactivos, pageable);
    var content = page.getContent().stream().map(this::toDto).toList();
    return new PageResponse<>(content, page.getTotalElements(), page.getTotalPages(), page.getNumber(), page.getSize());
  }
}
