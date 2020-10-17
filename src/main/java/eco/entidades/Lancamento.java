package eco.entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name="lancamento")
@Data
public class Lancamento implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(length=200)
	@NotBlank(message="A descrição do lançamento é obrigatório.")
	private String descricao;
	
	@Column(name="data_lancamento")
	@NotNull(message="A data do lançamento é obrigatória.")
	private LocalDate dataLancamento;
	
	
	
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean pago;
	
	@Column(length=255)
	private String obs;
	
	@Column(precision=10, scale=2)
	@NotNull
	private Double total;
	
	@Nullable
	@Column(name="data_pag_total", nullable= true)
	private LocalDate dataPagTotal;
	
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="categoria_id")
	private Categoria categoria;
	
	@ManyToOne
	@JoinColumn(name="ordem_servico_id")
	private OrdemServico ordemServico;
	
	@JsonIgnore
	@OneToMany(mappedBy="lancamento", cascade = CascadeType.ALL )
	private List<Recebimento> recebimentos = new ArrayList<>();
	
	
	@Column(name="taxa_categoria_r_id")
	@NotNull(message="categoria recebimento não informado")
	private Integer idCategoriaRecebimentoTaxa = 0;
	
	
	
	

}//fecha classe
