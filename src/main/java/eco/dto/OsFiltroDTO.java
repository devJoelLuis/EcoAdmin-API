package eco.dto;

import lombok.Data;

@Data
public class OsFiltroDTO {
	
	private Long idos;
	private String nomeCliente;
	private String descricaoOs;
	private boolean pendente = false;
	private Double pendenciaFinanceira = 0.00;
	private Double total = 0.00;
	private Double totalLancado = 0.00;
	private Double totalPago = 0.00;
	private String status;
	private int numOs;
  	private int anoOs;
  	private String statusCor;

}//fecha classe
