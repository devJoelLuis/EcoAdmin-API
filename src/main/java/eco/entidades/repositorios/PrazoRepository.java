package eco.entidades.repositorios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import eco.entidades.Prazo;

public interface PrazoRepository extends JpaRepository<Prazo, Long>{

	List<Prazo> findByOsIdOrderByDataVencimentoDesc(Long idos);

	Long countByDataVencimentoBetween(LocalDate now, LocalDate plusDays);

	Long countByAlertaAndDataAlertaBetween(boolean b, LocalDate plusYears, LocalDate now);

	Long countByDataAlertaBetween(LocalDate plusYears, LocalDate now);

	List<Prazo> findByAlertaAndDataAlertaLessThanEqual(boolean b, LocalDate now);

	List<Prazo> findByDataVencimentoBetween(LocalDate now, LocalDate plusDays);

	List<Prazo> findByDataVencimentoLessThan(LocalDate now);

	List<Prazo> findByOsNumOsAndOsAnoOs(int numOs, int anoOs);

	List<Prazo> findByCumpridoAndAlertaAndDataAlertaLessThanEqual(boolean b, boolean b2, LocalDate now);

	List<Prazo> findByCumpridoAndDataVencimentoLessThan(boolean b, LocalDate now);

	List<Prazo> findByCumpridoAndDataVencimentoBetween(boolean b, LocalDate now, LocalDate plusDays);

	Long countByCumpridoAndDataVencimentoBetween(boolean b, LocalDate now, LocalDate plusDays);

	Long countByCumpridoAndDataVencimentoLessThan(boolean b, LocalDate now);

	Long countByCumpridoAndAlertaAndDataAlertaLessThanEqual(boolean b, boolean c, LocalDate now);

}
