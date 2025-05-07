package workflow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import workflow.entidades.Empresa;
import workflow.entidades.Usuario;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
	
	Empresa findByUsuario(Usuario usuario);
	Optional<Empresa> findByUsuarioEmail(String email);
}
