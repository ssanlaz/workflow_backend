package workflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import workflow.entidades.Vacante;

public interface VacanteRepository extends JpaRepository<Vacante, Integer> {
	
	
	@Query("SELECT v FROM Vacante v WHERE v.empresa.idEmpresa = :idEmpresa AND v.estado = 'CREADA'")
	List<Vacante> buscarPorEmpresa(Integer idEmpresa);

}
