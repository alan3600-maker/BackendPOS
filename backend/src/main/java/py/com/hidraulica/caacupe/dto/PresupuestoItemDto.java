package py.com.hidraulica.caacupe.dto;

import java.math.BigDecimal;

public class PresupuestoItemDto {

    private String tipo;            // "PRODUCTO" (por ahora)
    private Long productoId;        // id del producto seleccionado
    private String descripcion;     // descripción que se muestra en el presupuesto
    private Integer cantidad;       // cantidad del producto
    private BigDecimal precioUnitario; // precio al momento del presupuesto
    private BigDecimal totalLinea;     // cantidad * precioUnitario
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Long getProductoId() {
		return productoId;
	}
	public void setProductoId(Long productoId) {
		this.productoId = productoId;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}
	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
	public BigDecimal getTotalLinea() {
		return totalLinea;
	}
	public void setTotalLinea(BigDecimal totalLinea) {
		this.totalLinea = totalLinea;
	}

}
