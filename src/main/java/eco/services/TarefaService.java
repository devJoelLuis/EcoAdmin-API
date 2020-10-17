package eco.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eco.dto.TecnicoTarefasDTO;
import eco.entidades.OrdemServico;
import eco.entidades.Tarefa;
import eco.entidades.Tecnico;
import eco.entidades.repositorios.OrdemServicoRepository;
import eco.entidades.repositorios.TarefaRepository;
import eco.entidades.repositorios.TecnicoRepository;
import eco.response.Response;

@Service
public class TarefaService {
	
	@Autowired
	private TarefaRepository repo;
	
	@Autowired
	private TecnicoRepository repoTec;
	
	@Autowired
	private OrdemServicoRepository repoOs;
	
	
	//cadastra
	public Response<Tarefa> cadastrar(Tarefa t) {
		Response<Tarefa> response = new Response<Tarefa>();
		try {
			//buscar a os
			Optional<OrdemServico> osOp = repoOs.findById(t.getOs().getId());
			if (!osOp.isPresent()) {
				throw new Exception("não foi possível encontrar uma O.S. com o id "+ t.getOs().getId()+"." );
			}
			Optional<Tecnico> tecOp = repoTec.findById(t.getTecnico().getId());
			if (!tecOp.isPresent()) {
				throw new Exception("não foi possível encontrar um técnico com o id "+ t.getTecnico().getId()+".");
			}
			t.setOs(osOp.get());
			t.setTecnico(tecOp.get());
			response.setDados(repo.save(t));
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao cadastrar uma nova tarefa: "+ e.getMessage());
			return response;
		}
		
	}
	
	
	
	
	
	//edita
	public Response<Tarefa> alterar(Tarefa t) {
		Response<Tarefa> response = new Response<Tarefa>();
		try {
			Optional<Tarefa> tOp = repo.findById(t.getId());
			if (!tOp.isPresent()) {
				throw new Exception("não foi possível encontrar uma tarefa com o id "+ t.getId());
			}
			Tarefa tbanco = tOp.get();
			BeanUtils.copyProperties(t, tbanco, "id");
			response.setDados(repo.save(tbanco));
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao alterar uma nova tarefa: "+ e.getMessage());
			return response;
		}
		
	}
	
	//exclui
	public Response<String> excluir(int id) {
		Response<String> response = new Response<>();
		try {
			Optional<Tarefa> tOp = repo.findById(id);
			if (!tOp.isPresent()) {
				throw new Exception("não foi possível encontrar uma tarefa com o id "+ id);
			}
			Tarefa tbanco = tOp.get();
			repo.delete(tbanco);
			response.setDados("ok");
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao excluir uma nova tarefa: "+ e.getMessage());
			return response;
		}
		
	}
	
	//get tarefa por id
	public Response<Tarefa> getById(int id) {
		Response<Tarefa> response = new Response<Tarefa>();
		try {
			Optional<Tarefa> tOp = repo.findById(id);
			if (!tOp.isPresent()) {
				throw new Exception("não foi possível encontrar uma tarefa com o id "+ id);
			}
			Tarefa tbanco = tOp.get();
			response.setDados(tbanco);
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao consultar por id uma nova tarefa: "+ e.getMessage());
			return response;
		}
		
	}
	
	//get tarefas dos técnicos
	public Response<List<TecnicoTarefasDTO>> getAllTarefasTecnicos() {
		Response<List<TecnicoTarefasDTO>> response = new Response<List<TecnicoTarefasDTO>>();
		try {
			//buscar todos os técnicos
			List<Tecnico> tecnicos = repoTec.findAll();
			List<TecnicoTarefasDTO> ltts = new ArrayList<>();
			for (Tecnico t: tecnicos) {
				TecnicoTarefasDTO tf = new TecnicoTarefasDTO();
				tf.setLembretes(t.getLembretes());
				tf.setTecnico(t);
				tf.setTarefas(repo.findByTecnicoIdOrderByPrioridadeAscTecnicoNomeAsc(t.getId()));
				ltts.add(tf);
			}
			response.setDados(ltts);
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro carregar as tarefas dos técnicos: "+ e.getMessage());
			return response;
		}
	}


	
	

}//fecha classeS
