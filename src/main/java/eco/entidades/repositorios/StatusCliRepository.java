package eco.entidades.repositorios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import eco.entidades.StatusCli;

public interface StatusCliRepository extends JpaRepository<StatusCli, Integer> {

	Long countByStatusCli(String estatus);

	Long countByStatusCliAndIdNot(String estatus, Integer id);

	Page<StatusCli> findByStatusCliContainingOrderByStatusCliAsc(String nome, Pageable pageable);

	

}
