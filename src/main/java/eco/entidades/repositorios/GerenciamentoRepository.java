package eco.entidades.repositorios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import eco.entidades.Gerenciamento;

public interface GerenciamentoRepository extends JpaRepository<Gerenciamento, Long> {

	Page<Gerenciamento> findByClienteId(int idcliente, Pageable pageable);

}
