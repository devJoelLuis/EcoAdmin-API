package eco.entidades.repositorios;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import eco.entidades.HistoricoCatRecebimento;

public interface HistoricoCatRecebimentoRepository extends JpaRepository<HistoricoCatRecebimento, Long> {

	List<HistoricoCatRecebimento> findByCategoriaRecebimentoId(Integer id);

	Page<HistoricoCatRecebimento> findByCategoriaRecebimentoIdOrderByDataHoraAsc(Integer id, Pageable pageable);

	

}
