package eco.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;


@Data
public class OrdemServicoDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private int numOs;
	private int anoOs;
	private String statusOs;
	private String tecnico;
	private LocalDate dataInicio;
	private String obs;
	private String local;
	private int alerta;
	private LocalDate dataAlerta;
	
	// cliente
	private String clienteNome;
	private String clienteEmpresa;
	private String telefones;
	private String email;
	
	
	
	//erro ao gerar relatorio
	private List<String> erros = new ArrayList<>();
	
	//relatorio
	private LocalDateTime dataHoraGeracaoRelatorio;
	
	
	
	
	
	
	
	
public OrdemServicoDTO() {
		
	}




public OrdemServicoDTO(Long id, int numOs, int anoOs, String statusOs, String tecnico, LocalDate dataInicio,
			String obs, String local, int alerta, LocalDate dataAlerta, String clienteNome, String clienteEmpresa,
			String telefones, String email) {
		this.id = id;
		this.numOs = numOs;
		this.anoOs = anoOs;
		this.statusOs = statusOs;
		this.tecnico = tecnico;
		this.dataInicio = dataInicio;
		this.obs = obs;
		this.local = local;
		this.alerta = alerta;
		this.dataAlerta = dataAlerta;
		this.clienteNome = clienteNome;
		this.clienteEmpresa = clienteEmpresa;
		this.telefones = telefones;
		this.email = email;
	}




	
	
	
	

}// fecha classe
