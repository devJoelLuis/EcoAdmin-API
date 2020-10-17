package eco.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdemServicoFiltroDTO {
	
	private String anoNumOs;
	private String tipoConsulta;
	private int page = 0;
	private int size = 20;

}
