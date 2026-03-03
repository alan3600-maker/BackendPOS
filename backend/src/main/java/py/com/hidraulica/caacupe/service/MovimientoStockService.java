package py.com.hidraulica.caacupe.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.com.hidraulica.caacupe.domain.MovimientoStock;
import py.com.hidraulica.caacupe.domain.MovimientoStockItem;
import py.com.hidraulica.caacupe.domain.Stock;
import py.com.hidraulica.caacupe.domain.enums.TipoMovimientoStock;
import py.com.hidraulica.caacupe.dto.MovimientoStockDto;
import py.com.hidraulica.caacupe.dto.MovimientoStockRequest;
import py.com.hidraulica.caacupe.dto.PageResponse;
import py.com.hidraulica.caacupe.exception.BusinessException;
import py.com.hidraulica.caacupe.exception.NotFoundException;
import py.com.hidraulica.caacupe.repository.DepositoRepository;
import py.com.hidraulica.caacupe.repository.MovimientoStockRepository;
import py.com.hidraulica.caacupe.repository.ProductoRepository;
import py.com.hidraulica.caacupe.repository.StockRepository;

@Service
public class MovimientoStockService {
	private final MovimientoStockRepository movimientoRepo;
	private final ProductoRepository productoRepo;
	private final DepositoRepository depositoRepo;
	private final StockRepository stockRepo;

	public MovimientoStockService(MovimientoStockRepository movimientoRepo, ProductoRepository productoRepo,
			DepositoRepository depositoRepo, StockRepository stockRepo) {
		this.movimientoRepo = movimientoRepo;
		this.productoRepo = productoRepo;
		this.depositoRepo = depositoRepo;
		this.stockRepo = stockRepo;
	}

	public MovimientoStock get(Long id) {
		return movimientoRepo.findById(id)
				.orElseThrow(() -> new NotFoundException("MovimientoStock no encontrado: " + id));
	}

	public List<MovimientoStock> list() {
		return movimientoRepo.findAll();
	}

	@Transactional
	public MovimientoStock create(MovimientoStockRequest req) {
		if (req.items == null || req.items.isEmpty()) {
			throw new BusinessException("El movimiento debe tener al menos 1 item.");
		}
		if (req.tipo == null) {
			throw new BusinessException("El tipo de movimiento es requerido.");
		}

		MovimientoStock m = new MovimientoStock();
		m.setTipo(req.tipo);
		m.setFecha(req.fecha != null ? req.fecha : OffsetDateTime.now());
		m.setReferenciaTipo(req.referenciaTipo);
		m.setReferenciaId(req.referenciaId);

		for (var it : req.items) {
			if (it.cantidad == null || it.cantidad.compareTo(BigDecimal.ZERO) <= 0) {
				throw new BusinessException("La cantidad debe ser mayor a 0.");
			}

			var prod = productoRepo.findById(it.productoId)
					.orElseThrow(() -> new NotFoundException("Producto no encontrado: " + it.productoId));
			var dep = depositoRepo.findById(it.depositoId)
					.orElseThrow(() -> new NotFoundException("Deposito no encontrado: " + it.depositoId));

			// 1) Item del movimiento
			MovimientoStockItem item = new MovimientoStockItem();
			item.setMovimiento(m);
			item.setProducto(prod);
			item.setDeposito(dep);
			item.setCantidad(it.cantidad);
			m.getItems().add(item);

			// 2) Ajuste de stock
			Stock stock = stockRepo.findByProductoIdAndDepositoId(prod.getId(), dep.getId()).orElse(null);
			if (stock == null) {
				stock = new Stock();
				stock.setProducto(prod);
				stock.setDeposito(dep);
				stock.setCantidad(BigDecimal.ZERO);
			}

			BigDecimal actual = stock.getCantidad() == null ? BigDecimal.ZERO : stock.getCantidad();
			BigDecimal nuevaCantidad;
			if (req.tipo == TipoMovimientoStock.ENTRADA) {
				nuevaCantidad = actual.add(it.cantidad);
			} else {
				nuevaCantidad = actual.subtract(it.cantidad);
				if (nuevaCantidad.compareTo(BigDecimal.ZERO) < 0) {
					throw new BusinessException("Stock insuficiente para productoId=" + prod.getId() + ", depositoId="
							+ dep.getId() + ". Disponible=" + actual + ", solicitado=" + it.cantidad);
				}
			}

			stock.setCantidad(nuevaCantidad);
			stockRepo.save(stock);
		}

		return movimientoRepo.save(m);
	}

	/**
	 * Revierte un movimiento existente creando un movimiento opuesto y ajustando
	 * stock. NO elimina el movimiento original.
	 *
	 * Convención de referencias: - Movimiento original: referenciaTipo=VENTA u OT,
	 * referenciaId=<id> y tipo=SALIDA - Movimiento reversa creado:
	 * referenciaTipo="REVERSA", referenciaId=<idMovimientoOriginal>
	 */
	@Transactional
	public MovimientoStock reversarMovimiento(Long movimientoId) {
		MovimientoStock original = get(movimientoId);

		// Evitar doble reversa
		if (movimientoRepo.existsByReferenciaTipoAndReferenciaId("REVERSA", original.getId())) {
			throw new BusinessException("El movimiento ya fue reversado. movimientoId=" + original.getId());
		}

		MovimientoStockRequest req = new MovimientoStockRequest();
		req.tipo = (original.getTipo() == TipoMovimientoStock.ENTRADA) ? TipoMovimientoStock.SALIDA
				: TipoMovimientoStock.ENTRADA;
		req.fecha = OffsetDateTime.now();
		req.referenciaTipo = "REVERSA";
		req.referenciaId = original.getId();
		req.items = new java.util.ArrayList<>();

		for (var it : original.getItems()) {
			MovimientoStockRequest.Item r = new MovimientoStockRequest.Item();
			r.productoId = it.getProducto().getId();
			r.depositoId = it.getDeposito().getId();
			r.cantidad = it.getCantidad();
			req.items.add(r);
		}

		return create(req);
	}

	@Transactional
	public MovimientoStock reversarPorReferencia(String referenciaTipo, Long referenciaId,
			TipoMovimientoStock tipoEsperado) {
		var mov = movimientoRepo
				.findFirstByReferenciaTipoAndReferenciaIdAndTipoOrderByIdDesc(referenciaTipo, referenciaId,
						tipoEsperado)
				.orElseThrow(() -> new NotFoundException(
						"No existe movimiento para referencia " + referenciaTipo + " #" + referenciaId));
		return reversarMovimiento(mov.getId());
	}

	@Transactional
	public void delete(Long id) {
		// Por simplicidad, NO revertimos stock al borrar.
		movimientoRepo.deleteById(id);
	}

	@Transactional
	public MovimientoStockDto createDto(MovimientoStockRequest req) {
		return toDto(create(req));
	}

	@Transactional(readOnly = true)
	public MovimientoStockDto getDto(Long id) {
		return toDto(get(id));
	}

	private MovimientoStockDto toDto(MovimientoStock m) {
		int count = (m.getItems() != null) ? m.getItems().size() : 0;
		return new MovimientoStockDto(m.getId(), m.getTipo(), m.getFecha(), m.getReferenciaTipo(), m.getReferenciaId(),
				count);
	}

	@Transactional(readOnly = true)
	public PageResponse<MovimientoStockDto> searchDto(TipoMovimientoStock tipo, OffsetDateTime desde,
			OffsetDateTime hasta, String referenciaTipo, Long referenciaId, int page, int size, String sortBy,
			String dir) {
		if (size <= 0)
			size = 10;
		if (page < 0)
			page = 0;

		// whitelist ordenable
		var allowed = java.util.Set.of("id", "fecha", "tipo");
		if (sortBy == null || !allowed.contains(sortBy))
			sortBy = "fecha";

		var direction = "asc".equalsIgnoreCase(dir) ? org.springframework.data.domain.Sort.Direction.ASC
				: org.springframework.data.domain.Sort.Direction.DESC;

		var pageable = org.springframework.data.domain.PageRequest.of(page, size,
				org.springframework.data.domain.Sort.by(direction, sortBy));

		var spec = org.springframework.data.jpa.domain.Specification
				.where(py.com.hidraulica.caacupe.repository.spec.MovimientoStockSpecs.tipo(tipo))
				.and(py.com.hidraulica.caacupe.repository.spec.MovimientoStockSpecs.fechaDesde(desde))
				.and(py.com.hidraulica.caacupe.repository.spec.MovimientoStockSpecs.fechaHasta(hasta))
				.and(py.com.hidraulica.caacupe.repository.spec.MovimientoStockSpecs.referenciaTipo(referenciaTipo))
				.and(py.com.hidraulica.caacupe.repository.spec.MovimientoStockSpecs.referenciaId(referenciaId));

		var p = movimientoRepo.findAll(spec, pageable);
		var content = p.getContent().stream().map(this::toDto).toList();

		return new PageResponse<>(content, p.getTotalElements(), p.getTotalPages(), p.getNumber(), p.getSize());
	}

}
