package eco.services;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eco.classes.DespesaFiltro;
import eco.entidades.CategoriaRecebimento;
import eco.entidades.Despesa;
import eco.entidades.OrdemServico;
import eco.entidades.repositorios.CategoriaRecebimentoRepository;
import eco.entidades.repositorios.DespesaRepository;
import eco.entidades.repositorios.OrdemServicoRepository;
import eco.response.Response;

@Service
public class DespesaService {
	
	
	@Autowired
	private DespesaRepository repo;
	
	@Autowired
	private CategoriaRecebimentoRepository repoCatRec;
	
	@Autowired
	private OrdemServicoRepository repoOs;
	
	
	
	// cadastrar nova despesa
	@Transactional
	public Response<Despesa> cadastrar(Despesa despesa) {
		Response<Despesa> response = new Response<>();
		try {
			
			OrdemServico os = new OrdemServico();
			// pegar o O.S. se exitir
			Long idOs = despesa.getOs().getId();
			if (idOs != null && idOs > 0) {
				os = repoOs.findById(idOs)
						.orElseThrow(() -> new Exception("não foi possível encontrar uma O.S. com id "+ idOs));
				despesa.setOs(os);
			}
			
			// verificar se categoria existe, se exite subtrair o valor da despesa e salvar
			Integer idcat = despesa.getCategoriaRecebimento().getId();
			CategoriaRecebimento cr = repoCatRec.findById(idcat)
					.orElseThrow(() -> new Exception("não existe uma categoria com o id "+ idcat));
			//pegar saldo da categoria
			Double saldo = cr.getTotalReceita().doubleValue();
			//pegar o valor a ser descontado
			Double valorDespesa = despesa.getValor().doubleValue();
			//subtrai o valor e verificar 
			Double total = saldo - valorDespesa;
			if (total < 0) {
				throw new Exception("saldo insuficiente.");
			}
			cr.setTotalReceita(cr.getTotalReceita().subtract(despesa.getValor()));
			cr = repoCatRec.save(cr);
			despesa.setCategoriaRecebimento(cr);
			
			if (despesa.getOs().getId() == null || despesa.getOs().getId() == 0) {
				despesa.setOs(null);
			}
			
			// salvar despesa e retornar
			Despesa dBanco = repo.save(despesa);
			response.setDados(dBanco);
			return response;
			
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao cadastrar uma despesa: "+ e.getMessage());
			return response;
		}
	}
	
	
	
	
	
	
	// alterar a despesa
		@Transactional
		public Response<Despesa> alterar(Despesa despesa) {
			Response<Despesa> response = new Response<>();
			try {
				
				//verificar se a despesa existe
				Despesa dBanco = repo.findById(despesa.getId())
						.orElseThrow(()-> new Exception("não foi possível encontrar um despesa com o id "+ despesa.getId()));
				
				OrdemServico os = new OrdemServico();
				// pegar o O.S. se exitir
				Long idOs = despesa.getOs().getId();
				if ( idOs > 0) {
					os = repoOs.findById(idOs)
							.orElseThrow(() -> new Exception("não foi possível encontrar uma O.S. com id "+ idOs));
					dBanco.setOs(os);
				}
				
			
				
							
				
				
				// verificar se categoria existe, se exite subtrair o valor da despesa e salvar
				Integer idcat = despesa.getCategoriaRecebimento().getId();
				CategoriaRecebimento cr = repoCatRec.findById(idcat)
						.orElseThrow(() -> new Exception("não existe uma categoria com o id "+ idcat));
				
				// verificar se o valor da despesa foi alterado, se foi verificar se foi para mais ou para menos e recalcular categoria
				Double diferenca = 0.00;
				Double valorAtualDesp = dBanco.getValor().doubleValue();
				Double valorNovoDesp = despesa.getValor().doubleValue();
				
				boolean categoriaMudada = despesa.getCategoriaRecebimento().getId() != dBanco.getCategoriaRecebimento().getId(); 
				
				/*
				 * o saldo fica na categoria de recebimento, por isso aqui temos três cenários:
				 * 
				 * 1- A categoria foi alterada, então o sistema terá que: acrescentar o  valor no saldo da antiga categoria 
				 * e subtrair o valor na nova categoria. Verificar se a nova categoria tem saldo suficiente.
				 * 
				 * 2- A categoria foi mudada e o valor tambem, então o sitema terá que: acrescentar o antigo valor a antiga categoria, 
				 * e subtrair o novo valor da nova categoria. Verificar se a nova categoria tem saldo suficiente.
				 * 
				 *  3- O cenário é que a categoria não foi alterada mas o valor sim, então o sistema terá que: verificar a diferença
				 *  se for para mais, subtrair a diferença do salto da categoria, se for para menos creditar o diferença no saldo.
				 *  Verificar se o saldo da categoria é suficiente para a alteração
				 */
				
				
				//--------------------- INICIO CENÁRIO 1 -----------------------------------------------------------------
				//Cenário 1: categoria foi mudada e valor mantido
				if (categoriaMudada && valorAtualDesp == valorNovoDesp ) {
				   CategoriaRecebimento categoriaAntiga = repoCatRec.findById(dBanco.getCategoriaRecebimento().getId()).get();
				   // trazer a antiga categoria para o contexto, alterar o saldo e salvar
				   Double saldoAntigaCat = categoriaAntiga.getTotalReceita().doubleValue();
				   Double novoSaldo = saldoAntigaCat + valorAtualDesp;
				   categoriaAntiga.setTotalReceita(new BigDecimal(novoSaldo));
				   // subtrair o valor da nova categoria e salvar
				   Double saldoNovaCat = cr.getTotalReceita().doubleValue();
				   novoSaldo = saldoNovaCat - valorNovoDesp;
				   if (novoSaldo < 0) {
					   throw new Exception("saldo da categoria "+ cr.getTotalReceita()+" insuficiente.");
				   }
				   cr.setTotalReceita(new BigDecimal(novoSaldo));
				   // salvar a nova categoria
				   repoCatRec.save(cr);
				   // salvar a antiga categoria
				   repoCatRec.save(categoriaAntiga);
				   dBanco.setCategoriaRecebimento(cr);
				}
				
				//---------------- fim cenário 1 --------------------------------------------------------------------
				
				//--------------------INICIO CENÁRIO 2 --------------------------------------------------------------
				//Cenário 2: categoria foi mudada e valor também
				if (categoriaMudada && valorAtualDesp != valorNovoDesp) {
					CategoriaRecebimento categoriaAntiga = repoCatRec.findById(dBanco.getCategoriaRecebimento().getId()).get();
					 // trazer a antiga categoria para o contexto, alterar o saldo e salvar
					Double saldoAntigaCat = categoriaAntiga.getTotalReceita().doubleValue();
					Double novoSaldo = saldoAntigaCat + valorAtualDesp;
					categoriaAntiga.setTotalReceita(new BigDecimal(novoSaldo));
					 // subtrair o valor da nova categoria e salvar
					 Double saldoNovaCat = cr.getTotalReceita().doubleValue();
					 novoSaldo = saldoNovaCat - valorNovoDesp;
					 if (novoSaldo < 0) {
					   throw new Exception("saldo da categoria "+ cr.getDescricao()+" insuficiente.");
					 }
					 //adicionar o novo saldo a nova categoria da despesa
					 cr.setTotalReceita(new BigDecimal(novoSaldo));
					 // salvar a nova categoria da despesa
					 repoCatRec.save(cr);
					 // salvar a antiga categoria da despesa
					 repoCatRec.save(categoriaAntiga);
					 //adicionar a nova categoria a despesa do banco
					 dBanco.setCategoriaRecebimento(cr);
				}
				//----------------- Fim Cenário 2--------------------------------------------------------------------
			    
				// --------------------------- INICIO CENÁRIO 3 -----------------------------------------------------
				 if (!categoriaMudada) {
					 // verificar se o valor foi mudado
					 if (valorNovoDesp > valorAtualDesp) {
							diferenca = valorNovoDesp - valorAtualDesp;
							Double receita = cr.getTotalReceita().doubleValue();
							Double total = receita - diferenca;
							if (total < 0) {
								 throw new Exception("saldo da categoria "+ cr.getDescricao()+" insuficiente.");
							}
							cr.setTotalReceita(new BigDecimal(total));
						} else if (valorNovoDesp < valorAtualDesp) {
							diferenca = valorAtualDesp - valorNovoDesp;
							Double receita = cr.getTotalReceita().doubleValue();
							cr.setTotalReceita(new BigDecimal(receita + diferenca));
						}
				 }
				
				//-----------------Fim cenário 3 ---------------------------------------------------------------------
				
				//copiar o restante das propriedades
				BeanUtils.copyProperties(despesa, dBanco, "id", "os", "categoriaRecebimento");
				if (despesa.getOs().getId() == 0) {
					despesa.setOs(null);
				}
				// salvar despesa e retornar
			    dBanco = repo.save(despesa);
				response.setDados(dBanco);
				return response;
				
			} catch (Exception e) {
				response.getErros().add("Ocorreu um erro ao alterar uma despesa: "+ e.getMessage());
				return response;
			}
		}
		
		
		
	
	// excluir despesa
	@Transactional	
	public Response<String> excluir(Long id) {
		Response<String> response = new Response<String>();
		try {
			
			//verificar se a despesa existe
			Despesa dBanco = repo.findById(id)
					.orElseThrow(()-> new Exception("não foi possível encontrar um despesa com o id "+ id));
			CategoriaRecebimento cr = repoCatRec.findById(dBanco.getCategoriaRecebimento().getId()).get();
			//pegar o valor da despesa
			Double valorDesp = dBanco.getValor().doubleValue();
			//pegar saldo atual
			Double saldo = cr.getTotalReceita().doubleValue();
			//somar novo saldo
			Double total = saldo + valorDesp;
			
			cr.setTotalReceita(new BigDecimal(total));
			repoCatRec.save(cr);
			repo.delete(dBanco);
			response.setDados("ok");
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao excluir uma despesa: "+ e.getMessage());
			return response;
		}
	}
		
		
		
		
	
	// get despesa id
	public Response<Despesa> getById(Long id) {
		Response<Despesa> response = new Response<Despesa>();
		try {
			Despesa dBanco = repo.findById(id)
					.orElseThrow(()-> new Exception("não foi possível encontrar um despesa com o id "+ id));
			response.setDados(dBanco);
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao consultar uma despesa por id: "+ e.getMessage());
			return response;
		}
	}






	public Response<Page<Despesa>> getAll(int page, int size) {
		Response<Page<Despesa>> response = new Response<Page<Despesa>>();
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<Despesa> despPage = repo.findAllByOrderByDataDesc(pageable);
			response.setDados(despPage);
			return response;
		} catch (Exception e) {
			response.getErros()
			.add("Ocorreu um erro ao tentar consultar todas as despesas: "+ e.getMessage());
			return response;
		}
	}
	
	
	public Response<Page<Despesa>> getFiltro(DespesaFiltro df) {
		Response<Page<Despesa>> response = new Response<Page<Despesa>>();
		try {
			Pageable pageable = PageRequest.of(df.getPage(), df.getSize());
			
			
			
			//consulta por data 
			if (df.getConsultaTipo().equals("porData")) {
				if (df.getDataInicio() == null || df.getDataFinal() == null)
				throw new Exception("informe a data início e a data final para pesquisar por data.");
			} else if (df.getConsultaTipo().equals("porData")) {
				LocalDate dtInicio = LocalDate.parse(df.getDataInicio());
				LocalDate dtFim = LocalDate.parse(df.getDataFinal());
				Page<Despesa> despPage = repo.findByDataBetweenOrderByDataDesc(dtInicio, dtFim, pageable);
				response.setDados(despPage);
				return response;
			}
			
			//consulta por categoria
			if (df.getIdcategoriaRecebimento() != null && df.getConsultaTipo().equals("porCategoria")) {
				Page<Despesa> despPage = repo.findByCategoriaRecebimentoIdOrderByDataDesc(df.getIdcategoriaRecebimento(), pageable);
				response.setDados(despPage);
				return response;
			}
			
			//consultar por descricao
			if (df.getDescricao() != null && df.getConsultaTipo().equals("porDescricao")) {
				Page<Despesa> despPage = repo.findByDescricaoContainingIgnoreCaseOrderByDataDesc(df.getDescricao(), pageable);
				response.setDados(despPage);
				System.out.println("executou");
				return response;
			}
			
			
			
			Page<Despesa> despPage = repo.findAllByOrderByDataDesc(pageable);
			response.setDados(despPage);
			return response;
		} catch (Exception e) {
			response.getErros()
			.add("Ocorreu um erro ao tentar filtrar as despesas: "+ e.getMessage());
			return response;
		}
	}
	
	
	
	

}//fecha classe
