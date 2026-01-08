package py.com.hidraulica.caacupe.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.com.hidraulica.caacupe.domain.Caja;
import py.com.hidraulica.caacupe.domain.Sucursal;
import py.com.hidraulica.caacupe.exception.NotFoundException;
import py.com.hidraulica.caacupe.repository.CajaRepository;
import py.com.hidraulica.caacupe.repository.SucursalRepository;

@Service
@Transactional
public class CajaService {

  private final CajaRepository cajaRepository;
  private final SucursalRepository sucursalRepository;

  public CajaService(CajaRepository cajaRepository, SucursalRepository sucursalRepository) {
    this.cajaRepository = cajaRepository;
    this.sucursalRepository = sucursalRepository;
  }

  public List<Caja> listarPorSucursal(Long sucursalId) {
    return cajaRepository.findBySucursalIdAndActivoTrueOrderByNombreAsc(sucursalId);
  }

  public Caja obtener(Long id) {
    return cajaRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Caja no encontrada"));
  }

  public Caja crear(Long sucursalId, Caja req) {
    Sucursal sucursal = sucursalRepository.findById(sucursalId)
        .orElseThrow(() -> new NotFoundException("Sucursal no encontrada"));

    req.setId(null);
    req.setSucursal(sucursal);

    if (req.getActivo() == null) req.setActivo(true);

    return cajaRepository.save(req);
  }

  public Caja actualizar(Long id, Caja req) {
    Caja c = obtener(id);

    if (req.getNombre() != null) c.setNombre(req.getNombre());
    if (req.getCodigo() != null) c.setCodigo(req.getCodigo()); // <- evitar pisar con null
    if (req.getActivo() != null) c.setActivo(req.getActivo());

    return cajaRepository.save(c);
  }

  public Caja setActivo(Long id, boolean activo) {
    Caja c = obtener(id);
    c.setActivo(activo);
    return cajaRepository.save(c);
  }
}
