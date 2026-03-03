package py.com.hidraulica.caacupe.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.com.hidraulica.caacupe.domain.Caja;
import py.com.hidraulica.caacupe.domain.Sucursal;
import py.com.hidraulica.caacupe.dto.CajaDto;
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

  public List<CajaDto> listarPorSucursal(Long sucursalId) {
    return cajaRepository.findBySucursalIdAndActivoTrueOrderByNombreAsc(sucursalId)
        .stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public Caja obtener(Long id) {
    return cajaRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Caja no encontrada"));
  }

  public CajaDto crear(Long sucursalId, Caja req) {
    Sucursal sucursal = sucursalRepository.findById(sucursalId)
        .orElseThrow(() -> new NotFoundException("Sucursal no encontrada"));

    req.setId(null);
    req.setSucursal(sucursal);

    if (req.getActivo() == null) req.setActivo(true);

    return toDto(cajaRepository.save(req));
  }

  public CajaDto actualizar(Long id, Caja req) {
    Caja c = obtener(id);

    if (req.getNombre() != null) c.setNombre(req.getNombre());
    if (req.getCodigo() != null) c.setCodigo(req.getCodigo());
    if (req.getActivo() != null) c.setActivo(req.getActivo());

    return toDto(cajaRepository.save(c));
  }

  public CajaDto setActivo(Long id, boolean activo) {
    Caja c = obtener(id);
    c.setActivo(activo);
    return toDto(cajaRepository.save(c));
  }

  private CajaDto toDto(Caja c) {
    Long sucursalId = (c.getSucursal() != null) ? c.getSucursal().getId() : null;
    return new CajaDto(c.getId(), sucursalId, c.getNombre(), c.getCodigo(), c.getActivo());
  }
}
