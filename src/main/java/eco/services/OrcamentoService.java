package eco.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eco.entidades.Cliente;
import eco.entidades.Orcamento;
import eco.entidades.repositorios.ClienteRepository;
import eco.entidades.repositorios.OrcamentoRepository;
import eco.response.Response;

@Service
public class OrcamentoService {
	
	@Autowired
	private OrcamentoRepository repo;
	
	@Autowired
	private ClienteRepository repoCli;
	
	
	//cadastrar
	@Transactional
	public Response<Orcamento> cadastrar(Orcamento o) {
		Response<Orcamento> response = new Response<>();
		try {
			o.setId(null);// certificar de que será um novo orçamento
			validarCadastro(o, false);
			Optional<Cliente> clienteOp = repoCli.findById(o.getCliente().getId());
			if(!clienteOp.isPresent()) {
				throw new Exception("não foi possível encontrar um cliente com o id: " + o.getCliente().getId());
			}
			
			if (o.getNumero() == 0) {
				Orcamento ultimo = repo.findTop1ByOrderByNumeroDescAnoDesc();
				if(ultimo == null) {
					o.setNumero(1);
				} else {
					o.setNumero(ultimo.getNumero() + 1);
				}
				o.setAno(LocalDate.now().getYear());
			}
		
			o.setCliente(clienteOp.get());
			response.setDados(repo.save(o));
			return response;
			
		} catch (Exception e) {
			response.getErros().add("Erro ao cadastrar um orçamento: "+ e.getMessage());
			return response;
		}
	}

	//valida um orçamento antes de cadastra-lo
	private void validarCadastro(Orcamento o, boolean edicao) throws Exception {
		if(o == null) {
			throw new Exception("o orçamento está nulo");
		}
		if(o.getCliente() == null) {
			throw new Exception("o cliente do orçamento está nulo");
		}
		if(o.getCliente().getId() == null) {
			throw new Exception("o id do cliente do orçamento está nulo");
		}
		
		if (!edicao) {
			Long count = repo.countByNumeroAndAno(o.getNumero(), o.getAno());
			if(count > 0) {
				throw new Exception("o número de orçamento "+ o.getNumero()+" / "+o.getAno() +" já está sendo utilizado");
			}
		} else {
			Long count = repo.countByNumeroAndAnoAndIdNot(o.getNumero(), o.getAno(), o.getId());
			if(count > 0) {
				throw new Exception("o número de orçamento "+ o.getNumero()+" / "+o.getAno() +" já está sendo utilizado");
			}
		}
		
		
	}
	
	//editar
	public Response<Orcamento> alterar(Orcamento o) {
		Response<Orcamento> response = new Response<>();
		try {

			validarCadastro(o, true);
			Optional<Orcamento>oOp = repo.findById(o.getId());
			if(!oOp.isPresent()) {
				throw new Exception("não foi possível encontrar um orcamento com o id: " + o.getCliente().getId());
			} 
			Optional<Cliente> clienteOp = repoCli.findById(o.getCliente().getId());
			if(!clienteOp.isPresent()) {
				throw new Exception("não foi possível encontrar um cliente com o id: " + o.getCliente().getId());
			} 
			o.setCliente(clienteOp.get());
			Orcamento oBanco = oOp.get();
			oBanco.alterarDados(o);
			response.setDados(repo.save(oBanco));
			return response;
			
		} catch (Exception e) {
			response.getErros().add("Erro ao alterar um orçamento: "+ e.getMessage());
			return response;
		}
	}
	
	//excluir
	public Response<String> excluir(Long id) {
		Response<String> response = new Response<>();
		try {
			
			Optional<Orcamento>oOp = repo.findById(id);
			if(!oOp.isPresent()) {
				throw new Exception("não foi possível encontrar um orcamento com o id: " + id);
			} 
			
			repo.delete(oOp.get());
			response.setDados("ok");
			return response;
			
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar excluír um orçamento: "+ e.getMessage());
			return response;
		}
	}
	
	//getId
	public Response<Orcamento> getById(Long id) {
		Response<Orcamento> response = new Response<>();
		try {

			Optional<Orcamento>oOp = repo.findById(id);
			if(!oOp.isPresent()) {
				throw new Exception("não foi possível encontrar um orcamento com o id: " + id);
			} 
			
			response.setDados(oOp.get());
			return response;
			
		} catch (Exception e) {
			response.getErros().add("Erro ao consultar um orçamento por id: "+ e.getMessage());
			return response;
		}
	}
	
	//get lista orçamento por id do cliente
	public Response<List<Orcamento>> getAllIdCliente(int idcliente) {
		Response<List<Orcamento>> response = new Response<>();
		try {

			List<Orcamento> orcamentos = repo.findByClienteIdOrderByAnoDescNumeroDesc(idcliente);
			if(orcamentos == null) orcamentos = new ArrayList<Orcamento>();
			response.setDados(orcamentos);
			return response;
			
		} catch (Exception e) {
			response.getErros().add("Erro ao consultar um orçamento por id: "+ e.getMessage());
			return response;
		}
	}
	
	
	
//
//	public Response<Index_num_or> getUltimoNumero() {
//		Response<Index_num_or> response = new Response<>();
//		try {
//			Index_num_or numero = new Index_num_or();
//			Long count = repoIdx.count();
//			if(count == 0) {
//			  numero.setAno(LocalDate.now().getYear());
//			  numero.setNumero(1);
//			  response.setDados(repoIdx.save(numero));
//			  return response;
//			}
//			numero = repoIdx.findTop1ByOrderByIdDesc();
//			Index_num_or num2 = new Index_num_or();
//			num2.setAno(numero.getAno());
//			num2.setNumero(numero.getNumero() + 1);
//			num2.setId(null);
//			response.setDados(repoIdx.save(num2));
//			return response;
//		} catch (Exception e) {
//			response.getErros().add("Erro ao tentar recuperar o ultimo número de orçamento: "+ e.getMessage());
//			return response;
//		}
//	}
//	
//	
//	public Response<String> deleteIndexOr(Long id) {
//		Response<String> response = new Response<>();
//		try {
//			Index_num_or numero = repoIdx.findById(id).get();
//			if (numero == null) {
//				throw new Exception("não foi possível encontrar um index de orçamento com o id: "+ id);
//			}
//		    repoIdx.delete(numero);
//		    response.setDados("ok");
//		    return response;
//		} catch (Exception e) {
//			response.getErros().add("Erro ao tentar deletar um index de Orçamento: "+ e.getMessage());
//			return response;
//		}
//	}
//	
	
	
	// get por data de entrega e idcliente
	public Response<List<Orcamento>> getIdClienteDataEntrega(String dtinicio, String dtfim, int id) {
		Response<List<Orcamento>> response = new Response<>();
		try {
			LocalDate dtini = LocalDate.parse(dtinicio);
			LocalDate dtf = LocalDate.parse(dtfim);
			List<Orcamento> orc = repo.findByDataEntregaBetweenAndClienteIdOrderByAnoDescNumeroDesc(dtini, dtf, id);
			if(orc == null) {
				orc = new ArrayList<>();
			}
			response.setDados(orc);
		    return response;
			
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar buscar por data de entrega :"+ e.getMessage());
			return response;
		}
	}
	
	
	//get all

}// fecha classe
