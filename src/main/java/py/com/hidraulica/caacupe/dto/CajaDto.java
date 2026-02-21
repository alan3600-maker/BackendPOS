package py.com.hidraulica.caacupe.dto;

public class CajaDto {
  private Long id;
  private Long sucursalId;
  private String nombre;
  private String codigo;
  private Boolean activo;

  public CajaDto() {}

  public CajaDto(Long id, Long sucursalId, String nombre, String codigo, Boolean activo) {
    this.id = id;
    this.sucursalId = sucursalId;
    this.nombre = nombre;
    this.codigo = codigo;
    this.activo = activo;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Long getSucursalId() { return sucursalId; }
  public void setSucursalId(Long sucursalId) { this.sucursalId = sucursalId; }

  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }

  public String getCodigo() { return codigo; }
  public void setCodigo(String codigo) { this.codigo = codigo; }

  public Boolean getActivo() { return activo; }
  public void setActivo(Boolean activo) { this.activo = activo; }
}
