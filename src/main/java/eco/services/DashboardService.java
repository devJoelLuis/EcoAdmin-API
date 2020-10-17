package eco.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eco.entidades.Dashboard;
import eco.entidades.repositorios.ClienteRepository;
import eco.entidades.repositorios.LicencasRepository;
import eco.entidades.repositorios.OrcamentoRepository;
import eco.entidades.repositorios.OrdemServicoRepository;
import eco.entidades.repositorios.PrazoRepository;
import eco.response.Response;

@Service
public class DashboardService {
	
	@Autowired
	private ClienteRepository repoCli;
	
	@Autowired
	private OrdemServicoRepository repoOs;
	
	
	
	
	@Autowired
	private LicencasRepository repoLi;
	
	@Autowired
	private OrcamentoRepository repoOrc;
	
	@Autowired
	private PrazoRepository repoPra;
	
	
	private final int DIAS_VENCER_LICENCA = 160; 
	
	
	
	public Response<Dashboard> gerarDash() {
		Response<Dashboard> response = new Response<>();
		//pegar ultimo dia do mes
//		LocalDate ultimoDiaMes = LocalDate.now()
//								.withMonth(LocalDate.now()
//				                .getMonthValue())
//								.with(TemporalAdjusters.lastDayOfMonth());
		
		
	   try {
			Dashboard db = new Dashboard();

			db.settApagar(0.0); // TODO: adiconar rotina total a pagar
			db.setTclientes(getTotalCliente());
			db.setTos(getTotalOrdemServico());
			
			db.setTprazosVencer(getPrazosAVencer());
			db.setTprazosAlerta(getPrazosAlerta());
			db.setTprazosVencidos(getPrazosVencidos());
			
			db.settOsAlerta(getTAlertaOs());
			db.settOrcamentosAlerta(getOrcamentoAlerta());
			db.settOrcamento(getOrcamentos());
			
			db.settLicencasAlerta(getTotalLicencaAlerta());
			db.setLicencasVencidas(getLicencasVencidas());
			db.settLicencas(getTotalLicencas());
			db.setLicencasVaiVencer(getLicencasVaiVencer());
		
		
			response.setDados(db);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao gerar o dahsboard: " + e.getMessage());
			return response;
		}
	}
	
	
	
	





	private int getPrazosVencidos() {
		Long count = repoPra.countByCumpridoAndDataVencimentoLessThan(false, LocalDate.now());
		return count.intValue();
	}









	private int getPrazosAlerta() {
		Long count = repoPra.countByCumpridoAndAlertaAndDataAlertaLessThanEqual(false, true, LocalDate.now());
		return count.intValue();
	}









	private int getPrazosAVencer() {
		Long count = repoPra.countByCumpridoAndDataVencimentoBetween(false, LocalDate.now(), LocalDate.now().plusDays(6));
		return count.intValue();
	}












	private int getLicencasVencidas() throws Exception {
		Long total = 0L;
		total = repoLi.countByArquivoMortoAndDataVencimentoLessThanEqual(false, LocalDate.now());
		return total.intValue();
	}









	private int getLicencasVaiVencer() throws Exception {
		Long total = 0L;
		total = repoLi.countByArquivoMortoAndDataVencimentoBetween(false, LocalDate.now(), LocalDate.now().plusDays(DIAS_VENCER_LICENCA));
		return total.intValue();
	}









	private int getTotalLicencas() throws Exception {
		Long total = 0L;
		total = repoLi.countByArquivoMorto(false);
		return total.intValue();
	}









	private int getOrcamentos() throws Exception {
		Long total = 0L;
		total = repoOrc.count();
		return total.intValue();
	}









	private int getOrcamentoAlerta() throws Exception {
		Long total = 0L;
		total= repoOrc.countByAlertaAndDataAlertaLessThanEqual(1, LocalDate.now());
		return total.intValue();
	}









	private int getTotalLicencaAlerta() throws Exception {
		Long total = 0L;
		total = repoLi.countByArquivoMortoAndAlertaAndDataAlertaLessThanEqual(false, 1, LocalDate.now());
		return total.intValue();
	}









	private int getTAlertaOs() throws Exception {
		Long total = 0L;
		total = repoOs.countByAlertaAndDataAlertaLessThanEqual(1, LocalDate.now());
		return total.intValue();
	}









	// retornar o total de cliente cadastrado no banco de dados
	private int getTotalCliente() throws Exception {
		try {
			Long tcli = repoCli.count();
			return tcli.intValue();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return 0;
		}
	} //fecha metodo total cliente
	
	
	
	
	
	
	// retorna o total de O.S. cadastradas no banco de dados
	private int getTotalOrdemServico() throws Exception {
		try {
			Long tos = repoOs.count();
			return tos.intValue();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return 0;
		}
	}
	
	
	
	// busca a lista de servico a receber vencidos
//	public Response<List<Servico>> getServicosAReceber(String dtinicio, String dtfim) {
//		Response<List<Servico>> response = new Response<>();
//		try {
//			LocalDate dtini = LocalDate.parse(dtinicio);
//			LocalDate dtf = LocalDate.parse(dtfim);
//			List<Servico> servicos = repoSe.findByPagoAndDataVencimentoBetweenOrderByDataVencimentoAsc(0, dtini, dtf);
//			response.setDados(servicos);
//			return response;
//		} catch (Exception e) {
//			response.getErros().add("Erro ao tentar consultar serviços a receber por data: "+e.getMessage());
//			return response;
//		}
//		
//	}
	
	
	// busca a lista de servico a perto de vencer
//		private List<Servico> getServicosPagamentoVencendo(int dias) {
//			
//			try {
//				List<Servico> servicos = repoSe.findByPagoAndDataVencimentoBetween(0, LocalDate.now(), LocalDate.now().plusDays(dias));
//				return servicos;
//			} catch (Exception e) {
//				System.out.println(e.getMessage());
//				return null;
//			}
//			
//		}
		
		
		
	  // buscas todos os serviços em alerta 
//		private List<Servico> getServAlerta() {
//			try {
//				List<Servico> servicos = repoSe.findByDataAlertaBetween(LocalDate.now().minusYears(10), LocalDate.now());
//		        return servicos;				
//			} catch (Exception e) {
//				System.out.println(e.getMessage());
//				return null;
//			}
//		}
		
		
	 // buscar todos as ordens de servico em alerta	
//		private List<OrdemServico> getOsAlerta() {
//			try {
//				List<OrdemServico> oss = repoOs.findByAlerta(1);
//		        return oss;				
//			} catch (Exception e) {
//				System.out.println(e.getMessage());
//				return null;
//			}
//		}
		
	
	
	

}//fecha classe
