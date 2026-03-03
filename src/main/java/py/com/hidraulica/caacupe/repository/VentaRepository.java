package py.com.hidraulica.caacupe.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import py.com.hidraulica.caacupe.domain.Venta;
import py.com.hidraulica.caacupe.domain.enums.EstadoVenta;
import py.com.hidraulica.caacupe.repository.proj.VentasPorCajeroRow;
import py.com.hidraulica.caacupe.repository.proj.VentasPorDiaRow;

public interface VentaRepository extends JpaRepository<Venta, Long>, JpaSpecificationExecutor<Venta> {

	@Query("""
			select v from Venta v
			join fetch v.cliente c
			left join fetch v.cobros co
			where v.estado = 'CONFIRMADA'
			  and v.fecha >= coalesce(:desde, v.fecha)
			  and v.fecha <= coalesce(:hasta, v.fecha)
			order by v.fecha asc, v.id asc
		""")
	List<Venta> findConfirmadasForReporte(@Param("desde") OffsetDateTime desde, @Param("hasta") OffsetDateTime hasta);
	@Query("""
			  select v from Venta v
			  join fetch v.cliente c
			  left join fetch v.turno t
			  left join fetch t.caja ca
			  where (:clienteId is null or c.id = :clienteId)
			    and (:estado is null or v.estado = :estado)
			    and v.fecha >= coalesce(:desde, v.fecha)
			    and v.fecha <= coalesce(:hasta, v.fecha)
			""")
	Page<Venta> search(@Param("clienteId") Long clienteId, @Param("estado") EstadoVenta estado,
			@Param("desde") OffsetDateTime desde, @Param("hasta") OffsetDateTime hasta, Pageable pageable);

	@Query("""
			  select v from Venta v
			  join fetch v.cliente c
			  left join fetch v.turno t
			  left join fetch t.caja ca
			  where v.id = :id
			""")
	Optional<Venta> findDtoById(@Param("id") Long id);

	@Query("""
			  select
			    function('date', v.fecha) as dia,
			    count(v.id) as cantidad,
			    coalesce(sum(v.total), 0) as total
			  from Venta v
			  where v.estado = 'CONFIRMADA'
			    and v.fecha >= coalesce(:desde, v.fecha)
			    and v.fecha <= coalesce(:hasta, v.fecha)
			  group by function('date', v.fecha)
			  order by function('date', v.fecha)
			""")
	List<VentasPorDiaRow> ventasPorDia(@Param("desde") OffsetDateTime desde, @Param("hasta") OffsetDateTime hasta);

	@Query("""
			  select
			    u.id as usuarioId,
			    u.username as usuarioNombre,
			    count(v.id) as cantidad,
			    coalesce(sum(v.total), 0) as total
			  from Venta v
			  join v.turno t
			  join t.usuarioApertura u
			  where v.estado = 'CONFIRMADA'
			    and v.fecha >= coalesce(:desde, v.fecha)
			    and v.fecha <= coalesce(:hasta, v.fecha)
			  group by u.id, u.username
			  order by total desc
			""")
	List<VentasPorCajeroRow> ventasPorCajero(@Param("desde") OffsetDateTime desde,
			@Param("hasta") OffsetDateTime hasta);

}
