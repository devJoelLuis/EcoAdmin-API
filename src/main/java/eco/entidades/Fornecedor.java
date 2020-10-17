package eco.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="fornecedor")
public class Fornecedor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message="O nome é obrigatório")
	@Column(length=100)
	private String nome;
	
	@Column(length=255)
	private String telefone;
	
	@Column(length=60)
	private String email;
	
	@Column(length=60)
	private String rua;
	
	@Column(length=45)
	private String numero;
	
	@Column(length=80)
	private String bairro;
	
	@Column(length=80)
	private String cidade;
	
	@Column(length=45)
	private String cep;
	
	@Column(length=255)
	private String obs;
	
	@Transient
	@OneToMany(mappedBy="fornecedor", cascade = CascadeType.ALL)
	private List<StatusFornecedor> statusFornecedor = new ArrayList<>();
	
	
	
	
	public void alteracao(Fornecedor f) {
		this.id = f.getId();
		this.nome = f.getNome();
		this.telefone = f.getTelefone();
		this.email = f.getEmail();
		this.rua = f.getRua();
		this.numero = f.getNumero();
		this.bairro = f.getBairro();
		this.cidade = f.getCidade();
		this.cep = f.getCep();
		this.obs = f.getObs();
		this.statusFornecedor = f.getStatusFornecedor();
	}
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRua() {
		return rua;
	}
	public void setRua(String rua) {
		this.rua = rua;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
	
	public List<StatusFornecedor> getStatusFornecedor() {
		return statusFornecedor;
	}



	public void setStatusFornecedor(List<StatusFornecedor> statusFornecedor) {
		this.statusFornecedor = statusFornecedor;
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
		Fornecedor other = (Fornecedor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	

}// fecha classe
