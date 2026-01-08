package py.com.hidraulica.caacupe.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import py.com.hidraulica.caacupe.domain.enums.EstadoDocumento;
import py.com.hidraulica.caacupe.domain.enums.ModalidadDocumento;
import py.com.hidraulica.caacupe.domain.enums.TipoDocumentoVenta;

@Entity
@Table(name = "documento_venta")
public class DocumentoVenta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Relación con la venta
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "venta_id", nullable = false)
	private Venta venta;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_documento", nullable = false, length = 30)
	private TipoDocumentoVenta tipoDocumento;

	@Enumerated(EnumType.STRING)
	@Column(name = "modalidad", nullable = false, length = 30)
	private ModalidadDocumento modalidad;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado", nullable = false, length = 30)
	private EstadoDocumento estado = EstadoDocumento.BORRADOR;

	// Numeración
	@Column(name = "serie", length = 20)
	private String serie;

	@Column(name = "numero")
	private Long numero;

	// Timbrado (Paraguay)
	@Column(name = "timbrado", length = 20)
	private String timbrado;

	@Column(name = "vigencia_desde")
	private LocalDate vigenciaDesde;

	@Column(name = "vigencia_hasta")
	private LocalDate vigenciaHasta;

	// Electrónica (SIFEN)
	@Column(name = "cdc", length = 60)
	private String cdc;

	@Lob
	@Column(name = "xml_sifen")
	private String xmlSifen;

	@Column(name = "fecha_emision", nullable = false)
	private OffsetDateTime fechaEmision = OffsetDateTime.now();

	// Snapshot del cliente (CRÍTICO)
	@Column(name = "cliente_nombre", nullable = false)
	private String clienteNombre;

	@Column(name = "cliente_doc", nullable = false)
	private String clienteDocumento;

	@Column(name = "cliente_direccion")
	private String clienteDireccion;

	// Totales congelados
	@Column(name = "total", nullable = false, precision = 19, scale = 2)
	private BigDecimal total;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Venta getVenta() {
		return venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}

	public TipoDocumentoVenta getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumentoVenta tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public ModalidadDocumento getModalidad() {
		return modalidad;
	}

	public void setModalidad(ModalidadDocumento modalidad) {
		this.modalidad = modalidad;
	}

	public EstadoDocumento getEstado() {
		return estado;
	}

	public void setEstado(EstadoDocumento estado) {
		this.estado = estado;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public String getTimbrado() {
		return timbrado;
	}

	public void setTimbrado(String timbrado) {
		this.timbrado = timbrado;
	}

	public LocalDate getVigenciaDesde() {
		return vigenciaDesde;
	}

	public void setVigenciaDesde(LocalDate vigenciaDesde) {
		this.vigenciaDesde = vigenciaDesde;
	}

	public LocalDate getVigenciaHasta() {
		return vigenciaHasta;
	}

	public void setVigenciaHasta(LocalDate vigenciaHasta) {
		this.vigenciaHasta = vigenciaHasta;
	}

	public String getCdc() {
		return cdc;
	}

	public void setCdc(String cdc) {
		this.cdc = cdc;
	}

	public String getXmlSifen() {
		return xmlSifen;
	}

	public void setXmlSifen(String xmlSifen) {
		this.xmlSifen = xmlSifen;
	}

	public OffsetDateTime getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(OffsetDateTime fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public String getClienteNombre() {
		return clienteNombre;
	}

	public void setClienteNombre(String clienteNombre) {
		this.clienteNombre = clienteNombre;
	}

	public String getClienteDocumento() {
		return clienteDocumento;
	}

	public void setClienteDocumento(String clienteDocumento) {
		this.clienteDocumento = clienteDocumento;
	}

	public String getClienteDireccion() {
		return clienteDireccion;
	}

	public void setClienteDireccion(String clienteDireccion) {
		this.clienteDireccion = clienteDireccion;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

}
