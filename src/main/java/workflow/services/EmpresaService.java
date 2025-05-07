package workflow.services;

import java.util.List;

import workflow.entidades.Empresa;
import workflow.entidades.Usuario;

public interface EmpresaService {
	
	Empresa alta(Empresa empresa);
    Empresa modificar(Empresa empresa);
    int eliminar(int idEmpresa);
    Empresa buscarUna(int idEmpresa);
    Empresa buscarPorUsuario(Usuario usuario);
    Empresa buscarPorEmail(String email);
    List<Empresa> buscarTodas();

}
