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

import eco.entidades.StatusCli;
import eco.response.Response;
import eco.services.StatusCliService;

@RestController
@RequestMapping("/status")
public class StatusCliController {
	
	@Autowired
	private StatusCliService service;
	
	// cadastrar novo estatus
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STATUS_CLIENTE')")
	public ResponseEntity<Response<StatusCli>> cadastrar(
			@RequestBody StatusCli st
			) {
		Response<StatusCli> response = service.cadastrarNovo(st);
		if(response.getErros().size() > 0) {
		  return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}// fecha cadastrar
	
	
	
	
	// editar estatos
	@PutMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STATUS_CLIENTE')")
	public ResponseEntity<Response<StatusCli>> editarEstatus(
			@RequestBody StatusCli st
			) {
		Response<StatusCli> response = service.editar(st);
		if(response.getErros().size() > 0) {
		  return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}// fecha editar estatos
	
	
	
	// excluir estatos
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STATUS_CLIENTE_EXCLUI')")
	public ResponseEntity<Response<String>> excluirEstatus(
			@PathVariable int id
			) {
		Response<String> response = service.excluir(id);
		if(response.getErros().size() > 0) {
		  return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}// fecha excluir estatos
	
	
	// buscar estatos por id
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STATUS_CLIENTE', 'ROLE_CLIENTE')")
	public ResponseEntity<Response<StatusCli>> buscarPorId(
			@PathVariable int id
			) {
		Response<StatusCli> response = service.buscarPorId(id);
		if(response.getErros().size() > 0) {
		  return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}// fecha buscar por id
	
	
	
	// buscar todos os estatos
	@GetMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STATUS_CLIENTE', 'ROLE_CLIENTE')")
	public ResponseEntity<Response<Page<StatusCli>>> listarTodos(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="size", defaultValue="10") int size,
			@RequestParam(value="nome", defaultValue="") String nome
			) {
		Response<Page<StatusCli>> response = service.listarTodos(page, size, nome);
		if(response.getErros().size() > 0) {
		  return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}// fecha buscar por id
	
	

}// fecha classe
