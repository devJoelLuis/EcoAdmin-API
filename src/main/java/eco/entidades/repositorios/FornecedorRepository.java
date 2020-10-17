package eco.entidades.repositorios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import eco.entidades.Fornecedor;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {

	Page<Fornecedor> findAllByOrderByNomeAsc(Pageable pageable);

	Page<Fornecedor> findByNomeContainingOrderByNomeAsc(String param, Pageable pageable);

}
