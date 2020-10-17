package eco.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import eco.entidades.Lancamento;
import eco.entidades.Recebimento;
import lombok.Data;

@Data
public class LancamentoOsDTO {
	
	private Long idlancamento;
	private String descricao;
	private Double total;
	private LocalDate data;
	private Double totalPago = 0.00;
	private Double totalReceber = 0.00;
	private List<Recebimento> recebimentos = new ArrayList<>();
	
	public void setValues(Lancamento l) {
		
		this.idlancamento = l.getId();
		this.descricao = l.getDescricao();
		this.total = l.getTotal();
		this.totalPago = 0D;
		this.data = l.getDataLancamento();
		for (Recebimento r: l.getRecebimentos()) {
			this.totalPago += r.getValor().doubleValue();
		}
		this.totalReceber = this.totalReceber - this.totalPago;
		this.recebimentos = l.getRecebimentos();
		
		
	}

}
