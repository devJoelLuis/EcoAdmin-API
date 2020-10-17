package eco.classes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DespesaFiltro {

	private int page;
	private int size;
	private String dataInicio;
	private String dataFinal;
	private String consultaTipo;
	private String descricao;
	private Integer idcategoriaRecebimento;
	
}//fecha classe
