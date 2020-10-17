package eco.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="tecnico")
public class Tecnico implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length=60)
	private String nome;
	
	@Column(length=60)
	private String telefone;
	
	@Column(length=60)
	private String email;
	
	@Column(length=60)
	private String obs;
	
	 @JsonIgnoreProperties(value = {"tecnico"})
     @OneToMany(mappedBy="tecnico")
	private List<Lembrete> lembretes = new ArrayList<>();
	
		
	
	
	
	
	
	
	
	public void alterarTecnico(Tecnico t) {
		this.id = t.getId();
		this.nome = t.getNome();
		this.telefone = t.getTelefone();
		this.email = t.getEmail();
		this.obs = t.getObs();
		this.lembretes = t.getLembretes();
		
	}
	
	
	
	
	
	
	public List<Lembrete> getLembretes() {
		return lembretes;
	}






	public void setLembretes(List<Lembrete> lembretes) {
		this.lembretes = lembretes;
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
		Tecnico other = (Tecnico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	

}//fecha classe
