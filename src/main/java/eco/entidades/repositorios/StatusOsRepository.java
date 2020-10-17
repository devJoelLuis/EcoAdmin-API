package eco.entidades.repositorios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import eco.entidades.StatusOs;

public interface StatusOsRepository extends JpaRepository<StatusOs, Integer> {

	Long countByNome(String nome);

	Long countByNomeAndIdNot(String nome, Integer id);

	Page<StatusOs> findByNomeContainingOrderByNomeAsc(String nome, Pageable pageable);

}
