package eco.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eco.entidades.Lembrete;
import eco.response.Response;
import eco.services.LembreteService;

@RestController
@RequestMapping("/lembretes")
public class LembreteController {
	
	@Autowired
	private LembreteService service;
	
	
	
	
	//cadastrar
	@PostMapping
	public ResponseEntity<Response<Lembrete>> cadastrar(
			@RequestBody Lembrete lembrete
			) {
		Response<Lembrete> response = service.cadastrar(lembrete);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	
	//editar
	@PutMapping
	public ResponseEntity<Response<Lembrete>> editar(
			@RequestBody Lembrete lembrete
			) {
		Response<Lembrete> response = service.editar(lembrete);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	
	
	//excluir
	@DeleteMapping("/{id}")
	public ResponseEntity<Response<String>> cadastrar(
			@PathVariable Long id
			) {
		Response<String> response = service.excluir(id);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	

}//fecha classe
