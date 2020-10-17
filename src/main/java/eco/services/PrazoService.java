package eco.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eco.entidades.OrdemServico;
import eco.entidades.Prazo;
import eco.entidades.repositorios.OrdemServicoRepository;
import eco.entidades.repositorios.PrazoRepository;
import eco.response.Response;

@Service
public class PrazoService {
	
	@Autowired
	private PrazoRepository repo;
	
	@Autowired
	private OrdemServicoRepository repoOs;
	
	
	//cadastrar
	public Response<Prazo> cadastrar(Prazo prazo) {
		Response<Prazo> response = new Response<Prazo>();
		try {
			prazo.setId(null);
			// buscar os
			Optional<OrdemServico> osOp = repoOs.findById(prazo.getOs().getId());
			if (!osOp.isPresent()) {
				throw new Exception("não foi possível encontrar uma O.S. com o id "+ prazo.getOs().getId());
			}
			//adicionar os
			prazo.setOs(osOp.get());
			//salvar
			response.setDados(repo.save(prazo));
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar cadastrar um novo prazo: "+ e.getMessage());
			return response;
		}
		
	}
	
	//editar
	public Response<Prazo> alterar(Prazo prazo) {
		Response<Prazo> response = new Response<Prazo>();
		try {
			// buscar os
			Optional<OrdemServico> osOp = repoOs.findById(prazo.getOs().getId());
			if (!osOp.isPresent()) {
				throw new Exception("não foi possível encontrar uma O.S. com o id "+ prazo.getOs().getId());
			}
			Optional<Prazo> pOp = repo.findById(prazo.getId());
			if (!pOp.isPresent()) {
				throw new Exception("não foi possível encontrar um prazo com o id "+ prazo.getOs().getId());
			}
			Prazo pBanco = pOp.get();
			BeanUtils.copyProperties(prazo, pBanco);
			pBanco.setOs(osOp.get());
			
			//salvar
			response.setDados(repo.save(pBanco));
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar alterar um prazo: "+ e.getMessage());
			return response;
		}
		
	}
	
	//excluir
	public Response<String> excluir(Long id) {
		Response<String> response = new Response<>();
		try {
			Optional<Prazo> pOp = repo.findById(id);
			if (!pOp.isPresent()) {
				throw new Exception("não foi possível encontrar um prazo com o id "+ id);
			}
			Prazo pBanco = pOp.get();
			repo.delete(pBanco);
			//salvar
			response.setDados("ok");
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar excluir um prazo: "+ e.getMessage());
			return response;
		}
		
	}
	
	
	//get id
	public Response<Prazo> getById(Long id) {
		Response<Prazo> response = new Response<Prazo>();
		try {
			
			Optional<Prazo> pOp = repo.findById(id);
			if (!pOp.isPresent()) {
				throw new Exception("não foi possível encontrar um prazo com o id "+ id);
			}
			Prazo pBanco = pOp.get();
			response.setDados(pBanco);
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar consultar um prazo por id: "+ e.getMessage());
			return response;
		}
		
	}
	
	//get all
	
	//get all por O.S. 
	public Response<List<Prazo>> getAllOsId(Long idos) {
		Response<List<Prazo>> response = new Response<>();
		try {
			
			List<Prazo> prazos = repo.findByOsIdOrderByDataVencimentoDesc(idos);
			response.setDados(prazos);
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar consultar um prazo por id: "+ e.getMessage());
			return response;
		}
		
	}
	
	
	
	
    // get all prazos em alerta
	public Response<List<Prazo>> getAllAlerta() {
		Response<List<Prazo>> response = new Response<>();
		try {
			List<Prazo> prazos = repo.findByCumpridoAndAlertaAndDataAlertaLessThanEqual(false, true, LocalDate.now());
			response.setDados(prazos);
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar consultar prazos em alerta: "+ e.getMessage());
			return response;
		}
	}
	
	
	
    // get all prazos vencendo
	public Response<List<Prazo>> getAllVencendo() {
		Response<List<Prazo>> response = new Response<>();
		try {
			List<Prazo> prazos = repo.findByCumpridoAndDataVencimentoBetween(false, LocalDate.now(), LocalDate.now().plusDays(6));
			response.setDados(prazos);
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar consultar prazos em alerta: "+ e.getMessage());
			return response;
		}
	}

	
	
	// get all prazos vencidos
	public Response<List<Prazo>> getAllVencido() {
		Response<List<Prazo>> response = new Response<>();
		try {
			List<Prazo> prazos = repo.findByCumpridoAndDataVencimentoLessThan(false, LocalDate.now());
			response.setDados(prazos);
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar consultar prazos em alerta: "+ e.getMessage());
			return response;
		}
	}

	//get all pageable
	public Response<Page<Prazo>> getAll(int page, int size) {
		Response<Page<Prazo>> response = new Response<Page<Prazo>>();
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<Prazo> prazosPage = repo.findAll(pageable);
			response.setDados(prazosPage);
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar consultar todos os prazos: "+ e.getMessage());
			return response;
		}
	}

	
	//get all prazos da O.S. número
	public Response<List<Prazo>> getAllByNumOs(int numOs, int anoOs) {
		Response<List<Prazo>> response = new Response<>();
		try {
			List<Prazo> prazos = repo.findByOsNumOsAndOsAnoOs(numOs, anoOs);
			response.setDados(prazos);
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar consultar prazos pelo número da O.S.: "+ e.getMessage());
			return response;
		}
	}
	
	
	
	

}//fecha classe
