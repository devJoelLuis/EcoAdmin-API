package eco.dto;

import java.io.Serializable;

public class ReciboDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
   private String total;
   private String recebemosde;
   private String importancia;
   private String referente;
   private String dia;
   private String mes;
   private String ano;
   
   
   
   
   
   
public ReciboDTO() {
}



public ReciboDTO(String total, String recebemosde, String importancia, String referente, String dia, String mes,
		String ano) {
	this.total = total;
	this.recebemosde = recebemosde;
	this.importancia = importancia;
	this.referente = referente;
	this.dia = dia;
	this.mes = mes;
	this.ano = ano;
}



public String getTotal() {
	return total;
}
public void setTotal(String total) {
	this.total = total;
}
public String getRecebemosde() {
	return recebemosde;
}
public void setRecebemosde(String recebemosde) {
	this.recebemosde = recebemosde;
}
public String getImportancia() {
	return importancia;
}
public void setImportancia(String importancia) {
	this.importancia = importancia;
}
public String getReferente() {
	return referente;
}
public void setReferente(String referente) {
	this.referente = referente;
}
public String getDia() {
	return dia;
}
public void setDia(String dia) {
	this.dia = dia;
}
public String getMes() {
	return mes;
}
public void setMes(String mes) {
	this.mes = mes;
}
public String getAno() {
	return ano;
}
public void setAno(String ano) {
	this.ano = ano;
}
   
   
}//fecha classe
