package eco.controller;

import java.util.List;

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

import eco.dto.OrdemServicoFiltroDTO;
import eco.dto.OsFiltroDTO;
import eco.entidades.OrdemServico;
import eco.response.Response;
import eco.services.OrdemServicoService;

@RestController
@RequestMapping("/ordem-servico")
public class OrdemServicoController {
	
	@Autowired
	private OrdemServicoService service;
	
	
	// cadastrar
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OS')")
	public ResponseEntity<Response<OrdemServico>> cadastrar(
			@RequestBody OrdemServico os
			){
		Response<OrdemServico> response = service.cadastrarOs(os);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	// alterar
	@PutMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OS')")
	public ResponseEntity<Response<OrdemServico>> editar(
			@RequestBody OrdemServico os
			){
		Response<OrdemServico> response = service.editarOs(os);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	// excluir
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OS_EXCLUI')")
	public ResponseEntity<Response<String>> excluir(
			@PathVariable Long id
			){
		Response<String> response = service.excluirOs(id);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	// get by id
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OS')")
	public ResponseEntity<Response<OrdemServico>> buscarPorId(
			@PathVariable Long id
			){
		Response<OrdemServico> response = service.buscarPorId(id);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	
	// get by id
		@GetMapping("/numOs")
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OS')")
		public ResponseEntity<Response<String>> buscarPorId() {
			Response<String> response = service.getUltimoNumeroOs();
			if(response.getErros().size() > 0) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		}
	
	
	
	
	// get cliente id
	@GetMapping("/cliente/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OS')")
	public ResponseEntity<Response<List<OrdemServico>>> buscarPorIdCliente(
			@PathVariable int id
			){
		Response<List<OrdemServico>> response = service.buscarPorCliente(id);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	
	// get all & all data inicio
	@GetMapping("/dataInicio")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OS')")
	public ResponseEntity<Response<Page<OrdemServico>>> buscarTodosDataInicio(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="size", defaultValue="10") int size,
			@RequestParam(value="data", defaultValue="") String data
			){
		if(size > 100) size = 100;
		Response<Page<OrdemServico>> response = service.listarTodasDataInicio(page, size, data);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	
	// get all ano sem parametro
	@GetMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OS')")
	public ResponseEntity<Response<Page<OsFiltroDTO>>> buscarTodos(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="size", defaultValue="10") int size,
			@RequestParam(value="ano", defaultValue="0") int ano
			){
		if(size > 100) size = 100;
		Response<Page<OsFiltroDTO>> response = service.getAll(page, size, ano);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	// get all alarme sem parametro
	@GetMapping("/alarme")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OS')")
	public ResponseEntity<Response<Page<OsFiltroDTO>>> buscarTodosAlarme(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="size", defaultValue="10") int size,
			@RequestParam(value="ano", defaultValue="0") int ano
			){
		if(size > 100) size = 100;
		Response<Page<OsFiltroDTO>> response = service.getAllAlerta(page, size, ano);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	

	
	
	// get all ano com parametro
	@GetMapping("/parametro")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OS')")
	public ResponseEntity<Response<Page<OsFiltroDTO>>> getAllParametro(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="size", defaultValue="10") int size,
			@RequestParam(value="ano", defaultValue="0") int ano,
			@RequestParam(value="param", defaultValue="") String param
			){
		if(size > 100) size = 100;
		Response<Page<OsFiltroDTO>> response = service.getAllComParametro(page, size, ano, param);
		if(response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	
	// get all ano com parametro e alarme
		@GetMapping("/parametro/alarme")
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OS')")
		public ResponseEntity<Response<Page<OrdemServico>>> getAllAlarmeParametro(
				@RequestParam(value="page", defaultValue="0") int page,
				@RequestParam(value="size", defaultValue="10") int size,
				@RequestParam(value="ano", defaultValue="0") int ano,
				@RequestParam(value="param", defaultValue="") String param
				){
			if(size > 100) size = 100;
			Response<Page<OrdemServico>> response = service.getAllAlarmeComParametro(page, size, ano, param);
			if(response.getErros().size() > 0) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		}
		
		// get all ano e por status id
		@GetMapping("/status")
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OS')")
		public ResponseEntity<Response<Page<OsFiltroDTO>>> getAllStatusId(
				@RequestParam(value="page", defaultValue="0") int page,
				@RequestParam(value="size", defaultValue="10") int size,
				@RequestParam(value="ano", defaultValue="0") int ano,
				@RequestParam(value="idstatus", defaultValue="0") int idstatus
				){
			if(size > 100) size = 100;
			Response<Page<OsFiltroDTO>> response = service.getAllStatusId(page, size, ano, idstatus);
			if(response.getErros().size() > 0) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		}
		
		
		// get all pendencia ano sem parametro
		@GetMapping("/pendencia")
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OS')")
		public ResponseEntity<Response<Page<OsFiltroDTO>>> getAllPendenciaAnoSemParam(
				@RequestParam(value="page", defaultValue="0") int page,
				@RequestParam(value="size", defaultValue="10") int size,
				@RequestParam(value="ano", defaultValue="0") int ano
				){
			if(size > 100) size = 100;
			Response<Page<OsFiltroDTO>> response = service.getAllPedenciaAnoSemParam(page, size, ano);
			if(response.getErros().size() > 0) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		}
		
		
		
		
		
		// get todas as os DTO do cliente getAllOsClienteId
		@GetMapping("/oscliente")
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OS')")
		public ResponseEntity<Response<List<OsFiltroDTO>>> getAllOsCliente(
				@RequestParam(value="idcliente", defaultValue="0") Integer idcliente,
				@RequestParam(value="ano", defaultValue="0") int ano
				){
			Response<List<OsFiltroDTO>> response = service.getAllOsClienteId(idcliente, ano);
			if(response.getErros().size() > 0) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		}
		
		
		
		
		
		// get all pendencia ano sem parametro
		@PostMapping("/filtro")
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OS')")
		public ResponseEntity<Response<Page<OsFiltroDTO>>> getAllFiltro(
				@RequestBody OrdemServicoFiltroDTO osfiltro
				){
			if(osfiltro.getSize() > 100) osfiltro.setSize(100);
			Response<Page<OsFiltroDTO>> response = service.getAllFiltro(osfiltro);
			if(response.getErros().size() > 0) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		}
	
	
		
       /* ********* desativado porque foi removido serviços
		@GetMapping("/relatorio/{idOs}")
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OS')")
		public ResponseEntity<byte[]> getRelatorio(
				@PathVariable Long idOs
				){
			try {
				byte[] relatorio = service.relatorioOs(idOs);
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
		*/
		
		// get all numero da os ou parte da descrição
		@GetMapping("/descnum")
		@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OS')")
		public ResponseEntity<Response<List<OrdemServico>>> getAllParametro(
				@RequestParam(value="param", defaultValue="") String param
				){
			Response<List<OrdemServico>> response = service.getToNumOrDescricao(param);
			if(response.getErros().size() > 0) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		}
		
		
		  // get all numero da os ou parte da descrição
				@GetMapping("/nomecliente")
				@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OS')")
				public ResponseEntity<Response<List<OrdemServico>>> getAllNomeClienteAno(
						@RequestParam(value="param", defaultValue="") String param,
						@RequestParam(value="ano", defaultValue="0") int ano
						){
					Response<List<OrdemServico>> response = service.getToAnoClienteAno(param, ano);
					if(response.getErros().size() > 0) {
						return ResponseEntity.badRequest().body(response);
					}
					return ResponseEntity.ok(response);
				}
		

}// fecha classe
