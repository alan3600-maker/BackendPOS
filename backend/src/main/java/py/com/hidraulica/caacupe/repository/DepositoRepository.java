package py.com.hidraulica.caacupe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import py.com.hidraulica.caacupe.domain.Deposito;

public interface DepositoRepository extends JpaRepository<Deposito, Long> {

		@Query("""
			  select p from Deposito p
			  where (
			      :q is null or :q = '' or
			      lower(p.nombre) like lower(concat('%', :q, '%'))			      
			    )
			""")
	Page<Deposito> search(@Param("q") String q, Pageable pageable);
}