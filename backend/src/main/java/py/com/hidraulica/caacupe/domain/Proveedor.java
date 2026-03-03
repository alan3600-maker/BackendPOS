package py.com.hidraulica.caacupe.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "proveedor")
public class Proveedor extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(name = "nombre_razon_social", nullable = false, length = 160)
  private String nombreRazonSocial;

  @Column(length = 30)
  private String ruc;

  @Column(length = 30)
  private String telefono;

  @Column(length = 200)
  private String direccion;

  @Column(nullable = false)
  private boolean activo = true;

  public Long getId() { return id; }

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
