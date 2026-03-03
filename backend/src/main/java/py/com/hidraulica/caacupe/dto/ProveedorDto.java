package py.com.hidraulica.caacupe.dto;

public class ProveedorDto {
  private Long id;
  private String nombreRazonSocial;
  private String ruc;
  private String telefono;
  private String direccion;
  private boolean activo;

  public ProveedorDto() {}

  public ProveedorDto(Long id, String nombreRazonSocial, String ruc, String telefono, String direccion, boolean activo) {
    this.id = id;
    this.nombreRazonSocial = nombreRazonSocial;
    this.ruc = ruc;
    this.telefono = telefono;
    this.direccion = direccion;
    this.activo = activo;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getNombreRazonSocial() { return nombreRazonSocial; }
  public void setNombreRazonSocial(String v) { this.nombreRazonSocial = v; }

  public String getRuc() { return ruc; }
  public void setRuc(String ruc) { this.ruc = ruc; }

  public String getTelefono() { return telefono; }
  public void setTelefono(String telefono) { this.telefono = telefono; }

  public String getDireccion() { return direccion; }
  public void setDireccion(String direccion) { this.direccion = direccion; }

  public boolean isActivo() { return activo; }
  public void setActivo(boolean activo) { this.activo = activo; }
}
