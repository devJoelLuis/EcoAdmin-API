package eco.entidades;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

/*
 * Classe responsável em manipular o histórico
 * de movimentação do saldo da categoria recebimento
 */
@Entity
@Table(name="historico_mov_cat_recebimento")
@Data
public class HistoricoCatRecebimento {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String descricao;
	
	@NotNull
	@Column(name="valor_modificado", precision=10, scale=2)
	private Double valorModificado;
	
	@NotNull
	@Column(name="valor_anterior", precision=10, scale=2)
	private Double valorAnterior;
	
	@Column(name="data_hora")
	private LocalDateTime dataHora;
	
	@Column(name="user_nome")
	private String userNome;
	
	@ManyToOne
	@JoinColumn(name="categoria_recebimento_id")
	private CategoriaRecebimento categoriaRecebimento;
	
	

}//fecha classe
