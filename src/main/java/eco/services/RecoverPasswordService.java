package eco.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import eco.entidades.Usuario;
import eco.entidades.repositorios.UsuarioRepository;
import eco.mail.Mailer;
import eco.response.Response;

@Service
public class RecoverPasswordService {
	
	@Autowired
	private UsuarioRepository repoUser;
	
	@Autowired
	private Mailer mailer;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	private Random random = new Random();

	public Response<String> recoverPasswd(String username) {
		Response<String> response = new Response<>();
		try {
			Usuario ubanco = repoUser.findByEmail(username);
			if (ubanco == null) {
				throw new Exception("não foi possível encontrar um usuário com o email "+ username);
			}
			if (ubanco.getRecuperacaoSenha() != null) {
				// testar se o ultimo pedido é menos que 5 minutos
				LocalDateTime now = LocalDateTime.now();
				if (ubanco.getRecuperacaoSenha().plusMinutes(5).isAfter(now)) {
					Duration d = Duration.between(now, ubanco.getRecuperacaoSenha().plusMinutes(5));
					throw new Exception("Um pedido de redifinição de senha já foi feito a menos de 5 minutos, aguade "+ 
							d.toMinutes() + " minutos e tente novamente");
				}
			}
			
			String novaSenha = gerarNovaSenha();
			List<String> remetentes = new ArrayList<>();
			remetentes.add(username.trim());
			mailer.enviarEmail("coami.socorro@gmail.com", remetentes  , "Nova senha", "A sua nova senha do sistema para o usuário "+ username+ " é: "+ novaSenha);
			ubanco.setSenha(encoder.encode(novaSenha));
			ubanco.setRecuperacaoSenha(LocalDateTime.now());
			repoUser.save(ubanco);
			response.setDados("ok");
			return response;
			
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao tentar recuperar a senha: "+ e.getMessage());
			return response;
		}
	}
	
	

	private String gerarNovaSenha() {
		char[] vet = new char[10];
		for (int i = 0; i < 10; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}



	private char randomChar() {
		int opt = random.nextInt(3);
		if (opt == 0) { //gera um digito
			return (char) (random.nextInt(10) + 48);
		} else if (opt == 1) { // gera letra maiuscula
			return (char) (random.nextInt(26) + 65);
		} else { // gera legra minuscula
			return (char) (random.nextInt(26) + 97);
		}
	}

}
