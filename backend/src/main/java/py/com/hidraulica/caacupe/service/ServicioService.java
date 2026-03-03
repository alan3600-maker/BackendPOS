package py.com.hidraulica.caacupe.service;

import py.com.hidraulica.caacupe.domain.Servicio;
import py.com.hidraulica.caacupe.exception.NotFoundException;
import py.com.hidraulica.caacupe.repository.ServicioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService implements CrudService<Servicio, Long> {

  private final ServicioRepository repo;

  public ServicioService(ServicioRepository repo) {
    this.repo = repo;
  }

  @Override
  public Servicio create(Servicio entity) {
    return repo.save(entity);
  }

  @Override
  public Servicio get(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Servicio no encontrado: " + id));
  }

  public Servicio getByCodigo(String codigo) {
    return repo.findByCodigo(codigo).orElseThrow(() -> new NotFoundException("Servicio no encontrado por codigo: " + codigo));
  }

  @Override
  public List<Servicio> list() {
    return repo.findAll();
  }

  public Page<Servicio> search(String q, Boolean activo, Pageable pageable) {
    if (q != null && !q.isBlank()) return repo.findByDescripcionContainingIgnoreCase(q.trim(), pageable);
    if (activo != null) return repo.findByActivo(activo, pageable);
    return repo.findAll(pageable);
  }

  public Servicio setActivo(Long id, boolean activo) {
    var current = get(id);
    current.setActivo(activo);
    return repo.save(current);
  }

  @Override
  public Servicio update(Long id, Servicio entity) {
    var current = get(id);
    current.setCodigo(entity.getCodigo());
    current.setDescripcion(entity.getDescripcion());
    current.setPrecio(entity.getPrecio());
    current.setActivo(entity.isActivo());
    return repo.save(current);
  }

  @Override
  public void delete(Long id) {
    if (!repo.existsById(id)) throw new NotFoundException("Servicio no encontrado: " + id);
    repo.deleteById(id);
  }
}
