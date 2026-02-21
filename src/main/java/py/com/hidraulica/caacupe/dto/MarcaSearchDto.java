package py.com.hidraulica.caacupe.dto;

/**
 * Búsqueda paginada de Marcas.
 */
public class MarcaSearchDto {
  private String q; // nombre
  private Boolean incluirInactivos;

  public String getQ() { return q; }
  public void setQ(String q) { this.q = q; }

  public Boolean getIncluirInactivos() { return incluirInactivos; }
  public void setIncluirInactivos(Boolean incluirInactivos) { this.incluirInactivos = incluirInactivos; }
}
