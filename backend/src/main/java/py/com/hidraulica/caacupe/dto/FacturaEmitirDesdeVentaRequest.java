package py.com.hidraulica.caacupe.dto;

import py.com.hidraulica.caacupe.domain.enums.TipoFactura;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class FacturaEmitirDesdeVentaRequest {
  @NotNull public TipoFactura tipo;

  // Para fiscal (opcional en base)
  public String serie;
  public String numero; // si querés setear manualmente, sino se genera
  public String timbrado;
  public LocalDate timbradoVencimiento;
}
