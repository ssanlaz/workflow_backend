package workflow.services;

import java.util.List;

import workflow.entidades.Usuario;

public interface UsuarioService {
	
	Usuario alta(Usuario usuario);
    Usuario modificar(Usuario usuario);
    int eliminar(String email);
    Usuario buscarUno(String email);
    List<Usuario> buscarTodos();
    List<Usuario> buscarPorRol(String rol);

}
