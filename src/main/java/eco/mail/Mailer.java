package eco.mail;

import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@Component
public class Mailer {
	
	@Autowired
	private JavaMailSender ms;
	
	public String enviarEmail(String remetente, List<String> destinatarios, String assunto, String mensagem) throws Exception {
		
			
			MimeMessage mm = ms.createMimeMessage();
			
			MimeMessageHelper helper = new MimeMessageHelper(mm, "UTF-8");
			helper.setFrom(remetente);
			helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
			helper.setSubject(assunto);
			helper.setText(mensagem, false);
			
			ms.send(mm);
			return "ok";
		
	}

}// fecha classe
