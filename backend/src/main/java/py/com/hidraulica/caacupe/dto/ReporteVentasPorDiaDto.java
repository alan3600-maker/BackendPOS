package py.com.hidraulica.caacupe.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReporteVentasPorDiaDto(
  LocalDate dia,
  long cantidad,
  BigDecimal total
) {}
