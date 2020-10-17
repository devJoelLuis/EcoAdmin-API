package eco.dto;

import java.io.Serializable;

public class ServicoFiltroDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int horaTecnica;
	private int pago;
	private int alerta;
	private String vencimentoIni;
	private String vencimentoFim;
	private String param;
	private int page;
	private int size;
	
	
	
	
	
	public int getHoraTecnica() {
		return horaTecnica;
	}
	public void setHoraTecnica(int horaTecnica) {
		this.horaTecnica = horaTecnica;
	}
	public int getPago() {
		return pago;
	}
	public void setPago(int pago) {
		this.pago = pago;
	}
	public int getAlerta() {
		return alerta;
	}
	public void setAlerta(int alerta) {
		this.alerta = alerta;
	}
	
	public String getVencimentoIni() {
		return vencimentoIni;
	}
	public void setVencimentoIni(String vencimentoIni) {
		this.vencimentoIni = vencimentoIni;
	}
	public String getVencimentoFim() {
		return vencimentoFim;
	}
	public void setVencimentoFim(String vencimentoFim) {
		this.vencimentoFim = vencimentoFim;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
		

}//fecha classe
