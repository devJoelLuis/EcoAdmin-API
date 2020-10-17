package eco.services;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;

import eco.entidades.Cliente;
import eco.entidades.Gerenciamento;
import eco.entidades.Parcela;
import eco.entidades.repositorios.ClienteRepository;
import eco.entidades.repositorios.GerenciamentoRepository;
import eco.entidades.repositorios.ParcelaRepositoy;
import eco.response.Response;

@Service
public class GerenciamentoService {
	
	@Autowired
	private GerenciamentoRepository repo;
	
	@Autowired
	private ClienteRepository repoCli;
	
	@Autowired
	private ParcelaService serviceParcela;
	
	@Autowired
	private ParcelaRepositoy repoPar;
	
	//cadastrar
	@Transactional
	public Response<Gerenciamento> cadastrar(Gerenciamento g) {
		Response<Gerenciamento> response = new Response<>();
		try {
			validarGerenciamento(g);
			//buscar o cliente
			Optional<Cliente> cliOp = repoCli.findById(g.getCliente().getId());
			if(!cliOp.isPresent()) {
				throw new Exception("não foi possível encontrar um cliente com o id "+ g.getCliente().getId() );
			}
			g.setCliente(cliOp.get());
			Gerenciamento gSalvo = repo.save(g);
			// gerar parcelas
			// criar uma lista de parcelas vazia
			List<Parcela> pars = new ArrayList<>();
			//criar uma data temp para manilupar as datas
			LocalDate dtTemp = g.getParcelas().get(0).getDataVencimento();
			LocalDate dtPagamento = LocalDate.parse("1970-01-01");
			Double valor = g.getParcelas().get(0).getValor();
			Parcela parcela = null;
			//fazer um for com o total de parcelas do gerenciamento
			int dia = dtTemp.getDayOfMonth();
			for (int i = 0; i < g.getTotalParcela(); i++) {
				parcela = new Parcela();
				parcela.setValor(valor);
				parcela.setPago(0);
				parcela.setDataPagamento(dtPagamento);
				parcela.setNumeroParcela(i + 1);// i + 1 porque i começa a contagem do zero
				// se for a primeira para parcela então fixa o que veio pelo cliente
				if(i == 0) {
					parcela.setDataVencimento(dtTemp);
				} else {
					dtTemp = dtTemp.plusMonths(1);
					//pegar o ultimo dia do mes
					int ultimoDiaMes = dtTemp.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
					//verificar se o dia do cliente é maior que o ultimo dia do mes desta parcela
					if(dia > ultimoDiaMes) {
						// se for maior então o dia será o ultimo do mês
						dtTemp = dtTemp.with(TemporalAdjusters.lastDayOfMonth()); 
					} else {
						//senão for maior então fixa o dia
						dtTemp = dtTemp.withDayOfMonth(dia);
					}
				}
				parcela.setDataVencimento(dtTemp);
				parcela.setGerenciamento(gSalvo);
				pars.add(serviceParcela.cadastroIN(parcela));
			}
			gSalvo.setParcelas(pars);
			// fim gerar parcelas
			response.setDados(gSalvo);
			return response;
		} catch (UnexpectedRollbackException e) {
			response.getErros().add("Erro ao tentar cadastrar um gerenciamento: "+ e.getMessage());
			return response;
		} catch (Exception ex) {
			response.getErros().add("Erro ao tentar cadastrar um gerenciamento: "+ ex.getMessage());
			return response;
		}
	}
	
	//alterar
		public Response<Gerenciamento> alterar(Gerenciamento g) {
			Response<Gerenciamento> response = new Response<>();
			try {
				if(g.getDescricao() == null) {
					throw new Exception("descrição não informada");
				}
				//buscar o gerenciamento
				Optional<Gerenciamento>gOp = repo.findById(g.getId());
				if(!gOp.isPresent()) {
					throw new Exception("não foi possível encontrar um gerenciamento com o id "+ g.getId());
				}
				Gerenciamento gBanco = gOp.get();
				//gBanco.alterarGerenciamento(g);
				gBanco.setDescricao(g.getDescricao());
				Gerenciamento gSalvo = repo.save(gBanco);
				response.setDados(gSalvo);
				return response;
			} catch (Exception e) {
				response.getErros().add("Erro ao tentar alterar um gerenciamento: "+ e.getMessage());
				return response;
			}
		}


	private void validarGerenciamento(Gerenciamento g) throws Exception {
		if(g == null) {
			throw new Exception("o gerencimanto está null");
		}
		if(g.getDescricao() == null) {
			throw new Exception("descrição não informada");
		}
		if(g.getDescricao().length() < 1) {
			throw new Exception("descrição não informada");
		}
		if(g.getCliente() == null) {
			throw new Exception("cliente não informado");
		}
		if(g.getParcelas().size() == 0) {
			throw new Exception("o gerenciamento não possui nenhum taxa cadastrada");
		}
		
	}
	

	
	//excluir
	@Transactional
	public Response<String> excluir(Long id) {
		Response<String> response = new Response<>();
		try {
			
			//buscar o gerenciamento
			Optional<Gerenciamento>gOp = repo.findById(id);
			if(!gOp.isPresent()) {
				throw new Exception("não foi possível encontrar um gerenciamento com o id "+ id);
			}
			List<Parcela> parcelas = repoPar.findByGerenciamentoIdOrderByNumeroParcelaAsc(id);
			if(parcelas != null) {
				repoPar.deleteAll(parcelas);
			}
			Gerenciamento gBanco = gOp.get();
			repo.delete(gBanco);
			response.setDados("ok");
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar excluir um gerenciamento: "+ e.getMessage());
			return response;
		}
	}

	
	//get id
	public Response<Gerenciamento> getById(Long id) {
		Response<Gerenciamento> response = new Response<>();
		try {
			
			
			Optional<Gerenciamento>gOp = repo.findById(id);
			if(!gOp.isPresent()) {
				throw new Exception("não foi possível encontrar um gerenciamento com o id "+ id);
			}
			Gerenciamento gBanco = gOp.get();
			response.setDados(gBanco);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar buscar por id um gerenciamento: "+ e.getMessage());
			return response;
		}
	}
	
	//get all pageable
	public Response<Page<Gerenciamento>> getAllPageableIdCliente(int page, int size, int idcliente) {
		Response<Page<Gerenciamento>> response = new Response<>();
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<Gerenciamento> gerPage = repo.findByClienteId(idcliente, pageable);
			response.setDados(gerPage);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar buscar todos os gerenciamentos: "+ e.getMessage());
			return response;
		}
	}
	

}// fecha classe
