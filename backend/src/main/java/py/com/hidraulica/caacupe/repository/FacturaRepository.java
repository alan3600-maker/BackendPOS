package py.com.hidraulica.caacupe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import py.com.hidraulica.caacupe.domain.Factura;

public interface FacturaRepository
extends JpaRepository<Factura, Long>, JpaSpecificationExecutor<Factura> {

	
	@EntityGraph(attributePaths = { "cliente", "venta" })
	Optional<Factura> findDetailById(Long id);

	@EntityGraph(attributePaths = { "cliente", "items" })
	Optional<Factura> findForPdfById(Long id);
}