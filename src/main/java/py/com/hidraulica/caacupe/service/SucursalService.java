package py.com.hidraulica.caacupe.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.com.hidraulica.caacupe.domain.Sucursal;
import py.com.hidraulica.caacupe.exception.NotFoundException;
import py.com.hidraulica.caacupe.repository.SucursalRepository;

@Service
@Transactional
public class SucursalService {

  private final SucursalRepository sucursalRepository;

  public SucursalService(SucursalRepository sucursalRepository) {
    this.sucursalRepository = sucursalRepository;
  }

  public List<Sucursal> listar(boolean soloActivas) {
    if (soloActivas) {
      return sucursalRepository.findByActivoTrue();
    }
    return sucursalRepository.findAll();
  }

  public Sucursal obtener(Long id) {
    return sucursalRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Sucursal no encontrada"));
  }

  public Sucursal crear(Sucursal s) {
    s.setId(null);
    if (s.getActivo() == null) s.setActivo(true);
    return sucursalRepository.save(s);
  }

  public Sucursal actualizar(Long id, Sucursal req) {
    Sucursal s = obtener(id);

    if (req.getNombre() != null) s.setNombre(req.getNombre());
    if (req.getDireccion() != null) s.setDireccion(req.getDireccion());
    if (req.getTelefono() != null) s.setTelefono(req.getTelefono());
    if (req.getActivo() != null) s.setActivo(req.getActivo());

    return sucursalRepository.save(s);
  }

  public Sucursal setActivo(Long id, boolean activo) {
    Sucursal s = obtener(id);
    s.setActivo(activo);
    return sucursalRepository.save(s);
  }
}

