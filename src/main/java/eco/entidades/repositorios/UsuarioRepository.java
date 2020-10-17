package eco.entidades.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import eco.entidades.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	public Usuario findByEmail(String email);

	public Long countByEmailAndIdNot(String email, Integer id);

	public Long countByNomeAndIdNot(String nome, Integer id);

	public Long countByEmail(String email);

	public Long countByNome(String nome);
}
