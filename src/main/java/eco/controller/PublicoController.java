package eco.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eco.response.Response;
import eco.services.RecoverPasswordService;

@RestController
@RequestMapping("/publico")
public class PublicoController {
	
	@Autowired
	private RecoverPasswordService servicePwd;
	
	@GetMapping("/recover-pwd/{username}")
	public ResponseEntity<Response<String>> getNovoPassword(
			@PathVariable String username
			){
		Response<String> response = servicePwd.recoverPasswd(username);
		if (response.getErros().size() > 0) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

}
