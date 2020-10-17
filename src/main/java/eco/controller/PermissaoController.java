package eco.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eco.entidades.Permissao;
import eco.response.Response;
import eco.services.PermissaoService;

@RestController
@RequestMapping("/permissoes")
public class PermissaoController {
	
	
	@Autowired
	private PermissaoService service;
	
   // get all
	@GetMapping
	public ResponseEntity<Response<List<Permissao>>> getAll() {
		Response<List<Permissao>> response = service.getAll();
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	

}// fecha classe
