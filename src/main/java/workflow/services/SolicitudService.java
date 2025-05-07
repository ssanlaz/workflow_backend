package workflow.services;

import java.util.List;

import workflow.entidades.Solicitud;

public interface SolicitudService {
	
	Solicitud alta(Solicitud solicitud);
    Solicitud modificar(Solicitud solicitud);
    int eliminar(int idSolicitud);
    Solicitud buscarUna(int idSolicitud);
    List<Solicitud> buscarTodas();
    Solicitud buscarPorVacanteYUsuario(int idVacante, String email);
    List<Solicitud> buscarPorIdVacante(int idVacante);
    List<Solicitud> buscarPorEmailUsuario(String email);
    List<Solicitud>buscarPorEmpresa(int idEmpresa);

}
