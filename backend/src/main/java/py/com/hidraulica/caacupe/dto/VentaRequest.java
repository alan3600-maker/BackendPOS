package py.com.hidraulica.caacupe.dto;

import py.com.hidraulica.caacupe.domain.enums.TipoItem;
import py.com.hidraulica.caacupe.domain.enums.MedioPago;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class VentaRequest {
  @NotNull public Long clienteId;
  public OffsetDateTime fecha;
  public String observacion;

  @Valid @NotNull public List<Item> items;
  public List<CobroItem> cobros;

  public static class Item {
    @NotNull public TipoItem tipo;

    // PRODUCTO
    public Long productoId;
    public Long depositoId;

    // SERVICIO
    public Long servicioId;

    public String descripcion;
    @NotNull public BigDecimal cantidad;
    @NotNull public BigDecimal precioUnitario;
  }

  public static class CobroItem {
    @NotNull public MedioPago medioPago;
    @NotNull public BigDecimal monto;
  }
}
