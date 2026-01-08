package py.com.hidraulica.caacupe.dto;

public class MarcaDto {
  private Long id;
  private String nombre;
  private boolean activo;

  public MarcaDto() {}

  public MarcaDto(Long id, String nombre, boolean activo) {
    this.id = id;
    this.nombre = nombre;
    this.activo = activo;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }

  public boolean isActivo() { return activo; }
  public void setActivo(boolean activo) { this.activo = activo; }
}
