package py.com.hidraulica.caacupe.dto;

public class CatalogoSearchDto {
  private String q;
  private Boolean incluirInactivos;

  public String getQ() { return q; }
  public void setQ(String q) { this.q = q; }

  public Boolean getIncluirInactivos() { return incluirInactivos; }
  public void setIncluirInactivos(Boolean incluirInactivos) { this.incluirInactivos = incluirInactivos; }
}
