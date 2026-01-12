package py.com.hidraulica.caacupe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import py.com.hidraulica.caacupe.domain.DocumentoVenta;
import py.com.hidraulica.caacupe.domain.enums.EstadoDocumento;
import py.com.hidraulica.caacupe.domain.enums.ModalidadDocumento;
import py.com.hidraulica.caacupe.domain.enums.TipoDocumentoVenta;

public interface DocumentoVentaRepository extends JpaRepository<DocumentoVenta, Long> {

	List<DocumentoVenta> findByVentaIdOrderByFechaEmisionDesc(Long ventaId);

	boolean existsByVentaIdAndTipoDocumentoAndModalidadAndEstado(Long ventaId, TipoDocumentoVenta tipoDocumento,
			ModalidadDocumento modalidad, EstadoDocumento estado);

	Optional<DocumentoVenta> findTopByModalidadAndTipoDocumentoOrderByNumeroDesc(ModalidadDocumento modalidad,
			TipoDocumentoVenta tipoDocumento);
}
