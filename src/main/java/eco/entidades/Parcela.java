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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="parcela")
public class Parcela implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
    @Column(name="data_vencimento")
	private LocalDate dataVencimento;
	
	@NotNull
    @Column(name="data_pagamento")
	private LocalDate dataPagamento;
    
    
	@NotNull
    @Column(precision=10, scale=2)
	private  Double valor;
    
	@NotNull
    @Column(name="numero_parcela")
	private int numeroParcela;
	
	@NotNull
	private int pago;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="gerenciamento_id")
	@JsonIgnoreProperties("gerenciamento")
	private Gerenciamento gerenciamento;
	
	
	
	public void alterar(Parcela p) {
		this.id = p.getId();
		this.dataVencimento = p.getDataVencimento();
		this.valor = p.getValor();
		this.numeroParcela = p.getNumeroParcela();
		this.pago = p.getPago();
		this.gerenciamento = p.getGerenciamento();
		this.dataPagamento = p.getDataPagamento();
	}
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDate getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public int getNumeroParcela() {
		return numeroParcela;
	}
	public void setNumeroParcela(int numeroParcela) {
		this.numeroParcela = numeroParcela;
	}
	public int getPago() {
		return pago;
	}
	public void setPago(int pago) {
		this.pago = pago;
	}
	public Gerenciamento getGerenciamento() {
		return gerenciamento;
	}
	public void setGerenciamento(Gerenciamento gerenciamento) {
		this.gerenciamento = gerenciamento;
	}
	
	public LocalDate getDataPagamento() {
		return dataPagamento;
	}



	public void setDataPagamento(LocalDate dataPagamento) {
		this.dataPagamento = dataPagamento;
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
		Parcela other = (Parcela) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	

} // fecha classe
