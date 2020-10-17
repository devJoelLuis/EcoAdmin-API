package eco.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eco.entidades.EmailAlerta;
import eco.response.Response;
import eco.services.EmailAlertaService;

@RestController
@RequestMapping("/emailsAlerta")
public class EmailAlertaController {

	@Autowired
	private EmailAlertaService service;

	// cadastrar
	@PostMapping
	public ResponseEntity<Response<EmailAlerta>> cadastrar(@Valid @RequestBody EmailAlerta em) {
		Response<EmailAlerta> response = service.cadastrar(em);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	// alterar
	@PutMapping
	public ResponseEntity<Response<EmailAlerta>> alterar(@Valid @RequestBody EmailAlerta em) {
		Response<EmailAlerta> response = service.alterar(em);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	// excluir
	@DeleteMapping("/{id}")
	public ResponseEntity<Response<String>> excluir(@PathVariable int id) {
		Response<String> response = service.Excluir(id);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	// get id
	@GetMapping("/{id}")
	public ResponseEntity<Response<EmailAlerta>> getId(@PathVariable int id) {
		Response<EmailAlerta> response = service.getById(id);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	// get all
	@GetMapping
	public ResponseEntity<Response<List<EmailAlerta>>> getall() {
		Response<List<EmailAlerta>> response = service.getAll();
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

}// fecha classe
