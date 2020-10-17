package eco.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import eco.entidades.EmailAlerta;
import eco.entidades.Licenca;
import eco.entidades.Orcamento;
import eco.entidades.OrdemServico;
import eco.entidades.Prazo;
import eco.entidades.repositorios.EmailAlertaRepository;
import eco.entidades.repositorios.LicencasRepository;
import eco.entidades.repositorios.OrcamentoRepository;
import eco.entidades.repositorios.OrdemServicoRepository;
import eco.entidades.repositorios.PrazoRepository;
import eco.mail.Mailer;

@Service
public class AlertasGeralService {
	
 @Autowired
 private EmailAlertaRepository repo;
 

 
 @Autowired
 private OrdemServicoRepository repoOs;
 
 @Autowired
 private LicencasRepository repoLicenca;
 
 @Autowired
 private OrcamentoRepository repoOrcamento;
 
 @Autowired
 private PrazoRepository repoPrazo;
 
 @Autowired
private Mailer mailer;
 
 
 
 
 
  
  // padrao cron;     segundos,  minutos, hora, dia do mes, mes dias semana
  @Scheduled(cron = "0 30 10 * * *")
  public void enviarAlertasEmail() {
	  System.out.println("executou");
	  System.out.println(LocalDateTime.now().toString());
	try {
		  List<EmailAlerta> emails = repo.findAll();  
		  if (emails != null) {
			  if (emails.size() > 0) {
				  
				  //variaveis
				  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				  
				  
				  
				  // buscar os alertas e gerar um corpo de email
				  StringBuilder sb = new StringBuilder("SISTEMA COAMI INFORMA EVENTOS EM ALERTAS\n\n\n");
				  
				  
				  
				  
				  
				  
				  // buscar alertas prazos
				  List<Prazo> prazos = repoPrazo.findByCumpridoAndAlertaAndDataAlertaLessThanEqual(false, true, LocalDate.now());
				  if (prazos != null && prazos.size() > 0) {
					sb.append("*****  PRAZOS EM ALERTA *****\n\n");
					for (Prazo p: prazos) {
						sb.append("CLIENTE: "+ p.getOs().getCliente().getNome()+"\n");
						sb.append("O.S.: " + p.getOs().getNumOs()+"/" + p.getOs().getAnoOs() + " - " + p.getOs().getLocal()+"\n");
						sb.append("DATA DE ALERTA: "+ p.getDataAlerta().format(formatter)+"\n");
						sb.append("DESCRIÇÃO: "+ p.getDescricao()+"\n");
						sb.append("VENCIMENTO: "+ p.getDataVencimento().format(formatter)+"\n");
						sb.append("\n\n");
					}
					 sb.append("\n\n\n");
				  }
				 
				  // termina buscar alerta prazos
				  
				  
				  
				  
				  
				  // buscar alertas os
				  List<OrdemServico> oss = repoOs.findByAlertaAndDataAlertaLessThanEqual(1, LocalDate.now());
				  if (oss != null && oss.size() > 0) {
					  sb.append("***** ORDENS DE SERVIÇO EM ALERTA *****\n\n");
					  for (OrdemServico os: oss) {
						  sb.append("DATA DE ALERTA: "+ os.getDataAlerta().format(formatter)+"\n");
						  sb.append("O.S Nº: "+ os.getNumOs()+"/"+ os.getAnoOs()+" - ");
						  sb.append("DESCRIÇÃO: "+ os.getLocal());
						  
						  if (os.getObs() != null) sb.append("\n OBSERVAÇÃO: "+ os.getObs());
						  sb.append(".\n\n");
					  }
					  sb.append("\n\n\n");
				  }
				
				  //termina alertas os
				  
				  
				  

				  
				  //buscar alertas licencas
				  List<Licenca> licencas = repoLicenca.findByArquivoMortoAndAlertaAndDataAlertaLessThanEqual(false, 1, LocalDate.now());
				  if ( licencas != null && licencas.size() > 0) {
					  sb.append("***** LICENCAS EM ALERTA *****\n\n");
					  for (Licenca l: licencas) {
						  sb.append("DATA DE ALERTA: "+ l.getDataAlerta().format(formatter)+"\n");
						  sb.append("LICENÇA Nº: "+ l.getNumero()+"\n");
						  sb.append("VENCIMENTO: "+ l.getDataVencimento().format(formatter)+ "\n");
						  sb.append("ASSUNTO: "+ l.getAssunto()+"\n");
						  sb.append("CLIENTE: "+ l.getCliente().getNome()+"\n");
						  sb.append("CLIENTE NOME FANTASIA: "+ l.getCliente().getNomeFantasia());
						  
						  if (l.getObs() != null)  sb.append("\n OBSERVAÇÃO: "+ l.getObs());
						  
						  sb.append(".\n\n");
						  
					  }
					  sb.append("\n\n\n");
				  }
				 
				  //temina alertas licencas
				  
				  
				  
				  
				  
				  
				  //buscar alertas orcamentos
				  List<Orcamento> orcamentos = repoOrcamento.findByAlertaAndDataAlertaLessThanEqual(1, LocalDate.now());
				  if (orcamentos != null && orcamentos.size() > 0) {
					  sb.append("***** ORÇAMENTOS EM ALERTA *****\n\n");
					  for (Orcamento o: orcamentos) {
						  sb.append("DATA DE ALERTA: "+ o.getDataAlerta().format(formatter)+"\n");
						  sb.append("ORÇAMENTO Nº: "+ o.getNumero() + "/"+o.getAno()+"\n");
						  sb.append("ASSUNTO: "+ o.getAssunto()+"\n");
						  sb.append("DATA ENTREGA: "+ o.getDataEntrega().format(formatter)+ "\n");
						  sb.append("CLIENTE: "+ o.getCliente().getNome()+"\n");
						  sb.append("CLIENTE NOME FANTASIA: "+ o.getCliente().getNomeFantasia());
						  
						  if (o.getObs() != null) sb.append("\n OBSERVAÇÃO: "+ o.getObs());
						 
					      sb.append(".\n\n");
						
						  
					  }
					  sb.append("\n\n\n");
				  }
				  
				  //buscar alertas orcamentos
				  
				  
				  
				  
				  // criar lista de emails string
				  List<String> emailsEnviar = new ArrayList<String>();
				  for (EmailAlerta ea: emails) {
					  emailsEnviar.add(ea.getEmail());
				  }
				  
				  //enviar email
				 mailer.enviarEmail("coami.socorro@gmail.com", emailsEnviar, "URGENTE: Sistema Coami alertas", sb.toString());
				  
				  
				  
			  } else {
				  System.out.println("Nenhum e-mail cadastrado");
			  }
		  }
		 
	} catch (Exception e) {
		System.out.println("Erro na agenda: "+ e.getMessage());
	}
	  
  }

  

  
}// fecha classe
