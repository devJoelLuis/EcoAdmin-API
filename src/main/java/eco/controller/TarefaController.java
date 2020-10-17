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

import eco.dto.TecnicoTarefasDTO;
import eco.entidades.Tarefa;
import eco.response.Response;
import eco.services.TarefaService;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

	@Autowired
	private TarefaService service;

	// cadastrar
	@PostMapping
	public ResponseEntity<Response<Tarefa>> cadastra(@Valid @RequestBody Tarefa tarefa) {
		Response<Tarefa> response = service.cadastrar(tarefa);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	// editar
	@PutMapping
	public ResponseEntity<Response<Tarefa>> alterar(@Valid @RequestBody Tarefa tarefa) {
		Response<Tarefa> response = service.alterar(tarefa);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	// excluir
	@DeleteMapping("/{id}")
	public ResponseEntity<Response<String>> excluir(@PathVariable int id) {
		Response<String> response = service.excluir(id);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	// get by id
	@GetMapping("/{id}")
	public ResponseEntity<Response<Tarefa>> getById(@PathVariable int id) {
		Response<Tarefa> response = service.getById(id);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	// get all tarefas por tecnicos
	@GetMapping
	public ResponseEntity<Response<List<TecnicoTarefasDTO>>> getAllTecnicosTarefas() {
		Response<List<TecnicoTarefasDTO>> response = service.getAllTarefasTecnicos();
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

}// fecha classe
