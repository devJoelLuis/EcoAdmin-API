package eco.services;

import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eco.entidades.CategoriaRecebimento;
import eco.entidades.Lancamento;
import eco.entidades.Recebimento;
import eco.entidades.repositorios.CategoriaRecebimentoRepository;
import eco.entidades.repositorios.LancamentoRepository;
import eco.entidades.repositorios.RecebimentoRepository;
import eco.response.Response;

@Service
public class RecebimentoService {

	@Autowired
	private RecebimentoRepository repo;

	@Autowired
	private LancamentoRepository repoLanc;
	
	@Autowired
	private CategoriaRecebimentoRepository repoCatRec;

	// cadastrar
    @Transactional
	public Response<Recebimento> cadastrar(Recebimento recebimento) {
		Response<Recebimento> response = new Response<Recebimento>();
		try {

			if (recebimento.getValor().doubleValue() < 0.01 ) {
				throw new Exception("o valor do recebimento tem que ser maior que zero.");
			}
			Long idlancamento = recebimento.getLancamento().getId();
			Integer idcatRec = recebimento.getCategoriaRecebimento().getId();
			CategoriaRecebimento cr = repoCatRec.findById(idcatRec)
					.orElseThrow(() -> new Exception("não foi possível encontrar a categoria de recebimento com o id: "+ idcatRec));
           
			Lancamento lancamento = repoLanc.findById(idlancamento).orElseThrow(
					() -> new Exception("não foi possível encontrar um lancaçamento com o id " + idlancamento));
			
			 Double  totalRecebimentos = calcularRecebimentos(lancamento, recebimento);
			
			 if (totalRecebimentos >= lancamento.getTotal()) {
				 lancamento.setPago(true);
			 } else {
				 lancamento.setPago(false);
			 }
			 
			 //creditar na categoria o valor do recebimento
			 Double saldoAtual = cr.getTotalReceita().doubleValue();
			 Double valorRecebido = recebimento.getValor().doubleValue();
			 Double novoSaldo = saldoAtual + valorRecebido;
			 cr.setTotalReceita(new BigDecimal(novoSaldo));
			
			cr.getTotalReceita().add(recebimento.getValor());
			cr = repoCatRec.save(cr);
			recebimento.setLancamento(repoLanc.save(lancamento));
			recebimento.setCategoriaRecebimento(cr);
			repoCatRec.save(cr);
			response.setDados(repo.save(recebimento));
			return response;

		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar cadastrar um recebimento: " + e.getMessage());
			return response;
		}
	}

	
	//calcula o total de recebimento de um lançamento
	private Double calcularRecebimentos(Lancamento lancamento, Recebimento recebimento) {
		Double totalr = 0.00;
		for (Recebimento r: lancamento.getRecebimentos()) {
			totalr +=   r.getValor().doubleValue();
		}
		totalr += recebimento.getValor().doubleValue();
		return totalr;
	}
	
	
	
	
	
	

	// alterar
	@Transactional
	public Response<Recebimento> alterar(Recebimento recebimento) {
		Response<Recebimento> response = new Response<Recebimento>();
		try {
			
			if (recebimento.getValor().doubleValue() < 0.01 ) {
				throw new Exception("o valor do recebimento tem que ser maior que zero.");
			}
			
			Integer idcatRec = recebimento.getCategoriaRecebimento().getId();
			CategoriaRecebimento cr = repoCatRec.findById(idcatRec)
					.orElseThrow(() -> new Exception("não foi possível encontrar a categoria de recebimento com o id: "+ idcatRec));
           

			Long idrecebimento = recebimento.getId();
			Recebimento rBanco = repo.findById(idrecebimento).orElseThrow(
					()-> new Exception("não foi possível encontrar um recebimento com o id "+ idrecebimento));
			
			Long idlancamento = rBanco.getLancamento().getId();
			Lancamento lancamento = repoLanc.findById(idlancamento).orElseThrow(
					() -> new Exception("não foi possível encontrar um lancaçamento com o id " + idlancamento));
			
			
			/*
			 * lógicas possíveis
			 * 1 - nem a categoria nem o valor do recebimento foi alterado
			 * 2- somente a categoria foi alterado o valor do recebimento é o mesmo
			 * 3- a categoria é a mesma mas o valor do recebimento foi alterado para mais +
			 * 4- a categoria é a mesma mas o valor do recebimento foi alterado para menos -
			 * 5- a categoria foi alterada e o valor do recebimento foi alterado para mais +
			 * 6- a categorai foi alterada e o valor do recebiento foi alterado para menos -
			 */
			
			 // 1- verificar se a categoria ou o valor do recebimento foram modificados
			 if (cr.getId() != rBanco.getCategoriaRecebimento().getId()
					 || recebimento.getValor().doubleValue() != rBanco.getValor().doubleValue()) {
				 // se a categoria não foi alterada
				 if (cr.getId() == rBanco.getCategoriaRecebimento().getId()) { // categoria não foi alterada
					 // verificar os valores para mais e para menos
					 Double diferenca = 0.00;
					 Double valorBanco = rBanco.getValor().doubleValue();
					 Double valorNovo = recebimento.getValor().doubleValue();
					 if (valorNovo > valorBanco) {
						diferenca = valorNovo - valorBanco;
						cr.setTotalReceita(new BigDecimal(cr.getTotalReceita().doubleValue() + diferenca));
					 } else if (valorNovo < valorBanco) {
						diferenca = valorBanco - valorNovo;
						cr.setTotalReceita(new BigDecimal(cr.getTotalReceita().doubleValue() - diferenca));
					 }
				 } else {
					 // categoria foi alterada
					 /*
					  * subtrair o valor atual do recebimento da categoria atual e salvar
					  */
					 Integer idcatAtual = rBanco.getCategoriaRecebimento().getId();
					 Double valorAtualRec = rBanco.getValor().doubleValue();
					 Double saldoCategoriaAtual = rBanco.getCategoriaRecebimento().getTotalReceita().doubleValue(); 
					 Double novoSaldo = saldoCategoriaAtual - valorAtualRec;
					 CategoriaRecebimento categoriaAtual = repoCatRec.findById( idcatAtual).get();
					 categoriaAtual.setTotalReceita(new BigDecimal(novoSaldo));
					 categoriaAtual = repoCatRec.save(categoriaAtual);
					 
					 // creditar o novo valor na nova categoria
					 cr.setTotalReceita(cr.getTotalReceita().add(recebimento.getValor()));
					 
				 }
			 }
			
			 
			 cr = repoCatRec.save(cr);
			
			
			 lancamento.getRecebimentos().remove(rBanco);
			 
			 
			 
			
			 Double  totalRecebimentos = calcularRecebimentos(lancamento, recebimento);
				
			 if (totalRecebimentos >= lancamento.getTotal()) {
				 lancamento.setPago(true);
			 } else {
				 lancamento.setPago(false);
			 }
					 
			 
			BeanUtils.copyProperties(recebimento, rBanco, "id", "lancamento");
			rBanco.setCategoriaRecebimento(cr);
			
			lancamento.getRecebimentos().add(rBanco);
			repoLanc.save(lancamento);
			response.setDados(repo.save(rBanco));
			return response;

		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar alterar um recebimento: " + e.getMessage());
			return response;
		}
	}
	
	
	
	
	
	
	
	//excluir
	@Transactional
	public Response<String> excluir(Long id) {
		Response<String> response = new Response<String>();
		try {
			
			Recebimento rBanco = repo.findById(id).orElseThrow(
					()-> new Exception("não foi possível encontrar um recebimento com o id "+ id));
			CategoriaRecebimento cr = rBanco.getCategoriaRecebimento();
			cr.getTotalReceita().subtract(rBanco.getValor());
			cr = repoCatRec.save(cr);
			
			Lancamento lancamento = rBanco.getLancamento();
			
			lancamento.getRecebimentos().remove(rBanco); 
			
			 Double  totalRecebimentos = calcularRecebimentosExcluir(lancamento);
				
			 if (totalRecebimentos >= lancamento.getTotal()) {
				 lancamento.setPago(true);
			 } else {
				 lancamento.setPago(false);
			 }
			
			repo.deleteById(rBanco.getId());
			repoLanc.save(lancamento);
			response.setDados("ok");
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar excluir um recebimento: " + e.getMessage());
			return response;
		}
	}
	
	
	
	// recalcula o lançamento depois que exclui um recebimento
    private Double calcularRecebimentosExcluir(Lancamento lancamento) {
    	Double totalr = 0.00;
		for (Recebimento r: lancamento.getRecebimentos()) {
			totalr +=   r.getValor().doubleValue();
		}
		return totalr;
	}


	//get by id
	public Response<Recebimento> getById(Long id) {
		Response<Recebimento> response = new Response<Recebimento>();
		try {
			Recebimento rBanco = repo.findById(id).orElseThrow(
					()-> new Exception("não foi possível encontrar um recebimento com o id "+ id));
			response.setDados(rBanco);
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar consultar um recebimento por id: " + e.getMessage());
			return response;
		}
	}
	
	
	
	public Response<Double> getTotalRecebido(Long idlancamento) {
		Response<Double> response = new Response<Double>();
		try {
			Lancamento lancamento = repoLanc.findById(idlancamento)
					.orElseThrow(() -> new Exception("não foi possível encontrar um lancamento com o id "+ idlancamento));
			Double total = 0.00;
			for (Recebimento r: lancamento.getRecebimentos()) {
				total += r.getValor().doubleValue();
			}
			response.setDados(total);
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar consultar o total recebido: " + e.getMessage());
			return response;
		}
	}
	
	
	
	
	
	

}// fecha classe
