package eco.entidades.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import eco.dto.SenhaUsuarioDTO;

public interface SenhaUsuarioDto extends JpaRepository<SenhaUsuarioDTO, Integer> {

}
