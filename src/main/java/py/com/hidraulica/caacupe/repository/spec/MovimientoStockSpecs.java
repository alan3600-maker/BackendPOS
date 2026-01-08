package py.com.hidraulica.caacupe.repository.spec;

import java.time.OffsetDateTime;

import org.springframework.data.jpa.domain.Specification;

import py.com.hidraulica.caacupe.domain.MovimientoStock;
import py.com.hidraulica.caacupe.domain.enums.TipoMovimientoStock;

public class MovimientoStockSpecs {

  public static Specification<MovimientoStock> tipo(TipoMovimientoStock tipo) {
    return (root, query, cb) -> tipo == null ? cb.conjunction() : cb.equal(root.get("tipo"), tipo);
  }

  public static Specification<MovimientoStock> fechaDesde(OffsetDateTime desde) {
    return (root, query, cb) -> desde == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("fecha"), desde);
  }

  public static Specification<MovimientoStock> fechaHasta(OffsetDateTime hasta) {
    return (root, query, cb) -> hasta == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("fecha"), hasta);
  }

  public static Specification<MovimientoStock> referenciaTipo(String referenciaTipo) {
    return (root, query, cb) -> (referenciaTipo == null || referenciaTipo.isBlank())
        ? cb.conjunction()
        : cb.equal(root.get("referenciaTipo"), referenciaTipo);
  }

  public static Specification<MovimientoStock> referenciaId(Long referenciaId) {
    return (root, query, cb) -> referenciaId == null ? cb.conjunction() : cb.equal(root.get("referenciaId"), referenciaId);
  }
}
