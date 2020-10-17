package eco.classes;

import java.util.ArrayList;
import java.util.List;

import eco.entidades.Despesa;
import eco.entidades.Lancamento;
import eco.entidades.Recebimento;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FluxoCaixaSub {
	
	
	private String categoria;
	private Double saldo;
	private List<Recebimento> recebimentos = new ArrayList<>();
	private List<Despesa> despesas = new ArrayList<Despesa>();
	private List<Lancamento>  lancamentosTaxa = new ArrayList<>();

}//fecha classe
