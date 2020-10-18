package eco.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eco.classes.TransferenciaSaldo;
import eco.entidades.CategoriaRecebimento;
import eco.entidades.HistoricoCatRecebimento;
import eco.entidades.Usuario;
import eco.entidades.repositorios.CategoriaRecebimentoRepository;
import eco.entidades.repositorios.HistoricoCatRecebimentoRepository;
import eco.entidades.repositorios.RecebimentoRepository;
import eco.entidades.repositorios.UsuarioRepository;
import eco.response.Response;

@Service
public class CategoriaRecebimentoService {
	
	@Autowired
	private CategoriaRecebimentoRepository repo;
	
	@Autowired
	private RecebimentoRepository repoRec;
	
	@Autowired
	private HistoricoCatRecebimentoRepository repoHistCat;
	
	@Autowired
	private UsuarioRepository repoUser;
	
	
	
	//cadastrar
	@Transactional
	public Response<CategoriaRecebimento> cadastrar(CategoriaRecebimento cr) {
		Response<CategoriaRecebimento> response = new Response<CategoriaRecebimento>();
		try {
			if (repo.existsByDescricao(cr.getDescricao())) {
				throw new Exception("já existe uma categoria com a descrição: "+ cr.getDescricao().toUpperCase());
			}
			String nomeUser = getUsuarioAuthenticado();
			CategoriaRecebimento crBanco = repo.save(cr);
			HistoricoCatRecebimento hcr = new HistoricoCatRecebimento();
			hcr.setCategoriaRecebimento(crBanco);
			hcr.setDataHora(LocalDateTime.now());
			hcr.setDescricao("Cadastrada nova categoria: "+ cr.getDescricao());
			hcr.setUserNome(nomeUser);
			hcr.setValorAnterior(0D);
			hcr.setValorModificado(cr.getTotalReceita().doubleValue());
			repoHistCat.save(hcr);
			response.setDados(crBanco);
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar cadastrar uma categoria de recebimento: "+ e.getMessage());
			return response;
		}
	}
	
	//alterar
	@Transactional
	public Response<CategoriaRecebimento> alterar(CategoriaRecebimento cr) {
		Response<CategoriaRecebimento> response = new Response<CategoriaRecebimento>();
		try {
			
			Integer idcatRec = cr.getId();
			CategoriaRecebimento crBanco = repo.findById(idcatRec)
					.orElseThrow(()-> new Exception("não foi possível encontrar uma categoria de recebimento com o id: "+ idcatRec));
			
			if (repo.existsByDescricaoAndIdNot(cr.getDescricao(), idcatRec)) {
				throw new Exception("já existe uma categoria com a descrição: "+ cr.getDescricao().toUpperCase());
			}
			
			String nomeUser = getUsuarioAuthenticado();
			HistoricoCatRecebimento hcr = new HistoricoCatRecebimento();
			hcr.setCategoriaRecebimento(crBanco);
			hcr.setDataHora(LocalDateTime.now());
			
			double saldoAtual = crBanco.getTotalReceita().doubleValue();
			double saldoNovo = cr.getTotalReceita().doubleValue();
			
			
			if (saldoAtual == saldoNovo) {
				hcr.setDescricao("Alterado categoria: "+ crBanco.getDescricao());
			} else if (saldoAtual > saldoNovo) {
				double diferenca =  saldoNovo - saldoAtual;
				hcr.setDescricao(String.format("DEBITADO: R$ %.2f", diferenca) );
			} else if (saldoAtual < saldoNovo) {
				double diferenca =  saldoNovo - saldoAtual;
				hcr.setDescricao(String.format("CREDITADO: R$ %.2f", diferenca) );
			}
			hcr.setUserNome(nomeUser);
			hcr.setValorAnterior(saldoAtual);
			hcr.setValorModificado(cr.getTotalReceita().doubleValue());
			repoHistCat.save(hcr);
			
			
			BeanUtils.copyProperties(cr, crBanco, "id");
			response.setDados(repo.save(crBanco));
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar alterar uma categoria de recebimento: "+ e.getMessage());
			return response;
		}
	}
	
	
	
	
	//excluir
	@Transactional
	public Response<String> excluir(Integer id) {
		Response<String> response = new Response<>();
		try {
			
			CategoriaRecebimento crBanco = repo.findById(id)
					.orElseThrow(()-> new Exception("não foi possível encontrar uma categoria de recebimento com o id: "+ id));
			if (repoRec.existsByCategoriaRecebimentoId(id)) {
				throw new Exception("A categoria "+ crBanco.getDescricao()+" não pode ser excluída porque existe recebimentos vinculados a ela.");
			}
			
			List<HistoricoCatRecebimento> hs = repoHistCat.findByCategoriaRecebimentoId(id);
			repoHistCat.deleteAll(hs);
	
			repo.delete(crBanco);
			response.setDados("ok");
			return response;
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar excluir uma categoria de recebimento: "+ e.getMessage());
			return response;
		}
	}
	
	
	//get by id
		public Response<CategoriaRecebimento> getById(Integer id) {
			Response<CategoriaRecebimento> response = new Response<>();
			try {
				
				CategoriaRecebimento crBanco = repo.findById(id)
						.orElseThrow(()-> new Exception("não foi possível encontrar uma categoria de recebimento com o id: "+ id));
		
				
				response.setDados(crBanco);
				return response;
			} catch (Exception e) {
				response.getErros().add("Ocorreu o seguinte erro ao tentar consultar uma categoria de recebimento: "+ e.getMessage());
				return response;
			}
		}
	
	//get all
		public Response<List<CategoriaRecebimento>> getAll() {
			Response<List<CategoriaRecebimento>> response = new Response<>();
			try {
				
					
				response.setDados(repo.findAllByOrderByIdAsc());
				return response;
			} catch (Exception e) {
				response.getErros().add("Ocorreu o seguinte erro ao tentar consultar uma categoria de recebimento: "+ e.getMessage());
				return response;
			}
		}
		
		
		
		
		//busca todo historico paginado de uma categoria de recebimanto
		public Response<Page<HistoricoCatRecebimento>> getAllHistorico(int page, int size, Integer id) {
			Response<Page<HistoricoCatRecebimento>> response = new Response<>();
			try {
				
				Pageable pageable = PageRequest.of(page, size);
				
				Page<HistoricoCatRecebimento> pagesHist = repoHistCat.findByCategoriaRecebimentoIdOrderByDataHoraAsc(id, pageable);
				
				response.setDados(pagesHist);
				return response;
				
			} catch (Exception e) {
				response.getErros().add("Ocorreu o seguinte erro ao tentar carregar o histórico da categoria: "+ e.getMessage());
				return response;
			}
		}
		
		
		
		
		
		//buscar o usuário logado
		private String getUsuarioAuthenticado() throws Exception {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = "";
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
			    username = authentication.getName();
			} 
			if (username == null || username.equals("")) {
		     	throw new Exception("não foi possível encontrar o e-mail do usuário logado");
			}
			Usuario user = repoUser.findByEmail(username);
			if (user == null) {
				throw new Exception("não foi possível encontrar o usuário logado");
			}
			return user.getNome();
		}
		
		
		
		
        // transferencia de saldo  
		@Transactional
		public Response<String> transferencia(TransferenciaSaldo ts) {
			Response<String> response = new Response<String>();
			try {
				
				if (ts.getIdCatRecebProvedor() == ts.getIdCatRecebReceptor()) {
					throw new Exception("não foi possível transferir porque as categorias são iguais.");
				}
				
				
				
				/*
				 *  OBS: (O nome da entidade de CategoriaRecebimento não é um nome adequado, porém o cliente insistiu que seja assim
				 *  para seu próprio entendimento)
				 * 
				 *  LÓGICA
				 *  1- buscar categoria provedora
				 *  2- buscar categoria receptora
				 *  3- verificar se a categoria receptora contem saldo suficiente para transferencia
				 *  4- creditar o saldo na categoria receptora
				 *  5- adicionar o novo saldo a categoria provedora
				 *  6- salvar categorias de recebimentos e retornar ok para o controller
				 */
				
				// 1
				CategoriaRecebimento ctrProvedora = repo.findById(ts.getIdCatRecebProvedor())
						                             .orElseThrow(() -> new Exception(
						                            		 String.format("não possível encontrar uma categoria com o id %s.", ts.getIdCatRecebProvedor())));
				// 2
				CategoriaRecebimento ctrReceptora = repo.findById(ts.getIdCatRecebReceptor())
                                                     .orElseThrow(() -> new Exception(
                       		                                 String.format("não possível encontrar uma categoria com o id %s.", ts.getIdCatRecebReceptor())));
				
				Double saldoAtualProvedora = ctrProvedora.getTotalReceita().doubleValue();
				Double novoSaldoProvedora = saldoAtualProvedora - ts.getValor();
				
				// 3
				if (novoSaldoProvedora < 0) {
					throw new Exception("saldo insuficiente para transferência.");
				}
				
				// 4
				Double saldoAtualCatReceptora = ctrReceptora.getTotalReceita().doubleValue();
				Double novoSaldoCatReceptora = saldoAtualCatReceptora + ts.getValor();
				ctrReceptora.setTotalReceita(new BigDecimal(novoSaldoCatReceptora));
				
				// 5 
				ctrProvedora.setTotalReceita(new BigDecimal(novoSaldoProvedora));
				
				
				
				// 6
				repo.save(ctrProvedora);
				repo.save(ctrReceptora);
				response.setDados("ok");
				return response;
				
				
			} catch (Exception e) {
				response.getErros().add("Ocorreu o seguinte erro ao tentar fazer a transferncia: "+ e.getMessage());
				return response;
			}
		}

		
		
		
	
		
		
		

}//fecha classe
