package eco.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="cliente")
public class Cliente implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length=60)
	private String nome;
	
	@Column(length=60, name="nome_fantasia")
	private String nomeFantasia;
	
	@Column(length=45)
	private String cnpj;
	
	@Column(length=45)
	private String rg;
	
	@Column(length=45)
	private String telefone;
	
	@Column(length=45)
	private String celular;
	
	@Column(length=45)
	private String email;
	
	@Column(length=60)
	private String rua;
	
	@Column(length=45)
	private String numero;
	
	@Column(length=45)
	private String bairro;
	
	@Column(length=45)
	private String cep;
	
	@Column(length=45)
	private String cidade;
	
	@Column(length=255)
	private String obs;
	
	@Column(name="data_cadastro")
	private LocalDateTime dataCadastro;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	private StatusCli statusCli;

	
	


	public void atualizaCliente(Cliente c) {
		this.nome = c.getNome();
		this.nomeFantasia = c.getNomeFantasia();
		this.cnpj = c.getCnpj();
		this.rg = c.getRg();
		this.telefone = c.getTelefone();
		this.celular = c.getCelular();
		this.email = c.getEmail();
		this.rua = c.getRua();
		this.numero = c.getNumero();
		this.bairro = c.getBairro();
		this.cep = c.getCep();
		this.cidade = c.getCidade();
		this.obs = c.getObs();
		this.dataCadastro = c.getDataCadastro();
		this.statusCli = c.getStatusCli();
		
	}



	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
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

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public LocalDateTime getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	

	public StatusCli getStatusCli() {
		return statusCli;
	}



	public void setStatusCli(StatusCli statusCli) {
		this.statusCli = statusCli;
	}



	public Integer getId() {
		return id;
	}
    
   

	


	public void setId(Integer id) {
		this.id = id;
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
		Cliente other = (Cliente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	
	
	
}//fecha classe Cliente
