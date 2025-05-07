package workflow.services;

import java.util.List;

import workflow.entidades.Vacante;

public interface VacanteService {
	
	Vacante alta(Vacante vacante);
    Vacante modificar(Vacante vacante);
    int eliminar(int idVacante);
    Vacante buscarUna(int idVacante);
    List<Vacante>buscarPorEmpresa(int idEmpresa);
    List<Vacante> buscarTodas();

}
