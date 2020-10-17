package eco.entidades;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Table(name="lembrete", schema="eco_admin")
@Data
public class Lembrete implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String descricao;
	
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean edit;
	
	@ManyToOne(cascade=CascadeType.DETACH)
	@JoinColumn(name="tecnico_id")
	@JsonIgnoreProperties(value = {"lembretes"})
	private Tecnico tecnico;
	

	
	
	

}//fecha classe
