package eco.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eco.classes.DespesaFiltro;
import eco.entidades.Despesa;
import eco.response.Response;
import eco.services.DespesaService;

@RestController
@RequestMapping("/despesas")
public class DespesaController {

	@Autowired
	private DespesaService service;

	// cadastrar
	@PostMapping
	public ResponseEntity<Response<Despesa>> cadastrar(@Valid @RequestBody Despesa desp) {
		Response<Despesa> response = service.cadastrar(desp);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	// alterar
	@PutMapping
	public ResponseEntity<Response<Despesa>> alterar(@Valid @RequestBody Despesa desp) {
		Response<Despesa> response = service.alterar(desp);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	// excluir
	@DeleteMapping("/{id}")
	public ResponseEntity<Response<String>> excluir(@PathVariable Long id) {
		Response<String> response = service.excluir(id);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	// get by id
	@GetMapping("/{id}")
	public ResponseEntity<Response<Despesa>> getById(@PathVariable Long id) {
		Response<Despesa> response = service.getById(id);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	// get by all
		@GetMapping
		public ResponseEntity<Response<Page<Despesa>>> getAll(
				@RequestParam(value="page", defaultValue="0") int page,
				@RequestParam(value="size", defaultValue="20") int size
				) {
			Response<Page<Despesa>> response = service.getAll(page, size);
			if (response.getErros().size() > 0) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		}
		
		
		
		// get filtro
				@PostMapping("/filtro")
				public ResponseEntity<Response<Page<Despesa>>> getFiltro(@RequestBody DespesaFiltro df) {
					Response<Page<Despesa>> response = service.getFiltro(df);
					if (response.getErros().size() > 0) {
						return ResponseEntity.badRequest().body(response);
					}
					return ResponseEntity.ok(response);
				}

}// fecha classe
