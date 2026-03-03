package py.com.hidraulica.caacupe.repository.spec;

import py.com.hidraulica.caacupe.domain.OrdenTrabajo;
import py.com.hidraulica.caacupe.domain.enums.EstadoOrdenTrabajo;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;

public final class OrdenTrabajoSpecs {
  private OrdenTrabajoSpecs() {}

  public static Specification<OrdenTrabajo> clienteId(Long clienteId) {
    return (root, q, cb) -> clienteId == null ? null : cb.equal(root.get("cliente").get("id"), clienteId);
  }

  public static Specification<OrdenTrabajo> estado(EstadoOrdenTrabajo estado) {
    return (root, q, cb) -> estado == null ? null : cb.equal(root.get("estado"), estado);
  }

  public static Specification<OrdenTrabajo> fechaDesde(OffsetDateTime desde) {
    return (root, q, cb) -> desde == null ? null : cb.greaterThanOrEqualTo(root.get("fecha"), desde);
  }

  public static Specification<OrdenTrabajo> fechaHasta(OffsetDateTime hasta) {
    return (root, q, cb) -> hasta == null ? null : cb.lessThanOrEqualTo(root.get("fecha"), hasta);
  }
}
