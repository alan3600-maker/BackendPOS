package py.com.hidraulica.caacupe.dto;

/**
 * Búsqueda paginada de Categorías.
 *
 * Mantiene el mismo contrato que ClienteSearchDto para consistencia.
 */
public class CategoriaSearchDto {
  private String q; // nombre
  private Boolean incluirInactivos;

  public String getQ() { return q; }
  public void setQ(String q) { this.q = q; }

  public Boolean getIncluirInactivos() { return incluirInactivos; }
  public void setIncluirInactivos(Boolean incluirInactivos) { this.incluirInactivos = incluirInactivos; }
}
