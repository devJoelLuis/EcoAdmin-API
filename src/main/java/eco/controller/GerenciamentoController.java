package eco.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eco.entidades.Gerenciamento;
import eco.response.Response;
import eco.services.GerenciamentoService;

@RestController
@RequestMapping("/gerenciamentos")
public class GerenciamentoController {
	
	@Autowired
	private GerenciamentoService service;
	
	
	// cadastrar
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_GERENCIAMENTO')")
	public ResponseEntity<Response<Gerenciamento>> cadastrar(
			@RequestBody Gerenciamento g
			){
		Response<Gerenciamento> response = service.cadastrar(g);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	// alterar
	@PutMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_GERENCIAMENTO')")
	public ResponseEntity<Response<Gerenciamento>> editar(
			@RequestBody Gerenciamento g
			){
		Response<Gerenciamento> response = service.alterar(g);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	// excluir
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_GERENCIAMENTO_EXCLUI')")
	public ResponseEntity<Response<String>> excluir(
			@PathVariable Long id
			){
		Response<String> response = service.excluir(id);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	// get by id
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_GERENCIAMENTO')")
	public ResponseEntity<Response<Gerenciamento>> buscarPorId(
			@PathVariable Long id
			){
		Response<Gerenciamento> response = service.getById(id);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	

	
	// get all pageable
	@GetMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_GERENCIAMENTO')")
	public ResponseEntity<Response<Page<Gerenciamento>>> buscarTodos(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="size", defaultValue="10") int size,
			@RequestParam(value="idcliente", defaultValue="0") int idcliente
			){
		if(size > 40) size = 40;
		Response<Page<Gerenciamento>> response = service.getAllPageableIdCliente(page, size, idcliente);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	
	

}// fecha classe
