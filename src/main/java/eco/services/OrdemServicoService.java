package eco.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eco.classes.TotaisCalculados;
import eco.dto.OrdemServicoFiltroDTO;
import eco.dto.OsFiltroDTO;
import eco.entidades.Cliente;
import eco.entidades.Lancamento;
import eco.entidades.OrdemServico;
import eco.entidades.Recebimento;
import eco.entidades.StatusOs;
import eco.entidades.Tecnico;
import eco.entidades.repositorios.ClienteRepository;
import eco.entidades.repositorios.LancamentoRepository;
import eco.entidades.repositorios.OrdemServicoRepository;
import eco.entidades.repositorios.StatusOsRepository;
import eco.entidades.repositorios.TecnicoRepository;
import eco.response.Response;

@Service
public class OrdemServicoService {
	
	@Autowired
	private OrdemServicoRepository repo;
	
   @Autowired    
   private TecnicoRepository repoTec;
   
   @Autowired
   private StatusOsRepository repoStOs;
   
   
   
   @Autowired
   private ClienteRepository repoCli;
   
   @Autowired
   private LancamentoRepository repoLanc;
	
	
 
	
	
	// cadastrar
	public Response<OrdemServico> cadastrarOs(OrdemServico os) {
		Response<OrdemServico> response = new Response<>();
		try {
			if(os.getDataInicio() == null) {
				throw new Exception("deve ser informada a data de início");
			}
			if( os.getNumOs() == 0 ) {
				os.setNumOs(gerarNumOs());
				os.setAnoOs(LocalDate.now().getYear());
			} else {
				Long count = repo.countByNumOsAndAnoOs(os.getNumOs(), os.getAnoOs());
				if(count > 0) {
					throw new Exception("já existe uma OS com número "+ os.getNumOs() + " cadastrada no sistema");
				}
			}
			//buscar o cliente
			Optional<Cliente> cliOp = repoCli.findById(os.getCliente().getId());
			if (!cliOp.isPresent()) {
				throw new Exception("não foi possível carregar o cliente com o id: "+ os.getCliente().getId());
			}
			os.setCliente(cliOp.get());
			os.setId(null);
			OrdemServico osSalvo = repo.save(os);
			response.setDados(osSalvo);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar cadastrar uma ordem de serviço: "+e.getMessage());
			return response;
		}
		
		
	}// fecha metodo cadastrar
	
	
	//gera um numero de os automaticamente baseado no ultimo id do banco e ano atual
	private int gerarNumOs() {
		OrdemServico o = repo.findTop1ByAnoOsOrderByNumOsDesc(LocalDate.now().getYear());
		int num;
		// se não existir nenhuma os retornará null
		if(o == null) {
		  num = 1;	
		} else {
		 num = o.getNumOs() + 1;
		}
		return num;
	}
	
	
	public Response<String> getUltimoNumeroOs() {
		Response<String> response = new Response<>();
		try {
			OrdemServico s = repo.findTop1ByAnoOsOrderByNumOsDesc(LocalDate.now().getYear());
			if(s == null) {
				response.setDados("1/" + LocalDate.now().getYear());
				return response;
			}
			String ret = (s.getNumOs() + 1)+ "/" + s.getAnoOs();
			response.setDados(ret);
			return response;
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return response;
		}
	}
	
	
	

	// alterar
	public Response<OrdemServico> editarOs(OrdemServico os) {
		Response<OrdemServico> response = new Response<>();
		try {
			if(os.getDataInicio() == null) {
				throw new Exception("deve ser informada a data de início");
			}
		     Optional<OrdemServico> osOp = repo.findById(os.getId());
		     if(!osOp.isPresent()) {
		    	 throw new Exception("não foi possível encontrar uma ordem de serviço com id "+ os.getId());
		     }
		     
		     //buscar status
		     Optional<StatusOs> stOp = repoStOs.findById(os.getStatusOs().getId());
		     if(!stOp.isPresent()) {
		    	 throw new Exception("não foi possível encontrar um status com o id "+ os.getStatusOs().getId() );
		     }
		    		     
		     //buscar tecnico
		     Optional<Tecnico> tecOp= repoTec.findById(os.getTecnico().getId());
		     if(!tecOp.isPresent()) {
		    	 throw new Exception("não foi possível encontrar um técnico com o id "+ os.getTecnico().getId() );
		     }
		     /*
		     // verificar se o status é Arquivado
		     // se for verificar se tem pendencias financeiras
		     // se tiver pendencias não pode ser arquivado
		     if(os.getStatusOs().getId() == 6) { //6 é o id do status de os 'Arquivado'
		    	 //verificar pendencias
		    	 Long count = repoServ.countByOrdemServicoIdAndPago(os.getId(), 0);
		    	 if(count > 0) {
		    		 throw new Exception("a O.S. não pode ser arquivada porque possue serviços com pendências financeiras!!!");
		    	 }
		     }
		     */
		    OrdemServico osBanco = osOp.get();
		    BeanUtils.copyProperties(os, osBanco);
		    osBanco.setStatusOs(stOp.get());
		    osBanco.setTecnico(tecOp.get());
			OrdemServico osSalvo = repo.save(osBanco);
			response.setDados(osSalvo);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar editar uma ordem de serviço: "+e.getMessage());
			return response;
		}
		
		
	}// fecha metodo editar
	
	
	
	
	
	
	
	// excluir
	@Transactional
	public Response<String> excluirOs(Long id) {
		Response<String> response = new Response<>();
		try {
			

			Optional<OrdemServico> osOp = repo.findById(id);
			if (!osOp.isPresent()) {
				throw new Exception("não foi possível encontrar uma ordem de serviço com id "+ id);
			}
			repo.delete(osOp.get());
			response.setDados("ok");
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar excluir uma ordem de serviço: "+e.getMessage());
			return response;
		}
		
		
	}// fecha metodo deletar
	
	
	
	
	
	
	// get por id
	public Response<OrdemServico> buscarPorId(Long id) {
		Response<OrdemServico> response = new Response<>();
		try {
			
		     Optional<OrdemServico> osOp = repo.findById(id);
		     if(!osOp.isPresent()) {
		    	 throw new Exception("não foi possível encontrar uma ordem de serviço com id "+ id);
		     }
		    OrdemServico osBanco = osOp.get();
			response.setDados(osBanco);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar buscar por id uma ordem de serviço: "+e.getMessage());
			return response;
		}
		
		
	}// fecha metodo get id
	
	
	
	
	
	// get por cliente
	public Response<List<OrdemServico>> buscarPorCliente(int idcli) {
		Response<List<OrdemServico>> response = new Response<>();
		try {
		     List<OrdemServico> os = repo.findByClienteIdOrderByAnoOsDescNumOsDesc(idcli);
			response.setDados(os);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar buscar por cliente uma ordem de serviço: "+e.getMessage());
			return response;
		}
		
		
	}// fecha metodo get por cliente
	
	
	
	
	
	
	
	// get todas
	public Response<Page<OrdemServico>> listarTodasDataInicio(int page, int size, String data ) {
		Response<Page<OrdemServico>> response = new Response<>();
		try {
			Pageable pageable = PageRequest.of(page, size); 
			if(data != null) {
//				 LocalDate datald = LocalDate.parse(data);
//				 Page<OrdemServico> pageOs = repo.findByDataInicioThanEqual(datald);
//				 response.setDados(pageOs);
				 return response;
			 } else {
				 Page<OrdemServico> pageOs = repo.findAllByOrderByAnoOsDescNumOsDesc(pageable);
				 response.setDados(pageOs);
				 return response;
			 }
		    
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar buscar ordens de serviços: "+e.getMessage());
			return response;
		}
		
		
	}// fecha metodo get por cliente
	
	
	
	// usado na filtragem get all sem parametro
	public Response<Page<OsFiltroDTO>> getAll(int page, int size, int ano) {
		Response<Page<OsFiltroDTO>> response = new Response<>();
		try {
			Pageable pageable = PageRequest.of(page, size); 
			 List<OrdemServico> listOs = repo.findByIdAfterOrderByAnoOsDescNumOsDesc(0L, pageable);
			 Long total = repo.countByIdGreaterThan(0L);
			 Page<OsFiltroDTO> pageOs = converterOstoOsFiltroDTOPage(listOs, pageable, total);
		     response.setDados(pageOs);
			 return response;
			
		    
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar buscar todas as ordens de serviços: "+e.getMessage());
			return response;
		}
		
		
	}// fecha metodo 


	
	// get all alerta
	public Response<Page<OsFiltroDTO>> getAllAlerta(int page, int size, int ano) {
		Response<Page<OsFiltroDTO>> response = new Response<>();
		try {
			Pageable pageable = PageRequest.of(page, size); 
			 List<OrdemServico> listOs = repo.findByAlertaAndDataAlertaLessThanEqualOrderByAnoOsDescNumOsDesc(pageable, 1, LocalDate.now());
			 Long total = repo.countByAlertaAndDataAlertaLessThanEqualOrderByAnoOsDescNumOsDesc(1, LocalDate.now());
			 Page<OsFiltroDTO> pageOs = converterOstoOsFiltroDTOPage(listOs, pageable, total); 
			 
		     response.setDados(pageOs);
			 return response;
			
		    
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar buscar ordens de serviços em alerta: "+e.getMessage());
			return response;
		}
	}
	
	
	
	
	
	
	
	// get all ano/param
	// usado na filtragem get all com parametro
		public Response<Page<OsFiltroDTO>> getAllComParametro(int page, int size, int ano, String param) {
			Response<Page<OsFiltroDTO>> response = new Response<>();
			int numeroOs = 0;
			try {
				Pageable pageable = PageRequest.of(page, size); 
				 Page<OsFiltroDTO> pageOs;
				 
				 // procura na descrição(local)
				 List<OrdemServico> listOs = repo.findByAnoOsAndLocalContainingOrderByAnoOsDescNumOsDesc(pageable, ano, param);
				 Long total = repo.countByAnoOsAndLocalContainingOrderByAnoOsDescNumOsDesc(ano, param);
				 
				 //se não encontrou na descrição procura no número
				 String arrayString[] = param.split("/");
				 try {
					numeroOs = Integer.parseInt(arrayString[0]);
				} catch (Exception e) {
					pageOs = converterOstoOsFiltroDTOPage(listOs, pageable, total);
					response.setDados(pageOs);
					 return response;
				}
				 
				 List<OrdemServico> listOs2 = repo.findByAnoOsAndNumOsOrderByAnoOsDescNumOsDesc(pageable, ano, numeroOs);	
				 total += repo.countByAnoOsAndNumOsOrderByAnoOsDescNumOsDesc(ano, numeroOs);
				 
				 listOs.addAll(listOs2);
				 
				 pageOs = converterOstoOsFiltroDTOPage(listOs, pageable, total);
				 
				 response.setDados(pageOs);
				 return response;
			   
			    
			} catch (Exception e) {
				response.getErros().add("Erro ao tentar buscar todas as ordens de serviços com parametro: "+e.getMessage());
				return response;
			}
			
			
		}// fecha metodo 


		public Response<Page<OrdemServico>> getAllAlarmeComParametro(int page, int size, int ano, String param) {
			Response<Page<OrdemServico>> response = new Response<>();
			int numeroOs = 0;
			try {
				Pageable pageable = PageRequest.of(page, size); 
				 Page<OrdemServico> pageOs;
				 
				 // procura na descrição(local)
				 pageOs = repo.findByAlertaAndDataAlertaLessThanEqualAndAnoOsAndLocalContainingOrderByAnoOsDescNumOsDesc(pageable, 1, LocalDate.now() , ano, param);
				 if (pageOs.getNumberOfElements() > 0) {
					 response.setDados(pageOs);
					 return response;					 
				 }
				 
				 //se não encontrou na descrição procura no número
				 String arrayString[] = param.split("/");
				 try {
					numeroOs = Integer.parseInt(arrayString[0]);
				} catch (Exception e) {
					 response.setDados(pageOs);
					 return response;
				}
				 System.out.println(numeroOs);
				 pageOs = repo.findByAlertaAndDataAlertaLessThanEqualAndAnoOsAndNumOsOrderByAnoOsDescNumOsDesc(pageable, 1, LocalDate.now(), ano, numeroOs);			 
			     response.setDados(pageOs);
				 return response;
				
			    
			} catch (Exception e) {
				response.getErros().add("Erro ao tentar buscar todas as ordens de serviços com parametro: "+e.getMessage());
				return response;
			}
		}

		
		// usado na filtragem get all por id do status
		public Response<Page<OsFiltroDTO>> getAllStatusId(int page, int size, int ano, int idstatus) {
			Response<Page<OsFiltroDTO>> response = new Response<>();
			try {
				Pageable pageable = PageRequest.of(page, size); 
				 List<OrdemServico> listOs = repo.findByAnoOsAndStatusOsIdOrderByAnoOsDescNumOsDesc(pageable, ano, idstatus);
				 Long total = repo.countByAnoOsAndStatusOsIdOrderByAnoOsDescNumOsDesc(ano, idstatus);
				 // calcular valor pendencia
				 Page<OsFiltroDTO> pageOs = converterOstoOsFiltroDTOPage(listOs, pageable, total);
			     response.setDados(pageOs);
				 return response;
				
			    
			} catch (Exception e) {
				response.getErros().add("Erro ao tentar buscar todas as ordens de serviços: "+e.getMessage());
				return response;
			}
			
			
		}// fecha metodo 
		
		
		
		

        // transforma uma lista de os em um Page osFiltroDto
		private Page<OsFiltroDTO> converterOstoOsFiltroDTOPage(List<OrdemServico> listOs, Pageable pageable, Long total) throws Exception {
			
			List<OsFiltroDTO> listDto = new ArrayList<>();
						
			for (OrdemServico os: listOs) {
				OsFiltroDTO dto = new OsFiltroDTO();
				dto.setIdos(os.getId());
				dto.setDescricaoOs(os.getLocal());
				dto.setNomeCliente(os.getCliente().getNome());
				dto.setStatus(os.getStatusOs().getNome());
				dto.setAnoOs(os.getAnoOs());
				dto.setNumOs(os.getNumOs());
				dto.setStatusCor(os.getStatusOs().getCor());
				TotaisCalculados tc = getCalcularTotais(os.getId());
				dto.setTotalLancado(tc.getTotalLancado());
				dto.setTotal(tc.getTotalReceber() - tc.getTotalRecebido());
				dto.setTotalPago(tc.getTotalRecebido());
				dto.setPendenciaFinanceira(tc.getTotalPendencia());			
				
				listDto.add(dto);
			}
			
			Page<OsFiltroDTO> dtoPage = new PageImpl<OsFiltroDTO>(listDto, pageable, total);
			return dtoPage;
			
		}

        
		private TotaisCalculados  getCalcularTotais(Long id) {
			List<Lancamento> ls =  repoLanc.findByOrdemServicoId(id);
			LocalDate now = LocalDate.now();
			TotaisCalculados tc = new TotaisCalculados();
			for (Lancamento l: ls) {
				if (l.getTotal() != null)
				tc.setTotalLancado(tc.getTotalLancado() + l.getTotal());
				if (l.isPago()) continue;
					Double recebido = 0.00;
					tc.setTotalReceber( tc.getTotalReceber() + l.getTotal());
					for (Recebimento r: l.getRecebimentos()) {
						tc.setTotalRecebido(tc.getTotalRecebido() + r.getValor().doubleValue());
						recebido += r.getValor().doubleValue();
					}
					if ((recebido < l.getTotal()) && 
							(l.getDataLancamento().isEqual(now) || l.getDataLancamento().isBefore(now))) {
						tc.setTotalPendencia( tc.getTotalPendencia() + (l.getTotal() - recebido));
					}
			}
			return tc;
		}


		/*
		// busca e calcula o total da pendencia financeira de uma O.S.
		private Double getPendenciasOs(Long idos) throws Exception {
			Double pendencia = 0.00;
			List<Lancamento> lancamentos = repoLanc.findByPagoAndOrdemServicoIdAndDataLancamentoLessThanEqual(false, idos, LocalDate.now());
			for (Lancamento l: lancamentos) {
				Double recebido = 0.00;
				for (Recebimento r: l.getRecebimentos()) {
					recebido += r.getValor().doubleValue();
				}
				if (l.getTotal() > recebido) {
					pendencia += l.getTotal() - recebido; 
				}
			}
			return pendencia;
		}


		
		// metodo utilizado para buscar os dados para o relatório da O.S.
		private OrdemServicoDTO getRelatorio (Long idOs) {
			OrdemServicoDTO osDto = new OrdemServicoDTO();
			try {
				// busca os
			 Optional<OrdemServico> osOp = repo.findById(idOs);
			 if(!osOp.isPresent()) {
				 throw new Exception("não foi possível encontrar uma O.S. com o id "+ idOs);
			 }
			 OrdemServico os = osOp.get();
			 osDto.setAlerta(os.getAlerta());
			 osDto.setAnoOs(os.getAnoOs());
			 osDto.setClienteEmpresa(os.getCliente().getNomeFantasia());
			 osDto.setClienteNome(os.getCliente().getNome());
			 osDto.setDataAlerta(os.getDataAlerta());
			 osDto.setDataInicio(os.getDataInicio());
			 osDto.setId(os.getId());
			 osDto.setLocal(os.getLocal());
			 osDto.setNumOs(os.getNumOs());
			 osDto.setObs(os.getObs());
			 osDto.setStatusOs(os.getStatusOs().getNome());
			 osDto.setTecnico(os.getTecnico().getNome());
			 osDto.setTelefones(os.getCliente().getCelular()+ ", "+ os.getCliente().getTelefone());
			 osDto.setEmail(os.getCliente().getEmail());
			 osDto.setDataHoraGeracaoRelatorio(LocalDateTime.now());
			 return osDto;
			} catch (Exception e) {
				osDto.getErros().add("Erro ao tentar gerar o relatório de O.S: "+e.getMessage());
				return osDto;
			}
		}
		
		*/
		
		
		/*
		public byte[] relatorioOs(Long id)  {
			try {
				OrdemServicoDTO dados = getRelatorio(id);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
				Map<String, Object> parametros = new HashMap<>();
				parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
				parametros.put("numOs", String.valueOf(dados.getNumOs()));
				parametros.put("anoOs", String.valueOf(dados.getAnoOs()));
				parametros.put("statusOs", dados.getStatusOs());
				parametros.put("descricao", dados.getLocal());
				parametros.put("tecnico", dados.getTecnico());
				parametros.put("obs", dados.getObs() == null ? "" : dados.getObs());
				parametros.put("nomeCliente", dados.getClienteNome());
				parametros.put("empresa", dados.getClienteEmpresa());
				parametros.put("telefones", dados.getTelefones() == null ? ""  : dados.getTelefones());
				parametros.put("email", dados.getEmail() == null ? "" : dados.getEmail());
				parametros.put("dataHora", LocalDateTime.now().plusHours(-3).format(formatter));
				
				
				if (dados.getServicos() == null || dados.getServicos().size() == 0) {
					InputStream inStream = this.getClass().getResourceAsStream("/relatorios/OsSemServico.jasper");
					System.out.println("inStream criado");
					JRDataSource jrdts = new JREmptyDataSource();
					JasperPrint jp = JasperFillManager.fillReport(inStream, parametros, jrdts); //, new JRBeanCollectionDataSource(dados)
					return JasperExportManager.exportReportToPdf(jp);
				} else {
					for( Servico s: dados.getServicos()) {
						if (s.getDataVencimento() != null)
						s.setVencimentoTemp(s.getDataVencimento());
					}
					InputStream inStream = this.getClass().getResourceAsStream("/relatorios/OrdemServico.jasper");
					System.out.println("inStream criado");
					JasperPrint jp = JasperFillManager.fillReport(inStream, parametros, 
							new JRBeanCollectionDataSource(dados.getServicos())); //, new JRBeanCollectionDataSource(dados)
					return JasperExportManager.exportReportToPdf(jp);
				}
				
			} catch (Exception e) {
				System.out.println(e.getCause());
				System.out.println(e.getMessage());
				return null;
			}
			
		}
		*/


		public Response<List<OrdemServico>> getToNumOrDescricao(String param) {
			Response<List<OrdemServico>> response = new Response<List<OrdemServico>>();
			try {
				if (param.length() < 2) {
					throw new Exception("informe no mínimo 2 caracters para consulta!");
				}
				if (param.contains("/")) {
				  String[] numos = param.split("/");
				  List<OrdemServico> oss = repo.findByNumOsAndAnoOsOrderByLocalAsc(Integer.valueOf(numos[0]), Integer.valueOf(numos[1]));
				  response.setDados(oss);
				  return response;
				} else {
					List<OrdemServico> oss = repo.findByLocalContainingIgnoreCaseOrderByLocalAsc(param);
					 response.setDados(oss);
					  return response;
				}
				
			    			
			} catch (Exception e) {
				response.getErros().add("Erro ao tentar buscar por número ou descrição: "+e.getMessage());
				return response;
			}
		}


		public Response<Page<OsFiltroDTO>> getAllPedenciaAnoSemParam(int page, int size, int ano) {
			Response<Page<OsFiltroDTO>> response = new Response<Page<OsFiltroDTO>>();
			try {
				
				Pageable pageable = PageRequest.of(page, size); 
				List<OrdemServico> listOs = repo.buscarTodasComPendencia(ano,LocalDate.now(), pageable);
				Long total = repo.buscarCountTodasComPendencia(ano, LocalDate.now());
				Page<OsFiltroDTO> pageDto = converterOstoOsFiltroDTOPage(listOs, pageable, total);
				response.setDados(pageDto);
				return response;
			} catch (Exception e) {
				response.getErros().add("Erro ao tentar buscar por pendências financeiras sem parametro: "+e.getMessage());
				return response;
			}
		}

		
		
		//get all O.S. do cliente
		public Response<List<OsFiltroDTO>> getAllOsClienteId(Integer idcliente, int ano) {
			Response<List<OsFiltroDTO>> response = new Response<>();
			try {
				if (!repoCli.existsById(idcliente)) {
					throw new Exception("não existe um cliente com o id "+ idcliente);
				}
				
				List<OrdemServico> oss = repo.findByAnoOsAndClienteIdOrderByNumOsDesc(ano, idcliente);
				
				List<OsFiltroDTO> osDto = converterOstoOsFiltroDTOList(oss);
				
				response.setDados(osDto);
				return response;
				
			} catch (Exception e) {
				response.getErros().add("Ocorreu um erro ao consultar as O.S.: " + e.getMessage());
				return response;
			}
		}
		
		
        // convert uma lista de os em OsfiltroDto para listar na tela do cliente 
		private List<OsFiltroDTO> converterOstoOsFiltroDTOList(List<OrdemServico> listOs) {
			List<OsFiltroDTO> listDto = new ArrayList<>();
			
			for (OrdemServico os: listOs) {
				OsFiltroDTO dto = new OsFiltroDTO();
				dto.setIdos(os.getId());
				dto.setDescricaoOs(os.getLocal());
				dto.setNomeCliente(os.getCliente().getNome());
				dto.setStatus(os.getStatusOs().getNome());
				dto.setAnoOs(os.getAnoOs());
				dto.setNumOs(os.getNumOs());
				dto.setStatusCor(os.getStatusOs().getCor());
				TotaisCalculados tc = getCalcularTotais(os.getId());
				dto.setTotal(tc.getTotalReceber() - tc.getTotalRecebido());
				dto.setTotalPago(tc.getTotalRecebido());
				dto.setPendenciaFinanceira(tc.getTotalPendencia());			
							
				
				listDto.add(dto);
			}
			
			return listDto;
		}
		
		
		
		

        // filtro de os 
		public Response<Page<OsFiltroDTO>> getAllFiltro(OrdemServicoFiltroDTO f) {
			Response<Page<OsFiltroDTO>> response = new Response<Page<OsFiltroDTO>>();
			try {
				Pageable pageable = PageRequest.of(f.getPage(), f.getSize()); 
				int anoOs = 0;
				int numOs = 0;
				
				// verificar se foi passado ano e numero da os, se sim faz a conversão para int
				if (f.getAnoNumOs() != null && f.getAnoNumOs().length() > 4) {
					String[] result = f.getAnoNumOs().split("/");
					if (result.length > 1) {
						try {
							numOs = Integer.parseInt(result[0]);
							anoOs = Integer.parseInt(result[1]);
						} catch (Exception e) {
							
						}
					} 
				}
				
				//verificar se foi passado somente ano da os, se sim convert para int
				if (f.getAnoNumOs() != null && f.getAnoNumOs().length() == 4) {
					try {
						anoOs = Integer.parseInt(f.getAnoNumOs());
					} catch (Exception e) {
					}
				}
				
			  List<OrdemServico> oss = new ArrayList<OrdemServico>();
			  Long totalEncontrado = 0L;
			  
			  //verifica qual tipo de consulta
			  switch(f.getTipoConsulta()) {
			  
				  case "alerta": 
					  if (anoOs > 0 && numOs > 0) {
						  oss = repo.findByAlertaAndNumOsAndAnoOsAndDataAlertaLessThanEqualOrderByAnoOsDescNumOsDesc(
								  pageable, 1, numOs, anoOs, LocalDate.now());
						  totalEncontrado = repo.countByAlertaAndNumOsAndAnoOsAndDataAlertaLessThanEqual( 1, numOs, anoOs, LocalDate.now());
					  } else
					  if (numOs == 0 && anoOs > 0) {
						  oss = repo.findByAlertaAndAnoOsAndDataAlertaLessThanEqualOrderByAnoOsDescNumOsDesc(
								  pageable, 1, anoOs, LocalDate.now());
						  totalEncontrado = repo.countByAlertaAndAnoOsAndDataAlertaLessThanEqual(1, anoOs, LocalDate.now());
					  } else {
						  oss = repo.findByAlertaAndDataAlertaLessThanEqualOrderByAnoOsDescNumOsDesc(1, LocalDate.now(), pageable);
						  totalEncontrado = repo.countByAlertaAndDataAlertaLessThanEqual(1, LocalDate.now());
					  }
					  break;
				  case "pendencia":
                      if (anoOs > 0 && numOs > 0) {
                    	  oss = repo.buscarTodasComPendenciaNumOsAnoOs(numOs, anoOs, LocalDate.now(), pageable);
                    	  totalEncontrado = repo.buscarCountTodasComPendenciaNumOsAnoOs(numOs, anoOs, LocalDate.now());
					  } else
					  if (numOs == 0 && anoOs > 0) {
						  oss = repo.buscarTodasComPendencia(anoOs, LocalDate.now(), pageable);
						  totalEncontrado = repo.buscarCountTodasComPendencia(anoOs, LocalDate.now());
					  } else {
						  oss = repo.buscarAllPendencia(LocalDate.now(), pageable);
						  totalEncontrado = repo.buscarCountAllPendencia(LocalDate.now());
					  }
					  break;
				  default:
					  //todas O.S.
                      if (anoOs > 0 && numOs > 0) {
						 oss = repo.findByNumOsAndAnoOs( numOs, anoOs); 
						 totalEncontrado = repo.countByNumOsAndAnoOs( numOs, anoOs);
					  } else 
					  if (numOs == 0 && anoOs > 0) {
						  oss = repo.findByAnoOsOrderByAnoOsDescNumOsDesc(anoOs, pageable); 
						  totalEncontrado = repo.countByAnoOs(anoOs);
					  } else {
						  oss = repo.findByIdGreaterThanOrderByAnoOsDescNumOsDesc(0L, pageable);
						  totalEncontrado = repo.countByIdGreaterThan(0L);
					  }
					  break;
			  
			  }//fecha switch
			  
			    Page<OsFiltroDTO> pageDto = converterOstoOsFiltroDTOPage(oss, pageable, totalEncontrado);
				response.setDados(pageDto);
				return response;
			  
			  
			} catch (Exception e) {
				response.getErros().add(e.getMessage());
				return response;
			}
		}


		public Response<List<OrdemServico>> getToAnoClienteAno(String param, int ano) {
			Response<List<OrdemServico>> response = new Response<List<OrdemServico>>();
			try {
				
				List<OrdemServico> oss = 
						repo.findByClienteNomeContainingIgnoreCaseAndAnoOsOrClienteNomeFantasiaContainingAndAnoOsOrderByClienteNomeFantasiaAsc(param, ano, param, ano);
				response.setDados(oss);
				return response;
				
			} catch (Exception e) {
				response.getErros().add("Ocorreu o seguinte erro ao tentar pesquisar pelo nome do cliente: "+e.getMessage());
				return response;
			}
		}


		
	  

}// fecha classe
