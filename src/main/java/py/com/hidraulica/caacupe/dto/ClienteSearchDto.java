package py.com.hidraulica.caacupe.dto;

public class ClienteSearchDto {
	private String q; //(nombre/razón social/ruc/teléfono)

	Boolean incluirInactivos;


  public String getQ() { return q; }
  public void setQ(String q) { this.q = q; }

  public Boolean getIncluirInactivos() { return incluirInactivos; }
  public void setIncluirInactivos(Boolean incluirInactivos) { this.incluirInactivos = incluirInactivos; }
}
