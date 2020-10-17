package eco.entidades.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import eco.entidades.Index_num_or;

public interface Index_num_orRepository extends JpaRepository<Index_num_or, Long> {
	
	Index_num_or findByNumeroAndAno(Long num, int ano);

	Index_num_or findTop1ByOrderByIdDesc();

}
