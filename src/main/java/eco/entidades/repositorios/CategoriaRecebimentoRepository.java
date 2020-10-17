package eco.entidades.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import eco.entidades.CategoriaRecebimento;

public interface CategoriaRecebimentoRepository extends JpaRepository<CategoriaRecebimento, Integer> {

	boolean existsByDescricao(String descricao);


	boolean existsByDescricaoAndIdNot(String descricao, Integer idcatRec);


	List<CategoriaRecebimento> findAllByOrderByDescricaoAsc();


	List<CategoriaRecebimento> findAllByOrderByIdAsc();

}
