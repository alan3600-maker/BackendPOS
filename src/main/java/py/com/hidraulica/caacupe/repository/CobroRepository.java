package py.com.hidraulica.caacupe.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import py.com.hidraulica.caacupe.domain.Cobro;
import py.com.hidraulica.caacupe.repository.proj.CobroResumenPorMedio;
import py.com.hidraulica.caacupe.repository.proj.CobrosPorMedioRow;

public interface CobroRepository extends JpaRepository<Cobro, Long> {

	@Query("""
			  select c.medioPago as medioPago, sum(c.monto) as total
			  from Cobro c
			  join c.venta v
			  where v.turno.id = :turnoId
			    and v.estado = 'CONFIRMADA'
			  group by c.medioPago
			""")
	List<CobroResumenPorMedio> resumenPorMedioEnTurno(@Param("turnoId") Long turnoId);

	@Query("""
			  select coalesce(sum(c.monto), 0)
			  from Cobro c
			  join c.venta v
			  where v.turno.id = :turnoId
			    and v.estado = 'CONFIRMADA'
			""")
	java.math.BigDecimal totalCobradoEnTurno(@Param("turnoId") Long turnoId);

	@Query("""
			  select c.medioPago as medioPago, coalesce(sum(c.monto), 0) as total
			  from Cobro c
			  join c.venta v
			  where v.estado = 'CONFIRMADA'
			    and (:desde is null or v.fecha >= :desde)
			    and (:hasta is null or v.fecha <= :hasta)
			  group by c.medioPago
			  order by c.medioPago
			""")
	List<CobrosPorMedioRow> cobrosPorMedio(@Param("desde") OffsetDateTime desde, @Param("hasta") OffsetDateTime hasta);

}
