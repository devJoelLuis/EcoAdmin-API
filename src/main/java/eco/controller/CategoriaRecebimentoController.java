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

import eco.classes.TransferenciaSaldo;
import eco.entidades.CategoriaRecebimento;
import eco.entidades.HistoricoCatRecebimento;
import eco.response.Response;
import eco.services.CategoriaRecebimentoService;

@RestController
@RequestMapping("/categoriasRecebimento")
public class CategoriaRecebimentoController {
	
	
	@Autowired
	private CategoriaRecebimentoService service;
	
	
	
	//cadastrar
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntity<Response<CategoriaRecebimento>> cadastrar(
		@Valid @RequestBody CategoriaRecebimento cr	
			) {
		Response<CategoriaRecebimento> response = service.cadastrar(cr);
		
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	//alterar
	@PutMapping	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntity<Response<CategoriaRecebimento>> alterar(
		@Valid @RequestBody CategoriaRecebimento cr	
			) {
		Response<CategoriaRecebimento> response = new Response<>();
		if (cr.getId() == 1) {
			response.getErros().add("A categoria com id 1 não pode ser excluída nem alterada.");
		} else {
			response = service.alterar(cr);
		}
		
		
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	//excluir
	@DeleteMapping("/{id}")	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntity<Response<String>> deletar(
		@PathVariable Integer id
			) {
		Response<String> response = new Response<String>();
		if (id == 1) {
			response.getErros().add("A categoria com id 1 não pode ser excluída nem alterada.");
		} else {
			response = service.excluir(id);
		}
		
		
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	//get id
	@GetMapping("/{id}")	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntity<Response<CategoriaRecebimento>> getById(
		@PathVariable Integer id
			) {
		Response<CategoriaRecebimento> response = service.getById(id);
		
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	//get all
	@GetMapping	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntity<Response<List<CategoriaRecebimento>>> getAll() {
		
		Response<List<CategoriaRecebimento>> response = service.getAll();
		
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	
	
	//get historico
	@GetMapping("/historico")	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntity<Response<Page<HistoricoCatRecebimento>>> getAllHistorico(
		@RequestParam(value="page", defaultValue="0") int page,
		@RequestParam(value="size", defaultValue="40") int size,
		@RequestParam(value="id", defaultValue="0") Integer id
			) {
		Response<Page<HistoricoCatRecebimento>> response = service.getAllHistorico(page, size, id);
		
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	
	@PostMapping("/transferencia")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntity<?> transferencia(
	 @RequestBody TransferenciaSaldo ts		
			) {
		Response<String> response = service.transferencia(ts);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	

}//fecha classe
