package py.com.hidraulica.caacupe.repository.proj;

import java.math.BigDecimal;
import py.com.hidraulica.caacupe.domain.enums.MedioPago;

public interface CobroResumenPorMedio {
  MedioPago getMedioPago();
  BigDecimal getTotal();
}

