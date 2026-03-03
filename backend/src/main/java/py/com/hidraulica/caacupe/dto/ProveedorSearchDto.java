package py.com.hidraulica.caacupe.dto;

/**
 * Búsqueda paginada de Proveedores.
 *
 * q busca por nombre/razón social, ruc, teléfono.
 */
public class ProveedorSearchDto {
  private String q;
  private Boolean incluirInactivos;

  public String getQ() { return q; }
  public void setQ(String q) { this.q = q; }

  public Boolean getIncluirInactivos() { return incluirInactivos; }
  public void setIncluirInactivos(Boolean incluirInactivos) { this.incluirInactivos = incluirInactivos; }
}
