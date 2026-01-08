package py.com.hidraulica.caacupe.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(
  name = "cliente",
  uniqueConstraints = {
    @UniqueConstraint(name = "uk_cliente_ruc", columnNames = "ruc")
  }
)
public class Cliente extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(name = "nombre_razon_social", nullable = false, length = 200)
	private String nombreRazonSocial;

	@Column(name = "ruc", length = 20)
	private String ruc;

	@Column(name = "telefono", length = 30)
	private String telefono;

	@Column(name = "direccion", length = 250)
	private String direccion;

	@Column(name = "email", length = 120)
	private String email;
	
	@Column(nullable = false)
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

	public String getNombreRazonSocial() {
		return nombreRazonSocial;
	}

	public void setNombreRazonSocial(String v) {
		this.nombreRazonSocial = v;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String v) {
		this.ruc = v;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String v) {
		this.telefono = v;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String v) {
		this.direccion = v;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String v) {
		this.email = v;
	}
}
