package eco.entidades;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name="categoria_recebimento")
@Data
/* 
 * OBS: (O nome da entidade de CategoriaRecebimento não é um nome adequado, 
 * porém o cliente insistiu que seja assim  para seu próprio entendimento)
 */
	
public class CategoriaRecebimento {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message="a descrição da categoria de recebimento é obrigatória.")
	private String descricao;
	
	@OneToMany(mappedBy="categoriaRecebimento")
	@JsonIgnore
	private List<Recebimento> recebimentos = new ArrayList<>();
	
	@Column(name="total_receita")
	private BigDecimal totalReceita = new BigDecimal(0);

}//fecha classe
