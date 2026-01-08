package py.com.hidraulica.caacupe.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import py.com.hidraulica.caacupe.domain.Proveedor;
import py.com.hidraulica.caacupe.dto.CatalogoSearchDto;
import py.com.hidraulica.caacupe.dto.PageResponse;
import py.com.hidraulica.caacupe.dto.ProveedorDto;
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
    return repo.save(entity);
  }

  public Proveedor get(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Proveedor no encontrado: " + id));
  }

  public Proveedor update(Long id, Proveedor body) {
    var current = get(id);
    current.setNombreRazonSocial(body.getNombreRazonSocial() != null ? body.getNombreRazonSocial().trim() : null);
    current.setRuc(body.getRuc() != null ? body.getRuc().trim() : null);
    current.setTelefono(body.getTelefono() != null ? body.getTelefono().trim() : null);
    current.setDireccion(body.getDireccion() != null ? body.getDireccion().trim() : null);
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

  public PageResponse<ProveedorDto> search(CatalogoSearchDto search, Pageable pageable) {
    String q = (search != null && StringUtils.hasText(search.getQ())) ? search.getQ().trim() : null;
    boolean incluirInactivos = search != null && Boolean.TRUE.equals(search.getIncluirInactivos());

    Page<Proveedor> page = repo.search(q, incluirInactivos, pageable);
    var content = page.getContent().stream().map(this::toDto).toList();
    return new PageResponse<>(content, page.getTotalElements(), page.getTotalPages(), page.getNumber(), page.getSize());
  }
}
