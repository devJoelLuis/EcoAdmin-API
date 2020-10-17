package eco.entidades.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import eco.entidades.Senha;

public interface SenhaRepository extends JpaRepository<Senha, Integer> {

	List<Senha> findByClienteId(int id);

}
