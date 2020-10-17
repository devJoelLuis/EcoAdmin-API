package eco.entidades.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import eco.entidades.Tarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Integer>{

	List<Tarefa> findByTecnicoIdOrderByTecnicoNomeAsc(Integer id);

	List<Tarefa> findByTecnicoIdOrderByPrioridadeAscTecnicoNomeAsc(Integer id);

}
