package py.com.hidraulica.caacupe.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import py.com.hidraulica.caacupe.domain.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
	Page<Producto> findByDescripcionContainingIgnoreCase(String q, Pageable pageable);

	Optional<Producto> findBySku(String sku);

  java.util.List<Producto> findAllByActivoTrue();

	Page<Producto> findByDescripcionContainingIgnoreCaseOrSkuContainingIgnoreCase(String descripcion, String sku,
			Pageable pageable);

	@Query("""
      select p from Producto p
      where (
        :q is null or :q = '' or
        lower(p.descripcion) like lower(concat('%', :q, '%')) or
        lower(p.sku) like lower(concat('%', :q, '%'))
      )
      and (:incluirInactivos = true or p.activo = true)
      """)
  Page<Producto> search(@Param("q") String q, @Param("incluirInactivos") boolean incluirInactivos, Pageable pageable);
	
	boolean existsBySkuIgnoreCase(String sku);

	boolean existsBySkuIgnoreCaseAndIdNot(String sku, Long id);

}
