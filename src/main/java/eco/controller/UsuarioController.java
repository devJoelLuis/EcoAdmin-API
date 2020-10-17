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

import eco.entidades.Usuario;
import eco.response.Response;
import eco.services.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioService service;
	
	//cadastra
	@PostMapping
	public ResponseEntity<Response<Usuario>> cadastrar(
			@Valid @RequestBody Usuario u
			){
		Response<Usuario> response = service.cadastrar(u);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	//alterar
	@PutMapping
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Response<Usuario>> alterar(
			@Valid @RequestBody Usuario u
			){
		Response<Usuario> response = service.alterar(u);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	//alterar
		@PutMapping("/perfil")
		public ResponseEntity<Response<Usuario>> alterarPerfil(
				@Valid @RequestBody Usuario u
				){
			Response<Usuario> response = service.alterarPerfil(u);
			if (response.getErros().size() > 0) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		}
	
	//excluir
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Response<String>> excluir(
			@PathVariable Integer id
			){
		Response<String> response = service.excluir(id);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	//get by id
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Response<Usuario>> getById(
			@PathVariable Integer id
			){
		Response<Usuario> response = service.getById(id);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	//get by id
		@GetMapping("/email/{email}")
		public ResponseEntity<Response<Usuario>> getByEmail(
				@PathVariable String email
				){
			Response<Usuario> response = service.getByEmail(email);
			if (response.getErros().size() > 0) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		}
		
		
	
	//get all
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Response<List<Usuario>>> getByall(){
		Response<List<Usuario>> response = service.getAll();
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	

}// fecha classe
