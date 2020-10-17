package eco.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Table(name ="despesa")
@Data
public class Despesa {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
    @NotBlank(message="A descrição é obrigatória.")
	private String descricao;
    
    @NotNull(message="A data é obrigatória.")
	private LocalDate data;
    
	private BigDecimal valor = new BigDecimal(0);
	private String obs;
	
	@JoinColumn(name="os_id")
	@OneToOne
	private OrdemServico os;
	
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean edit;
	
	@ManyToOne
	@JoinColumn(name="categoria_recebimento_id")
	@NotNull(message="A categoria é obrigatória")
	private CategoriaRecebimento categoriaRecebimento;
	

}//fecha classe
