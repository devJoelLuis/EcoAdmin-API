package eco.controller;

import java.util.List;

import javax.validation.Valid;

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

import eco.entidades.StatusOs;
import eco.response.Response;
import eco.services.StatusOsService;

@RestController
@RequestMapping("/statusOs")
public class StatusOsController {
	
	@Autowired
	private StatusOsService service;
	
	// cadastrar novo estatus
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STATUS_OS')")
	public ResponseEntity<Response<StatusOs>> cadastrar(
		@Valid	@RequestBody StatusOs st
			) {
		Response<StatusOs> response = service.cadastrarNovo(st);
		if(response.getErros().size() > 0) {
		  return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}// fecha cadastrar
	
	
	
	
	// editar estatos
	@PutMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STATUS_OS')")
	public ResponseEntity<Response<StatusOs>> editarStatusOs(
		@Valid	@RequestBody StatusOs st
			) {
		Response<StatusOs> response = service.editar(st);
		if(response.getErros().size() > 0) {
		  return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}// fecha editar estatos
	
	
	
	// excluir estatos
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STATUS_OS_EXCLUI')")
	public ResponseEntity<Response<String>> excluirStatusOs(
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
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STATUS_OS', 'ROLE_OS')")
	public ResponseEntity<Response<StatusOs>> buscarPorId(
			@PathVariable int id
			) {
		Response<StatusOs> response = service.buscarPorId(id);
		if(response.getErros().size() > 0) {
		  return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}// fecha buscar por id
	
	
	
	// buscar todos os estatos
	@GetMapping("/nome")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STATUS_OS', 'ROLE_OS')")
	public ResponseEntity<Response<Page<StatusOs>>> listarTodos(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="size", defaultValue="10") int size,
			@RequestParam(value="nome", defaultValue="") String nome
			) {
		Response<Page<StatusOs>> response = service.listarTodos(page, size, nome);
		if(response.getErros().size() > 0) {
		  return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}// fecha buscar all por nome
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STATUS_OS', 'ROLE_OS')")
	public ResponseEntity<Response<List<StatusOs>>> getAll() {
		Response<List<StatusOs>> response = service.listarTodosSemPageable();
		if(response.getErros().size() > 0) {
			  return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
	}
	
	

}// fecha classe
