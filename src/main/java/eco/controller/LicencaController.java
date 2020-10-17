package eco.controller;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

import eco.classes.LicencaConsultaFiltro;
import eco.entidades.Licenca;
import eco.response.Response;
import eco.services.LicencaService;

@RestController
@RequestMapping("/licencas")
public class LicencaController {
	
	@Autowired
	private LicencaService service;
	
	
	
	//cadastrar
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LICENCA')")
	public ResponseEntity<Response<Licenca>> cadastrar(
			@Valid @RequestBody Licenca l
			) {
		Response<Licenca> response = service.cadastrar(l);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	//alterar
	@PutMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LICENCA')")
	public ResponseEntity<Response<Licenca>> alterar(
			@Valid @RequestBody Licenca l
			) {
		Response<Licenca> response = service.alterar(l);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	//excluir
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LICENCA_EXCLUI')")
	public ResponseEntity<Response<String>> excluir(
			@PathVariable Long id
			) {
		Response<String> response = service.excluir(id);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	//get all id cliente
	@GetMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LICENCA')")
	public ResponseEntity<Response<Page<Licenca>>> getAllIdCliente(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="size", defaultValue="10") int size,
			@RequestParam(value="idcliente", defaultValue="0") int idcliente
			) {
		if(size > 40 ) size = 40;
		Response<Page<Licenca>> response = service.getAllIdCLiente(page, size, idcliente);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/vencimento")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LICENCA')")
	public ResponseEntity<Response<Page<Licenca>>> getAllVencimento(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="size", defaultValue="10") int size,
			@RequestParam(value="idcliente", defaultValue="0") int idcliente,
			@RequestParam(value="dtinicio") String dtinicio,
			@RequestParam(value="dtfim") String dtfim
			) {
		if(size > 40 ) size = 40;
		Response<Page<Licenca>> response = service.getAllVencimento(page, size, idcliente, dtinicio, dtfim);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	@GetMapping("/numero")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LICENCA')")
	public ResponseEntity<Response<String>> getUltimoNumero() {
		Response<String> response = service.getUltimoNumero();
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	//get id
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LICENCA')")
	public ResponseEntity<Response<Licenca>> getById(
			@PathVariable Long id
			) {
		Response<Licenca> response = service.getById(id);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	//get all com um intervalo de data de vencimento
		@GetMapping("/all")
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LICENCA')")
		public ResponseEntity<Response<Page<Licenca>>> getAllIdCliente(
				@RequestParam(value="page", defaultValue="0") int page,
				@RequestParam(value="size", defaultValue="10") int size,
				@RequestParam(value="dtinicio", defaultValue="") String dtinicio,
				@RequestParam(value="dtfim", defaultValue="") String dtfim,
				@RequestParam(value="alerta", defaultValue="0") int alerta
				) {
			if(size > 100 ) size = 100;
			Response<Page<Licenca>> response = service.getAllDtVencimento(page, size, dtinicio, dtfim, alerta);
			if (response.getErros().size() > 0) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		}
		
		
		@GetMapping("/all/alertaAll")
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LICENCA')")
		public ResponseEntity<Response<Page<Licenca>>> getAllPeriodoAletarAll(
				@RequestParam(value="page", defaultValue="0") int page,
				@RequestParam(value="size", defaultValue="10") int size,
				@RequestParam(value="dtinicio", defaultValue="") String dtinicio,
				@RequestParam(value="dtfim", defaultValue="") String dtfim
				) {
			if(size > 100 ) size = 100;
			Response<Page<Licenca>> response = service.getAllDtVencimentoAlertaAll(page, size, dtinicio, dtfim);
			if (response.getErros().size() > 0) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		}
		
		
		@GetMapping("/vencidas")
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LICENCA')")
		public ResponseEntity<Response<Page<Licenca>>> getAllVencidas(
				@RequestParam(value="page", defaultValue="0") int page,
				@RequestParam(value="size", defaultValue="10") int size
				
				) {
			if(size > 100 ) size = 100;
			Response<Page<Licenca>> response = service.getAllVencidas(page, size);
			if (response.getErros().size() > 0) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		}
		
		
		@GetMapping("/alerta")
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LICENCA')")
		public ResponseEntity<Response<Page<Licenca>>> getAllAlerta(
				@RequestParam(value="page", defaultValue="0") int page,
				@RequestParam(value="size", defaultValue="10") int size
				
				) {
			if(size > 100 ) size = 100;
			Response<Page<Licenca>> response = service.getAllAlerta(page, size);
			if (response.getErros().size() > 0) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		}
		
		
		@GetMapping("/todas")
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LICENCA')")
		public ResponseEntity<Response<Page<Licenca>>> getAllIdCliente(
				@RequestParam(value="page", defaultValue="0") int page,
				@RequestParam(value="size", defaultValue="10") int size
			
				) {
			if(size > 40 ) size = 40;
			Response<Page<Licenca>> response = service.getAll(page, size);
			if (response.getErros().size() > 0) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		}
		
		
	
		//get all com um intervalo de data de vencimento com parametro
				@GetMapping("/allparam")
				@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LICENCA')")
				public ResponseEntity<Response<Page<Licenca>>> getAllIdCliente(
						@RequestParam(value="page", defaultValue="0") int page,
						@RequestParam(value="size", defaultValue="10") int size,
						@RequestParam(value="dtinicio", defaultValue="") String dtinicio,
						@RequestParam(value="dtfim", defaultValue="") String dtfim,
						@RequestParam(value="param", defaultValue="") String param,
						@RequestParam(value="alerta", defaultValue="0") int alerta
						) {
					if(size > 100 ) size = 100;
					Response<Page<Licenca>> response = service.getAllDtVencimentoComParam(page, size, dtinicio, dtfim, param, alerta);
					if (response.getErros().size() > 0) {
						return ResponseEntity.badRequest().body(response);
					}
					return ResponseEntity.ok(response);
				}
				
				
				@PostMapping("/filtro")
				@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LICENCA')")
				public ResponseEntity<Response<Page<Licenca>>> getAllFiltro(
						@RequestBody LicencaConsultaFiltro lf
						) {
					if(lf.getSize() > 100 ) lf.setSize(100);
					Response<Page<Licenca>> response = service.getAllFiltro(lf);
					if (response.getErros().size() > 0) {
						return ResponseEntity.badRequest().body(response);
					}
					return ResponseEntity.ok(response);
				}
				
				//get relatorio de licença
				@GetMapping("/relatorio/{idOs}")
				@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LICENCA')")
				public ResponseEntity<byte[]> getRelatorio(
						@PathVariable Long idOs
						){
					try {
						byte[] relatorio = service.relatorio(idOs);
						return ResponseEntity.ok()
								.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
								.body(relatorio);
					} catch (Exception e) {
						byte[] erro = null;
						try {
							//gravar log do erro
							Path path = Paths.get("/home/joel/erro.log");
							BufferedWriter writer = Files.newBufferedWriter(path);
							writer.write(e.getMessage());
						} catch (Exception e2) {
							System.out.print(e2.getMessage());
						}
						System.out.println(e.getCause());
						System.out.println(e.getMessage());
						return ResponseEntity.badRequest().body(erro);
					}
				}
	

}// fecha classe
