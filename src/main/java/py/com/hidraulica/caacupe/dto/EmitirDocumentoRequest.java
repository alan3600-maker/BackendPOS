package py.com.hidraulica.caacupe.dto;

import jakarta.validation.constraints.NotNull;
import py.com.hidraulica.caacupe.domain.enums.ModalidadDocumento;
import py.com.hidraulica.caacupe.domain.enums.TipoDocumentoVenta;

public class EmitirDocumentoRequest {
	  @NotNull public TipoDocumentoVenta tipoDocumento;
	  @NotNull public ModalidadDocumento modalidad;

	  public String timbrado;
	  public String serie;

	  // Para NOTA_CREDITO / NOTA_DEBITO
	  public Long documentoOrigenId;
	}