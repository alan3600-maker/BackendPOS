package py.com.hidraulica.caacupe.repository.proj;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface VentasPorDiaRow {
  LocalDate getDia();
  Long getCantidad();
  BigDecimal getTotal();
}

