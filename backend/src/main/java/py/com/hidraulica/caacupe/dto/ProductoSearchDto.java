package py.com.hidraulica.caacupe.dto;

import java.math.BigDecimal;

public class ProductoSearchDto {
	private String q;

	private Long categoriaId;

	private Long marcaId;

	private Boolean incluirInactivos;

	private BigDecimal precioDesde;
	private BigDecimal precioHasta;

	private String codigoBarra;

  public String getQ() { return q; }
  public void setQ(String q) { this.q = q; }

  public Long getCategoriaId() { return categoriaId; }
  public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }

  public Long getMarcaId() { return marcaId; }
  public void setMarcaId(Long marcaId) { this.marcaId = marcaId; }

  public Boolean getIncluirInactivos() { return incluirInactivos; }
  public void setIncluirInactivos(Boolean incluirInactivos) { this.incluirInactivos = incluirInactivos; }

  public BigDecimal getPrecioDesde() { return precioDesde; }
  public void setPrecioDesde(BigDecimal precioDesde) { this.precioDesde = precioDesde; }

  public BigDecimal getPrecioHasta() { return precioHasta; }
  public void setPrecioHasta(BigDecimal precioHasta) { this.precioHasta = precioHasta; }

  public String getCodigoBarra() { return codigoBarra; }
  public void setCodigoBarra(String codigoBarra) { this.codigoBarra = codigoBarra; }
}
