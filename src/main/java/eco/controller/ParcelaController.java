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

import eco.entidades.Parcela;
import eco.response.Response;
import eco.services.ParcelaService;

@RestController
@RequestMapping("/parcelas")
public class ParcelaController {
	
	@Autowired
	private ParcelaService service;
	
	
	
	//cadastrar
	@PostMapping
	public ResponseEntity<Response<Parcela>> cadastrar(
		@Valid	@RequestBody Parcela p
			){
		Response<Parcela> response = service.cadastrar(p);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		} 
		return ResponseEntity.ok(response);
	}
	
	
	
	//alterar
	@PutMapping
	public ResponseEntity<Response<Parcela>> alterar(
		@Valid	@RequestBody Parcela p
			){
		Response<Parcela> response = service.alterar(p);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		} 
		return ResponseEntity.ok(response);
	}
	
	
	//alterar
	@PutMapping("/status")
	public ResponseEntity<Response<Parcela>> alterarPagoDevedor(
		@RequestParam(value="id", defaultValue="0") Long id,
		@RequestParam(value="pago", defaultValue="0") int pago,
		@RequestParam(value="pagamento", defaultValue="1970-01-01") String pagamento
			){
		Response<Parcela> response = service.alterarPagoDevedor(id, pago, pagamento);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		} 
		return ResponseEntity.ok(response);
	}
	
	
	
	//excluir
	@DeleteMapping("/{id}")
	public ResponseEntity<Response<String>> excluir(
		@PathVariable Long id
			){
		Response<String> response = service.excluir(id);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		} 
		return ResponseEntity.ok(response);
	}
	
	
	
	
	//getId
	@GetMapping("/{id}")
	public ResponseEntity<Response<Parcela>> getById(
	   @PathVariable Long id		
			){
		Response<Parcela> response = service.getById(id);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		} 
		return ResponseEntity.ok(response);
	}
	
	
	
	
	
	
	//get all pageable to Gerenciamento id
	@GetMapping
	public ResponseEntity<Response<Page<Parcela>>> getAllByGerenciamentoId(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="size", defaultValue="10") int size,
			@RequestParam(value="idg", defaultValue="0") Long idg
			) {
		if(size > 60) size = 60; // prevenir abuso
		Response<Page<Parcela>> response = service.getAllByGerenciamentoId(page, size, idg);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		} 
		return ResponseEntity.ok(response);
	}
	
	
	

}// fecha classe
