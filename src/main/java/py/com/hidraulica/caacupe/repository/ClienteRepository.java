package py.com.hidraulica.caacupe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import py.com.hidraulica.caacupe.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

  boolean existsByRuc(String ruc);

  java.util.List<Cliente> findAllByActivoTrue();

  @org.springframework.data.jpa.repository.Query("""
      select c from Cliente c
      where (
        :q is null or :q = '' or
        lower(c.nombreRazonSocial) like lower(concat('%', :q, '%')) or
        lower(coalesce(c.ruc, '')) like lower(concat('%', :q, '%')) or
        lower(coalesce(c.telefono, '')) like lower(concat('%', :q, '%'))
      )
      and (:incluirInactivos = true or c.activo = true)
      """)
  Page<Cliente> search(@org.springframework.data.repository.query.Param("q") String q,
                       @org.springframework.data.repository.query.Param("incluirInactivos") boolean incluirInactivos,
                       Pageable pageable);

}