package eco.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eco.entidades.Lembrete;
import eco.entidades.Tarefa;
import eco.entidades.Tecnico;

public class TecnicoTarefasDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Tecnico tecnico;
	private List<Tarefa> tarefas = new ArrayList<>();
	private List<Lembrete> lembretes = new ArrayList<>();

	public List<Tarefa> getTarefas() {
		return tarefas;
	}

	public void setTarefas(List<Tarefa> tarefas) {
		this.tarefas = tarefas;
	}

	public Tecnico getTecnico() {
		return tecnico;
	}

	public void setTecnico(Tecnico tecnico) {
		this.tecnico = tecnico;
	}

	public List<Lembrete> getLembretes() {
		return lembretes;
	}

	public void setLembretes(List<Lembrete> lembretes) {
		this.lembretes = lembretes;
	}
	
	

}//fecha classe
