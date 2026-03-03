package py.com.hidraulica.caacupe.repository.spec;

import py.com.hidraulica.caacupe.domain.Presupuesto;
import py.com.hidraulica.caacupe.domain.enums.EstadoPresupuesto;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;

public final class PresupuestoSpecs {
  private PresupuestoSpecs() {}

  public static Specification<Presupuesto> clienteId(Long clienteId) {
    return (root, q, cb) -> clienteId == null ? null : cb.equal(root.get("cliente").get("id"), clienteId);
  }

  public static Specification<Presupuesto> estado(EstadoPresupuesto estado) {
    return (root, q, cb) -> estado == null ? null : cb.equal(root.get("estado"), estado);
  }

  public static Specification<Presupuesto> fechaDesde(OffsetDateTime desde) {
    return (root, q, cb) -> desde == null ? null : cb.greaterThanOrEqualTo(root.get("fecha"), desde);
  }

  public static Specification<Presupuesto> fechaHasta(OffsetDateTime hasta) {
    return (root, q, cb) -> hasta == null ? null : cb.lessThanOrEqualTo(root.get("fecha"), hasta);
  }
}
