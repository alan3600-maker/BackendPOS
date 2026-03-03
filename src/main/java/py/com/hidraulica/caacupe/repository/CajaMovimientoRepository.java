package py.com.hidraulica.caacupe.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import py.com.hidraulica.caacupe.domain.CajaMovimiento;
import py.com.hidraulica.caacupe.domain.enums.TipoMovimientoCaja;

public interface CajaMovimientoRepository extends JpaRepository<CajaMovimiento, Long> {

  @Query("""
      select m from CajaMovimiento m
      left join fetch m.usuario u
      where m.turno.id = :turnoId
      order by m.fecha desc, m.id desc
      """)
  List<CajaMovimiento> findByTurnoId(@Param("turnoId") Long turnoId);

  @Query("""
      select coalesce(sum(m.monto), 0)
      from CajaMovimiento m
      where m.turno.id = :turnoId
        and m.tipo = :tipo
      """)
  BigDecimal totalByTurnoAndTipo(@Param("turnoId") Long turnoId, @Param("tipo") TipoMovimientoCaja tipo);
}
