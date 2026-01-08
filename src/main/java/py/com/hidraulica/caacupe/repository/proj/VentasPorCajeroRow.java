package py.com.hidraulica.caacupe.repository.proj;

import java.math.BigDecimal;

public interface VentasPorCajeroRow {
  Long getUsuarioId();
  String getUsuarioNombre(); // opcional, si podés
  Long getCantidad();
  BigDecimal getTotal();
}
