package py.com.hidraulica.caacupe.service;

import java.util.List;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import py.com.hidraulica.caacupe.domain.Producto;
import py.com.hidraulica.caacupe.dto.PageResponse;
import py.com.hidraulica.caacupe.dto.ProductoDto;
import py.com.hidraulica.caacupe.exception.BusinessException;
import py.com.hidraulica.caacupe.exception.NotFoundException;
import py.com.hidraulica.caacupe.repository.ProductoRepository;

@Service
public class ProductoService implements CrudService<Producto, Long> {

  private final ProductoRepository repo;

  public ProductoService(ProductoRepository repo) {
    this.repo = repo;
  }

  private ProductoDto toDto(Producto p) {
    Long categoriaId = p.getCategoria() != null ? p.getCategoria().getId() : null;
    String categoriaNombre = p.getCategoria() != null ? p.getCategoria().getNombre() : null;

    Long marcaId = p.getMarca() != null ? p.getMarca().getId() : null;
    String marcaNombre = p.getMarca() != null ? p.getMarca().getNombre() : null;

    Long proveedorId = p.getProveedor() != null ? p.getProveedor().getId() : null;
    String proveedorNombre = p.getProveedor() != null ? p.getProveedor().getNombreRazonSocial() : null;

    return new ProductoDto(
        p.getId(), p.getSku(), p.getDescripcion(), p.getPrecio(),
        p.isActivo(),
        categoriaId, categoriaNombre,
        marcaId, marcaNombre,
        proveedorId, proveedorNombre
    );
  }

  public PageResponse<ProductoDto> searchDto(String q, Boolean incluirInactivos, int page, int size, String sortBy, String dir) {
    if (size <= 0) size = 20;
    if (page < 0) page = 0;

    // whitelist campos ordenables
    Set<String> allowed = Set.of("id", "sku", "descripcion", "precio");
    if (!StringUtils.hasText(sortBy) || !allowed.contains(sortBy)) sortBy = "id";

    Sort.Direction direction = "asc".equalsIgnoreCase(dir) ? Sort.Direction.ASC : Sort.Direction.DESC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

    String s = StringUtils.hasText(q) ? q.trim() : null;
    boolean inc = Boolean.TRUE.equals(incluirInactivos);

    Page<Producto> p = repo.search(s, inc, pageable);
    var content = p.getContent().stream().map(this::toDto).toList();
    return new PageResponse<>(content, p.getTotalElements(), p.getTotalPages(), p.getNumber(), p.getSize());
  }

  @Override
  public Producto create(Producto entity) {
    String sku = entity.getSku() != null ? entity.getSku().trim() : null;
    entity.setSku(sku);

    if (StringUtils.hasText(sku) && repo.existsBySkuIgnoreCase(sku)) {
      throw new BusinessException("Ya existe un producto con SKU: " + sku);
    }

    try {
      return repo.save(entity);
    } catch (DataIntegrityViolationException ex) {
      throw new BusinessException("Ya existe un producto con SKU: " + sku);
    }
  }

  @Override
  public Producto get(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Producto no encontrado: " + id));
  }

  public Producto getBySku(String sku) {
    return repo.findBySku(sku).orElseThrow(() -> new NotFoundException("Producto no encontrado por sku: " + sku));
  }

  @Override
  public List<Producto> list() {
    // por defecto solo activos
    return repo.findAllByActivoTrue();
  }

  @Override
  public Producto update(Long id, Producto entity) {
    var current = get(id);

    String sku = entity.getSku() != null ? entity.getSku().trim() : null;

    if (StringUtils.hasText(sku) && repo.existsBySkuIgnoreCaseAndIdNot(sku, id)) {
      throw new BusinessException("Ya existe un producto con SKU: " + sku);
    }

    current.setSku(sku);
    current.setDescripcion(entity.getDescripcion());
    current.setPrecio(entity.getPrecio());

    // relaciones (opcionales por ahora, para no romper datos ya existentes)
    current.setCategoria(entity.getCategoria() != null ? entity.getCategoria() : current.getCategoria());
    current.setMarca(entity.getMarca() != null ? entity.getMarca() : current.getMarca());
    current.setProveedor(entity.getProveedor() != null ? entity.getProveedor() : current.getProveedor());

    try {
      return repo.save(current);
    } catch (DataIntegrityViolationException ex) {
      throw new BusinessException("Ya existe un producto con SKU: " + sku);
    }
  }

  public void desactivar(Long id) {
    var entity = get(id);
    entity.setActivo(false);
    repo.save(entity);
  }

  public void activar(Long id) {
    var entity = get(id);
    entity.setActivo(true);
    repo.save(entity);
  }

  @Override
  public void delete(Long id) {
    // soft-delete
    desactivar(id);
  }
}
