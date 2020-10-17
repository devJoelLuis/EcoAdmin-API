package eco.entidades;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserSistema extends User {
	private static final long serialVersionUID = 1L;
	
	private Usuario usuario;

	public UserSistema(Usuario usuario, String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	
	

}
