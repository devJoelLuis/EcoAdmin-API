package eco.services;

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

		
		
		
	
		
		
		

}//fecha classe
