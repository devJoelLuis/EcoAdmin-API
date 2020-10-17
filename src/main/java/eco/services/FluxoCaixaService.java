package eco.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import eco.classes.FluxoCaixa;
import eco.classes.FluxoCaixaSub;
import eco.entidades.CategoriaRecebimento;
import eco.entidades.Despesa;
import eco.entidades.Lancamento;
import eco.entidades.repositorios.CategoriaRecebimentoRepository;
import eco.entidades.repositorios.DespesaRepository;
import eco.entidades.repositorios.LancamentoRepository;
import eco.entidades.repositorios.RecebimentoRepository;
import eco.response.Response;




@Service
public class FluxoCaixaService {
	
	private CategoriaRecebimentoRepository repoCatRec;
	private RecebimentoRepository repoRec;
	private LancamentoRepository repoLanc;
	private DespesaRepository repoDesp;
	
	//construtor
	public FluxoCaixaService(CategoriaRecebimentoRepository repo, 
			RecebimentoRepository repoRec, LancamentoRepository repoLanc, DespesaRepository repoDesp) {
		this.repoCatRec = repo;
		this.repoRec = repoRec;
		this.repoLanc = repoLanc;
		this.repoDesp = repoDesp;
	}

	
	
	public Response<FluxoCaixa> getFluxoCaixa(String dtini, String dtfim) {
		Response<FluxoCaixa> response = new Response<>();
		try {
			
			LocalDate dtinicio = LocalDate.parse(dtini);
			LocalDate dtfinal = LocalDate.parse(dtfim);
			
			List<CategoriaRecebimento> listCatRec = repoCatRec.findAll();
			List<FluxoCaixaSub> fluxosSub = new ArrayList<FluxoCaixaSub>();
			for (CategoriaRecebimento cr: listCatRec) {
				if (cr.getDescricao().equals("SEM CATEGORIA")) continue;
				FluxoCaixaSub fcs = new FluxoCaixaSub();
				fcs.setRecebimentos(repoRec.findByCategoriaRecebimentoIdAndDataBetweenOrderByDataDesc(cr.getId(), dtinicio, dtfinal));
				fcs.setLancamentosTaxa(repoLanc.findByPagoAndDataLancamentoBetweenOrderByDataLancamentoDesc(false, dtinicio, dtfinal));
				fcs.setDespesas(repoDesp.findByCategoriaRecebimentoIdAndDataBetweenOrderByDataDesc(cr.getId(), dtinicio, dtfinal));
				fcs.setCategoria(cr.getDescricao());
				
				//buscar os lançamentos do tipo taxa no período
				List<Lancamento> lts = repoLanc.buscarLancamentosComTaxaRecebimentoBetween(cr.getId(), dtinicio, dtfinal);
				
				//criar despesas e adicionar
				for (Lancamento l: lts) {
					Despesa desp = new Despesa();
					desp.setData(l.getDataLancamento());
					desp.setDescricao(l.getDescricao());
					desp.setEdit(false);
					desp.setObs(l.getObs());
					desp.setOs(l.getOrdemServico());
					desp.setValor(new BigDecimal(l.getTotal()));
					fcs.getDespesas().add(desp);
				}
				
				
				fcs.setSaldo(cr.getTotalReceita().doubleValue());
				fluxosSub.add(fcs);
			}
			
			FluxoCaixa fc = new FluxoCaixa();
			fc.setFluxosSub(fluxosSub);
			fc.setLancamentos(repoLanc.findByIdCategoriaRecebimentoTaxaAndDataLancamentoBetweenOrderByDataLancamentoDesc(0, dtinicio, dtfinal));
			
			response.setDados(fc);
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar gerar o fluxo de caixa: "+ e.getMessage());
			return response;
		}
	}
	
	
	
	
	

}//fecha classe
