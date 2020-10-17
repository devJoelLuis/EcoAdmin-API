package eco.controller;

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

import eco.entidades.Recebimento;
import eco.response.Response;
import eco.services.RecebimentoService;

@RestController
@RequestMapping("/recebimentos")
public class RecebimentoController {

	@Autowired
	private RecebimentoService service;

	// cadastrar
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_RECEBIMENTO')")
	public ResponseEntity<Response<Recebimento>> cadastrar(@RequestBody Recebimento recebimento) {
		Response<Recebimento> response = service.cadastrar(recebimento);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	// editar
	@PutMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_RECEBIMENTO')")
	public ResponseEntity<Response<Recebimento>> alterar(@RequestBody Recebimento recebimento) {
		Response<Recebimento> response = service.alterar(recebimento);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	// excluir
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_RECEBIMENTO_EXCLUI')")
	public ResponseEntity<Response<String>> excluir(@PathVariable Long id) {
		Response<String> response = service.excluir(id);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	// get By Id
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_RECEBIMENTO')")
	public ResponseEntity<Response<Recebimento>> getById(@PathVariable Long id) {
		Response<Recebimento> response = service.getById(id);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	// get By Id
		@GetMapping("/totalrecebido/{idlancamento}")
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_RECEBIMENTO')")
		public ResponseEntity<Response<Double>> getTotalRecebido(@PathVariable Long idlancamento) {
			Response<Double> response = service.getTotalRecebido(idlancamento);
			if (response.getErros().size() > 0) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		}
	

}// fecha classe
