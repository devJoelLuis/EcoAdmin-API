package eco.entidades;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Table(name="tarefa", schema="eco_adminbd")
@Data
public class Tarefa implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message="a descrição é obrigatória!!")
	private String descricao;
	
	@NotNull(message="o técnico é obrigatório!")
	@ManyToOne(cascade=CascadeType.DETACH)
	@JoinColumn(name="tecnico_id")
	private Tecnico tecnico;
	
	@NotNull(message="a O.S. é obrigatória!")
	@ManyToOne(cascade=CascadeType.DETACH)
	@JoinColumn(name="os_id")
	private OrdemServico os;
	
	@NotNull(message="É obrigatório informar a prioridade")
	private Integer prioridade = 10;  // 10 = prioridade, 20 = lista, 30 = aguardando
	
	
	
	
	
	

}//fecha classe
