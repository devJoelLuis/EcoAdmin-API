package eco.entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Table(name="ordem_servico")
@Data
public class OrdemServico implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	

	@Column(name="data_inicio")
	private LocalDate dataInicio;
	
	@Column(length=255)
	private String obs;
	
	@Column(length=200)
	private String local;
	
	private int alerta;
	
	@ManyToOne
	@JoinColumn(name="cliente_id")
	@JsonIgnoreProperties("ordemServicos")
	private Cliente cliente;
	
	
	
     @Column(name="data_alerta")
	 private LocalDate dataAlerta;
     
     
     @JsonIgnoreProperties(value = {"os"})
     @OneToMany(mappedBy="os")
     private List<Prazo> prazos = new ArrayList<>();
     
     @Column(name="num_os")
  	 private int numOs;
  	
  	 @Column(name="ano_os")
  	 private int anoOs;
     
     @ManyToOne
     @JoinColumn(name="status_id")
     private StatusOs statusOs;
     
     @ManyToOne
     @JoinColumn(name="tecnico_id")
     private Tecnico tecnico;
    	
	
	
	

}// fecha classe
