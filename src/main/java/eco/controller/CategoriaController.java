package eco.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eco.entidades.Categoria;
import eco.response.Response;
import eco.services.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
	
	@Autowired
	private CategoriaService service;
	
	
	
	// cadastrar 
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CATEGORIA')")
	public ResponseEntity<Response<Categoria>> cadastrar(
		@Valid	@RequestBody Categoria categoria
			) {
		Response<Categoria> response = service.cadastrar(categoria);
		if(response.getErros().size() > 0) {
		  return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}// fecha cadastrar
	
	
	
	
	// editar 
	@PutMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CATEGORIA')")
	public ResponseEntity<Response<Categoria>> editarEstatus(
		@Valid	@RequestBody Categoria categoria
			) {
		Response<Categoria> response = service.alterar(categoria);
		if(response.getErros().size() > 0) {
		  return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}// fecha editar 
	
	
	
	// excluir 
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CATEGORIA_EXCLUI')")
	public ResponseEntity<Response<String>> excluirEstatus(
			@PathVariable int id
			) {
		Response<String> response = service.excluir(id);
		if(response.getErros().size() > 0) {
		  return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}// fecha excluir 
	
	
	// buscar  por id
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CATEGORIA', 'ROLE_LANCAMENTO')")
	public ResponseEntity<Response<Categoria>> buscarPorId(
			@PathVariable int id
			) {
		Response<Categoria> response = service.getId(id);
		if(response.getErros().size() > 0) {
		  return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}// fecha buscar por id
	
	
	
	// buscar todos
	@GetMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CATEGORIA', 'ROLE_LANCAMENTO')")
	public ResponseEntity<Response<List<Categoria>>> getAll() {
		Response<List<Categoria>> response = service.getAll();
		if(response.getErros().size() > 0) {
		  return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}// fecha get all
	
	
	
	

}// fecha classe
