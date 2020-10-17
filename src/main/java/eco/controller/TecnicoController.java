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

import eco.dto.TecnicoDTO;
import eco.entidades.Tecnico;
import eco.response.Response;
import eco.services.TecnicoService;

@RestController
@RequestMapping("/tecnicos")
public class TecnicoController {
	
	@Autowired
	private TecnicoService service;
	
	
	@GetMapping("/dto")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_TECNICO')")
	public ResponseEntity<List<TecnicoDTO>> getAllDto() {
		return ResponseEntity.ok(service.getAllDto());
	}
	
	
	
	
	
	//cadastrar
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_TECNICO')")
	public ResponseEntity<Response<Tecnico>> cadastrar(
	 @RequestBody Tecnico t		
			) {
		Response<Tecnico> response = service.cadastrar(t);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	//alterar
	@PutMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_TECNICO')")
	public ResponseEntity<Response<Tecnico>> alterar(
	 @RequestBody Tecnico t		
			) {
		Response<Tecnico> response = service.alterar(t);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	//excluir
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_TECNICO_EXCLUI')")
	public ResponseEntity<Response<String>> excluir(
	 @PathVariable int id
			) {
		Response<String> response = service.excluir(id);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	//get por id
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_TECNICO', 'ROLE_OS')")
	public ResponseEntity<Response<Tecnico>> getById(
	 @PathVariable int id	
			) {
		Response<Tecnico> response = service.getById(id);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	//get all
	@GetMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_TECNICO', 'ROLE_OS')")
	public ResponseEntity<Response<Page<Tecnico>>> getAll(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="size", defaultValue="10") int size
			) {
		Response<Page<Tecnico>> response = service.getAll(page, size);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	//get all part nome
	@GetMapping("/nome")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_TECNICO', 'ROLE_OS')")
	public ResponseEntity<Response<Page<Tecnico>>> getAllPartNome(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="size", defaultValue="10") int size,
			@RequestParam(value="nome", defaultValue="") String nome
			) {
		Response<Page<Tecnico>> response = service.getAllParteNome(page, size, nome);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	

	
}//fecha classe
