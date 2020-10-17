package eco.entidades.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import eco.entidades.StatusFornecedor;

public interface StatusFornecedorRepository extends JpaRepository<StatusFornecedor, Integer> {

	List<StatusFornecedor> findByFornecedorId(int id);

}
