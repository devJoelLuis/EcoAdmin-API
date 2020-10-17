package eco.entidades.repositorios;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import eco.entidades.Despesa;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {

	Page<Despesa> findAllByOrderByDataDesc(Pageable pageable);

	Page<Despesa> findByDataBetweenOrderByDataDesc(LocalDate dtInicio, LocalDate dtFim, Pageable pageable);

	Page<Despesa> findByCategoriaRecebimentoIdOrderByDataDesc(Integer idcategoriaRecebimento, Pageable pageable);

	Page<Despesa> findByDescricaoContainingIgnoreCaseOrderByDataDesc(String descricao, Pageable pageable);

	List<Despesa> findByCategoriaRecebimentoIdAndDataBetweenOrderByDataDesc(Integer id, LocalDate dtinicio,
			LocalDate dtfinal);



}
