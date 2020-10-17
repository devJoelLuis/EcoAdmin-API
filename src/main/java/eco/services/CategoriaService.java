package eco.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eco.entidades.Categoria;
import eco.entidades.repositorios.CategoriaRepository;
import eco.entidades.repositorios.LancamentoRepository;
import eco.response.Response;

@Service
public class CategoriaService {

	
	@Autowired
	private CategoriaRepository repo;
	
	@Autowired
	private LancamentoRepository repoLanc;
	
	
	
	//cadastrar
	public Response<Categoria> cadastrar(Categoria c) {
		Response<Categoria> response = new Response<>();
		try {
			if (c == null) {
				throw new Exception("a categoria para cadastro está nula");
			}
			if (c.getDescricao() == null || c.getDescricao().length() < 2) {
				throw new Exception("a descrição da categoria deve conter no mínimo três dígitos!!!");
			}
			
			
			
			//verificar se já existe uma categoria com a mesma descrição
			if (repo.existsByDescricao(c.getDescricao())) {
				throw new Exception("já existe uma categoria com a descrição "+ c.getDescricao());
			}
			c.setDescricao(c.getDescricao().toUpperCase());
			response.setDados(repo.save(c));
			return response;
			
		} catch (Exception e) {
			
				response.getErros().add("Erro: ao tentar cadastrar uma categoria: "+ e.getMessage());	
			
			
			return response;
		}
	}
	
	
	
	
	//alterar
	public Response<Categoria> alterar(Categoria c) {
		Response<Categoria> response = new Response<>();
		try {
			if (c == null) {
				throw new Exception("a categoria para cadastro está nula");
			}
			if (c.getDescricao() == null || c.getDescricao().length() < 2) {
				throw new Exception("a descrição da categoria deve conter no mínimo três dígitos!!!");
			}
			Optional<Categoria> catOp = repo.findById(c.getId());
			if (!catOp.isPresent()) {
				throw new Exception("não foi possível encontrar uma categoria com o id "+ c.getId());
			}
			
	      
			
			
			//verificar se já existe uma categoria com a mesma descrição
			if (repo.existsByDescricaoAndIdNot(c.getDescricao(), c.getId())) {
				throw new Exception("já existe uma categoria com a descrição "+ c.getDescricao());
			}
			
			
			Categoria catBanco = catOp.get();
			catBanco.setDescricao(c.getDescricao().toUpperCase());
			response.setDados(repo.save(c));
			return response;
			
		} catch (Exception e) {
			if (e.getMessage().contains("descricao_UNIQUE")) {
				response.getErros().add("Erro: já existe uma categoria com a descrição informada!!!");
			} else {
				response.getErros().add("Erro: ao tentar cadastrar uma categoria: "+ e.getMessage());	
			}
			return response;
		}
	}
	
	
	//excluir
	public Response<String> excluir(int id) {
		Response<String> response = new Response<>();
		try {

			// verificar se existe lancamentos vinculados com a categoria
			Long count = repoLanc.countByCategoriaId(id);
			if (count > 0) {
				throw new Exception("a categoria não pode ser excluída porque possui lançamentos vinculados a ela!!!");
			}
			Optional<Categoria> catOp = repo.findById(id);
			if (!catOp.isPresent()) {
				throw new Exception("não foi possível encontrar uma categoria com o id "+ id);
			}
			Categoria catBanco = catOp.get();
			repo.delete(catBanco);
			response.setDados("ok");
			return response;
			
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar excluir uma categoria: "+ e.getMessage());
			return response;
		}
	}
	
	
	
	
	
	//get id
	public Response<Categoria> getId(int id) {
		Response<Categoria> response = new Response<>();
		try {
			
			Optional<Categoria> catOp = repo.findById(id);
			if (!catOp.isPresent()) {
				throw new Exception("não foi possível encontrar uma categoria com o id "+ id);
			}
			Categoria catBanco = catOp.get();
			response.setDados(catBanco);
			return response;
			
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar buscar uma categoria por id: "+ e.getMessage());
			return response;
		}
	}
	
	
	
	
	
	//get all
	public Response<List<Categoria>> getAll() {
		Response<List<Categoria>> response = new Response<>();
		try {
			
			List<Categoria> categorias = repo.findAllByOrderByDescricaoAsc();
			response.setDados(categorias);
			return response;
			
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar todas as categorias: "+ e.getMessage());
			return response;
		}
	}
	
	
	
	
	
	
	
}//fecha classe
