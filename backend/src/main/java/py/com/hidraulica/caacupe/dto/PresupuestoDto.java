package py.com.hidraulica.caacupe.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class PresupuestoDto {
	  public Long id;
	  public Long clienteId;
	  public String clienteNombre;
	  public OffsetDateTime fecha;
	  public String estado;
	  public String observacion;
	  public BigDecimal total;
	  public List<PresupuestoItemDto> items;
	  public Long getId() {
		  return id;
	  }
	  public void setId(Long id) {
		  this.id = id;
	  }
	  public Long getClienteId() {
		  return clienteId;
	  }
	  public void setClienteId(Long clienteId) {
		  this.clienteId = clienteId;
	  }
	  public String getClienteNombre() {
		  return clienteNombre;
	  }
	  public void setClienteNombre(String clienteNombre) {
		  this.clienteNombre = clienteNombre;
	  }
	  public OffsetDateTime getFecha() {
		  return fecha;
	  }
	  public void setFecha(OffsetDateTime fecha) {
		  this.fecha = fecha;
	  }
	  public String getEstado() {
		  return estado;
	  }
	  public void setEstado(String estado) {
		  this.estado = estado;
	  }
	  public String getObservacion() {
		  return observacion;
	  }
	  public void setObservacion(String observacion) {
		  this.observacion = observacion;
	  }
	  public BigDecimal getTotal() {
		  return total;
	  }
	  public void setTotal(BigDecimal total) {
		  this.total = total;
	  }
	  public List<PresupuestoItemDto> getItems() {
		  return items;
	  }
	  public void setItems(List<PresupuestoItemDto> items) {
		  this.items = items;
	  }
	}