package py.com.hidraulica.caacupe.service;

import py.com.hidraulica.caacupe.domain.*;
import py.com.hidraulica.caacupe.domain.enums.EstadoOrdenTrabajo;
import py.com.hidraulica.caacupe.domain.enums.TipoMovimientoStock;
import py.com.hidraulica.caacupe.dto.MovimientoStockRequest;
import py.com.hidraulica.caacupe.dto.OrdenTrabajoRequest;
import py.com.hidraulica.caacupe.exception.BusinessException;
import py.com.hidraulica.caacupe.exception.NotFoundException;
import py.com.hidraulica.caacupe.repository.*;
import py.com.hidraulica.caacupe.repository.spec.OrdenTrabajoSpecs;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class OrdenTrabajoService {

  private final OrdenTrabajoRepository repo;
  private final ClienteRepository clienteRepo;
  private final ProductoRepository productoRepo;
  private final DepositoRepository depositoRepo;
  private final MovimientoStockService movimientoStockService;

  public OrdenTrabajoService(OrdenTrabajoRepository repo,
                             ClienteRepository clienteRepo,
                             ProductoRepository productoRepo,
                             DepositoRepository depositoRepo,
                             MovimientoStockService movimientoStockService) {
    this.repo = repo;
    this.clienteRepo = clienteRepo;
    this.productoRepo = productoRepo;
    this.depositoRepo = depositoRepo;
    this.movimientoStockService = movimientoStockService;
  }

  public OrdenTrabajo get(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("OT no encontrada: " + id));
  }

  public java.util.List<OrdenTrabajo> list() {
    return repo.findAll();
  }

  public Page<OrdenTrabajo> search(Long clienteId, EstadoOrdenTrabajo estado, OffsetDateTime desde, OffsetDateTime hasta, Pageable pageable) {
    Specification<OrdenTrabajo> spec = Specification.where(OrdenTrabajoSpecs.clienteId(clienteId))
        .and(OrdenTrabajoSpecs.estado(estado))
        .and(OrdenTrabajoSpecs.fechaDesde(desde))
        .and(OrdenTrabajoSpecs.fechaHasta(hasta));
    return repo.findAll(spec, pageable);
  }

  @Transactional
  public OrdenTrabajo create(OrdenTrabajoRequest req) {
    OrdenTrabajo ot = new OrdenTrabajo();
    ot.setCliente(clienteRepo.findById(req.clienteId).orElseThrow(() -> new NotFoundException("Cliente no encontrado: " + req.clienteId)));
    ot.setFecha(req.fecha != null ? req.fecha : OffsetDateTime.now());
    ot.setDescripcion(req.descripcion);
    ot.setEstado(EstadoOrdenTrabajo.ABIERTA);

    if (req.repuestos != null) {
      for (var r : req.repuestos) {
        var prod = productoRepo.findById(r.productoId).orElseThrow(() -> new NotFoundException("Producto no encontrado: " + r.productoId));
        var dep = depositoRepo.findById(r.depositoId).orElseThrow(() -> new NotFoundException("Deposito no encontrado: " + r.depositoId));

        OrdenTrabajoRepuesto rep = new OrdenTrabajoRepuesto();
        rep.setOrdenTrabajo(ot);
        rep.setProducto(prod);
        rep.setDeposito(dep);
        rep.setCantidad(r.cantidad);
        ot.getRepuestos().add(rep);
      }
    }

    return repo.save(ot);
  }

  @Transactional
  public OrdenTrabajo update(Long id, OrdenTrabajoRequest req) {
    OrdenTrabajo ot = get(id);
    if (ot.getEstado() == EstadoOrdenTrabajo.FINALIZADA) throw new BusinessException("No se puede editar una OT FINALIZADA.");

    ot.setCliente(clienteRepo.findById(req.clienteId).orElseThrow(() -> new NotFoundException("Cliente no encontrado: " + req.clienteId)));
    ot.setFecha(req.fecha != null ? req.fecha : ot.getFecha());
    ot.setDescripcion(req.descripcion);

    ot.getRepuestos().clear();
    if (req.repuestos != null) {
      for (var r : req.repuestos) {
        var prod = productoRepo.findById(r.productoId).orElseThrow(() -> new NotFoundException("Producto no encontrado: " + r.productoId));
        var dep = depositoRepo.findById(r.depositoId).orElseThrow(() -> new NotFoundException("Deposito no encontrado: " + r.depositoId));

        OrdenTrabajoRepuesto rep = new OrdenTrabajoRepuesto();
        rep.setOrdenTrabajo(ot);
        rep.setProducto(prod);
        rep.setDeposito(dep);
        rep.setCantidad(r.cantidad);
        ot.getRepuestos().add(rep);
      }
    }

    return repo.save(ot);
  }

  @Transactional
  public void delete(Long id) {
    OrdenTrabajo ot = get(id);
    if (ot.getEstado() != EstadoOrdenTrabajo.ABIERTA) throw new BusinessException("Solo se puede borrar una OT ABIERTA.");
    repo.delete(ot);
  }

  @Transactional
  public OrdenTrabajo cambiarEstado(Long id, EstadoOrdenTrabajo estado) {
    OrdenTrabajo ot = get(id);
    ot.setEstado(estado);
    return repo.save(ot);
  }

  @Transactional
  public OrdenTrabajo consumirRepuestos(Long id) {
    OrdenTrabajo ot = get(id);
    if (ot.getRepuestos().isEmpty()) throw new BusinessException("OT sin repuestos.");

    MovimientoStockRequest mov = new MovimientoStockRequest();
    mov.tipo = TipoMovimientoStock.SALIDA;
    mov.fecha = OffsetDateTime.now();
    mov.referenciaTipo = "OT";
    mov.referenciaId = ot.getId();
    mov.items = new java.util.ArrayList<>();

    for (var r : ot.getRepuestos()) {
      MovimientoStockRequest.Item mi = new MovimientoStockRequest.Item();
      mi.productoId = r.getProducto().getId();
      mi.depositoId = r.getDeposito().getId();
      mi.cantidad = r.getCantidad();
      mov.items.add(mi);
    }

    movimientoStockService.create(mov);
    ot.setEstado(EstadoOrdenTrabajo.EN_PROCESO);
    return repo.save(ot);
  }

  /**
   * Entrega 3: Revertir el consumo de repuestos (reversa de stock).
   * - Reversa del último movimiento SALIDA asociado a la OT
   * - Vuelve el estado a ABIERTA (si estaba EN_PROCESO)
   */
  @Transactional
  public OrdenTrabajo revertirConsumo(Long id) {
    OrdenTrabajo ot = get(id);

    if (ot.getEstado() != EstadoOrdenTrabajo.EN_PROCESO) {
      throw new BusinessException("Solo se puede revertir consumo cuando la OT está EN_PROCESO.");
    }

    movimientoStockService.reversarPorReferencia("OT", ot.getId(), TipoMovimientoStock.SALIDA);
    ot.setEstado(EstadoOrdenTrabajo.ABIERTA);
    return repo.save(ot);
  }
}
