package eco.controller;

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

import eco.entidades.Fornecedor;
import eco.response.Response;
import eco.services.FornecedorService;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {
	
	@Autowired
	private FornecedorService service;
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_FORNECEDOR')")
	public ResponseEntity<Response<Fornecedor>> cadastrar(
			@Valid @RequestBody Fornecedor f
			){
		Response<Fornecedor> response = service.cadastrar(f);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	@PutMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_FORNECEDOR')")
	public ResponseEntity<Response<Fornecedor>> alterar(
			@Valid @RequestBody Fornecedor f
			){
		Response<Fornecedor> response = service.alterar(f);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_FORNECEDOR')")
	public ResponseEntity<Response<Page<Fornecedor>>> getAllParam(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="size", defaultValue="10") int size,
			@RequestParam(value="param", defaultValue="") String param
			) {
		Response<Page<Fornecedor>> response = service.getAllParam(page, size, param);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_FORNECEDOR')")
	public ResponseEntity<Response<Fornecedor>> getById(
			@PathVariable int id
			) {
		Response<Fornecedor> response = service.getById(id);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_FORNECEDOR_EXCLUI')")
	public ResponseEntity<Response<String>> delete(
         @PathVariable int id			
			) {
		Response<String> response = service.delete(id);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

}// fecha classe
