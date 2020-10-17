package eco.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eco.entidades.Permissao;
import eco.entidades.repositorios.PermissaoRepository;
import eco.response.Response;

@Service
public class PermissaoService {
	
	@Autowired
	private PermissaoRepository repo;
	
	//get all
	public Response<List<Permissao>> getAll() {
		Response<List<Permissao>> response = new Response<>();
		try {
			
			List<Permissao> permissoes = repo.findAll();
			response.setDados(permissoes);
			return response;
			
		} catch (Exception e) {
			response.getErros().add("Erro ao carregar todas as permissões: "+ e.getMessage());
			return response;
		}
	}
	
	
	
	//salvar uma lista de permissoes
	public void salvarLista(List<Permissao> p) {
		repo.saveAll(p);
	}



	
	
	

}// fecha classe
