package eco.controller;

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

import eco.dto.LancamentoOsDTO;
import eco.entidades.Lancamento;
import eco.response.Response;
import eco.services.LancamentoService;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {
	
	@Autowired
	private LancamentoService service;
	
	// cadastrar novo estatus
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LANCAMENTO')")
	public ResponseEntity<Response<String>> cadastrar(
		@Valid	@RequestBody Lancamento lancamento,
		@RequestParam(value="parcelas", defaultValue="0") int parcelas
			) {
		Response<String> response = service.cadastrar(lancamento, parcelas);
		if(response.getErros().size() > 0) {
		  return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}// fecha cadastrar
	
	
	
	
	// editar estatos
	@PutMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LANCAMENTO')")
	public ResponseEntity<Response<Lancamento>> editarEstatus(
		@Valid	@RequestBody Lancamento lancamento
			) {
		Response<Lancamento> response = service.alterar(lancamento);
		if(response.getErros().size() > 0) {
		  return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}// fecha editar estatos
	
	
	
	// excluir estatos
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LANCAMENTO_EXCLUI')")
	public ResponseEntity<Response<String>> excluirEstatus(
			@PathVariable Long id
			) {
		Response<String> response = service.excluir(id);
		if(response.getErros().size() > 0) {
		  return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}// fecha excluir estatos
	
	
	// buscar estatos por id
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LANCAMENTO')")
	public ResponseEntity<Response<Lancamento>> buscarPorId(
			@PathVariable Long id
			) {
		Response<Lancamento> response = service.getPorId(id);
		if(response.getErros().size() > 0) {
		  return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}// fecha buscar por id
	
	

	// buscar todos lançamentos pelo id 
	@GetMapping("/idos")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LANCAMENTO')")
	public ResponseEntity<Response<Page<Lancamento>>> getAllLancamentosIdOs(
		@RequestParam(value="page", defaultValue="0") int page,
		@RequestParam(value="size", defaultValue="20") int size,
		@RequestParam(value="idos", defaultValue="0") Long idos
			) {
		Response<Page<Lancamento>> response = service.getAllByIdOs(idos, page, size);
		if(response.getErros().size() > 0) {
		  return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}// fecha buscar por id
	

	
	// buscar idos lancamentoDTO
		@GetMapping("/dto")
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LANCAMENTO')")
		public ResponseEntity<Response<Page<LancamentoOsDTO>>> getAllLancamentosIdOsDto(
			@RequestParam(value="filtro", defaultValue="") String filtro,
			@RequestParam(value="idos", defaultValue="0") Long idos,
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="size", defaultValue="20") int size,
			@RequestParam(value="inicio", defaultValue="0") String inicio,
			@RequestParam(value="fim", defaultValue="0") String fim
			
				) {
			Response<Page<LancamentoOsDTO>> response = service.getLancamentoOsDto(page, size, filtro, idos, inicio, fim);
			if(response.getErros().size() > 0) {
			  return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
			
		}// fecha buscar por id
		

	
	

}// fecha classe
