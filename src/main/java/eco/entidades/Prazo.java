package eco.entidades;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Table(name="prazo", schema="eco_adminbd")
@Data
public class Prazo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message="A descrição é obrigatória")
	@Column(length=255)
	private String descricao;
	
	@Column(name="data_alerta")
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	private LocalDate dataAlerta;
	
	@Column(name="data_vencimento", nullable = false)
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	@NotNull(message="a data de vencimento é obrigatória")
	private LocalDate dataVencimento;
	
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean alerta;
	
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean edit;
	
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean cumprido;
	
	@ManyToOne(cascade=CascadeType.DETACH)
	@JoinColumn(name="os_id")
	@JsonIgnoreProperties(value = {"prazos", "servicos"})
	private OrdemServico os;
	
	
	

}//fecha classe
