package py.com.hidraulica.caacupe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import py.com.hidraulica.caacupe.domain.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

  @Query("""
      select p from Proveedor p
      where
        (:q is null or :q = '' or
          lower(p.nombreRazonSocial) like lower(concat('%', :q, '%')) or
          lower(coalesce(p.ruc, '')) like lower(concat('%', :q, '%')) or
          lower(coalesce(p.telefono, '')) like lower(concat('%', :q, '%'))
        )
        and (:incluirInactivos = true or p.activo = true)
      """)
  Page<Proveedor> search(@Param("q") String q, @Param("incluirInactivos") boolean incluirInactivos, Pageable pageable);
}
