package py.com.hidraulica.caacupe.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import py.com.hidraulica.caacupe.domain.Cliente;
import py.com.hidraulica.caacupe.dto.ClienteDto;
import py.com.hidraulica.caacupe.dto.ClienteSearchDto;
import py.com.hidraulica.caacupe.dto.PageResponse;
import py.com.hidraulica.caacupe.exception.BusinessException;
import py.com.hidraulica.caacupe.exception.NotFoundException;
import py.com.hidraulica.caacupe.repository.ClienteRepository;

@Service
public class ClienteService implements CrudService<Cliente, Long> {

  private final ClienteRepository repo;

  public ClienteService(ClienteRepository repo) {
    this.repo = repo;
  }

  private ClienteDto toDto(Cliente c) {
    return new ClienteDto(c.getId(), c.getNombreRazonSocial(), c.getRuc(), c.getTelefono(), c.getDireccion(), c.getEmail(), c.isActivo());
  }

  @Override
  public Cliente create(Cliente entity) {
    entity.setNombreRazonSocial(entity.getNombreRazonSocial() != null ? entity.getNombreRazonSocial().trim() : null);
    entity.setRuc(entity.getRuc() != null ? entity.getRuc().trim() : null);
    entity.setTelefono(entity.getTelefono() != null ? entity.getTelefono().trim() : null);
    entity.setDireccion(entity.getDireccion() != null ? entity.getDireccion().trim() : null);
    entity.setEmail(entity.getEmail() != null ? entity.getEmail().trim() : null);

    if (StringUtils.hasText(entity.getRuc()) && repo.existsByRuc(entity.getRuc())) {
      throw new BusinessException("Ya existe un cliente con RUC: " + entity.getRuc());
    }
    return repo.save(entity);
  }

  @Override
  public Cliente get(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado: " + id));
  }

  @Override
  public List<Cliente> list() {
    return repo.findAllByActivoTrue();
  }

  @Override
  public Cliente update(Long id, Cliente entity) {
    var current = get(id);

    String newRuc = entity.getRuc() != null ? entity.getRuc().trim() : null;
    if (StringUtils.hasText(newRuc) && repo.existsByRuc(newRuc) && (current.getRuc() == null || !newRuc.equals(current.getRuc()))) {
      throw new BusinessException("Ya existe un cliente con RUC: " + newRuc);
    }

    current.setNombreRazonSocial(entity.getNombreRazonSocial() != null ? entity.getNombreRazonSocial().trim() : null);
    current.setRuc(newRuc);
    current.setTelefono(entity.getTelefono() != null ? entity.getTelefono().trim() : null);
    current.setDireccion(entity.getDireccion() != null ? entity.getDireccion().trim() : null);
    current.setEmail(entity.getEmail() != null ? entity.getEmail().trim() : null);

    return repo.save(current);
  }

  public void desactivar(Long id) {
    var entity = get(id);
    entity.setActivo(false);
    repo.save(entity);
  }

  public void activar(Long id) {
    var entity = get(id);
    entity.setActivo(true);
    repo.save(entity);
  }

  @Override
  public void delete(Long id) {
    desactivar(id);
  }

  public boolean existsByRuc(String ruc) {
    return repo.existsByRuc(ruc);
  }

  // Mantengo tu estilo de searchDto para no romper frontend
  public PageResponse<ClienteDto> searchDto(String q, Boolean incluirInactivos, int page, int size, String sortBy, String dir) {
    if (size <= 0) size = 20;
    if (page < 0) page = 0;

    Set<String> allowed = Set.of("id", "nombreRazonSocial", "ruc", "telefono");
    if (!StringUtils.hasText(sortBy) || !allowed.contains(sortBy)) sortBy = "id";

    Sort.Direction direction = "asc".equalsIgnoreCase(dir) ? Sort.Direction.ASC : Sort.Direction.DESC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

    String s = StringUtils.hasText(q) ? q.trim() : null;
    boolean inc = Boolean.TRUE.equals(incluirInactivos);

    Page<Cliente> p = repo.search(s, inc, pageable);
    var content = p.getContent().stream().map(this::toDto).toList();
    return new PageResponse<>(content, p.getTotalElements(), p.getTotalPages(), p.getNumber(), p.getSize());
  }

  // Search "nuevo" basado en ClienteSearchDto (para tu endpoint paginado)
  public PageResponse<ClienteDto> search(ClienteSearchDto search, Pageable pageable) {
    String q = search != null ? search.getQ() : null;
    Boolean inc = search != null ? search.getIncluirInactivos() : null;
    String s = StringUtils.hasText(q) ? q.trim() : null;

    Page<Cliente> p = repo.search(s, Boolean.TRUE.equals(inc), pageable);
    var content = p.getContent().stream().map(this::toDto).toList();
    return new PageResponse<>(content, p.getTotalElements(), p.getTotalPages(), p.getNumber(), p.getSize());
  }
}
