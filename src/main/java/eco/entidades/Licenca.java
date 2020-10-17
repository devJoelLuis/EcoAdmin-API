package eco.entidades;

import java.io.Serializable;
import java.time.LocalDate;

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


@Entity
@Table(name="licenca")
@Data
public class Licenca implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(length=45, unique=true)
	@NotNull
	private String numero;
	
	@Column(length=255)
	@NotNull
	private String assunto;
	
	 @Column(name="data_vencimento")
	 @NotNull
	private LocalDate dataVencimento;
	
    @Column(name="data_inicio")
    @NotNull
	private LocalDate dataInicio;
    
    @Column(name="data_alerta")
    @NotNull
    private LocalDate dataAlerta;
	
	@ManyToOne
	@JoinColumn(name="cliente_id")
	private Cliente cliente;
	
	@Column(length=255)
	private String obs;
	
	@Column(name = "arquivo_morto", nullable = false, columnDefinition = "TINYINT(1)")
	private boolean arquivoMorto;
	
	@NotNull
	private int alerta;
	
	
	
	// recebe uma licenca e altera o objeto
	public void alterarLicenca(Licenca l) {
		this.id = l.getId();
		this.assunto = l.getAssunto();
		this.cliente = l.getCliente();
		this.dataInicio = l.getDataInicio();
		this.dataVencimento = l.getDataVencimento();
		this.dataAlerta = l.getDataAlerta();
		this.numero = l.getNumero();
		this.obs = l.getObs();
		this.alerta = l.getAlerta();
		this.arquivoMorto = l.isArquivoMorto();
	}
	
	
}//fecha classe
