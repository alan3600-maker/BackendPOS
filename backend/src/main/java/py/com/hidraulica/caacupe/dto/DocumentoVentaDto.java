package py.com.hidraulica.caacupe.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import py.com.hidraulica.caacupe.domain.enums.EstadoDocumento;
import py.com.hidraulica.caacupe.domain.enums.ModalidadDocumento;
import py.com.hidraulica.caacupe.domain.enums.TipoDocumentoVenta;

public record DocumentoVentaDto(
	    Long id,
	    Long ventaId,
	    Long documentoOrigenId,
	    TipoDocumentoVenta tipoDocumento,
	    ModalidadDocumento modalidad,
	    EstadoDocumento estado,
	    OffsetDateTime fechaEmision,
	    String serie,
	    Long numero,
	    String timbrado,
	    String cdc,
	    BigDecimal total,
	    String clienteNombre,
	    String clienteDocumento
	) {}
