package eco.services;

import java.util.List;
import java.util.Optional;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eco.entidades.Fornecedor;
import eco.entidades.StatusFornecedor;
import eco.entidades.repositorios.FornecedorRepository;
import eco.entidades.repositorios.StatusFornecedorRepository;
import eco.response.Response;

@Service
public class FornecedorService {
	
	@Autowired
	private FornecedorRepository repo;
	
	@Autowired
	private StatusFornecedorRepository repoSt;
	
	//cadastrar
	@Transactional
	public Response<Fornecedor> cadastrar(Fornecedor f) {
		Response<Fornecedor> response = new Response<>();
		try {
			validar(f, false);
			//salvar o fornecedor
			Fornecedor fsalvo = repo.save(f);
			for(StatusFornecedor s: fsalvo.getStatusFornecedor()) {
				s.setFornecedor(fsalvo);
			}
			repoSt.saveAll(fsalvo.getStatusFornecedor());
			response.setDados(fsalvo);
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao tentar cadastrar um fornecedor: "+ e.getMessage());
			return response;
		}
	}

	private void validar(Fornecedor f, boolean edicao) throws Exception {
		if(f == null) {
			throw new Exception("o fornecedor está nulo!!!");
		}
		if(f.getStatusFornecedor() == null) {
			throw new Exception("não foi informado nenhum status do fornecedor!!!");
		}
		if(f.getStatusFornecedor().size() == 0) {
			throw new Exception("não foi informado nenhum status do fornecedor!!!");
		}
		if(f.getNome() == null || f.getNome().length() < 2) {
			throw new Exception("nome do fornecedor não informado!!!");
		}
		
	}
	
	//alterar
	
	//excluir
	
	//getall pageable param
	public Response<Page<Fornecedor>> getAllParam(int page, int size, String param) {
		Response<Page<Fornecedor>> response = new Response<>();
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<Fornecedor> fornecedoresPage = null;
			if(param == null || param.equals("")) {
				fornecedoresPage = repo.findAllByOrderByNomeAsc(pageable);
				response.setDados(fornecedoresPage);
				return response;
			}
			
			fornecedoresPage = repo.findByNomeContainingOrderByNomeAsc(param, pageable);
			response.setDados(fornecedoresPage);
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao tentar consultar os fornecedores: "+ e.getMessage());
			return response;
		}
	}

	public Response<Fornecedor> getById(int id) {
		Response<Fornecedor> response = new Response<>();
		try {
			Optional<Fornecedor> fOp = repo.findById(id);
			if (!fOp.isPresent()) {
				throw new Exception("não foi possível encontrar um fornecedor com o id "+ id);
			}
		  Fornecedor f = fOp.get();
		  f.setStatusFornecedor(repoSt.findByFornecedorId(id));
		  response.setDados(fOp.get());
		  return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao tentar buscar por id: "+ e.getMessage());
			return response;
		}
	}

	@Transactional
	public Response<String> delete(int id) {
		Response<String> response = new Response<>();
		try {
	        Optional<Fornecedor> fOp = repo.findById(id);
	        if (!fOp.isPresent()) {
	        	throw new Exception("não foi possível encontrar um fornecedor com o id "+ id);
	        }
	        Fornecedor fornecedor = fOp.get();
	        List<StatusFornecedor> stfs = repoSt.findByFornecedorId(id);
	        if(stfs.size() > 0) repoSt.deleteAll(stfs);
	        repo.delete(fornecedor);
	        response.setDados("ok");
	        return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao tentar deletar o fornecedor: " + e.getMessage());
			return response;
		}
	}

	
	
	//editar
	public Response<Fornecedor> alterar(Fornecedor f) {
		Response<Fornecedor> response = new Response<Fornecedor>();
		try {
			
			if (f.getStatusFornecedor() == null) {
				throw new Exception("não foi passado status do fornecedor!");
			}
			
			Fornecedor fornecedorBanco = repo.findById(f.getId()).get();
			if (fornecedorBanco == null) {
				throw new Exception("não foi possível encontrar um fornecedor com o id: "+ f.getId());
			}
			
			BeanUtils.copyProperties(fornecedorBanco, f);
			
			response.setDados(repo.save(fornecedorBanco));
			return response;
			
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar alterar um fornecedor: "+ e.getMessage());
			return response;
		}
	}

	
	
	//getPorId
	
	//getPorStatus
	

}// fecha classe
