package py.com.hidraulica.caacupe.dto;

import java.math.BigDecimal;

public record ReporteVentasPorCajeroDto(
  Long usuarioId,
  String usuarioNombre,
  long cantidad,
  BigDecimal total
) {}
