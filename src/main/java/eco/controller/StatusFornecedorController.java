package eco.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eco.entidades.StatusFornecedor;
import eco.response.Response;
import eco.services.StatusFornecedorService;

@RestController
@RequestMapping("/statusFornecedor")
public class StatusFornecedorController {
	
	
	@Autowired
	private StatusFornecedorService service;
	
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STATUS_FORNECEDOR_EXCLUI')")
	public ResponseEntity<Response<String>> delele(
			@PathVariable int id) {
		Response<String> response = service.excluir(id);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}
	
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STATUS_FORNECEDOR')")
	public ResponseEntity<Response<StatusFornecedor>> cadastrar(
	   @Valid		@RequestBody StatusFornecedor stf) {
		Response<StatusFornecedor> response = service.cadastrar(stf);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}
	
	

}// fecha classe
