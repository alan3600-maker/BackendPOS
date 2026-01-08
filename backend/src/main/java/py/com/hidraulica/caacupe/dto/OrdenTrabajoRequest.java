package py.com.hidraulica.caacupe.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class OrdenTrabajoRequest {
  @NotNull public Long clienteId;
  public OffsetDateTime fecha;
  public String descripcion;

  @Valid public List<Repuesto> repuestos;

  public static class Repuesto {
    @NotNull public Long productoId;
    @NotNull public Long depositoId;
    @NotNull public BigDecimal cantidad;
  }
}
