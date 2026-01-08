package py.com.hidraulica.caacupe.dto;

import java.math.BigDecimal;

public class ProductoDto {
  private Long id;
  private String sku;
  private String descripcion;
  private BigDecimal precio;

  private Boolean activo;

  private Long categoriaId;
  private String categoriaNombre;

  private Long marcaId;
  private String marcaNombre;

  private Long proveedorId;
  private String proveedorNombre;

  public ProductoDto() {}

  // Compatibilidad: constructor viejo
  public ProductoDto(Long id, String sku, String descripcion, BigDecimal precio) {
    this.id = id;
    this.sku = sku;
    this.descripcion = descripcion;
    this.precio = precio;
  }

  // Constructor nuevo (para listas)
  public ProductoDto(Long id, String sku, String descripcion, BigDecimal precio,
                     Boolean activo,
                     Long categoriaId, String categoriaNombre,
                     Long marcaId, String marcaNombre,
                     Long proveedorId, String proveedorNombre) {
    this.id = id;
    this.sku = sku;
    this.descripcion = descripcion;
    this.precio = precio;
    this.activo = activo;
    this.categoriaId = categoriaId;
    this.categoriaNombre = categoriaNombre;
    this.marcaId = marcaId;
    this.marcaNombre = marcaNombre;
    this.proveedorId = proveedorId;
    this.proveedorNombre = proveedorNombre;
  }

  public Long getId() { return id; }
  public String getSku() { return sku; }
  public String getDescripcion() { return descripcion; }
  public BigDecimal getPrecio() { return precio; }

  public Boolean getActivo() { return activo; }

  public Long getCategoriaId() { return categoriaId; }
  public String getCategoriaNombre() { return categoriaNombre; }

  public Long getMarcaId() { return marcaId; }
  public String getMarcaNombre() { return marcaNombre; }

  public Long getProveedorId() { return proveedorId; }
  public String getProveedorNombre() { return proveedorNombre; }
}
