package eco.entidades.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import eco.entidades.EmailAlerta;

public interface EmailAlertaRepository extends JpaRepository<EmailAlerta, Integer> {

	List<EmailAlerta> findAllByOrderByEmailAsc();

}
