package py.com.hidraulica.caacupe.repository.spec;

import py.com.hidraulica.caacupe.domain.Venta;
import py.com.hidraulica.caacupe.domain.enums.EstadoVenta;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;

public final class VentaSpecs {
  private VentaSpecs() {}

  public static Specification<Venta> clienteId(Long clienteId) {
    return (root, q, cb) -> clienteId == null ? null : cb.equal(root.get("cliente").get("id"), clienteId);
  }

  public static Specification<Venta> estado(EstadoVenta estado) {
    return (root, q, cb) -> estado == null ? null : cb.equal(root.get("estado"), estado);
  }

  public static Specification<Venta> fechaDesde(OffsetDateTime desde) {
    return (root, q, cb) -> desde == null ? null : cb.greaterThanOrEqualTo(root.get("fecha"), desde);
  }

  public static Specification<Venta> fechaHasta(OffsetDateTime hasta) {
    return (root, q, cb) -> hasta == null ? null : cb.lessThanOrEqualTo(root.get("fecha"), hasta);
  }
}
