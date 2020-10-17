package eco.geredorSenha;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorSenha {
	
	public static void main(String[] args) {
	
		BCryptPasswordEncoder b = new BCryptPasswordEncoder();
		System.out.println(b.encode("112233"));
		
	}
	

}
