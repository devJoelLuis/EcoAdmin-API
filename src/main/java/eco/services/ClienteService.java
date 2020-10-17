package eco.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eco.dto.ClienteDTO;
import eco.entidades.Cliente;
import eco.entidades.Historico;
import eco.entidades.Licenca;
import eco.entidades.Orcamento;
import eco.entidades.OrdemServico;
import eco.entidades.StatusCli;
import eco.entidades.repositorios.ClienteRepository;
import eco.entidades.repositorios.HistoricoRepository;
import eco.entidades.repositorios.LicencasRepository;
import eco.entidades.repositorios.OrcamentoRepository;
import eco.entidades.repositorios.OrdemServicoRepository;
import eco.entidades.repositorios.StatusCliRepository;
import eco.response.Response;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private HistoricoRepository repoH;
	
	@Autowired
	private StatusCliRepository repoEs;
	
	@Autowired
	private OrdemServicoService serviceOs;
	
    @Autowired
    private OrdemServicoRepository repoOs;
    
    @Autowired
    private OrcamentoRepository repoOrc;
    
    @Autowired
    private LicencasRepository repoLi;
	
	
	
	
	
	
	
	
	//cadastrar
	public Response<Cliente> cadastrar(Cliente c) {
		Response<Cliente> response = new Response<Cliente>();
		try {
			c.setId(null);// para 
			validarCliente(c);
			
			//verificar se nome já exite no banco de dados
			Long count = repo.countByNome(c.getNome());
			if(count > 0) {
				throw new Exception("o nome "+ c.getNome()+" já existe (cadastrado) no banco de dados");
			}
			//fim verificar se nome já exite
						
			c.setDataCadastro(LocalDateTime.now());

			Cliente clienteSalvo = repo.save(c);
			Historico his = new Historico();
			his.setDataEvento(LocalDateTime.now());
			his.setEvento("cadastro de um novo cliente: " +clienteSalvo.getNome()+ ", e id: "+clienteSalvo.getId());
			//TODO: adicionar informações do usuario ao histórico
			his.setUserEmail("teste email user");
			his.setUserId("5555");
			his.setUserNome("teste historico user nome");
			repoH.save(his);
			response.setDados(clienteSalvo);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar cadastrar um novo cliente: "+ e.getMessage());
			return response;
		}
	}

	private void validarCliente(Cliente c) throws Exception {
		if(c == null) {
			throw new Exception("o cliente está nulo");
		}
		if(c.getNome() == null) {
			throw new Exception("o nome do cliente está nulo");
		}
		if(c.getNome().length() < 2) {
			throw new Exception("o nome deve conter no mínimo dois caracteres");
		}
		
	}
	
	
	//alterar
	public Response<Cliente> Alterar(Cliente c) {
		Response<Cliente> response = new Response<Cliente>();
		try {
			validarCliente(c);
			if(c.getId() == null) {
				throw new Exception(" o id do cliente passado está nulo ");
			}
			Cliente clienteBanco = repo.findById(c.getId()).get();
			if(!clienteBanco.getNome().equals(c.getNome())) {
				Long count = repo.countByNomeAndIdNot(c.getNome(), c.getId());
				if(count > 0) {
					throw new Exception("o nome "+ c.getNome() +" já se encontra cadastrado no banco de dados");
				}
			}
			if(c.getStatusCli() != null) {
			    Optional<StatusCli> st = repoEs.findById(c.getStatusCli().getId());
			    c.setStatusCli(st.get());
			}
			
			clienteBanco.atualizaCliente(c);
			Cliente clienteSalvo = repo.save(clienteBanco);
			Historico his = new Historico();
			his.setDataEvento(LocalDateTime.now());
			his.setEvento("Editado cliente: " +clienteSalvo.getNome()+ ", e id: " +clienteSalvo.getId());
			//TODO: adicionar informações do usuario ao histórico
			his.setUserEmail("teste email user");
			his.setUserId("5555");
			his.setUserNome("teste historico user nome");
			repoH.save(his);
			response.setDados(clienteSalvo);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar editar o novo cliente: "+ e.getMessage());
			return response;
		}
	}
	
	
	
	
	
	
	//buscar por id
	public Response<Cliente> buscarPorId(int id) {
		Response<Cliente> response = new Response<Cliente>();
		try {
			Optional<Cliente> opCliente = repo.findById(id);
			if(!opCliente.isPresent()) {
				throw new Exception("não foi possível encontrar um cliente com o id: "+id);
			}
			response.setDados(opCliente.get());
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar buscar um cliente pelo id " +e.getMessage());
			return response;
		}
	}
	
	// listar todos
	public Response<Page<Cliente>> buscarTodos(int page, int size, String orderby) {
		Response<Page<Cliente>> response = new Response<>();
		try {
			Pageable pageable = PageRequest.of(page, size);
			
			
			switch(orderby) {
			case "nome":
				Page<Cliente>clientePage = repo.findAllByOrderByNomeAsc(pageable);
				response.setDados(clientePage);
				break;
			case "nomeDesc":
				response.setDados(repo.findAllByOrderByNomeDesc(pageable));
				break;	
			case "nomef":
				response.setDados(repo.findAllByOrderByNomeFantasiaAsc(pageable));
				break;
			case "nomefDesc":
				response.setDados(repo.findAllByOrderByNomeFantasiaDesc(pageable));
				break;
			case "tel":
				response.setDados(repo.findAllByOrderByTelefoneAsc(pageable));
				break;
			case "telDesc":
				response.setDados(repo.findAllByOrderByTelefoneDesc(pageable));
				break;	
			case "cel":
				response.setDados(repo.findAllByOrderByCelularAsc(pageable));
				break;
			case "celDesc":
				response.setDados(repo.findAllByOrderByCelularDesc(pageable));
				break;		
			case "email":
				response.setDados(repo.findAllByOrderByEmailAsc(pageable));
				break;
			default: 
				response.setDados(repo.findAllByOrderByNomeAsc(pageable));
				break;
			}//fecha switch
			
			
			
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar buscar todos os clientes" +e.getMessage());
			return response;
		}
	}
	
	
	//deletar cliente
	@Transactional
	public Response<String> deletar(int id) {
		Response<String> response = new Response<String>();
		try {
			Optional<Cliente> opCliente = repo.findById(id);
			if(!opCliente.isPresent()) {
				throw new Exception("não foi possível encontrar um cliente com o id: "+id);
			}
			Historico h = new Historico();
			h.setDataEvento(LocalDateTime.now());
			h.setEvento("Excluído o cliente de id: "+opCliente.get().getId()+" e nome "+opCliente.get().getNome());
			
			List<OrdemServico> Oss = repoOs.findByClienteIdOrderByAnoOsDescNumOsAsc(id);
			for(OrdemServico os: Oss) {
				serviceOs.excluirOs(os.getId());
			}
			//deletar orcamentos
			List<Orcamento>orcamentos = repoOrc.findByClienteId(id);
			if(orcamentos.size()>0) {
				repoOrc.deleteAll(orcamentos);
			}
			List<Licenca> licencas = repoLi.findByClienteId(id);
			if(licencas.size() > 0) {
				repoLi.deleteAll(licencas);
			}
			
			repo.delete(opCliente.get());//deleta o cliente
			
			//TODO: adicionar informações do usuario ao histórico
			h.setUserEmail("teste email user");
			h.setUserId("5555");
			h.setUserNome("teste historico user nome");
			repoH.save(h);
			
			response.setDados("ok");
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao tentar deletar um cliente: " +e.getMessage());
			return response;
		}
	}
	
	
    //buscar todos parte do nome
	public Response<Page<Cliente>> buscarTodosParteNome(int page, int size, String orderby, String nome) {
		Response<Page<Cliente>> response = new Response<>();
		try {
			if(nome == null) throw new Exception("Não foi possível consultar por nome, porque o nome está nullo");
			if(nome.equals("")) throw new Exception("Não foi possível consultar por nome, porque o nome está em branco");
			nome = nome.toUpperCase();
			Pageable pageable = PageRequest.of(page, size);
			Page<Cliente> clientesPag = null;
			
			switch(orderby) {
			case "nome":
			   	clientesPag = repo.findByNomeContainingOrderByNomeAsc(nome, pageable);
			   	break;
			case "nomeDesc":
				clientesPag = repo.findByNomeContainingOrderByNomeDesc(nome, pageable);
				break;	
			case "nomef":
				clientesPag = repo.findByNomeContainingOrderByNomeFantasiaAsc(nome, pageable);
				break;
			case "nomefDesc":
				clientesPag = repo.findByNomeContainingOrderByNomeFantasiaDesc(nome, pageable);
				break;
			case "tel":
				clientesPag = repo.findByNomeContainingOrderByTelefoneAsc(nome, pageable);
				break;
			case "telDesc":
				clientesPag = repo.findByNomeContainingOrderByTelefoneDesc(nome, pageable);
				break;	
			case "cel":
				clientesPag = repo.findByNomeContainingOrderByCelularAsc(nome, pageable);
				break;
			case "celDesc":
				clientesPag = repo.findByNomeContainingOrderByCelularDesc(nome, pageable);
				break;		
			default: 
				clientesPag = repo.findByNomeContainingOrderByNomeAsc(nome, pageable);
				break;
			}//fecha switch
			
			
			response.setDados(clientesPag);
			return response;
		} catch (Exception e) {
			response.getErros().add("Erro ao consultar por nome" +e.getMessage());
			return response;
		}
	}
	
	
	 //buscar todos parte do nome
		public Response<Page<Cliente>> buscarTodosParteNomeFantasia(int page, int size, String orderby, String nome) {
			Response<Page<Cliente>> response = new Response<>();
			try {
				if(nome == null) throw new Exception("Não foi possível consultar por nome fantasia, porque o nome está nullo");
				if(nome.equals("")) throw new Exception("Não foi possível consultar por nome fantasia, porque o nome está em branco");
				nome = nome.toUpperCase();
				Pageable pageable = PageRequest.of(page, size);
				Page<Cliente> clientesPag = null;
				
				switch(orderby) {
				case "nome":
				 	clientesPag = repo.findByNomeFantasiaContainingOrderByNomeAsc(nome, pageable);
				   	break;
				case "nomeDesc":
				 	clientesPag = repo.findByNomeFantasiaContainingOrderByNomeDesc(nome, pageable);
				   	break;	
				case "nomef":
					clientesPag = repo.findByNomeFantasiaContainingOrderByNomeFantasiaAsc(nome, pageable);
				   	break;
				case "nomefDesc":
					clientesPag = repo.findByNomeFantasiaContainingOrderByNomeFantasiaDesc(nome, pageable);
				   	break;
				case "tel":
					clientesPag = repo.findByNomeFantasiaContainingOrderByTelefoneAsc(nome, pageable);
				   	break;
				case "telDesc":
					clientesPag = repo.findByNomeFantasiaContainingOrderByTelefoneDesc(nome, pageable);
				   	break;	
				case "cel":
					clientesPag = repo.findByNomeFantasiaContainingOrderByCelularAsc(nome, pageable);
				  	break;
				case "celDesc":
					clientesPag = repo.findByNomeFantasiaContainingOrderByCelularDesc(nome, pageable);
				    break;		
				default: 
					clientesPag = repo.findByNomeFantasiaContainingOrderByNomeAsc(nome, pageable);
				    break;
				}//fecha switch
				
				
				response.setDados(clientesPag);
				return response;
			} catch (Exception e) {
				response.getErros().add("Erro ao tentar consultar por nome fantasia" +e.getMessage());
				return response;
			}
		}

		
		//get todos por parametro nome ou nome fantasia retornado uma lista dto de cliente, utilizado em consultas rápidas
		public Response<List<ClienteDTO>> getClientesDtoParam(String param) {
			Response<List<ClienteDTO>> response = new Response<List<ClienteDTO>>();
			try {
				param = param + "%";
				List<ClienteDTO> clientes = new ArrayList<ClienteDTO>();
				List<?> retorno = repo.buscaPorNomeEnomeFantasiaContem(param);
				
				for (int i = 0; i < retorno.size(); i++) {
					ClienteDTO clidto = new ClienteDTO();
					Object[] temp = (Object[]) retorno.get(i);
					clidto.setId((int)temp[0]);
					clidto.setNome((String)temp[1]);
					clidto.setNomeFantasia((String)temp[2]);
					clientes.add(clidto);
				}
				
				response.setDados(clientes);
				return response;
			} catch (Exception e) {
				response.getErros().add("Erro ao tentar consultar por nome clienteDto:" +e.getMessage());
				return response;
			}
		}
	
	
	
		
	

}//fecha classe 
