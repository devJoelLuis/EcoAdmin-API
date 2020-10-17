package eco.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eco.entidades.Dashboard;
import eco.response.Response;
import eco.services.DashboardService;

@RestController
@RequestMapping("/dash")
public class DashboardController {

	
	@Autowired
	private DashboardService ds;
	
	
	@GetMapping
	public ResponseEntity<Response<Dashboard>> getDash() {
		Response<Dashboard> d = ds.gerarDash();
		if (d.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(d);
		}
		return ResponseEntity.ok(d);
	}
	
//	@GetMapping("/servicoreceber")
//	public ResponseEntity<Response<List<Servico>>> servicosReceber(
//			@RequestParam(value="dtinicio") String dtinicio,
//			@RequestParam(value="dtfim") String dtfim
//			) {
//		 Response<List<Servico>> response = ds.getServicosAReceber(dtinicio, dtfim);
//		 if(response.getErros().size() > 0) {
//			return  ResponseEntity.badRequest().body(response);
//		 }
//		return ResponseEntity.ok(response);
//	}
	
	
	
}// fecha classe
