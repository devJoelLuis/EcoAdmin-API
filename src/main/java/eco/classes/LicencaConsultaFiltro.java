package eco.classes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LicencaConsultaFiltro {
	
	  private int page = 0;
	  private int size = 20;
	  private String dtInicio = "";
	  private String dtFim = "";
	  private String param = "";
	  private String tipo = "tudo";

}//fecha classe
