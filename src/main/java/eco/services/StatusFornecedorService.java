package eco.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eco.entidades.Fornecedor;
import eco.entidades.StatusFornecedor;
import eco.entidades.repositorios.FornecedorRepository;
import eco.entidades.repositorios.StatusFornecedorRepository;
import eco.response.Response;

@Service
public class StatusFornecedorService {
	
	@Autowired
	private StatusFornecedorRepository repo;
	
	@Autowired
	private FornecedorRepository repoF;
	
	
	public Response<String> excluir(Integer id) {
		 Response<String> response = new Response<>();
		 try {
			Optional<StatusFornecedor> sOp = repo.findById(id);
			if (!sOp.isPresent()) {
				throw new Exception("não foi possível encontrar um status de fornecedor com o id "+ id);
			}
			repo.delete(sOp.get());
		    response.setDados("ok");
		    return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao tentar excluir um status do fornecedor "+ e.getMessage());
			return response;
		}
	} // fecha excluir

	
	
   // cadastro
	public Response<StatusFornecedor> cadastrar(StatusFornecedor stf) {
		Response<StatusFornecedor> response = new Response<>();
		try {
			Optional<Fornecedor> fOp = repoF.findById(stf.getFornecedor().getId());
			if (!fOp.isPresent()) {
				throw new Exception("não foi possível encontrar um status do fornecedor com o id "
			+ stf.getFornecedor().getId());
			}
			stf.setFornecedor(fOp.get());
			response.setDados(repo.save(stf));
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao tentar cadastrar um status de fornecedor "+ e.getMessage());
			return response;
		}
	}
	
	
	

}// fecha classe
