package eco.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import eco.entidades.UserSistema;
import eco.entidades.Usuario;
import eco.entidades.repositorios.UsuarioRepository;

@Service
public class UserDetailServiceImp implements UserDetailsService {
	
	@Autowired
    private UsuarioRepository repoUser;
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario user = repoUser.findByEmail(email);
		if(user == null) {
			throw new UsernameNotFoundException("Email ou senha invalido");
		}
		return new UserSistema(user, email, user.getSenha(), getPermissoes(user));
	}


	private Collection<? extends GrantedAuthority> getPermissoes(Usuario user) {
		Set<SimpleGrantedAuthority> aut = new HashSet<>();
		user.getPermissoes().forEach(p -> aut.add(new SimpleGrantedAuthority(p.getRole())));
		return aut;
	}


	
}
