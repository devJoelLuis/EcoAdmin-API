package eco.entidades.repositorios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import eco.entidades.Orcamento;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {
	
	List<Orcamento> findByClienteId(int id);

	Orcamento findTop1ByOrderByIdDesc();

	Long countByNumero(int numero);

	List<Orcamento> findByDataEntregaBetweenAndClienteId(LocalDate dtini, LocalDate dtf, int id);

	Long countByNumeroAndAno(int numero, int ano);

	Long countByNumeroAndAnoAndIdNot(int numero, int ano, Long id);


	Orcamento findTop1ByOrderByNumeroDescAnoDesc();

	Long countByAlertaAndDataAlertaLessThanEqual(int i, LocalDate now);

	List<Orcamento> findByDataEntregaBetweenAndClienteIdOrderByAnoDescNumeroAsc(LocalDate dtini, LocalDate dtf,
			int id);



	List<Orcamento> findByAlertaAndDataAlertaLessThanEqual(int i, LocalDate now);

	List<Orcamento> findByClienteIdOrderByAnoDescNumeroDesc(int idcliente);

	List<Orcamento> findByDataEntregaBetweenAndClienteIdOrderByAnoDescNumeroDesc(LocalDate dtini, LocalDate dtf,
			int id);

}
