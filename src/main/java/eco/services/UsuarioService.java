package eco.services;

import java.util.List;
import java.util.Optional;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import eco.dto.SenhaUsuarioDTO;
import eco.entidades.Usuario;
import eco.entidades.repositorios.SenhaUsuarioDto;
import eco.entidades.repositorios.UsuarioRepository;
import eco.response.Response;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository repo;
	
	
	private BCryptPasswordEncoder encode;
	
	
	@Autowired
	public UsuarioService(BCryptPasswordEncoder encode) {
		this.encode = encode;
	}



	@Autowired
	private SenhaUsuarioDto repoSenhaUser;
	
	
	// cadastrar
	public Response<Usuario> cadastrar(Usuario u){
		Response<Usuario> response = new Response<>();
		try {
			 validarUsuario(u, false);
			 u.setSenha(encode.encode(u.getSenha()));
			 response.setDados(repo.save(u));
			 return response;
			
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao tentar cadastrar um usuário: "+ e.getMessage());
			return response;
		}	
	}

	
	
	private void validarUsuario(Usuario u, boolean edit) throws Exception {
		
		if (edit) {
			if (u.getId() == null) throw new Exception("o id não foi passado");
			
			Long count = repo.countByEmailAndIdNot(u.getEmail(), u.getId());
			if (count > 0) throw new Exception("o e-mail passado já está sendo utilizado por outro usuário do sistema");
			
			count = repo.countByNomeAndIdNot(u.getNome(), u.getId());
			if (count > 0) throw new Exception("o nome já está cadastrado no sistema");
		} else {
			
			Long count = repo.countByEmail(u.getEmail());
			if (count > 0) throw new Exception("o e-mail passado já está sendo utilizado por outro usuário do sistema");
			
			count = repo.countByNome(u.getNome());
			if (count > 0) throw new Exception("o nome já está cadastrado no sistema");
			
		}
	}
	
	
	
	
	// alterar
	public Response<Usuario> alterar(Usuario u){
		Response<Usuario> response = new Response<>();
		try {
			 validarUsuario(u, true);
			 SenhaUsuarioDTO senhaUserDto = new SenhaUsuarioDTO();
			
			 Optional<Usuario> userOp = repo.findById(u.getId());
			 if (!userOp.isPresent()) {
				 throw new Exception("não foi possível encontrar um usuário com o id "+ u.getId());
			 }
			 Usuario userBanco = userOp.get();
			 if (!u.getSenha().equals("none")) {
				 u.setSenha(encode.encode(u.getSenha()));
			 } else {
				 senhaUserDto = repoSenhaUser.findById(u.getId()).get();
				 u.setSenha(senhaUserDto.getSenha());
			 }
			
			 BeanUtils.copyProperties(userBanco, u);
			 //userBanco.setPermissoes(u.getPermissoes());
			 response.setDados(repo.save(userBanco));
			 return response;
			
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao tentar alterar um usuário: "+ e.getMessage());
			return response;
		}	
	}
	
	// excluir
	public Response<String> excluir(Integer id){
		Response<String> response = new Response<>();
		try {
			
			 Optional<Usuario> userOp = repo.findById(id);
			 if (!userOp.isPresent()) {
				 throw new Exception("não foi possível encontrar um usuário com o id "+ id);
			 }
			 Usuario userBanco = userOp.get();
			 repo.delete(userBanco);
			 response.setDados("ok");
			 return response;
			
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao tentar excluir um usuário: "+ e.getMessage());
			return response;
		}	
	}
	
	//get id
	public Response<Usuario> getById(Integer id){
		Response<Usuario> response = new Response<>();
		try {
			 Optional<Usuario> userOp = repo.findById(id);
			 if (!userOp.isPresent()) {
				 throw new Exception("não foi possível encontrar um usuário com o id "+ id);
			 }
			 Usuario userBanco = userOp.get();
			 response.setDados(userBanco);
			 return response;
			
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao tentar buscar um usuário por id: "+ e.getMessage());
			return response;
		}	
	}
	
	
	
	//get all
	public Response<List<Usuario>> getAll() {
		Response<List<Usuario>> response = new Response<>();
		try {
			 List<Usuario> usuarios = repo.findAll();
			 response.setDados(usuarios);
			 return response;
			
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao tentar buscar um usuário por id: "+ e.getMessage());
			return response;
		}	
	}
	
	
	//get user por email
	public Response<Usuario> getByEmail(String email){
		Response<Usuario> response = new Response<>();
		try {
			//IMPORTANTE somente o usuário logado pode requerer os dados dele mesmo
			
			// pegar o usuário logado
			String username = getUsuarioAuthenticado().toLowerCase().trim();
					 Usuario ubanco = repo.findByEmail(email);
			 if (ubanco == null) {
				 throw new Exception("não foi possível encontrar um usuário com e-mail "+ email);
			 }
			// compara se é o mesmo username (email) caso não seja a pessoa não tem permissao
			if(!ubanco.getEmail().equals(username)) {
				throw new Exception("você não tem permissão para alterar o usuário " + ubanco.getNome());
			}
						
			 response.setDados(ubanco);
			 return response;
			
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao tentar buscar um usuário pelo e-mail: "+ e.getMessage());
			return response;
		}	
	}
	
	
	
	// alterar perfil
	public Response<Usuario> alterarPerfil(Usuario u){
		Response<Usuario> response = new Response<>();
		try {
			// verificar se o usuário está alterando ele mesmo
			String username = getUsuarioAuthenticado();
			
			Usuario ubanco = repo.findByEmail(username);
			if (ubanco == null) {
				throw new Exception("não foi possível encontrar no banco de dados e-mail "+ username);
			}
			
			if(ubanco.getId() != u.getId()) {
				throw new Exception("você não tem permissão para alterar o usuário " + ubanco.getNome());
			}
			
		
			 validarUsuario(u, true);
			 SenhaUsuarioDTO senhaUserDto = new SenhaUsuarioDTO();
			
			 Optional<Usuario> userOp = repo.findById(u.getId());
			 if (!userOp.isPresent()) {
				 throw new Exception("não foi possível encontrar um usuário com o id "+ u.getId());
			 }
			 Usuario userBanco = userOp.get();
			 if (!u.getSenha().equals("none")) {
				 u.setSenha(encode.encode(u.getSenha()));
			 } else {
				 senhaUserDto = repoSenhaUser.findById(u.getId()).get();
				 u.setSenha(senhaUserDto.getSenha());
			 }
			//fixa a permissão porque o usuário não pode alterar permissão atraves do perfil,
			 u.setPermissoes(ubanco.getPermissoes()); 
			 BeanUtils.copyProperties(userBanco, u);
			 response.setDados(repo.save(userBanco));
			 return response;
			
			
		} catch (Exception e) {
			response.getErros().add("Ocorreu um erro ao tentar alterar o perfil do usuário: "+ e.getMessage());
			return response;
		}	
	}
	
	
	
	// busca o usuário logado
	private String getUsuarioAuthenticado() throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = "";
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    username = authentication.getName();
		} 
		if (username == null || username.equals("")) {
	     	throw new Exception("não foi possível encontrar o e-mail do usuário logado");
		}
		return username;
	}
	
	

}// fecha classe
