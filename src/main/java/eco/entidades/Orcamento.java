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

@Entity
@Table(name="orcamento")
public class Orcamento implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private int numero;
	
	@NotNull
	private int ano;
	
	@NotNull
	@Column(length=255)
	private String assunto;
	
	@NotNull
	@Column(precision=10, scale=2)
	private Double valor;
	
	@NotNull
	@Column(name="data_entrega")
	private LocalDate dataEntrega;
	
	@Column(name="data_alerta")
	private LocalDate dataAlerta;
	
	@Column(length=255)
	private String obs;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="cliente_id")
	private Cliente cliente;
	
	@NotNull
	private int alerta;
	
	
	
	//altera os dados
	public void alterarDados(Orcamento o) {
		this.id = o.getId();
		this.assunto = o.getAssunto();
		this.cliente = o.getCliente();
		this.dataEntrega = o.getDataEntrega();
		this.dataAlerta = o.getDataAlerta();
		this.numero = o.getNumero();
		this.ano = o.getAno();
		this.obs = o.getObs();
		this.valor = o.getValor();
		this.alerta = o.getAlerta();
		
	}
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public int getNumero() {
		return numero;
	}



	public void setNumero(int numero) {
		this.numero = numero;
	}



	public int getAno() {
		return ano;
	}



	public void setAno(int ano) {
		this.ano = ano;
	}

    

	public int getAlerta() {
		return alerta;
	}



	public void setAlerta(int alerta) {
		this.alerta = alerta;
	}



	public String getAssunto() {
		return assunto;
	}
	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public LocalDate getDataEntrega() {
		return dataEntrega;
	}
	public void setDataEntrega(LocalDate dataEntrega) {
		this.dataEntrega = dataEntrega;
	}
	
	public LocalDate getDataAlerta() {
		return dataAlerta;
	}



	public void setDataAlerta(LocalDate dataAlerta) {
		this.dataAlerta = dataAlerta;
	}



	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Orcamento other = (Orcamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
	
	
	

}// fecha classe
