package eco.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eco.entidades.OrdemServico;
import eco.entidades.StatusOs;
import eco.entidades.repositorios.OrdemServicoRepository;
import eco.entidades.repositorios.StatusOsRepository;
import eco.response.Response;

@Service
public class StatusOsService {
	
	@Autowired
	private StatusOsRepository repo;
	
	@Autowired
	private OrdemServicoRepository repoOs;
	
	
	// cadastrar novo statusOs
	public Response<StatusOs> cadastrarNovo(StatusOs e) {
		Response<StatusOs> response = new Response<>();
		try {
			if(e == null) {
				throw new Exception("o status recebido está nulo");
			}
			if(e.getNome() == null || e.getNome().length() < 1) {
				throw new Exception("o nome do status tem que ter no mínimo dois caracteres");
			}
			// verificar se o statusOs já existe
			Long count = repo.countByNome(e.getNome());
			if(count > 0) {
				throw new Exception("já existe um status com a descrição "+e.getNome()+
						" cadastrado no banco de dados");
			}
			e.setId(null); // garantir que será um novo statusOs
			StatusOs statusOsSalvo = repo.save(e);
			response.setDados(statusOsSalvo);
			return response;
			
		} catch (Exception e2) {
			response.getErros().add("Erro ao cadastrar novo status da O.S.: " +e2.getMessage());
			return response;
		}
		
	}// fecha cadastrar statusOs
	
	
	
	
	// editar statusOs
	public Response<StatusOs> editar(StatusOs e) {
		Response<StatusOs> response = new Response<>();
		try {
			if(e == null) {
				throw new Exception("o status recebido está nulo");
			}
			if(e.getId() == null) {
				throw new Exception("o id do status está nulo");
			}
			if(e.getNome() == null || e.getNome().length() < 1) {
				throw new Exception("o nome do status tem que ter no mínimo dois caracteres");
			}
			if(e.getId() == 6) {// estatus arquivado não pode ser alterado
				throw new Exception("o status arquivado não pode ser alterado nem excluído!!!");
			}
			// verificar se o statusOs já existe
			Long count = repo.countByNomeAndIdNot(e.getNome(), e.getId());
			if(count > 0) {
				throw new Exception("já existe um status com a descrição "+e.getNome()+
						" cadastrado no banco de dados");
			}
			 Optional<StatusOs> stOpt = repo.findById(e.getId());
			 if(!stOpt.isPresent()) {
				 throw new Exception("não foi possível encontrar um status com o id "+ e.getId());
			 }
			StatusOs stBanco = stOpt.get();
			stBanco.setNome(e.getNome());
			stBanco.setCor(e.getCor());
			StatusOs statusOsSalvo = repo.save(stBanco);
			response.setDados(statusOsSalvo);
			return response;
			
		} catch (Exception e2) {
			response.getErros().add("Erro ao tentar editar o status: " +e2.getMessage());
			return response;
		}
		
	}// fecha editar statusOs
	
	// excluir statusOs
	public Response<String> excluir(int id) {
		Response<String> response = new Response<>();
		try {
			
			 Optional<StatusOs> stOpt = repo.findById(id);
			 if(!stOpt.isPresent()) {
				 throw new Exception("não foi possível encontrar um status com o id "+ id);
			 }
			 if(id == 6) {// estatus arquivado não pode ser alterado
					throw new Exception("o status arquivado não pode ser alterado nem excluído!!!");
				}
			StatusOs stBanco = stOpt.get();
			List<OrdemServico> oss = repoOs.findByStatusOsOrderByAnoOsDescNumOsAsc(stBanco);
			if(oss.size() > 0) {
				String vinculados = "";
				StringBuilder sb = new StringBuilder();
				for (OrdemServico os : oss) {
					sb.append(" [");
					sb.append(os.getNumOs()+"/"+os.getAnoOs());
					sb.append("], [");
					sb.append(os.getCliente().getNome());
					sb.append("] -");
				}
				vinculados = sb.toString();
				throw new Exception("Não será possível excluír esse status porque as seguintes O.S. estão vinculadas a ele : "+ vinculados);
			}
			repo.delete(stBanco);
			response.setDados("ok");
			return response;
			
		} catch (Exception e2) {
			response.getErros().add("Erro ao tentar excluir o status da O.S.: " +e2.getMessage());
			return response;
		}
		
	}// fecha excluir statusOs
	
	
	// lista todos statusOs
	public Response<Page<StatusOs>> listarTodos(int page, int size, String nome) {
		Response<Page<StatusOs>> response = new Response<>();
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<StatusOs> statusOsPage;
			if(nome != null && nome.length() > 0) {
				statusOsPage = repo.findByNomeContainingOrderByNomeAsc(nome, pageable);
			} else {
				statusOsPage = repo.findAll(pageable);
			}
			response.setDados(statusOsPage);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar listar todos os status da O.S. :" + e.getMessage());
			return response;
		}
	}
	
	// buscar statusOs por id
	public Response<StatusOs> buscarPorId(int id) {
		Response<StatusOs> response = new Response<>();
		try {
			Optional<StatusOs> stOpt = repo.findById(id);
			if(!stOpt.isPresent()) {
				throw new Exception("não foi possível encontrar um status com o id "+ id);
			}
			response.setDados(stOpt.get());
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao buscar status da O.S. por id: "+ e.getMessage());
			return response;
		}
	}
	
	
	// get all sem pageable
	public Response<List<StatusOs>> listarTodosSemPageable() {
		Response<List<StatusOs>> response = new Response<>();
		try {
			
			List<StatusOs> stos = repo.findAll();
			
			response.setDados(stos);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar listar todos os status da O.S. :" + e.getMessage());
			return response;
		}
	}
	
	

}// fecha classe
