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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Table(name="recebimento")
@Data
public class Recebimento {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="forma_pagamento")
	private String formaPagamento;
	
	@NotNull(message="A data de pagamento é obrigatória")
	private LocalDate data;
	
	@NotNull(message="O valor do pagamento é obrigatório")
	private BigDecimal valor;
	
	private String obs;
	
	@ManyToOne
	@JoinColumn(name="lancamento_id")
	private Lancamento lancamento;
	
	@ManyToOne
	@JoinColumn(name="categoria_recebimento_id")
	@JsonIgnoreProperties("recebimentos")
	private CategoriaRecebimento categoriaRecebimento;

}
