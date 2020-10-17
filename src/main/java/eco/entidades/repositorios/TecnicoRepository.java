package eco.entidades.repositorios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import eco.entidades.Tecnico;

public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {

	Long countByNome(String nome);

	Long countByNomeAndIdNot(String nome, Integer id);

	Page<Tecnico> findByNomeIgnoreCaseContainingOrderByNomeAsc(String nome, Pageable pageable);

	Long countById(Integer tecnicoId);

}
