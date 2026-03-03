package py.com.hidraulica.caacupe.dto;

import java.math.BigDecimal;
import py.com.hidraulica.caacupe.domain.enums.MedioPago;

public record ReporteCobrosPorMedioDto(
  MedioPago medioPago,
  BigDecimal total
) {}
