package eco.entidades.repositorios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import eco.entidades.OrdemServico;
import eco.entidades.StatusOs;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
	

	OrdemServico findTop1ByAnoOsOrderByNumOsDesc(int year);



	Long countByNumOsAndAnoOs(int numOs, int year);


	Long countById(Long id);



	List<OrdemServico> findByDataAlertaLessThanEqualOrderByAnoOsDescNumOsAsc(LocalDate now);





	Long countById(Integer tecnicoId);






	List<OrdemServico> findByClienteIdOrderByAnoOsDesc(int idcli);



	List<OrdemServico> findByClienteIdOrderByAnoOsDescNumOsAsc(int idcli);




	Long countByAlertaAndDataAlertaLessThanEqual(int i, LocalDate now);





	List<OrdemServico> findByStatusOsOrderByAnoOsDescNumOsAsc(StatusOs stBanco);



	Page<OrdemServico> findByAnoOsOrderByAnoOsDescNumOsAsc(Pageable pageable, int ano);



	Page<OrdemServico> findAllByOrderByAnoOsDescNumOsAsc(Pageable pageable);



	Page<OrdemServico> findByAnoOsAndLocalContainingOrderByAnoOsDescNumOsAsc(Pageable pageable, int ano, String param);



	Page<OrdemServico> findByAnoOsAndNumOsOrderByAnoOsDescNumOsAsc(Pageable pageable, int ano, int numeroOs);



	Page<OrdemServico> findByAlertaAndDataAlertaLessThanEqualAndAnoOsAndLocalContainingOrderByAnoOsDescNumOsAsc(
			Pageable pageable, int i, LocalDate now, int ano, String param);



	Page<OrdemServico> findByAlertaAndDataAlertaLessThanEqualAndAnoOsAndNumOsOrderByAnoOsDescNumOsAsc(Pageable pageable,
			int i, LocalDate now, int ano, int numeroOs);



	Page<OrdemServico> findByAnoOsAndStatusOsIdOrderByAnoOsDescNumOsAsc(Pageable pageable, int ano, int idstatus);



	Page<OrdemServico> findByAlertaAndAnoOsAndDataAlertaLessThanEqualOrderByAnoOsDescNumOsAsc(Pageable pageable, int i,
			int ano, LocalDate now);





	List<OrdemServico> findByAlertaAndDataAlertaLessThanEqual(int i, LocalDate now);



	List<OrdemServico> findByNumOsAndAnoOsOrderByLocalAsc(Integer valueOf, Integer valueOf2);



	List<OrdemServico> findByLocalContainingIgnoreCaseOrderByLocalAsc(String param);



	List<OrdemServico> findByClienteIdOrderByAnoOsDescNumOsDesc(int idcli);



	Page<OrdemServico> findAllByOrderByAnoOsDescNumOsDesc(Pageable pageable);



	List<OrdemServico> findByAnoOsAndLocalContainingOrderByAnoOsDescNumOsDesc(Pageable pageable, int ano, String param);



	List<OrdemServico> findByAlertaAndAnoOsAndDataAlertaLessThanEqualOrderByAnoOsDescNumOsDesc(Pageable pageable, int i,
			int ano, LocalDate now);



	List<OrdemServico> findByAnoOsOrderByAnoOsDescNumOsDesc(Pageable pageable, int ano);



	List<OrdemServico> findByAnoOsAndNumOsOrderByAnoOsDescNumOsDesc(Pageable pageable, int ano, int numeroOs);



	Page<OrdemServico> findByAlertaAndDataAlertaLessThanEqualAndAnoOsAndLocalContainingOrderByAnoOsDescNumOsDesc(
			Pageable pageable, int i, LocalDate now, int ano, String param);



	Page<OrdemServico> findByAlertaAndDataAlertaLessThanEqualAndAnoOsAndNumOsOrderByAnoOsDescNumOsDesc(
			Pageable pageable, int i, LocalDate now, int ano, int numeroOs);



	List<OrdemServico> findByAnoOsAndStatusOsIdOrderByAnoOsDescNumOsDesc(Pageable pageable, int ano, int idstatus);


	/*
    @Query(value = "SELECT * FROM ordem_servico os INNER JOIN lancamento la "
    		+ "ON os.id = la.ordem_servico_id WHERE os.ano_os = ?1 AND la.pago = 0 ORDER BY os.id",
    		countQuery="SELECT count(*) FROM ordem_servico os INNER JOIN lancamento la "
    				+ "ON os.id = la.ordem_servico_id WHERE os.ano_os = ?1 AND la.pago = 0 ORDER BY os.id",
    		nativeQuery=true)  */
	@Query(value="FROM OrdemServico os WHERE EXISTS (FROM Lancamento l WHERE l.ordemServico.id = os.id AND l.pago = 0 AND l.dataLancamento <= ?2) AND os.anoOs = ?1 "
			+ "ORDER BY os.anoOs,  os.numOs")
	List<OrdemServico> buscarTodasComPendencia(int ano, LocalDate now, Pageable pageable );
   /* 
    @Query(value = "SELECT count(*) FROM ordem_servico os INNER JOIN lancamento la ON os.id = la.ordem_servico_id WHERE os.ano_os = ?1 AND la.pago = 0 ORDER BY os.id",
    		countQuery="SELECT count(*) FROM ordem_servico os INNER JOIN lancamento la ON os.id = la.ordem_servico_id WHERE os.ano_os = ?1 AND la.pago = 0 ORDER BY os.id",
    		nativeQuery=true)  */
	@Query(value="SELECT count(os) FROM OrdemServico os WHERE EXISTS (FROM Lancamento l WHERE l.ordemServico.id = os.id AND l.pago = 0 AND l.dataLancamento <= ?2) AND os.anoOs = ?1")
	Long buscarCountTodasComPendencia(int ano, LocalDate now);



	Long countByAnoOs(int ano);



	Long countByAlertaAndAnoOsAndDataAlertaLessThanEqualOrderByAnoOsDescNumOsDesc(int i, int ano, LocalDate now);



	Long countByAnoOsAndStatusOsIdOrderByAnoOsDescNumOsDesc(int ano, int idstatus);



	Long countByAnoOsAndLocalContainingOrderByAnoOsDescNumOsDesc(int ano, String param);



	Long countByAnoOsAndNumOsOrderByAnoOsDescNumOsDesc(int ano, int numeroOs);



	Long countByIdGreaterThan(Long id);



	List<OrdemServico> findByIdAfterOrderByAnoOsDescNumOsDesc(Long id, Pageable pageable);



	List<OrdemServico> findByAlertaAndDataAlertaLessThanEqualOrderByAnoOsDescNumOsDesc(Pageable pageable, int i,
			LocalDate now);



	Long countByAlertaAndDataAlertaLessThanEqualOrderByAnoOsDescNumOsDesc(int i, LocalDate now);



	List<OrdemServico> findByAlertaAndNumOsAndAnoOsAndDataAlertaLessThanEqualOrderByAnoOsDescNumOsDesc(
			Pageable pageable, int i, int numOs, int anoOs, LocalDate now);



	Long countByAlertaAndNumOsAndAnoOsAndDataAlertaLessThanEqual(int i, int numOs, int anoOs, LocalDate now);



	Long countByAlertaAndAnoOsAndDataAlertaLessThanEqual(int i, int anoOs, LocalDate now);


	@Query(value="FROM OrdemServico os WHERE EXISTS (FROM Lancamento l WHERE l.ordemServico.id = os.id AND l.pago = 0 AND l.dataLancamento <= ?3) "
			+ "AND os.numOs = ?1 AND os.anoOs = ?2 "
			+ "ORDER BY os.anoOs,  os.numOs")
	List<OrdemServico> buscarTodasComPendenciaNumOsAnoOs(int numOs, int anoOs, LocalDate now, Pageable pageable);


	@Query(value="SELECT count(os) FROM OrdemServico os WHERE EXISTS (FROM Lancamento l WHERE l.ordemServico.id = os.id AND l.pago = 0 AND l.dataLancamento <= ?3) "
			+ "AND os.numOs = ?1 AND os.anoOs = ?2 "
			+ "ORDER BY os.anoOs,  os.numOs")
	Long buscarCountTodasComPendenciaNumOsAnoOs(int numOs, int anoOs, LocalDate now);



	List<OrdemServico> findByNumOsAndAnoOs(int numOs, int anoOs);



	List<OrdemServico> findByAnoOsOrderByAnoOsDescNumOsDesc(int anoOs, Pageable pageable);



	List<OrdemServico> findByAlertaAndDataAlertaLessThanEqualOrderByAnoOsDescNumOsDesc(int i, LocalDate now,
			Pageable pageable);


	@Query(value="FROM OrdemServico os WHERE EXISTS (FROM Lancamento l WHERE l.ordemServico.id = os.id AND l.pago = 0 AND l.dataLancamento <= ?1) "
			+ "ORDER BY os.anoOs,  os.numOs")
	List<OrdemServico> buscarAllPendencia(LocalDate now, Pageable pageable);


	@Query(value="SELECT count(os) FROM OrdemServico os WHERE EXISTS (FROM Lancamento l WHERE l.ordemServico.id = os.id AND l.pago = 0 AND l.dataLancamento <= ?1) "
			+ "ORDER BY os.anoOs,  os.numOs")
	Long buscarCountAllPendencia(LocalDate now);



	List<OrdemServico> findByIdGreaterThanOrderByAnoOsDescNumOsDesc(Long i, Pageable pageable);



	List<OrdemServico> findByAnoOsAndClienteIdOrderByNumOsDesc(int ano, Integer idcliente);



	List<OrdemServico> findByAnoOsAndClienteNomeContaininIgnoreCasegOrClienteNomeFantasiaContaininIgnoreCasegOrderByClienteNomeAsc(
			int ano, String param, String param2);



	List<OrdemServico> findByClienteNomeContainingIgnoreCaseAndAnoOsOrClienteNomeFantasiaContainingAndAnoOsOrderByClienteNomeFantasiaAsc(
			String param, int ano, String param2, int ano2);











	



	



	



}
