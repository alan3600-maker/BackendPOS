package py.com.hidraulica.caacupe.dto;

import py.com.hidraulica.caacupe.domain.enums.TipoItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class PresupuestoRequest {
  @NotNull public Long clienteId;
  public OffsetDateTime fecha;
  public String observacion;

  @Valid @NotNull public List<Item> items;

  public static class Item {
    @NotNull public TipoItem tipo;
    public Long productoId;
    public Long servicioId;
    public String descripcion;
    @NotNull public BigDecimal cantidad;
    @NotNull public BigDecimal precioUnitario;
  }
}
