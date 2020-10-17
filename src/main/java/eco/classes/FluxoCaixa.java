package eco.classes;

import java.util.ArrayList;
import java.util.List;

import eco.entidades.Lancamento;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FluxoCaixa {
	
	
	List<FluxoCaixaSub> fluxosSub = new ArrayList<>();
	List<Lancamento> lancamentos = new ArrayList<>();
	
	

}//fecha classe
