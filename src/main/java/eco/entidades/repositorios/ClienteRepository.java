package eco.entidades.repositorios;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import eco.entidades.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

	Long countByNome(String nome);

	Long countByNomeAndIdNot(String nome, Integer id);
	
	//procura todos e ordena

	Page<Cliente> findAllByOrderByNomeAsc(Pageable pageable);

	Page<Cliente> findAllByOrderByNomeFantasiaAsc(Pageable pageable);

	Page<Cliente> findAllByOrderByTelefoneAsc(Pageable pageable);

	Page<Cliente> findAllByOrderByCelularAsc(Pageable pageable);

	Page<Cliente> findAllByOrderByEmailAsc(Pageable pageable);

	Page<Cliente> findAllByOrderByNomeDesc(Pageable pageable);

	Page<Cliente> findAllByOrderByNomeFantasiaDesc(Pageable pageable);

	Page<Cliente> findAllByOrderByTelefoneDesc(Pageable pageable);

	Page<Cliente> findAllByOrderByCelularDesc(Pageable pageable);
	
	
	//procura todos por nome e ordena

	Page<Cliente> findByNomeContainingOrderByNomeAsc(String nome, Pageable pageable);
	
	Page<Cliente> findByNomeContainingOrderByNomeDesc(String nome, Pageable pageable);
	
	Page<Cliente> findByNomeContainingOrderByNomeFantasiaAsc(String nome, Pageable pageable);
	
	Page<Cliente> findByNomeContainingOrderByNomeFantasiaDesc(String nome, Pageable pageable);
	
	Page<Cliente> findByNomeContainingOrderByTelefoneAsc(String nome, Pageable pageable);
	
	Page<Cliente> findByNomeContainingOrderByTelefoneDesc(String nome, Pageable pageable);
	
	Page<Cliente> findByNomeContainingOrderByCelularAsc(String nome, Pageable pageable);
	
	Page<Cliente>findByNomeContainingOrderByCelularDesc(String nome, Pageable pageable);

	Long countByStatusCliId(Integer id);

	Page<Cliente> findByNomeFantasiaContainingOrderByNomeAsc(String nome, Pageable pageable);

	Page<Cliente> findByNomeFantasiaContainingOrderByCelularDesc(String nome, Pageable pageable);

	Page<Cliente> findByNomeFantasiaContainingOrderByCelularAsc(String nome, Pageable pageable);

	Page<Cliente> findByNomeFantasiaContainingOrderByTelefoneDesc(String nome, Pageable pageable);

	Page<Cliente> findByNomeFantasiaContainingOrderByTelefoneAsc(String nome, Pageable pageable);

	Page<Cliente> findByNomeFantasiaContainingOrderByNomeFantasiaDesc(String nome, Pageable pageable);

	Page<Cliente> findByNomeFantasiaContainingOrderByNomeFantasiaAsc(String nome, Pageable pageable);

	Page<Cliente> findByNomeFantasiaContainingOrderByNomeDesc(String nome, Pageable pageable);

	@Query( value= "SELECT id, nome, nome_fantasia FROM eco_adminbd.cliente "
			+ "WHERE nome LIKE :param OR nome_fantasia LIKE :param ORDER BY nome ASC", 
			nativeQuery = true)
	List<?> buscaPorNomeEnomeFantasiaContem(@Param("param") String param);

	

	

}//fecha interface
