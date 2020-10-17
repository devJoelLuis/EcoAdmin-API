package eco.controller;

import java.util.List;

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

import eco.entidades.Senha;
import eco.response.Response;
import eco.services.SenhaService;

@RestController
@RequestMapping("/senhas")
public class SenhaController {
  
	@Autowired
	private SenhaService service;
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SENHA_EXCLUI')")
	public ResponseEntity<Response<String>> excluir(
	  @PathVariable int id		
			){
		Response<String> response = service.excluir(id);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SENHA')")
	public ResponseEntity<Response<Senha>> cadastrar(
	 @RequestBody Senha senha		
			){
		Response<Senha> response = service.cadastrar(senha);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	@PutMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SENHA')")
	public ResponseEntity<Response<Senha>> alterar(
	 @RequestBody Senha senha		
			){
		Response<Senha> response = service.alterar(senha);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/cliente/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SENHA')")
	public ResponseEntity<Response<List<Senha>>> findAllClienteId(
			@PathVariable int id
			) {
		Response<List<Senha>> response = service.findAllClienteId(id);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	
}// fecha classe
