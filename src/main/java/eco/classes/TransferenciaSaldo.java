package eco.classes;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferenciaSaldo {
	
	@NotNull(message= "Informe o id da categoria provedora.")
	private Integer idCatRecebProvedor;
	
	@NotNull(message= "Informe o id da categoria receptora.")
	private Integer idCatRecebReceptor;
	
	@NotNull(message= "Informe o valor da tranferência.")
	private Double valor = 0.00;

}// fecha classe
