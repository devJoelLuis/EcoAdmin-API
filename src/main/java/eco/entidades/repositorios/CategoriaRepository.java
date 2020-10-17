package eco.entidades.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import eco.entidades.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

	List<Categoria> findAllByOrderByDescricaoAsc();

	boolean existsByDescricaoAndIdNot(String descricao, Integer id);

	boolean existsByDescricao(String descricao);

}
