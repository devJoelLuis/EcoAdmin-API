package eco.controller;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eco.dto.ReciboDTO;
import eco.services.ReciboService;

@RestController
@RequestMapping("/recibo")
public class ReciboController {
	
	@Autowired
	private ReciboService service;
	
	//get relatorio de licença
	@PostMapping
	public ResponseEntity<byte[]> getRelatorio(
			@RequestBody ReciboDTO rc
			){
		try {
			byte[] relatorio = service.relatorio(rc);
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

}
