package py.com.hidraulica.caacupe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import py.com.hidraulica.caacupe.domain.Marca;

public interface MarcaRepository extends JpaRepository<Marca, Long> {

  boolean existsByNombreIgnoreCase(String nombre);

  @Query("""
      select m from Marca m
      where
        (:q is null or :q = '' or lower(m.nombre) like lower(concat('%', :q, '%')))
        and (:incluirInactivos = true or m.activo = true)
      """)
  Page<Marca> search(@Param("q") String q, @Param("incluirInactivos") boolean incluirInactivos, Pageable pageable);
}
