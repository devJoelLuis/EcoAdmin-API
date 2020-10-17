package eco.entidades.repositorios;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import eco.entidades.Parcela;

public interface ParcelaRepositoy extends JpaRepository<Parcela, Long> {

	Page<Parcela> findByGerenciamentoId(Pageable pageable, Long idg);

	List<Parcela> findByGerenciamentoIdOrderByNumeroParcelaAsc(Long id);

}
