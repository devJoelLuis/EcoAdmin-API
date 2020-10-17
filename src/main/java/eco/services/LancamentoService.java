package eco.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eco.dto.LancamentoOsDTO;
import eco.entidades.Categoria;
import eco.entidades.CategoriaRecebimento;
import eco.entidades.Lancamento;
import eco.entidades.OrdemServico;
import eco.entidades.Recebimento;
import eco.entidades.repositorios.CategoriaRecebimentoRepository;
import eco.entidades.repositorios.CategoriaRepository;
import eco.entidades.repositorios.LancamentoRepository;
import eco.entidades.repositorios.OrdemServicoRepository;
import eco.entidades.repositorios.RecebimentoRepository;
import eco.response.Response;

@Service
public class LancamentoService {
	
	@Autowired
	private LancamentoRepository repo;
	
	@Autowired
	private OrdemServicoRepository repoOs;
	
	@Autowired
	private CategoriaRepository repoCat;
	
	@Autowired
	private RecebimentoRepository repoRec;
	
	@Autowired
	private CategoriaRecebimentoRepository repoCatRec;
	
	
	
	
	
	//cadastra
	@Transactional
	public Response<String> cadastrar(Lancamento l, int parcelas) {
		Response<String> response = new Response<>();
		try {
		
			// caso seja uma taxa, não pode ser parcelado, então fixa as parcelas em 1
			if (l.getIdCategoriaRecebimentoTaxa() > 0) parcelas = 1;
			
			l = validar(l);
			if (parcelas > 24) {
				throw new Exception("o número máximo de parcelas é 24.");
			}
			if (parcelas <= 1) {
				//verificar se é taxa, se for debitar no saldo da categoria recebimento
				Integer idCatRec = l.getIdCategoriaRecebimentoTaxa();
				if (idCatRec > 0) {
					CategoriaRecebimento cr = repoCatRec.findById(idCatRec)
							.orElseThrow(() -> new Exception("não foi possível encontrar uma categoria de recebimento com o ID "+ idCatRec));
					Double saldoAtual = cr.getTotalReceita().doubleValue();
					Double novoSaldo = saldoAtual - l.getTotal();
					if (novoSaldo < 0) {
						throw new Exception("A categoria "+ cr.getDescricao()+" não possui saldo suficiente!");
					}
					cr.setTotalReceita(new BigDecimal(novoSaldo));
					repoCatRec.save(cr);
				}
				repo.save(l);
				response.setDados("ok");
				return response;
			}
			
			// se chegar aqui é porque tem parcelas
			LocalDate dataPrimeiraParcela = l.getDataLancamento();
			List<Lancamento> lancamentos = new ArrayList<Lancamento>();
			for (int i = 1; i <= parcelas; i++) {
				Lancamento lnew = new Lancamento();
				lnew.setCategoria(l.getCategoria());
				lnew.setDataLancamento(dataPrimeiraParcela);
				lnew.setDataPagTotal(l.getDataPagTotal());
				lnew.setDescricao("Parcela "+ i +": "+ l.getDescricao());
				lnew.setId(null);
				lnew.setObs(l.getObs());
				lnew.setOrdemServico(l.getOrdemServico());
				lnew.setPago(l.isPago());
				lnew.setRecebimentos(l.getRecebimentos());
				lnew.setTotal(l.getTotal());
				lancamentos.add(lnew);
				dataPrimeiraParcela = dataPrimeiraParcela.plusMonths(1);
			}
			repo.saveAll(lancamentos);
			response.setDados("ok");
			return response;
		
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar cadastrar um lançamento: "+ e.getMessage());
			return response;
		}
		
	}
	
	
	
	
	

	// valida para cadastro
	private Lancamento validar(Lancamento l) throws Exception {
		if (l == null) {
			throw new Exception("o lançamento está nulo!!!");
		}
		OrdemServico osBanco = repoOs.findById(l.getOrdemServico().getId())
				                     .orElseThrow(()-> new Exception("Não foi possível carregar a Ordem de Serviço com o id "+ l.getOrdemServico().getId()));
		l.setOrdemServico(osBanco);
		Categoria categoriaBb = repoCat.findById(l.getCategoria().getId())
				                       .orElseThrow(() -> new Exception("Não foi possível carregar uma categoria com id "+ l.getCategoria().getId()));
		l.setCategoria(categoriaBb);
		return l;
		
	}
	
	
	
	
	//altera
	@Transactional
	public Response<Lancamento> alterar(Lancamento l) {
		Response<Lancamento> response = new Response<>();
		try {
			l = validar(l);
			Long idLancamento = l.getId();
			Lancamento lbanco = repo.findById(idLancamento)
					                .orElseThrow(() -> new Exception("Não foi possível encontrar um lançamento com o id: "+ idLancamento));
			/*
			 * cenários possíveis para alteração quando for taxa
			 * 1- era taxa e deixou de ser: creditar o valor do lancamento na categoria recebimento com o id igual a idCategoriaRecebimentoTaxa setar 
			 * como zero a propriedade do lancamento idCategoriaRecebimentoTaxa.
			 * 
			 * 2- é taxa e continua mas o valor mudou para mais: pegar o novo valor do lancamento, 
			 * subtrair com o valor do lancamento que está no banco e debitar a difereça na categoria de recebimento
			 * verificando se o saldo é suficiente para a mudança, caso não seja emitir uma exceção.
			 * 
			 * 3- é taxa e o valor mudou para menos: pegar o novo valor, subtrair com o valor do lançamento que esta
			 * no banco de dados, creditar a difereça na categoria recebimento.
			 * 
			 * 4- é taxa e o valor não mudou: procede com a lógica.
			 * 
			 * 5- não era taxa e passou a ser: carregar a categoria que contenha o id igual a propriedade do lancamento
			 * idCategoriaRecebimentoTaxa, validar se o id existe, e debitar o valor do lancamento na categora carregada.
			 * 
			 * 6- não é taxa e continua não sendo taxa: continua a lógica de excução;
			 * 
			 * 7 - é taxa continua sendo taxa e mudou a categoria de recebimento: creditar o valor do lancamento na atual categoria
			 * do lancamento banco, e debitar na nova categoria.
			 */
			
			//se qualquer um for taxa entra no if, senão continua a lógica.
			if (l.getIdCategoriaRecebimentoTaxa() > 0 || lbanco.getIdCategoriaRecebimentoTaxa() > 0) {
				
				//era taxa e deixou de ser
				if (lbanco.getIdCategoriaRecebimentoTaxa() > 0 && l.getIdCategoriaRecebimentoTaxa() == 0) {
					//creditar o total atual na categoria recebimento e setar a propriedade do lançamento idCategoriaRecebimentoTaxa como zero
					CategoriaRecebimento cr = repoCatRec.findById(lbanco.getIdCategoriaRecebimentoTaxa()).get();
					double saldoAtual = cr.getTotalReceita().doubleValue();
					double novoSaldo = saldoAtual + lbanco.getTotal();
					cr.setTotalReceita(new BigDecimal(novoSaldo));
					repoCatRec.save(cr);
					l.setIdCategoriaRecebimentoTaxa(0);
					lbanco.setIdCategoriaRecebimentoTaxa(0);
					
				} else if (l.getIdCategoriaRecebimentoTaxa() == lbanco.getIdCategoriaRecebimentoTaxa()) { 
					// é taxa continua taxa e  continua a mesma categoria
					//buscar categoria recebimento
					CategoriaRecebimento cr = repoCatRec.findById(lbanco.getIdCategoriaRecebimentoTaxa()).get();
					
					//valor mudou
					if (l.getTotal() != lbanco.getTotal()) {
						if (l.getTotal() < lbanco.getTotal()) {
							//mudou para menos
							// pegar a diferença e creditar na categoria recebimento
							double saldoAtual = cr.getTotalReceita().doubleValue();
							double diferenca = lbanco.getTotal() - l.getTotal();
							double novoTotal = saldoAtual + diferenca;
							cr.setTotalReceita(new BigDecimal(novoTotal));
							repoCatRec.save(cr);
							
						} else if ( l.getTotal() > lbanco.getTotal()) {
							//mudou para mais
							//pegar a diferença e debitar na categoria recebimento
							double saldoAtual = cr.getTotalReceita().doubleValue();
							double diferenca = l.getTotal() - lbanco.getTotal();
							double novoSaldo = saldoAtual - diferenca;
							if (novoSaldo < 0) {
								throw new Exception("a categoria "+ cr.getDescricao() + " não possui saldo suficiente!");
							}
							cr.setTotalReceita(new BigDecimal(novoSaldo));
							repoCatRec.save(cr);
						}
						
					}
					
					
				} else if ( l.getIdCategoriaRecebimentoTaxa() != lbanco.getIdCategoriaRecebimentoTaxa()
						&& lbanco.getIdCategoriaRecebimentoTaxa() > 0) {
					// é taxa mas mudou a categoria, creditar o valor atual na categoria recebimento atual, creditar o novo 
					// na nova categoria.
					//buscar categoria atual e creditar o valor atual
					Integer idcategoria = lbanco.getIdCategoriaRecebimentoTaxa();
					CategoriaRecebimento cr = repoCatRec.findById(idcategoria).get();
					double saldoAtual = cr.getTotalReceita().doubleValue();
					double credito = lbanco.getTotal();
					double novoSaldo = saldoAtual + credito;
					
					cr.setTotalReceita(new BigDecimal(novoSaldo));
					repoCatRec.save(cr);
					
					//buscar a nova categoria e debitar o valor se possível
					Integer idNewCategoria = l.getIdCategoriaRecebimentoTaxa();
					CategoriaRecebimento crNew = repoCatRec.findById(idNewCategoria)
							.orElseThrow(() -> new Exception("não foi possível encontrar uma categoria com o id "+ idNewCategoria+ "."));
					saldoAtual = crNew.getTotalReceita().doubleValue();
					double debito = l.getTotal();
					novoSaldo = saldoAtual - debito;
					
					//verificar se saldo foi suficiente para debitar
					if (novoSaldo < 0) {
						throw new Exception("a categoria "+ crNew.getDescricao() +" não possui saldo suficiente.");
					}
					crNew.setTotalReceita(new BigDecimal(novoSaldo));
					lbanco.setIdCategoriaRecebimentoTaxa(l.getIdCategoriaRecebimentoTaxa());
					
				} else if (lbanco.getIdCategoriaRecebimentoTaxa() == 0 && l.getIdCategoriaRecebimentoTaxa() > 0) {
					// não era taxa, mas passou a ser
					//carregar a categoria recebimento e debitar o total do lançamento
					Integer idNewCategoria = l.getIdCategoriaRecebimentoTaxa();
					CategoriaRecebimento crNew = repoCatRec.findById(idNewCategoria)
							.orElseThrow(() -> new Exception("não foi possível encontrar uma categoria com o id "+ idNewCategoria+ "."));
					double saldoAtual = crNew.getTotalReceita().doubleValue();
					double debito = l.getTotal();
					double novoSaldo = saldoAtual - debito;
					
					//verificar se saldo foi suficiente para debitar
					if (novoSaldo < 0) {
						throw new Exception("a categoria "+ crNew.getDescricao() +" não possui saldo suficiente.");
					}
					crNew.setTotalReceita(new BigDecimal(novoSaldo));
					lbanco.setIdCategoriaRecebimentoTaxa(l.getIdCategoriaRecebimentoTaxa());
					
					
				}
				
				
			}
			
			BeanUtils.copyProperties( l, lbanco, "id", "ordemServico");// ignora as propriedades id e ordemServico
			response.setDados(repo.save(lbanco));
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar alterar o lançamento: "+ e.getMessage());
			return response;
		}
		
	}
	
	
	
	
	
	
	//excluir
	@Transactional
	public Response<String> excluir(Long id) {
		Response<String> response = new Response<>();
		try {
			Optional<Lancamento> lOp = repo.findById(id);
			if (!lOp.isPresent()) {
			  throw new Exception("não foi possível encontrar um lancamento com o id: "+ id);
			}
			Lancamento lbanco = lOp.get();
			// verificar se é taxa, se for creditar o valor de volta no saldo da categoria recebimento
			if (lbanco.getIdCategoriaRecebimentoTaxa() > 0) {
				CategoriaRecebimento cr = repoCatRec.findById(lbanco.getIdCategoriaRecebimentoTaxa()).get();
				double saldoAtual = cr.getTotalReceita().doubleValue();
				double novoSaldo = saldoAtual + lbanco.getTotal();
				cr.setTotalReceita(new BigDecimal(novoSaldo));
				repoCatRec.save(cr);
			}
			repo.delete(lbanco);
			response.setDados("OK");
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar alterar o lançamento: "+ e.getMessage());
			return response;
		}
		
	}
	
	
	

	
	
	
	//get por id
	public Response<Lancamento> getPorId(Long id) {
		Response<Lancamento> response = new Response<>();
		try {
			Optional<Lancamento> lOp = repo.findById(id);
			if (!lOp.isPresent()) {
			  throw new Exception("não foi possível encontrar um lançamento com o id: "+ id);
			}
			response.setDados(lOp.get());
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar consultar um lançamento por id: "+ e.getMessage());
			return response;
		}
	}




     //get all id os
	public Response<Page<Lancamento>> getAllByIdOs(Long idos, int page, int size) {
		Response<Page<Lancamento>> response = new Response<Page<Lancamento>>();
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<Lancamento> lancamentosPage = repo.findByOrdemServicoId(idos, pageable);
			response.setDados(lancamentosPage);
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar buscar os lançamentos pelo id da os "+ e.getMessage());
			return response;
		}
	}
	
	







	public Response<Page<LancamentoOsDTO>> getLancamentoOsDto(int page, int size, String filtro, Long idos,
			String inicio, String fim) {
		Response<Page<LancamentoOsDTO>> response = new Response<>();
		try {
			corrigirLogicaPagos();
			LocalDate dataInicio = LocalDate.parse(inicio);
			LocalDate dataFim = LocalDate.parse(fim);
			
			Pageable pageable = PageRequest.of(page, size);
			
			List<LancamentoOsDTO> listDto = new ArrayList<LancamentoOsDTO>();
			List<Lancamento> lancamentos = new ArrayList<Lancamento>();
			List<Recebimento> recebimentos = new ArrayList<>();
			
			switch (filtro) {
				case "aberto":
					lancamentos = repo.findByOrdemServicoIdAndPagoAndDataLancamentoBetween(idos, false, dataInicio, dataFim, pageable);
					break;
	            case "pago":
	            	lancamentos = repo.findByOrdemServicoIdAndPagoAndDataLancamentoBetween(idos, true, dataInicio, dataFim, pageable);
					break;	
	
				default:
					//TODOS
				   lancamentos = repo.findByOrdemServicoIdAndDataLancamentoBetween(idos, dataInicio, dataFim, pageable);
				break;
			}//fecha switch
			
			for (Lancamento l: lancamentos) {
				LancamentoOsDTO dto = new LancamentoOsDTO();
				dto.setIdlancamento(l.getId());
				dto.setDescricao(l.getDescricao());
				dto.setTotal(l.getTotal());
				dto.setData(l.getDataLancamento());
				recebimentos = repoRec.findByLancamentoId(l.getId());
				dto.setRecebimentos(recebimentos);
				if (recebimentos.size() == 0) {
					dto.setTotalReceber(l.getTotal());
				} else {
					//somar todos recebimentos
					for (Recebimento r: recebimentos) {
						dto.setTotalPago(dto.getTotalPago() + r.getValor().doubleValue());
						dto.setTotalReceber(dto.getTotal() - dto.getTotalPago());
						if (dto.getTotalPago() == null) {
							dto.setTotalPago(0.00);
						}
						if (dto.getTotalReceber() == null) {
							dto.setTotalReceber(0.00);
						}
					}
				}
				
				listDto.add(dto);
			}
			
			//converter a listaDto em page
			Page<LancamentoOsDTO> pagedto = new PageImpl<LancamentoOsDTO>(listDto, pageable, listDto.size());
			
			response.setDados(pagedto);
			return response;
			
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao tentar consultar os lançamentos DTO: " + e.getMessage());
			return response;
		}
	}






	private void corrigirLogicaPagos() throws Exception {
		List<Lancamento> lancamentosCorrigir = repo.getLancamentosCorrigir();		
		for (Lancamento l: lancamentosCorrigir) {
			l.setPago(true);
			repo.save(l);
		}
	}
	
	
	
	
	

}//fecha classe
