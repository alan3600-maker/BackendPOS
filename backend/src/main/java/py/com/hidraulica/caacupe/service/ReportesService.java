package py.com.hidraulica.caacupe.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.com.hidraulica.caacupe.dto.ReporteCobrosPorMedioDto;
import py.com.hidraulica.caacupe.dto.ReporteVentasPorCajeroDto;
import py.com.hidraulica.caacupe.dto.ReporteVentasPorDiaDto;
import py.com.hidraulica.caacupe.repository.CobroRepository;
import py.com.hidraulica.caacupe.repository.VentaRepository;

@Service
@Transactional(readOnly = true)
public class ReportesService {

  private final VentaRepository ventaRepo;
  private final CobroRepository cobroRepo;

  public ReportesService(VentaRepository ventaRepo, CobroRepository cobroRepo) {
    this.ventaRepo = ventaRepo;
    this.cobroRepo = cobroRepo;
  }

  public List<ReporteVentasPorDiaDto> ventasPorDia(OffsetDateTime desde, OffsetDateTime hasta) {
    return ventaRepo.ventasPorDia(desde, hasta).stream()
        .map(r -> new ReporteVentasPorDiaDto(r.getDia(), r.getCantidad(), r.getTotal()))
        .toList();
  }

  public List<ReporteCobrosPorMedioDto> cobrosPorMedio(OffsetDateTime desde, OffsetDateTime hasta) {
    return cobroRepo.cobrosPorMedio(desde, hasta).stream()
        .map(r -> new ReporteCobrosPorMedioDto(r.getMedioPago(), r.getTotal()))
        .toList();
  }

  public List<ReporteVentasPorCajeroDto> ventasPorCajero(OffsetDateTime desde, OffsetDateTime hasta) {
    return ventaRepo.ventasPorCajero(desde, hasta).stream()
        .map(r -> new ReporteVentasPorCajeroDto(r.getUsuarioId(), r.getUsuarioNombre(), r.getCantidad(), r.getTotal()))
        .toList();
  }
}
