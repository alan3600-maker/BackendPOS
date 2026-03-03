package py.com.hidraulica.caacupe.repository.spec;

import py.com.hidraulica.caacupe.domain.Factura;
import py.com.hidraulica.caacupe.domain.enums.EstadoFactura;
import py.com.hidraulica.caacupe.domain.enums.TipoFactura;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;

public final class FacturaSpecs {
  private FacturaSpecs() {}

  public static Specification<Factura> clienteId(Long clienteId) {
    return (root, q, cb) -> clienteId == null ? null : cb.equal(root.get("cliente").get("id"), clienteId);
  }

  public static Specification<Factura> tipo(TipoFactura tipo) {
    return (root, q, cb) -> tipo == null ? null : cb.equal(root.get("tipo"), tipo);
  }

  public static Specification<Factura> estado(EstadoFactura estado) {
    return (root, q, cb) -> estado == null ? null : cb.equal(root.get("estado"), estado);
  }

  public static Specification<Factura> numeroLike(String numero) {
    return (root, q, cb) -> (numero == null || numero.isBlank()) ? null :
        cb.like(cb.lower(root.get("numero")), "%" + numero.trim().toLowerCase() + "%");
  }

  public static Specification<Factura> fechaDesde(OffsetDateTime desde) {
    return (root, q, cb) -> desde == null ? null : cb.greaterThanOrEqualTo(root.get("fecha"), desde);
  }

  public static Specification<Factura> fechaHasta(OffsetDateTime hasta) {
    return (root, q, cb) -> hasta == null ? null : cb.lessThanOrEqualTo(root.get("fecha"), hasta);
  }
}
