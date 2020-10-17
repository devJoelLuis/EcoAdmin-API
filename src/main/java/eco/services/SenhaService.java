package eco.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eco.entidades.Cliente;
import eco.entidades.Senha;
import eco.entidades.repositorios.ClienteRepository;
import eco.entidades.repositorios.SenhaRepository;
import eco.response.Response;

@Service
public class SenhaService {
	
	@Autowired
	private SenhaRepository repo;
	
	@Autowired
	private ClienteRepository repoCli;
	
	
	public Response<String> excluir(int id) {
		Response<String> response = new Response<String>();
		try {
		   Optional<Senha> senhaBancoOP = repo.findById(id);
		   if(!senhaBancoOP.isPresent()) {
			   throw new Exception("não foi possível encontrar um senha com o id: "+ id);
		   }
		   repo.delete(senhaBancoOP.get());
		   response.setDados("ok");
		   return response;
			
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar excluír uma senha de cliente: "+ e.getMessage());
			return response;
		}
		
	}
	
	
	public Response<Senha> cadastrar(Senha s) {
		Response<Senha> response = new Response<>();
		try {
		  
		   if(s.getDescricao() == null || s.getDescricao().equals("")) {
			   throw new Exception("não foi informado uma descrição para senha");
		   }
		   if(s.getSenha() == null || s.getSenha().equals("")) {
			   throw new Exception("não foi informado uma senha");
		   }
		   Optional<Cliente> cliOp = repoCli.findById(s.getCliente().getId());
		   if(!cliOp.isPresent()) {
			   throw new Exception("não foi possível encontrar um cliente com o id: "+ s.getCliente().getId());  
		   }
		   s.setCliente(cliOp.get());
		   response.setDados(repo.save(s));
		   return response;
			
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar cadastrar uma senha de cliente: "+ e.getMessage());
			return response;
		}
		
	}
	
	public Response<Senha> alterar(Senha s) {
		Response<Senha> response = new Response<>();
		try {
		  
		   if(s.getDescricao() == null || s.getDescricao().equals("")) {
			   throw new Exception("não foi informado uma descrição para senha");
		   }
		   if(s.getSenha() == null || s.getSenha().equals("")) {
			   throw new Exception("não foi informado uma senha");
		   }
		   Optional<Senha> shOp = repo.findById(s.getId());
		   if(!shOp.isPresent()) {
			   throw new Exception("não foi possível encontrar um senha com o id: "+ s.getId()); 
		   }
		   Senha senhaBanco = shOp.get();
		   Optional<Cliente> cliOp = repoCli.findById(s.getCliente().getId());
		   if(!cliOp.isPresent()) {
			   throw new Exception("não foi possível encontrar um cliente com o id: "+ s.getCliente().getId());  
		   }
		   senhaBanco.alterar(s);
		   senhaBanco.setCliente(cliOp.get());
		   response.setDados(repo.save(senhaBanco));
		   return response;
			
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar alterar uma senha de cliente: "+ e.getMessage());
			return response;
		}
		
	}
	
	public Response<List<Senha>> findAllClienteId(int id) {
		Response<List<Senha>> response = new Response<>();
		try {
			List<Senha> senhas = repo.findByClienteId(id);
			if(senhas == null) {
				senhas= new ArrayList<>();
			}
			response.setDados(senhas);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar as senhas do cliente: "+ e.getMessage());
			return response;
		}
	}

	

}// fecha classe
