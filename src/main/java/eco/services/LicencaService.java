package eco.services;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eco.classes.LicencaConsultaFiltro;
import eco.entidades.Cliente;
import eco.entidades.Licenca;
import eco.entidades.repositorios.ClienteRepository;
import eco.entidades.repositorios.LicencasRepository;
import eco.response.Response;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class LicencaService {
	
	@Autowired
	private LicencasRepository repo;
	
	@Autowired 
	private ClienteRepository repoCli;
	
	
	//cadastrar 
	public Response<Licenca> cadastrar(Licenca l) {
		Response<Licenca> response = new Response<>();
		
		try {
			validar(l, false);
			Optional<Cliente>  cliOp = repoCli.findById(l.getCliente().getId());
			if(!cliOp.isPresent()) {
				throw new Exception("não foi possível encontrar um cliente com o id "+ l.getCliente().getId());
			}
			l.setCliente(cliOp.get());
			response.setDados(repo.save(l));
			return response;
			
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar cadastrar uma licença: "+ e.getMessage());
			return response;
		}
	}


	private void validar(Licenca l, boolean alteracao) throws Exception {
	   if(l == null) {
		   throw new Exception("a licença está null");
	   }
	   if(l.getNumero() == null) {
		   throw new Exception("o número da licença está null");
	   }
	   if(l.getNumero().length() < 1) {
		   throw new Exception("o número da licença está incorreto");
	   }
	   if(l.getCliente() == null) {
		   throw new Exception("o cliente está null");
	   }
	   if(!alteracao) {
		   Long count = repo.countByNumero(l.getNumero());
		   if(count > 0) {
			   throw new Exception("o número de licença "+ l.getNumero() +" já está sendo utilizdo");
		   } 
	   }
	   if(alteracao) {
		   Long count = repo.countByNumeroAndIdNot(l.getNumero(), l.getId());
		   if(count > 0) {
			   throw new Exception("o número de licença "+ l.getNumero() +" já está sendo utilizdo");
		   } 
	   }
	 }
	
	
	
	//alterar
	public Response<Licenca> alterar(Licenca l) {
		Response<Licenca> response = new Response<>();
		
		try {
			validar(l, true);
			Optional<Cliente>  cliOp = repoCli.findById(l.getCliente().getId());
			if(!cliOp.isPresent()) {
				throw new Exception("não foi possível encontrar um cliente com o id "+ l.getCliente().getId());
			}
			Optional<Licenca> lbancoOp = repo.findById(l.getId());
			if(!lbancoOp.isPresent()) {
				throw new Exception("não foi possível encontrar uma licença com o id "+ l.getId());
			}
			Licenca lbanco = lbancoOp.get();
			lbanco.alterarLicenca(l);
			lbanco.setCliente(cliOp.get());
			response.setDados(repo.save(lbanco));
			return response;
			
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar editar uma licença: "+ e.getMessage());
			return response;
		}
	}

	
	
	//excluir
	public Response<String> excluir(Long id) {
		Response<String> response = new Response<>();
		
		try {
			Optional<Licenca> lbancoOp = repo.findById(id);
			if(!lbancoOp.isPresent()) {
				throw new Exception("não foi possível encontrar uma licença com o id "+ id);
			}
			repo.delete(lbancoOp.get());
			response.setDados("ok");
			return response;
			
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar excluir uma licença: "+ e.getMessage());
			return response;
		}
	}
	
	
	//get all by id cliente
	public Response<Page<Licenca>> getAllIdCLiente(int page, int size, int idcliente) {
		Response<Page<Licenca>> response = new Response<>();
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<Licenca> licencasPage = repo.findByArquivoMortoAndClienteIdOrderByDataVencimentoAsc(false, idcliente, pageable);
			response.setDados(licencasPage);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar buscar todas as licenças pelo id do cliente: "+ e.getMessage());
			return response;
		}
	}


	public Response<Licenca> getById(Long id) {
        Response<Licenca> response = new Response<>();
		
		try {
			
			Optional<Licenca> lbancoOp = repo.findById(id);
			if(!lbancoOp.isPresent()) {
				throw new Exception("não foi possível encontrar uma licença com o id "+ id);
			}
			response.setDados(repo.save(lbancoOp.get()));
			return response;
			
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar editar uma licença: "+ e.getMessage());
			return response;
		}
	}


	public Response<String> getUltimoNumero() {
		Response<String> response = new Response<>();
		try {
			Licenca l = repo.findTop1ByOrderByIdDesc();
			if(l == null) {
				response.setDados("");
				return response;
			}
			response.setDados(l.getNumero());
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar buscar o número da licença: "+ e.getMessage());
			return response;
		}
	}


	public Response<Page<Licenca>> getAllVencimento(int page, int size, int idcliente, String dtinicio, String dtfim) {
		Response<Page<Licenca>> response = new Response<>();
		try {
			
			LocalDate dtini = LocalDate.parse(dtinicio);
			LocalDate dtf = LocalDate.parse(dtfim);
			Pageable pageable = PageRequest.of(page, size);
			Page<Licenca> lPages = 
					repo.findByArquivoMortoAndClienteIdAndDataVencimentoBetweenOrderByDataVencimentoAsc(false, idcliente, dtini, dtf, pageable);
			response.setDados(lPages);
			return response;
			
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar buscar licenças por data: "+ e.getMessage());
		    return response;
		}
	}


	public Response<Page<Licenca>> getAllDtVencimento(int page, int size, String inicio, String fim, int alerta) {
		Response<Page<Licenca>> response = new Response<>();
		try {
			if (inicio == null || inicio.equals("")) {
			   throw new Exception("data de vencimento de inicio não foi informada");	
			}
			if (fim == null || fim.equals("")) {
				throw new Exception("data de vencimento final não foi informada");	
			}
			LocalDate dtinicio = LocalDate.parse(inicio);
			LocalDate dtfim = LocalDate.parse(fim);
			Pageable pageable = PageRequest.of(page, size);
			Page<Licenca> licencasPage = null;
			  if (alerta == 1) {
				  licencasPage = repo.findByArquivoMortoAndAlertaAndDataVencimentoBetweenOrderByDataVencimentoAsc(false, alerta, dtinicio, dtfim, pageable);
			  } else {
				  licencasPage = repo.findByArquivoMortoAndDataVencimentoBetweenOrderByDataVencimentoAsc(false, dtinicio, dtfim, pageable);
			  }
				
			
			 
			response.setDados(licencasPage);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar buscar todas as licenças: "+ e.getMessage());
			return response;
		}
	}

	
	

	public Response<Page<Licenca>> getAllDtVencimentoComParam(int page, int size, String inicio, String fim,
			String param, int alerta) {
		Response<Page<Licenca>> response = new Response<>();
		try {
			if (inicio == null || inicio.equals("")) {
			   throw new Exception("data de vencimento de inicio não foi informada");	
			}
			if (fim == null || fim.equals("")) {
				throw new Exception("data de vencimento final não foi informada");	
			}
			if (param == null) {
				throw new Exception("parametro não informado");
			}
			LocalDate dtinicio = LocalDate.parse(inicio);
			LocalDate dtfim = LocalDate.parse(fim);
			Pageable pageable = PageRequest.of(page, size);
			Page<Licenca> licencasPage = null;
			if (alerta == 1) {
				licencasPage = repo.findByArquivoMortoAndAlertaAndNumeroAndDataVencimentoBetweenOrderByDataVencimentoAsc(false, alerta, param, dtinicio, dtfim, pageable);
			} else {
				licencasPage = repo.findByArquivoMortoAndNumeroAndDataVencimentoBetweenOrderByDataVencimentoAsc(false, param, dtinicio, dtfim, pageable);
			}
			if (licencasPage.getNumberOfElements() > 0) {
				response.setDados(licencasPage);
				return response;
			}
			
			if (alerta == 1) {
				licencasPage = repo.findByArquivoMortoAndAlertaAndAssuntoContainingIgnoreCaseAndDataVencimentoBetweenOrderByDataVencimentoAsc(false, alerta, param, dtinicio, dtfim, pageable);	
			} else {
				licencasPage = repo.findByArquivoMortoAndAssuntoContainingIgnoreCaseAndDataVencimentoBetweenOrderByDataVencimentoAsc(false, param, dtinicio, dtfim, pageable);
			}
			
			response.setDados(licencasPage);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar buscar todas as licenças: "+ e.getMessage());
			return response;
		}
	}
	
	
	
	public Response<Page<Licenca>> getAllDtVencimentoAlertaAll(int page, int size, String inicio, String fim) {
		Response<Page<Licenca>> response = new Response<>();
		try {
			if (inicio == null || inicio.equals("")) {
			   throw new Exception("data de vencimento de inicio não foi informada");	
			}
			if (fim == null || fim.equals("")) {
				throw new Exception("data de vencimento final não foi informada");	
			}
			LocalDate dtinicio = LocalDate.parse(inicio);
			LocalDate dtfim = LocalDate.parse(fim);
			Pageable pageable = PageRequest.of(page, size);
			Page<Licenca> licencasPage = null;
			
			
				licencasPage = repo.findByArquivoMortoAndDataVencimentoBetweenOrderByDataVencimentoAsc(false, dtinicio, dtfim, pageable);
			
			 
			response.setDados(licencasPage);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar buscar todas as licenças: "+ e.getMessage());
			return response;
		}
	}
	
	
	
	
	public Response<Page<Licenca>> getAllVencidas(int page, int size) {
		Response<Page<Licenca>> response = new Response<>();
		try {
			
			Pageable pageable = PageRequest.of(page, size);
						
			 Page<Licenca> licencasPage = repo.findByArquivoMortoAndDataVencimentoLessThanEqualOrderByDataVencimentoAsc(false, LocalDate.now(), pageable);
					 
			response.setDados(licencasPage);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar buscar todas as licenças vencidas: "+ e.getMessage());
			return response;
		}
	}
	
	
	
	public Response<Page<Licenca>> getAllAlerta(int page, int size) {
		Response<Page<Licenca>> response = new Response<>();
		try {
			
			Pageable pageable = PageRequest.of(page, size);
						
			 Page<Licenca> licencasPage = repo.findByArquivoMortoAndAlertaAndDataAlertaLessThanEqualOrderByDataVencimentoAsc(false, 1, LocalDate.now(), pageable);
					 
			response.setDados(licencasPage);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar buscar todas as licenças em alerta: "+ e.getMessage());
			return response;
		}
	}
	
	

	public Response<Page<Licenca>> getAll(int page, int size) {
		Response<Page<Licenca>> response = new Response<>();
		try {
			
			Pageable pageable = PageRequest.of(page, size);
						
			 Page<Licenca> licencasPage = repo.findByArquivoMortoOrderByDataVencimentoAsc(false, pageable);
					 
			response.setDados(licencasPage);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar buscar todas as licenças: "+ e.getMessage());
			return response;
		}
	}


	
	
	
	
	// get relatorio
	public byte[] relatorio(Long id)  {
		try {
			Licenca dados = repo.findById(id).get();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String telefones = dados.getCliente().getCelular()+", "+dados.getCliente().getTelefone();
			Map<String, Object> parametros = new HashMap<>();
			parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
			parametros.put("numero", dados.getNumero());
			parametros.put("dataInicio", dados.getDataInicio().format(dtFormat));
			parametros.put("dataVencimento", dados.getDataVencimento().format(dtFormat));
			parametros.put("descricao", dados.getAssunto());
			parametros.put("obs", dados.getObs() == null ? "" : dados.getObs());
			parametros.put("clienteNome", dados.getCliente().getNome());
			parametros.put("clienteEmpresa", dados.getCliente().getNomeFantasia());
			parametros.put("telefones", telefones);
			parametros.put("email", dados.getCliente().getEmail() == null ? "" : dados.getCliente().getEmail());
			parametros.put("dataHora", LocalDateTime.now().plusHours(-3).format(formatter));
			
			
			
				InputStream inStream = this.getClass().getResourceAsStream("/relatorios/licenca.jasper");
				System.out.println("inStream criado");
				JRDataSource jrdts = new JREmptyDataSource();
				JasperPrint jp = JasperFillManager.fillReport(inStream, parametros, jrdts); //, new JRBeanCollectionDataSource(dados)
				return JasperExportManager.exportReportToPdf(jp);
			
			
		} catch (Exception e) {
			System.out.println(e.getCause());
			System.out.println(e.getMessage());
			return null;
		}
	}


	public Response<Page<Licenca>> getAllFiltro(LicencaConsultaFiltro lf) {
		Response<Page<Licenca>> response = new Response<Page<Licenca>>();
		System.out.println(lf.toString());
		
	  try {
			
		    //criar data inicio
		    LocalDate dtInicio = LocalDate.parse(lf.getDtInicio());
		    //criar data fim
		    LocalDate dtFim = LocalDate.parse(lf.getDtFim());
		   //criar pageable
		    Pageable pageable = PageRequest.of(lf.getPage(), lf.getSize());
		    
		    Page<Licenca> licPage = null;
		    
		    if (lf.getTipo().equals("vencimento")) {
		    	licPage = repo.findByArquivoMortoAndDataVencimentoBetweenOrderByDataVencimentoAsc(false, dtInicio, dtFim, pageable);
		    } else {
		    	if (lf.getParam().length() > 0) {
					// foi passado parametro para consulta assunto contains
					if (lf.getTipo().equals("tudo")) {
						// tudo ignora data inicio e fim
						licPage = repo.findByArquivoMortoAndAssuntoContainingOrNumeroContainingOrderByDataVencimentoAsc(false, lf.getParam(), lf.getParam(), pageable);
					} else if (lf.getTipo().equals("alerta")) {
						licPage = repo.findByArquivoMortoAndAlertaAndDataAlertaLessThanEqualAndAssuntoContainingOrNumeroContainingOrderByDataVencimentoAsc(
								                           false, 1, LocalDate.now(), lf.getParam(), lf.getParam(), pageable);
					} else if (lf.getTipo().equals("morto")) {
						licPage = repo.findByArquivoMortoOrderByDataVencimentoAsc(true, pageable);
					}
				} else {
					// sem parametros de consulta
					if (lf.getTipo().equals("tudo")) {
						// tudo ignora data inicio e fim
						licPage = repo.findByArquivoMortoOrderByDataVencimentoAsc(false, pageable);
					} else if (lf.getTipo().equals("alerta")) {
						licPage = repo.findByArquivoMortoAndAlertaAndDataAlertaLessThanEqualOrderByDataVencimentoAsc(false, 1, LocalDate.now(), pageable);
					} else if (lf.getTipo().equals("morto")) {
						licPage = repo.findByArquivoMortoOrderByDataVencimentoAsc(true, pageable);
					}
				}
		    }
			
			
			
			response.setDados(licPage);
			return response;
				
		} catch (Exception e) {
			response.getErros().add("Ocorreu o seguinte erro ao consultar um licença com filtro: "+ e.getMessage());
			return response;
		}
		
		
		
		
	}//fecha consulta com filtro








	

}// fecha classe

