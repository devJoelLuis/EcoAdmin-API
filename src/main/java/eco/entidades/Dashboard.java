package eco.entidades;

import java.io.Serializable;

public class Dashboard implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	
	
	
	private int tclientes; //total de clientes cadastrados
	private int tos; //total de O.S. no sistema
	private int tOsAlerta;
	private int tservico; //total de servico no sistema
	private int tservicoAlerta;
	private int tservicoVencidos;
	private int tprazosAlerta;
	private int tprazosVencidos;
	private int tprazosVencer;
	private int thorasTecnicas;
	private int thorasTecnicasVencidas;
	private Double valorHorasTecnicasVencidas;
	private int tservicoCusto;
	private int tservicoCustoVencidos;
	private Double valorServicosCustoVencidos;
	private int tLicencas;
	private int tLicencasAlerta;
	private int licencasVaiVencer;
	private int licencasVencidas;
	private int tOrcamentosAlerta;
	private int tOrcamento;
	private Double tAreceber; // total a receber
	private Double tApagar; // total a pagar
	
	
	
	
	
	
	
	
	
	public int getLicencasVencidas() {
		return licencasVencidas;
	}
	public void setLicencasVencidas(int licencasVencidas) {
		this.licencasVencidas = licencasVencidas;
	}
	public int gettLicencas() {
		return tLicencas;
	}
	public void settLicencas(int tLicencas) {
		this.tLicencas = tLicencas;
	}
	public int getLicencasVaiVencer() {
		return licencasVaiVencer;
	}
	public void setLicencasVaiVencer(int licencasVaiVencer) {
		this.licencasVaiVencer = licencasVaiVencer;
	}
	public int gettOrcamento() {
		return tOrcamento;
	}
	public void settOrcamento(int tOrcamento) {
		this.tOrcamento = tOrcamento;
	}
	public int getTclientes() {
		return tclientes;
	}
	public void setTclientes(int tclientes) {
		this.tclientes = tclientes;
	}
	public int getTos() {
		return tos;
	}
	public void setTos(int tos) {
		this.tos = tos;
	}
	public int getTservico() {
		return tservico;
	}
	public void setTservico(int tservico) {
		this.tservico = tservico;
	}
	public int getTservicoAlerta() {
		return tservicoAlerta;
	}
	public void setTservicoAlerta(int tservicoAlerta) {
		this.tservicoAlerta = tservicoAlerta;
	}
	public int gettOsAlerta() {
		return tOsAlerta;
	}
	public void settOsAlerta(int tOsAlerta) {
		this.tOsAlerta = tOsAlerta;
	}
	public int gettLicencasAlerta() {
		return tLicencasAlerta;
	}
	public void settLicencasAlerta(int tLicencasAlerta) {
		this.tLicencasAlerta = tLicencasAlerta;
	}
	public int gettOrcamentosAlerta() {
		return tOrcamentosAlerta;
	}
	public void settOrcamentosAlerta(int tOrcamentosAlerta) {
		this.tOrcamentosAlerta = tOrcamentosAlerta;
	}
	public Double gettAreceber() {
		return tAreceber;
	}
	public void settAreceber(Double tAreceber) {
		this.tAreceber = tAreceber;
	}
	public Double gettApagar() {
		return tApagar;
	}
	public void settApagar(Double tApagar) {
		this.tApagar = tApagar;
	}
	public int getTservicoVencidos() {
		return tservicoVencidos;
	}
	public void setTservicoVencidos(int tservicoVencidos) {
		this.tservicoVencidos = tservicoVencidos;
	}
	public int getThorasTecnicas() {
		return thorasTecnicas;
	}
	public void setThorasTecnicas(int thorasTecnicas) {
		this.thorasTecnicas = thorasTecnicas;
	}
	public int getThorasTecnicasVencidas() {
		return thorasTecnicasVencidas;
	}
	public void setThorasTecnicasVencidas(int thorasTecnicasVencidas) {
		this.thorasTecnicasVencidas = thorasTecnicasVencidas;
	}
	public Double getValorHorasTecnicasVencidas() {
		return valorHorasTecnicasVencidas;
	}
	public void setValorHorasTecnicasVencidas(Double valorHorasTecnicasVencidas) {
		this.valorHorasTecnicasVencidas = valorHorasTecnicasVencidas;
	}
	public int getTservicoCusto() {
		return tservicoCusto;
	}
	public void setTservicoCusto(int tservicoCusto) {
		this.tservicoCusto = tservicoCusto;
	}
	public int getTservicoCustoVencidos() {
		return tservicoCustoVencidos;
	}
	public void setTservicoCustoVencidos(int tservicoCustoVencidos) {
		this.tservicoCustoVencidos = tservicoCustoVencidos;
	}
	public Double getValorServicosCustoVencidos() {
		return valorServicosCustoVencidos;
	}
	public void setValorServicosCustoVencidos(Double valorServicosCustoVencidos) {
		this.valorServicosCustoVencidos = valorServicosCustoVencidos;
	}
	public int getTprazosAlerta() {
		return tprazosAlerta;
	}
	public void setTprazosAlerta(int tprazosAlerta) {
		this.tprazosAlerta = tprazosAlerta;
	}
	public int getTprazosVencidos() {
		return tprazosVencidos;
	}
	public void setTprazosVencidos(int tprazosVencidos) {
		this.tprazosVencidos = tprazosVencidos;
	}
	public int getTprazosVencer() {
		return tprazosVencer;
	}
	public void setTprazosVencer(int tprazosVencer) {
		this.tprazosVencer = tprazosVencer;
	}
	
	
    
	
	

}// fecha classe
