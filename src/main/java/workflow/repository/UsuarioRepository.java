package workflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import workflow.entidades.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String>{
	
	@Query("SELECT u FROM Usuario u WHERE u.email = ?1")
	Usuario findByEmail( String email);

	List<Usuario> findByRol(String rol);
	
	
}
