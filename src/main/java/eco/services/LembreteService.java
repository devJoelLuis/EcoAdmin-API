package eco.services;

import java.util.Optional;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eco.entidades.Lembrete;
import eco.entidades.Tecnico;
import eco.entidades.repositorios.LembreteRepository;
import eco.entidades.repositorios.TecnicoRepository;
import eco.response.Response;

@Service
public class LembreteService {
	
	@Autowired
	private LembreteRepository repo;
	
	@Autowired
	private TecnicoRepository repoTec;
	
	//cadastrar
	public Response<Lembrete> cadastrar(Lembrete lembrete) {
		Response<Lembrete> response = new Response<>();
		try {
			// buscar o técnico
			Optional<Tecnico> tecnicoOp = repoTec.findById(lembrete.getTecnico().getId());
			if (!tecnicoOp.isPresent()) {
				throw new Exception("Não foi possível encontrar um técnico com o id: "+ lembrete.getTecnico().getId());
			}
			Tecnico tbanco = tecnicoOp.get();
			lembrete.setTecnico(tbanco);
			lembrete.setId(null);
			response.setDados(repo.save(lembrete));
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar cadastrar um lembrete: "+ e.getMessage());
			return response;
		}
	}
	
	
	
	//editar
	public Response<Lembrete> editar(Lembrete lembrete) {
		Response<Lembrete> response = new Response<>();
		try {
			// buscar o técnico
			Optional<Tecnico> tecnicoOp = repoTec.findById(lembrete.getTecnico().getId());
			if (!tecnicoOp.isPresent()) {
				throw new Exception("Não foi possível encontrar um técnico com o id: "+ lembrete.getTecnico().getId());
			}
			// buscar o lembrete
			Optional<Lembrete> lOp = repo.findById(lembrete.getId());
			if (!lOp.isPresent()) {
				throw new Exception("Não foi possível encontrar um lembrete com o id: "+ lembrete.getId());
			}
			Lembrete lbanco = lOp.get();
			Tecnico tbanco = tecnicoOp.get();
			BeanUtils.copyProperties(lbanco, lembrete);
			lbanco.setTecnico(tbanco);
			response.setDados(repo.save(lbanco));
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar editar um lembrete: "+ e.getMessage());
			return response;
		}
	}
	
	
	
	//excluir
	public Response<String> excluir(Long id) {
		Response<String> response = new Response<>();
		try {
						
			// buscar o lembrete
			Optional<Lembrete> lOp = repo.findById(id);
			if (!lOp.isPresent()) {
				throw new Exception("Não foi possível encontrar um lembrete com o id: "+ id);
			}
			Lembrete lbanco = lOp.get();
			repo.delete(lbanco);
			response.setDados("ok");
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar excluir um lembrete: "+ e.getMessage());
			return response;
		}
	}
	
	
	//get id
	
	
	//get all

}//fecha classe
