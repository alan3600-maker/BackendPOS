package py.com.hidraulica.caacupe.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.transaction.Transactional;
import py.com.hidraulica.caacupe.domain.CajaTurno;
import py.com.hidraulica.caacupe.domain.Cobro;
import py.com.hidraulica.caacupe.domain.Venta;
import py.com.hidraulica.caacupe.domain.VentaItem;
import py.com.hidraulica.caacupe.domain.enums.EstadoTurnoCaja;
import py.com.hidraulica.caacupe.domain.enums.EstadoVenta;
import py.com.hidraulica.caacupe.domain.enums.TipoItem;
import py.com.hidraulica.caacupe.domain.enums.TipoMovimientoStock;
import py.com.hidraulica.caacupe.dto.MovimientoStockRequest;
import py.com.hidraulica.caacupe.dto.PageResponse;
import py.com.hidraulica.caacupe.dto.VentaDto;
import py.com.hidraulica.caacupe.dto.VentaRequest;
import py.com.hidraulica.caacupe.exception.BusinessException;
import py.com.hidraulica.caacupe.exception.NotFoundException;
import py.com.hidraulica.caacupe.repository.CajaTurnoRepository;
import py.com.hidraulica.caacupe.repository.ClienteRepository;
import py.com.hidraulica.caacupe.repository.DepositoRepository;
import py.com.hidraulica.caacupe.repository.ProductoRepository;
import py.com.hidraulica.caacupe.repository.ServicioRepository;
import py.com.hidraulica.caacupe.repository.VentaRepository;
import py.com.hidraulica.caacupe.repository.spec.VentaSpecs;

@Service
public class VentaService {

	private final VentaRepository ventaRepo;
	private final ClienteRepository clienteRepo;
	private final ProductoRepository productoRepo;
	private final ServicioRepository servicioRepo;
	private final DepositoRepository depositoRepo;
	private final MovimientoStockService movimientoStockService;
	private final CajaTurnoRepository cajaTurnoRepo;

	public VentaService(VentaRepository ventaRepo, ClienteRepository clienteRepo, ProductoRepository productoRepo,
			ServicioRepository servicioRepo, DepositoRepository depositoRepo,
			MovimientoStockService movimientoStockService, CajaTurnoRepository cajaTurnoRepo) {
		this.ventaRepo = ventaRepo;
		this.clienteRepo = clienteRepo;
		this.productoRepo = productoRepo;
		this.servicioRepo = servicioRepo;
		this.depositoRepo = depositoRepo;
		this.movimientoStockService = movimientoStockService;
		this.cajaTurnoRepo = cajaTurnoRepo;
	}

	public Venta get(Long id) {
		return ventaRepo.findDtoById(id).orElseThrow(() -> new NotFoundException("Venta no encontrada: " + id));
	}

	public java.util.List<Venta> list() {
		return ventaRepo.findAll();
	}

	public Page<Venta> search(Long clienteId, EstadoVenta estado, OffsetDateTime desde, OffsetDateTime hasta,
			Pageable pageable) {
		Specification<Venta> spec = Specification.where(VentaSpecs.clienteId(clienteId)).and(VentaSpecs.estado(estado))
				.and(VentaSpecs.fechaDesde(desde)).and(VentaSpecs.fechaHasta(hasta));
		return ventaRepo.findAll(spec, pageable);
	}

	public VentaDto getDto(Long id) {
		return toDto(get(id));
	}

	@Transactional
	public VentaDto createDto(VentaRequest req) {
		return toDto(create(req));
	}

	@Transactional
	public VentaDto updateDto(Long id, VentaRequest req) {
		return toDto(update(id, req));
	}

	@Transactional
	public VentaDto confirmarDto(Long id, Long cajaId) {
		return toDto(confirmar(id, cajaId));
	}

	@Transactional
	public VentaDto anularDto(Long id) {
		return toDto(anular(id));
	}

	// Opcional (si lo vas a usar desde front; si no, mejor eliminar el endpoint
	// /ventas GET)
	public java.util.List<VentaDto> listDto() {
		return ventaRepo.findAll().stream().map(this::toDto).toList();
	}

	@Transactional
	public Venta create(VentaRequest req) {
		Venta v = new Venta();
		var cliente = clienteRepo.findById(req.clienteId)
				.orElseThrow(() -> new NotFoundException("Cliente no encontrado: " + req.clienteId));
		v.setCliente(cliente);
		v.setFecha(req.fecha != null ? req.fecha : OffsetDateTime.now());
		v.setObservacion(req.observacion);
		v.setEstado(EstadoVenta.BORRADOR);
		if (req.cajaId == null) {
			throw new BusinessException("Debe indicar cajaId para crear una venta.");
		}

		CajaTurno turno = cajaTurnoRepo
				.findFirstByCajaIdAndEstadoOrderByFechaAperturaDesc(req.cajaId, EstadoTurnoCaja.ABIERTA)
				.orElseThrow(() -> new BusinessException("No hay turno abierto para la caja " + req.cajaId));

		v.setTurno(turno);

		if (req.items == null || req.items.isEmpty())
			throw new BusinessException("La venta debe tener items.");

		BigDecimal total = BigDecimal.ZERO;

		for (var it : req.items) {
			if (it.cantidad == null || it.cantidad.signum() <= 0)
				throw new BusinessException("Cantidad inválida.");
			if (it.precioUnitario == null || it.precioUnitario.signum() < 0)
				throw new BusinessException("Precio inválido.");

			VentaItem item = new VentaItem();
			item.setVenta(v);
			item.setTipo(it.tipo);

			if (it.tipo == TipoItem.PRODUCTO) {
				if (it.productoId == null || it.depositoId == null)
					throw new BusinessException("PRODUCTO requiere productoId y depositoId.");
				var prod = productoRepo.findById(it.productoId)
						.orElseThrow(() -> new NotFoundException("Producto no encontrado: " + it.productoId));
				var dep = depositoRepo.findById(it.depositoId)
						.orElseThrow(() -> new NotFoundException("Deposito no encontrado: " + it.depositoId));
				item.setProducto(prod);
				item.setDeposito(dep);
				item.setDescripcion(
						it.descripcion != null && !it.descripcion.isBlank() ? it.descripcion : prod.getDescripcion());
			} else {
				if (it.servicioId == null)
					throw new BusinessException("SERVICIO requiere servicioId.");
				var srv = servicioRepo.findById(it.servicioId)
						.orElseThrow(() -> new NotFoundException("Servicio no encontrado: " + it.servicioId));
				item.setServicio(srv);
				item.setDescripcion(
						it.descripcion != null && !it.descripcion.isBlank() ? it.descripcion : srv.getDescripcion());
			}

			item.setCantidad(it.cantidad);
			item.setPrecioUnitario(it.precioUnitario);
			var totalLinea = it.precioUnitario.multiply(it.cantidad);
			item.setTotalLinea(totalLinea);

			v.getItems().add(item);
			total = total.add(totalLinea);
		}

		v.setTotal(total);

		if (req.cobros != null) {
			for (var c : req.cobros) {
				Cobro cobro = new Cobro();
				cobro.setVenta(v);
				cobro.setMedioPago(c.medioPago);
				cobro.setMonto(c.monto);
				v.getCobros().add(cobro);
			}
		}

		return ventaRepo.save(v);
	}

	@Transactional
	public Venta update(Long id, VentaRequest req) {
		Venta v = get(id);
		if (v.getEstado() != EstadoVenta.BORRADOR)
			throw new BusinessException("Solo se puede editar una venta en BORRADOR.");
		// No permitimos cambiar caja/turno en update
		if (req.cajaId != null) {
			if (v.getTurno() == null || v.getTurno().getCaja() == null || v.getTurno().getCaja().getId() == null) {
				throw new BusinessException("La venta no tiene caja/turno válido.");
			}
			Long cajaActual = v.getTurno().getCaja().getId();
			if (!req.cajaId.equals(cajaActual)) {
				throw new BusinessException("No se puede cambiar la caja de una venta existente.");
			}
		}
		v.getItems().clear();
		v.getCobros().clear();

		var cliente = clienteRepo.findById(req.clienteId)
				.orElseThrow(() -> new NotFoundException("Cliente no encontrado: " + req.clienteId));
		v.setCliente(cliente);
		v.setFecha(req.fecha != null ? req.fecha : v.getFecha());
		v.setObservacion(req.observacion);

		BigDecimal total = BigDecimal.ZERO;

		for (var it : req.items) {
			VentaItem item = new VentaItem();
			item.setVenta(v);
			item.setTipo(it.tipo);

			if (it.tipo == TipoItem.PRODUCTO) {
				if (it.productoId == null || it.depositoId == null)
					throw new BusinessException("PRODUCTO requiere productoId y depositoId.");
				var prod = productoRepo.findById(it.productoId)
						.orElseThrow(() -> new NotFoundException("Producto no encontrado: " + it.productoId));
				var dep = depositoRepo.findById(it.depositoId)
						.orElseThrow(() -> new NotFoundException("Deposito no encontrado: " + it.depositoId));
				item.setProducto(prod);
				item.setDeposito(dep);
				item.setDescripcion(
						it.descripcion != null && !it.descripcion.isBlank() ? it.descripcion : prod.getDescripcion());
			} else {
				if (it.servicioId == null)
					throw new BusinessException("SERVICIO requiere servicioId.");
				var srv = servicioRepo.findById(it.servicioId)
						.orElseThrow(() -> new NotFoundException("Servicio no encontrado: " + it.servicioId));
				item.setServicio(srv);
				item.setDescripcion(
						it.descripcion != null && !it.descripcion.isBlank() ? it.descripcion : srv.getDescripcion());
			}

			item.setCantidad(it.cantidad);
			item.setPrecioUnitario(it.precioUnitario);
			var totalLinea = it.precioUnitario.multiply(it.cantidad);
			item.setTotalLinea(totalLinea);

			v.getItems().add(item);
			total = total.add(totalLinea);
		}

		v.setTotal(total);

		if (req.cobros != null) {
			for (var c : req.cobros) {
				Cobro cobro = new Cobro();
				cobro.setVenta(v);
				cobro.setMedioPago(c.medioPago);
				cobro.setMonto(c.monto);
				v.getCobros().add(cobro);
			}
		}

		return ventaRepo.save(v);
	}

	@Transactional
	public void delete(Long id) {
		Venta v = get(id);
		if (v.getEstado() == EstadoVenta.CONFIRMADA)
			throw new BusinessException("No se puede borrar una venta CONFIRMADA.");
		ventaRepo.delete(v);
	}

	@Transactional
	public Venta confirmar(Long id, Long cajaId) {
		Venta v = get(id);

		if (v.getEstado() != EstadoVenta.BORRADOR)
			throw new BusinessException("La venta debe estar en BORRADOR para confirmar.");

		if (cajaId == null)
			throw new BusinessException("Debe indicar cajaId para confirmar la venta.");

		// 0) La venta debe tener turno (por NOT NULL, pero igual validamos)
		if (v.getTurno() == null)
			throw new BusinessException("La venta no tiene turno asignado.");

		// 1) Validar que el turno esté ABIERTO
		if (v.getTurno().getEstado() != EstadoTurnoCaja.ABIERTA) {
			throw new BusinessException("No se puede confirmar: el turno de la venta está CERRADO.");
		}

		// 2) Validar que cajaId coincide con la caja del turno
		if (v.getTurno().getCaja() == null || v.getTurno().getCaja().getId() == null) {
			throw new BusinessException("El turno no tiene caja asociada.");
		}
		if (!v.getTurno().getCaja().getId().equals(cajaId)) {
			throw new BusinessException("La venta pertenece a otra caja. No se puede confirmar con cajaId=" + cajaId);
		}

		// 3) Validar cobro contado: sum(cobros) == total
		BigDecimal total = v.getTotal() != null ? v.getTotal() : BigDecimal.ZERO;

		BigDecimal cobrado = v.getCobros() == null ? BigDecimal.ZERO
				: v.getCobros().stream().map(Cobro::getMonto).filter(m -> m != null).reduce(BigDecimal.ZERO,
						BigDecimal::add);

		if (cobrado.compareTo(total) != 0) {
			throw new BusinessException("El cobro debe ser igual al total. Total=" + total + " Cobrado=" + cobrado);
		}

		// 4) Stock (igual que ya tenías)
		MovimientoStockRequest mov = new MovimientoStockRequest();
		mov.tipo = TipoMovimientoStock.SALIDA;
		mov.fecha = OffsetDateTime.now();
		mov.referenciaTipo = "VENTA";
		mov.referenciaId = v.getId();
		mov.items = new java.util.ArrayList<>();

		for (var it : v.getItems()) {
			if (it.getTipo() == TipoItem.PRODUCTO) {
				MovimientoStockRequest.Item mi = new MovimientoStockRequest.Item();
				mi.productoId = it.getProducto().getId();
				mi.depositoId = it.getDeposito().getId();
				mi.cantidad = it.getCantidad();
				mov.items.add(mi);
			}
		}

		if (!mov.items.isEmpty()) {
			movimientoStockService.create(mov);
		}

		v.setEstado(EstadoVenta.CONFIRMADA);
		return ventaRepo.save(v);
	}

	/**
	 * Entrega 3: Anular una venta confirmada REVIRTIENDO el stock. - Busca el
	 * movimiento SALIDA generado al confirmar la venta (referenciaTipo=VENTA,
	 * referenciaId=<ventaId>) - Crea una reversa (ENTRADA) con
	 * referenciaTipo=REVERSA, referenciaId=<movimientoIdOriginal>
	 */
	@Transactional
	public Venta anular(Long id) {
		Venta v = get(id);

		if (v.getEstado() == EstadoVenta.CONFIRMADA) {
			// Reversa de stock
			movimientoStockService.reversarPorReferencia("VENTA", v.getId(), TipoMovimientoStock.SALIDA);
		}

		v.setEstado(EstadoVenta.ANULADA);
		return ventaRepo.save(v);
	}

	private VentaDto toDto(Venta v) {
		var c = v.getCliente();

		Long turnoId = v.getTurno() != null ? v.getTurno().getId() : null;

		Long cajaId = null;
		if (v.getTurno() != null && v.getTurno().getCaja() != null) {
			cajaId = v.getTurno().getCaja().getId();
		}

		return new VentaDto(v.getId(), v.getFecha(), v.getEstado(), c != null ? c.getId() : null,
				c != null ? c.getNombreRazonSocial() : null, v.getTotal(), turnoId, cajaId);
	}

	public PageResponse<VentaDto> searchDto(Long clienteId, EstadoVenta estado, OffsetDateTime desde,
			OffsetDateTime hasta, String q, int page, int size, String sortBy, String dir) {
		if (size <= 0)
			size = 10;
		if (page < 0)
			page = 0;

		// whitelist ordenable
		Set<String> allowed = Set.of("id", "fecha", "estado", "total");
		if (!StringUtils.hasText(sortBy) || !allowed.contains(sortBy))
			sortBy = "fecha";

		Sort.Direction direction = "asc".equalsIgnoreCase(dir) ? Sort.Direction.ASC : Sort.Direction.DESC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

		// q: por ahora lo dejamos sin efecto (si querés luego buscamos por
		// nombre/numero/etc)
		Page<Venta> p = ventaRepo.search(clienteId, estado, desde, hasta, pageable);

		var content = p.getContent().stream().map(this::toDto).toList();
		return new PageResponse<>(content, p.getTotalElements(), p.getTotalPages(), p.getNumber(), p.getSize());
	}

}
