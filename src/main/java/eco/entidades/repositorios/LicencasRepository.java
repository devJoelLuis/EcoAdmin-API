package eco.entidades.repositorios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import eco.entidades.Licenca;

public interface LicencasRepository extends JpaRepository<Licenca, Long> {

	Long countByNumero(String numero);

	Long countByNumeroAndIdNot(String numero, Long id);

	List<Licenca> findByClienteId(int id);

	Page<Licenca> findByArquivoMortoAndClienteIdOrderByDataVencimentoAsc(boolean arquivoMorto, int idcliente, Pageable pageable);

	Licenca findTop1ByOrderByIdDesc();

	Page<Licenca> findByArquivoMortoAndClienteIdAndDataVencimentoBetweenOrderByDataVencimentoAsc(boolean arquivoMorto, int idcliente, LocalDate dtini,
			LocalDate dtf, Pageable pageable);


	Long countByArquivoMortoAndAlertaAndDataAlertaLessThanEqual(boolean arquivoMorto, int i, LocalDate now);

	Long countByArquivoMortoAndDataVencimentoBetween(boolean arquivoMorto, LocalDate now, LocalDate plusDays);

	Long countByArquivoMortoAndDataVencimentoLessThanEqual(boolean arquivoMorto, LocalDate now);

	Page<Licenca> findByArquivoMortoAndAlertaAndDataVencimentoBetweenOrderByDataVencimentoAsc(boolean arquivoMorto, int alerta, LocalDate dtinicio,
			LocalDate dtfim, Pageable pageable);

	Page<Licenca> findByArquivoMortoAndAlertaAndNumeroAndDataVencimentoBetweenOrderByDataVencimentoAsc(boolean arquivoMorto, int alerta, String param,
			LocalDate dtinicio, LocalDate dtfim, Pageable pageable);

	Page<Licenca> findByArquivoMortoAndAlertaAndAssuntoContainingIgnoreCaseAndDataVencimentoBetweenOrderByDataVencimentoAsc(boolean arquivoMorto, int alerta,
			String param, LocalDate dtinicio, LocalDate dtfim, Pageable pageable);

	Page<Licenca> findByArquivoMortoAndDataVencimentoBetweenOrderByDataVencimentoAsc(boolean arquivoMorto, LocalDate dtinicio, LocalDate dtfim,
			Pageable pageable);

	Page<Licenca> findByArquivoMortoAndNumeroAndDataVencimentoBetweenOrderByDataVencimentoAsc(boolean arquivoMorto, String param, LocalDate dtinicio,
			LocalDate dtfim, Pageable pageable);

	Page<Licenca> findByArquivoMortoAndAssuntoContainingIgnoreCaseAndDataVencimentoBetweenOrderByDataVencimentoAsc(boolean arquivoMorto, String param,
			LocalDate dtinicio, LocalDate dtfim, Pageable pageable);

	Page<Licenca> findByArquivoMortoAndDataVencimentoLessThanEqualOrderByDataVencimentoAsc(boolean arquivoMorto, LocalDate now, Pageable pageable);


	Page<Licenca> findByArquivoMortoOrderByDataVencimentoAsc(boolean arquivoMorto, Pageable pageable);

	Page<Licenca> findByArquivoMortoAndAlertaAndDataAlertaLessThanEqualOrderByDataVencimentoAsc(boolean arquivoMorto, int i, LocalDate now,
			Pageable pageable);



	List<Licenca> findByArquivoMortoAndAlertaAndDataAlertaLessThanEqual(boolean arquivoMorto, int i, LocalDate now);


	Page<Licenca> findByArquivoMortoAndAlertaAndDataAlertaLessThanEqualAndAssuntoContainingOrderByDataVencimentoAsc(
			boolean b, int i, LocalDate now, String param, Pageable pageable);

	Page<Licenca> findByArquivoMortoAndAssuntoContainingOrNumeroContainingOrderByDataVencimentoAsc(boolean b,
			String param, String param2, Pageable pageable);

	Page<Licenca> findByArquivoMortoAndAlertaAndDataAlertaLessThanEqualAndAssuntoContainingOrNumeroContainingOrderByDataVencimentoAsc(
			boolean b, int i, LocalDate now, String param, String param2, Pageable pageable);

	Long countByArquivoMorto(boolean b);

	

	




	

}
