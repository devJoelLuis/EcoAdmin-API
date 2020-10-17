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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eco.entidades.Orcamento;
import eco.response.Response;
import eco.services.OrcamentoService;

@RestController
@RequestMapping("/orcamentos")
public class OrcamentoController {
	
	@Autowired
	private OrcamentoService service;
	
	// cadastro
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ORCAMENTO')")
	public ResponseEntity<Response<Orcamento>> cadastro(
		@Valid @RequestBody Orcamento orc
	) {
		Response<Orcamento> response = service.cadastrar(orc);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	
	// alterar
	@PutMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ORCAMENTO')")
	public ResponseEntity<Response<Orcamento>> alterar(
		@Valid @RequestBody Orcamento orc
	) {
		Response<Orcamento> response = service.alterar(orc);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	// excluir
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ORCAMENTO_EXCLUI')")
	public ResponseEntity<Response<String>> deletar(
		@PathVariable Long id
	) {
		Response<String> response = service.excluir(id);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	//get id
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ORCAMENTO')")
	public ResponseEntity<Response<Orcamento>> getById(
		@PathVariable Long id
	) {
		Response<Orcamento> response = service.getById(id);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	//get all id do cliente
	@GetMapping("/cliente")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ORCAMENTO')")
	public ResponseEntity<Response<List<Orcamento>>> getByIdCliente(
		@RequestParam(value="id", defaultValue="0") int idcliente
	) {
		Response<List<Orcamento>> response = service.getAllIdCliente(idcliente);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
//	
//	//get ultimo numero do orçamento
//		@GetMapping("/numero")
//		public ResponseEntity<Response<Index_num_or>> getUltimoNumero() {
//			Response<Index_num_or> response = service.getUltimoNumero();
//			if (response.getErros().size() > 0) {
//				return ResponseEntity.badRequest().body(response);
//			}
//			return ResponseEntity.ok(response);
//		}
//		
//		// excluir
//		@DeleteMapping("/numero/{id}")
//		public ResponseEntity<Response<String>> deletarNumero(
//			@PathVariable Long id
//		) {
//			Response<String> response = service.deleteIndexOr(id);
//			if (response.getErros().size() > 0) {
//				return ResponseEntity.badRequest().body(response);
//			}
//			return ResponseEntity.ok(response);
//		}
		
		
		
    // get por id cliente e data entrega
		@GetMapping("/data")
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ORCAMENTO')")
        public ResponseEntity<Response<List<Orcamento>>> getIdClienteDataEntrega(
           @RequestParam(value="inicio", defaultValue="") String dtinicio,
           @RequestParam(value="fim", defaultValue="") String dtfim,
           @RequestParam(value="idcliente", defaultValue="0") int id
        		) {
			Response<List<Orcamento>> response = service.getIdClienteDataEntrega(dtinicio, dtfim, id);
			if (response.getErros().size() > 0) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		}
	

}// fecha classe
