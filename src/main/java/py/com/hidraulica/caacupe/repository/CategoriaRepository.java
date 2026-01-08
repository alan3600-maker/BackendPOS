package py.com.hidraulica.caacupe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import py.com.hidraulica.caacupe.domain.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

  boolean existsByNombreIgnoreCase(String nombre);

  @Query("""
      select c from Categoria c
      where
        (:q is null or :q = '' or lower(c.nombre) like lower(concat('%', :q, '%')))
        and (:incluirInactivos = true or c.activo = true)
      """)
  Page<Categoria> search(@Param("q") String q, @Param("incluirInactivos") boolean incluirInactivos, Pageable pageable);
}
