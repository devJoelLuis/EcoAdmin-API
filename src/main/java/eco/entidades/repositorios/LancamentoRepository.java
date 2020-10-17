package eco.entidades.repositorios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import eco.entidades.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{


	Long countByCategoriaId(int id);




	List<Lancamento> findByOrdemServicoIdAndPagoAndDataLancamentoBetween(Long idos, boolean b, LocalDate dataInicio,
			LocalDate dataFim, Pageable pageable);




	List<Lancamento> findByOrdemServicoIdAndDataLancamentoBetween(Long idos, LocalDate dataInicio, LocalDate dataFim,
			Pageable pageable);




	Page<Lancamento> findByOrdemServicoId(Long idos, Pageable pageable);




	List<Lancamento> findByOrdemServicoId(Long idos);



	
    @Query( value= "SELECT * FROM lancamento l where total <= " + 
    		"(select sum(r.valor) from recebimento r where r.lancamento_id = l.id) and pago = 0", nativeQuery= true)
	List<Lancamento> getLancamentosCorrigir();




	List<Lancamento> findByOrdemServicoIdAndPago(Long id, boolean b);





	List<Lancamento> findByPagoAndOrdemServicoIdAndDataLancamentoLessThanEqual(boolean b, Long idos, LocalDate now);




	List<Lancamento> findByIdCategoriaRecebimentoTaxaAndDataLancamentoBetweenOrderByDataLancamentoDesc(Integer id,
			LocalDate dtinicio, LocalDate dtfinal);




	List<Lancamento> findByPagoAndIdCategoriaRecebimentoTaxaAndDataLancamentoBetweenOrderByDataLancamentoDesc(boolean b,
			Integer id, LocalDate dtinicio, LocalDate dtfinal);






	List<Lancamento> findByPagoAndDataLancamentoBetweenOrderByDataLancamentoDesc(boolean b, LocalDate dtinicio,
			LocalDate dtfinal);



	

	@Query("FROM Lancamento l WHERE l.idCategoriaRecebimentoTaxa = ?1 AND l.dataLancamento BETWEEN ?2 AND ?3")
	List<Lancamento> buscarLancamentosComTaxaRecebimentoBetween(Integer id, LocalDate dtinicio, LocalDate dtfinal);



   




}
