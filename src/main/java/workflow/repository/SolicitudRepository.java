package workflow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import workflow.entidades.Solicitud;

public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {

	
	
	Optional<Solicitud> findByVacanteIdVacanteAndUsuarioEmail(int idVacante, String email);
	List<Solicitud> findByVacanteIdVacante(int idVacante);
	List<Solicitud> findByUsuarioEmail(String email);

	@Query("SELECT s FROM Solicitud s WHERE s.vacante.empresa.idEmpresa = :idEmpresa")
	List<Solicitud> findByEmpresaId( int idEmpresa);

}
