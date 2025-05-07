package workflow.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import workflow.entidades.Usuario;
import workflow.repository.UsuarioRepository;
@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public Usuario alta(Usuario usuario) {
		try {
            if (usuarioRepository.existsById(usuario.getEmail())) {
                return null;
            }
            return usuarioRepository.save(usuario);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	

	@Override
	public Usuario modificar(Usuario usuario) {
		try {
            if (!usuarioRepository.existsById(usuario.getEmail())) {
                return null;
            }
            return usuarioRepository.save(usuario);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	@Override
	public int eliminar(String email) {
		try {
            if (usuarioRepository.existsById(email)) {
                usuarioRepository.deleteById(email);
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

	@Override
	public Usuario buscarUno(String email) {
		
		return usuarioRepository.findByEmail(email);
	}

	@Override
	public List<Usuario> buscarTodos() {
		
		return usuarioRepository.findAll();
	}


	@Override
	public List<Usuario> buscarPorRol(String rol) {
		
		return usuarioRepository.findByRol(rol);
	}

}
