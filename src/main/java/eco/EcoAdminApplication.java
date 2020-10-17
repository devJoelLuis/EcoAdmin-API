package eco;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import eco.entidades.repositorios.PermissoesStaticInsert;

@SpringBootApplication
public class EcoAdminApplication implements CommandLineRunner {
	
	@Autowired
	private PermissoesStaticInsert perssisteRoles;
	
	 @PostConstruct
	  public void init() {
	    //TimeZone.setDefault(TimeZone.getTimeZone("GMT-3:00"));
		 TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	  }
	  
  
	public static void main(String[] args) {
		SpringApplication.run(EcoAdminApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		
		perssisteRoles.perssistirRoles();
		
	}



}
