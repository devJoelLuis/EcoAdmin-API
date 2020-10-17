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

import eco.entidades.Prazo;
import eco.response.Response;
import eco.services.PrazoService;

@RestController
@RequestMapping("/prazos")
public class PrazoController {

	@Autowired
	private PrazoService service;

	// cadastrar
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PRAZO')")
	public ResponseEntity<Response<Prazo>> cadastrar(@RequestBody Prazo prazo) {
		Response<Prazo> response = service.cadastrar(prazo);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	// editar
	@PutMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PRAZO')")
	public ResponseEntity<Response<Prazo>> alterar(@RequestBody Prazo prazo) {
		Response<Prazo> response = service.alterar(prazo);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	// excluir
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PRAZO_EXCLUI')")
	public ResponseEntity<Response<String>> excluir(@PathVariable Long id) {
		Response<String> response = service.excluir(id);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	// get By Id
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PRAZO')")
	public ResponseEntity<Response<Prazo>> getById(@PathVariable Long id) {
		Response<Prazo> response = service.getById(id);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	// get all pageable
	@GetMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PRAZO')")
	public ResponseEntity<Response<Page<Prazo>>> getAll(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="size", defaultValue="20") int size
			) {
		Response<Page<Prazo>> response = service.getAll(page, size);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	
	

	// get all O.S. id
	@GetMapping("/os/{idos}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PRAZO')")
	public ResponseEntity<Response<List<Prazo>>> getAllByOsId(@PathVariable Long idos) {
		Response<List<Prazo>> response = service.getAllOsId(idos);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	// get all O.S. id
		@GetMapping("/os")
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PRAZO')")
		public ResponseEntity<Response<List<Prazo>>> getAllByNumOs(
				@RequestParam(value="numOs", defaultValue="0") int numOs,
				@RequestParam(value="anoOs", defaultValue="0") int anoOs
				) {
			Response<List<Prazo>> response = service.getAllByNumOs(numOs, anoOs);
			if (response.getErros().size() > 0) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		}

	// get all O.S. em alerta
	@GetMapping("/alerta")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PRAZO')")
	public ResponseEntity<Response<List<Prazo>>> getAllAlerta() {
		Response<List<Prazo>> response = service.getAllAlerta();
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	// get all O.S. vencendo
	@GetMapping("/vencendo")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PRAZO')")
	public ResponseEntity<Response<List<Prazo>>> getAllVencendo() {
		Response<List<Prazo>> response = service.getAllVencendo();
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	// get all O.S. vencido
		@GetMapping("/vencido")
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PRAZO')")
		public ResponseEntity<Response<List<Prazo>>> getAllVencido() {
			Response<List<Prazo>> response = service.getAllVencido();
			if (response.getErros().size() > 0) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		}

}// fecha classe
