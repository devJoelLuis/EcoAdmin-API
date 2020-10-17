package eco.services;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eco.entidades.Gerenciamento;
import eco.entidades.Parcela;
import eco.entidades.repositorios.GerenciamentoRepository;
import eco.entidades.repositorios.ParcelaRepositoy;
import eco.response.Response;

@Service
public class ParcelaService {
	
	@Autowired
	private ParcelaRepositoy repo;
	
	
	@Autowired
	private GerenciamentoRepository repoGer;
	
	// cadastro para controller
	public Response<Parcela> cadastrar(Parcela p) {
		Response<Parcela> response = new Response<>();
		try {
			validar(p);
			p.setId(null);
			Gerenciamento g = repoGer.findById(p.getGerenciamento().getId()).get();
			g.setTotalParcela(g.getTotalParcela() + 1);
			Gerenciamento gSalvo = repoGer.save(g);
			p.setGerenciamento(gSalvo);
			p.setNumeroParcela(gSalvo.getTotalParcela());
			response.setDados(repo.save(p));
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar cadastrar uma taxa: "+ e.getMessage());
			return response;
		}
	}
	
	private void validar(Parcela p) throws Exception {
		
		if(p.getDataVencimento() == null) {
			throw new Exception("a data de vencimento está nula");
		}
		if(p.getGerenciamento() == null) {
			throw new Exception("o gerenciamento está nulo");
		}
		if(p.getValor() == null || p.getValor() == 0) {
			throw new Exception("valor não informado");
		}
		
	}

	// utilização interna no back-End
	public Parcela cadastroIN(Parcela p) {
		return repo.save(p);
	}

	//alteracao
	public Response<Parcela> alterar(Parcela p) {
		Response<Parcela> response = new Response<>();
		try {
			Optional<Parcela> parcelaOp = repo.findById(p.getId());
			if(!parcelaOp.isPresent()) {
				throw new Exception("não foi possível encontrar uma parcela com o id "+ p.getId());
			}
			Parcela pBanco = parcelaOp.get();
			pBanco.alterar(p);
			response.setDados(repo.save(pBanco));
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar alterar uma taxa: "+ e.getMessage());
			return response;
		}
	}

	// excluir
	public Response<String> excluir(Long id) {
		Response<String> response = new Response<>();
		try {
			Optional<Parcela> parcelaOp = repo.findById(id);
			if(!parcelaOp.isPresent()) {
				throw new Exception("não foi possível encontrar uma parcela com o id "+ id);
			}
			Parcela pBanco = parcelaOp.get();
			repo.delete(pBanco);
			response.setDados("ok");
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar excluir uma taxa: "+ e.getMessage());
			return response;
		}
	}

	// consultar por id
	public Response<Parcela> getById(Long id) {
		Response<Parcela> response = new Response<>();
		try {
			Optional<Parcela> parcelaOp = repo.findById(id);
			if(!parcelaOp.isPresent()) {
				throw new Exception("não foi possível encontrar uma parcela com o id "+ id);
			}
			response.setDados(parcelaOp.get());
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar consultar uma taxa por id: "+ e.getMessage());
			return response;
		}
	}
	
	//buscar todos por pageable e id do gerenciamento
	public Response<Page<Parcela>> getAllByGerenciamentoId(int page, int size, Long idg) {
		Response<Page<Parcela>> response = new Response<>();
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<Parcela> parcelas = repo.findByGerenciamentoId(pageable, idg);
			response.setDados(parcelas);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro no metodo getAllByGerenciamentoId: "+ e.getMessage());
			return response;
		}
	}

	public Response<Parcela> alterarPagoDevedor(Long id, int pago, String dtPagamento) {
		Response<Parcela> response = new Response<>();
		try {
			if(id == null) {
				throw new Exception("o id da parcela está nulo");
			}
			Optional<Parcela> pOp = repo.findById(id);
			if(!pOp.isPresent()) {
				throw new Exception("não foi possível encontrar uma parcela com o id: "+ id);
			}
			LocalDate dtPag = LocalDate.parse(dtPagamento);
			Parcela p = pOp.get();
			p.setPago(pago);
			p.setDataPagamento(dtPag);
			Parcela pSalvo = repo.save(p);
			response.setDados(pSalvo);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar alterar pago ou devedor: "+ e.getMessage());
			return response;
		}
	}
	
	

}// fecha classe
