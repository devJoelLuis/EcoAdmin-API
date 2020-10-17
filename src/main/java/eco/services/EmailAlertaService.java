package eco.services;

import java.util.List;
import java.util.Optional;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eco.entidades.EmailAlerta;
import eco.entidades.repositorios.EmailAlertaRepository;
import eco.response.Response;

@Service
public class EmailAlertaService {
	
	@Autowired
	private EmailAlertaRepository repo;
	
	
	//cadastrar
	public Response<EmailAlerta> cadastrar(EmailAlerta em) {
		Response<EmailAlerta> response = new Response<>();
		try {
			response.setDados(repo.save(em));
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao tentar cadastrar um novo e-mail receptor de alerta"+ e.getMessage());
			return response;
		}
	}
	
	
	// editar
	public Response<EmailAlerta> alterar(EmailAlerta em) {
		Response<EmailAlerta> response = new Response<>();
		try {
			Optional<EmailAlerta> emOp = repo.findById(em.getId());
			if (!emOp.isPresent()) {
				throw new Exception("não foi possível encontrar um e-mail com o id "+ em.getId());
			}
			EmailAlerta emBanco = emOp.get();
			BeanUtils.copyProperties(emBanco, em);
			response.setDados(repo.save(em));
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao tentar alterar um e-mail receptor de alerta"+ e.getMessage());
			return response;
		}
	}
	
	
	// excluir
	public Response<String> Excluir(int id) {
		Response<String> response = new Response<>();
		try {
			Optional<EmailAlerta> emOp = repo.findById(id);
			if (!emOp.isPresent()) {
				throw new Exception("não foi possível encontrar um e-mail com o id "+ id);
			}
			EmailAlerta emBanco = emOp.get();
			repo.delete(emBanco);
			response.setDados("ok");
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao tentar excluir um e-mail receptor de alerta"+ e.getMessage());
			return response;
		}
	}
	
	//get id
	public Response<EmailAlerta> getById(int id) {
		Response<EmailAlerta> response = new Response<>();
		try {
			Optional<EmailAlerta> emOp = repo.findById(id);
			if (!emOp.isPresent()) {
				throw new Exception("não foi possível encontrar um e-mail com o id "+ id);
			}
			EmailAlerta emBanco = emOp.get();
			response.setDados(emBanco);
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao tentar buscar um e-mail receptor de alerta"+ e.getMessage());
			return response;
		}
	}
	
	//get all
	public Response<List<EmailAlerta>> getAll() {
		Response<List<EmailAlerta>> response = new Response<>();
		try {
			response.setDados(repo.findAllByOrderByEmailAsc());
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao tentar buscar todos os e-mails receptores de alerta"+ e.getMessage());
			return response;
		}
	}

}// fecha classe
