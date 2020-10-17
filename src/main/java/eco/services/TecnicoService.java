package eco.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eco.dto.TecnicoDTO;
import eco.entidades.Tecnico;
import eco.entidades.repositorios.TecnicoRepository;
import eco.response.Response;

@Service
public class TecnicoService {
	
	@Autowired
	private TecnicoRepository repo;
	
	
	
	
	
	//cadastrar novo
	public Response<Tecnico> cadastrar(Tecnico t) {
		Response<Tecnico> response = new Response<>();
		try {
			validarTecnico(t);
            // fixar id como null para garantir que será um novo técnico
			Tecnico tsalvo = repo.save(t);
			response.setDados(tsalvo);
			return response;
		} catch (Exception e) {
		  response.getErros().add("Erro ao cadastrar um novo técnico: " + e.getMessage());
		  return response;
		}
		
	}//fecha cadastrar tecnico




    // valida o tecnico antes de cadastrar
	private void validarTecnico(Tecnico t) throws Exception {
		if(t == null) {
			throw new Exception("O objeto técnico está nulo");
		}
		if(t.getNome() == null || t.getNome() == "") {
			throw new Exception("O nome do técnico não foi informado");
		}
		Long count = repo.countByNome(t.getNome());
		if(count > 0) {
			throw new Exception("Já existe um técnico com o nome "+ t.getNome() 
			+ " cadastrado no banco de dados");
		}
	}
	
	
	
	
	
	
	
	//editar
	
	//cadastrar novo
	public Response<Tecnico> alterar(Tecnico t) {
		Response<Tecnico> response = new Response<>();
		try {
			Optional<Tecnico> tbancoOp = repo.findById(t.getId());
			if(!tbancoOp.isPresent()) {
				throw new Exception("Não foi possível encontrar um técnico com o id "+ t.getId());
			}
			validarTecnicoAlteracao(t);
            Tecnico tbanco = tbancoOp.get();
            tbanco.alterarTecnico(t);
			Tecnico tsalvo = repo.save(tbanco);
			response.setDados(tsalvo);
			return response;
		} catch (Exception e) {
		  response.getErros().add("Erro ao alterar os dados do técnico: " + e.getMessage());
		  return response;
		}
		
	}//fecha alterar tecnico




	private void validarTecnicoAlteracao(Tecnico t) throws Exception {
		
		if(t.getNome() == null || t.getNome() == "") {
			throw new Exception("O nome do técnico não foi informado");
		}
		Long count = repo.countByNomeAndIdNot(t.getNome(), t.getId());
		if(count > 0) {
			throw new Exception("já existe um técnico com o nome "+ t.getNome() + " cadastrado no banco de dados");
		}
		
	} // fecha metodo de validação

	
	
	
	
	
	
	//excluir
	public Response<String> excluir(int id) {
		Response<String> response = new Response<>();
		try {
			Optional<Tecnico> tbancoOp = repo.findById(id);
			if(!tbancoOp.isPresent()) {
				throw new Exception("Não foi possível encontrar um técnico com o id "+ id);
			}
            Tecnico tbanco = tbancoOp.get();
			repo.delete(tbanco);
			response.setDados("ok");
			return response;
		} catch (Exception e) {
		  response.getErros().add("Erro ao excluir o técnico: " + e.getMessage());
		  return response;
		}
		
	}//fecha excluir tecnico

	
		
	
	
	//get por id
	public Response<Tecnico> getById(int id) {
		Response<Tecnico> response = new Response<>();
		try {
			Optional<Tecnico> tbancoOp = repo.findById(id);
			if(!tbancoOp.isPresent()) {
				throw new Exception("Não foi possível encontrar um técnico com o id "+ id);
			}
			response.setDados(tbancoOp.get());
			return response;
		} catch (Exception e) {
		  response.getErros().add("Erro ao consultar o técnico por id: " + e.getMessage());
		  return response;
		}
		
	}//fecha getById tecnico
	
	
	
	//get all
	public Response<Page<Tecnico>> getAll(int page, int size) {
		Response<Page<Tecnico>> response = new Response<>();
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<Tecnico> tpage = repo.findAll(pageable);
			response.setDados(tpage);
			return response;
		} catch (Exception e) {
		  response.getErros().add("Erro ao consultar todos os técnicos: " + e.getMessage());
		  return response;
		}
		
	}//fecha getall tecnico
	
	
	
	
	
	//get all por parte do nome
	public Response<Page<Tecnico>> getAllParteNome(int page, int size, String nome) {
		Response<Page<Tecnico>> response = new Response<>();
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<Tecnico> tpage = repo.findByNomeIgnoreCaseContainingOrderByNomeAsc(nome, pageable);
			response.setDados(tpage);
			return response;
		} catch (Exception e) {
		  response.getErros().add("Erro ao consultar todos os técnicos: " + e.getMessage());
		  return response;
		}
		
	}//fecha getall tecnico




	public List<TecnicoDTO> getAllDto() {
		List<Tecnico> tecnicos = repo.findAll();
		List<TecnicoDTO> tecnicosDTO = new ArrayList<>();
		for(Tecnico t: tecnicos) {
			TecnicoDTO tdto = new TecnicoDTO();
			tdto.setId(t.getId());
			tdto.setNome(t.getNome());
			tecnicosDTO.add(tdto);
		}
		
		return tecnicosDTO;
	}
	
	

}//fecha classe
