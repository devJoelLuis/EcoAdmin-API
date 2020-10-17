package eco.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="status_fornecedor")
public class StatusFornecedor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length=255)
	private String descricao;
	
	@Column(length=255)
	private String obs;
	
	@ManyToOne
	@JoinColumn(name="fornecedor_id")
	@JsonIgnoreProperties("statusFornecedor")
	private Fornecedor fornecedor;
	
	
	
	
	
	public void alteracao(StatusFornecedor sf) {
		this.id = sf.getId();
		this.descricao = sf.getDescricao();
		this.obs = sf.getObs();
	
	}
	
	
	

	



	public Fornecedor getFornecedor() {
		return fornecedor;
	}








	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}








	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
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
		StatusFornecedor other = (StatusFornecedor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}// fecha classe
