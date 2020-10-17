package eco.controller;

import java.util.List;

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

import eco.dto.ClienteDTO;
import eco.entidades.Cliente;
import eco.response.Response;
import eco.services.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
	
	

	@Autowired
	private ClienteService service;
	
	
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CLIENTE')")
	public ResponseEntity<Response<Cliente>> cadastrar(
			@RequestBody Cliente c
			){
		Response<Cliente> response = service.cadastrar(c);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	//buscar por id
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CLIENTE')")
	public ResponseEntity<Response<Cliente>> buscarPorId(@PathVariable int id) {
		Response<Cliente> response = service.buscarPorId(id);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	//buscar todos
	@GetMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CLIENTE')")
	public ResponseEntity<Response<Page<Cliente>>> buscarTodos(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="size", defaultValue="10") int size,
			@RequestParam(value="order", defaultValue="nome") String order
			) {
		if(size > 40) size = 40;
		Response<Page<Cliente>> response = service.buscarTodos(page, size, order);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	//buscar todos parte do nome
		@GetMapping("/nome")
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CLIENTE')")
		public ResponseEntity<Response<Page<Cliente>>> buscarTodosParteNome(
				@RequestParam(value="page", defaultValue="0") int page,
				@RequestParam(value="size", defaultValue="10") int size,
				@RequestParam(value="order", defaultValue="nome") String order,
				@RequestParam(value="nome", defaultValue="") String nome
				) {
			if(size > 40) size = 40;
			Response<Page<Cliente>> response = service.buscarTodosParteNome(page, size, order, nome);
			if(response.getErros().size() > 0) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		}
		
		
		//buscar todos parte do nome fantasia
				@GetMapping("/nomeFantasia")
				@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CLIENTE')")
				public ResponseEntity<Response<Page<Cliente>>> buscarTodosParteNomeFantasia(
						@RequestParam(value="page", defaultValue="0") int page,
						@RequestParam(value="size", defaultValue="10") int size,
						@RequestParam(value="order", defaultValue="nome") String order,
						@RequestParam(value="nome", defaultValue="") String nome
						) {
					if(size > 40) size = 40;
					Response<Page<Cliente>> response = service.buscarTodosParteNomeFantasia(page, size, order, nome);
					if(response.getErros().size() > 0) {
						return ResponseEntity.badRequest().body(response);
					}
					return ResponseEntity.ok(response);
				}
				
				
				//buscar todos parte do nome ou nome fantasia, retornando lista dto
				@GetMapping("/clientedto")
				@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CLIENTE')")
				public ResponseEntity<Response<List<ClienteDTO>>> getClienteDtoParam(
						@RequestParam(value="param", defaultValue="") String param
						) {
					Response<List<ClienteDTO>> response = service.getClientesDtoParam(param);
					if(response.getErros().size() > 0) {
						return ResponseEntity.badRequest().body(response);
					}
					return ResponseEntity.ok(response);
				}
				
		
		
	
	//alterar cliente
	@PutMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CLIENTE')")
	public ResponseEntity<Response<Cliente>> alterar(
			@RequestBody Cliente c
			){
		Response<Cliente> response = service.Alterar(c);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	//deletar cliente
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CLIENTE_EXCLUI')")
	public ResponseEntity<Response<String>> delete(@PathVariable int id) {
		Response<String> response = service.deletar(id);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	
	
}//fecha classe
