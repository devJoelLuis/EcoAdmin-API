package eco.entidades.repositorios;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PermissoesStaticInsert {
	
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void perssistirRoles() {
		
		
		
		em.createNativeQuery("SET @@foreign_key_checks = 0;").executeUpdate(); //desativa verificação de chave estrangeira para delete
        em.createNativeQuery("SET SQL_SAFE_UPDATES = 0;").executeUpdate(); //desativa verificação de chava estrangeira para update
        
        em.createNativeQuery("DELETE FROM permissao;").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES (1, 'ROLE_ADMIN'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (1, 'ROLE_ADMIN'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (2, 'ROLE_FINANCAS'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (3, 'ROLE_CATEGORIA'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (4, 'ROLE_CATEGORIA_EXCLUI'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (5, 'ROLE_CLIENTE'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (6, 'ROLE_CLIENTE_EXCLUI'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (7, 'ROLE_CLIENTE_GERENCIAR'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (8, 'ROLE_FORNECEDOR'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (9, 'ROLE_FORNECEDOR_EXCLUI'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (10, 'ROLE_GERENCIAMENTO'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (11, 'ROLE_GERENCIAMENTO_EXCLUI'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (12, 'ROLE_HISTORICO'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (13, 'ROLE_LICENCA'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (14, 'ROLE_LICENCA_EXCLUI'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (15, 'ROLE_ORCAMENTO'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (16, 'ROLE_ORCAMENTO_EXCLUI'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (17, 'ROLE_OS'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (18, 'ROLE_OS_EXCLUI'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (19, 'ROLE_SENHA'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (20, 'ROLE_SENHA_EXCLUI'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (21, 'ROLE_STATUS_CLIENTE'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (22, 'ROLE_STATUS_CLIENTE_EXCLUI'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (23, 'ROLE_STATUS_FORNECEDOR'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (24, 'ROLE_STATUS_FORNECEDOR_EXCLUI'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (25, 'ROLE_STATUS_OS'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (26, 'ROLE_STATUS_OS_EXCLUI'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (27, 'ROLE_TECNICO'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (28, 'ROLE_TECNICO_EXCLUI'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (29, 'ROLE_PRAZO'); ").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  permissao (id, role) VALUES  (30, 'ROLE_PRAZO_EXCLUI'); ").executeUpdate();
        
        em.createNativeQuery("DELETE IGNORE FROM categoria_recebimento WHERE id = 1;").executeUpdate();
        em.createNativeQuery("INSERT IGNORE INTO  categoria_recebimento (id, descricao) VALUES  (1, 'SEM CATEGORIA'); ").executeUpdate();
        
        em.createNativeQuery("SET @@foreign_key_checks = 1;").executeUpdate(); // reativa verificação
        em.createNativeQuery("SET SQL_SAFE_UPDATES = 1;").executeUpdate();// reativa verificação
		
	}

}//fecha classe
