package eco.entidades.repositorios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import eco.entidades.Recebimento;

public interface RecebimentoRepository extends JpaRepository<Recebimento, Long> {

	List<Recebimento> findByLancamentoId(Long id);

	boolean existsByCategoriaRecebimentoId(Integer id);

	List<Recebimento> findByCategoriaRecebimentoIdAndDataBetweenOrderByDataDesc(Integer id, LocalDate dtinicio,
			LocalDate dtfinal);

}//fecha classe
