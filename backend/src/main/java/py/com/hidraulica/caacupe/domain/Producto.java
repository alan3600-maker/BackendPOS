package py.com.hidraulica.caacupe.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import py.com.hidraulica.caacupe.domain.enums.IvaTipo;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "producto", indexes = { @Index(name = "ix_producto_sku", columnList = "sku"),
		@Index(name = "ix_producto_desc", columnList = "descripcion") })
public class Producto extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 60, nullable = false, unique = true)
	@NotBlank
	private String sku;

	@NotBlank
	@Column(name = "descripcion", nullable = false, length = 250)
	private String descripcion;

	@DecimalMin(value = "0.00")
	@NotNull
	private BigDecimal precio;
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoria_id")
	private Categoria categoria;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "marca_id")
	private Marca marca;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "proveedor_id")
	private Proveedor proveedor;
	@Enumerated(EnumType.STRING)
	@Column(name = "iva_tipo", nullable = false)
	private IvaTipo ivaTipo = IvaTipo.IVA10;

	@Column(name = "activo", nullable = false)
	private boolean activo = true;

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public Long getId() {
		return id;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String v) {
		this.sku = v;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String v) {
		this.descripcion = v;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal v) {
		this.precio = v;
	}

	public Categoria getCategoria() { return categoria; }
	public void setCategoria(Categoria categoria) { this.categoria = categoria; }

	public Marca getMarca() { return marca; }
	public void setMarca(Marca marca) { this.marca = marca; }

	public Proveedor getProveedor() { return proveedor; }
	public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }
}
