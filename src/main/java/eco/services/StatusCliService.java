package eco.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eco.entidades.StatusCli;
import eco.entidades.repositorios.ClienteRepository;
import eco.entidades.repositorios.StatusCliRepository;
import eco.response.Response;

@Service
public class StatusCliService {
	
	@Autowired
	private StatusCliRepository repo;
	
	@Autowired
	private ClienteRepository repoCli;
	
	
	// cadastrar novo estatus
	public Response<StatusCli> cadastrarNovo(StatusCli e) {
		Response<StatusCli> response = new Response<>();
		try {
			if(e == null) {
				throw new Exception("o status recebido está nulo");
			}
			if(e.getStatusCli() == null || e.getStatusCli().length() < 1) {
				throw new Exception("o nome do status tem que ter no mínimo dois caracteres");
			}
			// verificar se o estatus já existe
			Long count = repo.countByStatusCli(e.getStatusCli());
			if(count > 0) {
				throw new Exception("já existe um status com a descrição "+e.getStatusCli()+
						" cadastrado no banco de dados");
			}
			e.setId(null); // garantir que será um novo estatus
			StatusCli estatusSalvo = repo.save(e);
			response.setDados(estatusSalvo);
			return response;
			
		} catch (Exception e2) {
			response.getErros().add("Erro ao cadastrar novo status: " +e2.getMessage());
			return response;
		}
		
	}// fecha cadastrar estatus
	
	
	
	
	// editar estatus
	public Response<StatusCli> editar(StatusCli e) {
		Response<StatusCli> response = new Response<>();
		try {
			if(e == null) {
				throw new Exception("o status recebido está nulo");
			}
			if(e.getId() == null) {
				throw new Exception("o id do status está nulo");
			}
			if(e.getStatusCli() == null || e.getStatusCli().length() < 1) {
				throw new Exception("o nome do status tem que ter no mínimo dois caracteres");
			}
			// verificar se o estatus já existe
			Long count = repo.countByStatusCliAndIdNot(e.getStatusCli(), e.getId());
			if(count > 0) {
				throw new Exception("já existe um status com a descrição "+e.getStatusCli()+
						" cadastrado no banco de dados");
			}
			 Optional<StatusCli> stOpt = repo.findById(e.getId());
			 if(!stOpt.isPresent()) {
				 throw new Exception("não foi possível encontrar um estatus com o id "+ e.getId());
			 }
			StatusCli stBanco = stOpt.get();
			stBanco.setStatusCli(e.getStatusCli());
			StatusCli estatusSalvo = repo.save(stBanco);
			response.setDados(estatusSalvo);
			return response;
			
		} catch (Exception e2) {
			response.getErros().add("Erro ao tentar editar o status: " +e2.getMessage());
			return response;
		}
		
	}// fecha editar estatus
	
	// excluir estatus
	public Response<String> excluir(int id) {
		Response<String> response = new Response<>();
		try {
			
			 Optional<StatusCli> stOpt = repo.findById(id);
			 if(!stOpt.isPresent()) {
				 throw new Exception("não foi possível encontrar um estatus com o id "+ id);
			 }
			StatusCli stBanco = stOpt.get();
			Long count = repoCli.countByStatusCliId(stBanco.getId());
			if(count > 0) {
				throw new Exception("Não será possível excluír o status de cliente "+ stBanco.getStatusCli() +" porque existe clientes vinculados a ele!!!");
			}
			repo.delete(stBanco);
			response.setDados("ok");
			return response;
			
		} catch (Exception e2) {
			response.getErros().add("Erro ao tentar excluir o status: " +e2.getMessage());
			return response;
		}
		
	}// fecha excluir estatus
	
	
	// lista todos estatus
	public Response<Page<StatusCli>> listarTodos(int page, int size, String nome) {
		Response<Page<StatusCli>> response = new Response<>();
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<StatusCli> estatusPage;
			if(nome != null && nome.length() > 0) {
				estatusPage = repo.findByStatusCliContainingOrderByStatusCliAsc(nome, pageable);
			} else {
				estatusPage = repo.findAll(pageable);
			}
			response.setDados(estatusPage);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar listar todos os status :" + e.getMessage());
			return response;
		}
	}
	
	// buscar estatus por id
	public Response<StatusCli> buscarPorId(int id) {
		Response<StatusCli> response = new Response<>();
		try {
			Optional<StatusCli> stOpt = repo.findById(id);
			if(!stOpt.isPresent()) {
				throw new Exception("não foi possível encontrar um status com o id "+ id);
			}
			response.setDados(stOpt.get());
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao buscar estatus por id: "+ e.getMessage());
			return response;
		}
	}
	
	

}// fecha classe
