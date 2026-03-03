package py.com.hidraulica.caacupe.dto;

import py.com.hidraulica.caacupe.domain.enums.TipoMovimientoStock;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.List;

public class MovimientoStockRequest {
  @NotNull public TipoMovimientoStock tipo;
  public OffsetDateTime fecha;
  public String referenciaTipo;
  public Long referenciaId;

  @Valid @NotNull public List<Item> items;

  public static class Item {
    @NotNull public Long productoId;
    @NotNull public Long depositoId;
    @NotNull public java.math.BigDecimal cantidad;
  }
}
