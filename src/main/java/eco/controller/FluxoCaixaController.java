package eco.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eco.classes.FluxoCaixa;
import eco.response.Response;
import eco.services.FluxoCaixaService;

@RestController
@RequestMapping("/fluxocaixa")
public class FluxoCaixaController {
	
	
	private FluxoCaixaService service;
	
	
	
	
	public FluxoCaixaController(FluxoCaixaService service) {
		this.service = service;
	}




	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping
	public ResponseEntity<Response<FluxoCaixa>> getFluxo(
		@RequestParam(value="dtini") String dtini,
		@RequestParam(value="dtfim") String dtfim
			) {
		Response<FluxoCaixa> response = service.getFluxoCaixa(dtini, dtfim);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	

}//fecha classe
