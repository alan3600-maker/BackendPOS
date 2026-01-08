package py.com.hidraulica.caacupe.repository;

import java.time.OffsetDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import py.com.hidraulica.caacupe.domain.Venta;
import py.com.hidraulica.caacupe.domain.enums.EstadoVenta;

public interface VentaRepository extends JpaRepository<Venta, Long>, JpaSpecificationExecutor<Venta> {
	@Query("""
			  select v from Venta v
			  join fetch v.cliente c
			  where (:clienteId is null or c.id = :clienteId)
			    and (:estado is null or v.estado = :estado)
			    and (:desde is null or v.fecha >= :desde)
			    and (:hasta is null or v.fecha <= :hasta)
			""")
			Page<Venta> search(
			  @Param("clienteId") Long clienteId,
			  @Param("estado") EstadoVenta estado,
			  @Param("desde") OffsetDateTime desde,
			  @Param("hasta") OffsetDateTime hasta,
			  Pageable pageable
			);

}
