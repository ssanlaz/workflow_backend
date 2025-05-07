package workflow.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import workflow.entidades.Empresa;
import workflow.entidades.Usuario;
import workflow.repository.EmpresaRepository;
@Service
public class EmpresaServiceImpl implements EmpresaService{
	
	@Autowired
	private EmpresaRepository empresaRepository;

	@Override
	public Empresa alta(Empresa empresa) {
		try {
            return empresaRepository.save(empresa);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	

	@Override
	public Empresa modificar(Empresa empresa) {
		try {
            if (empresaRepository.existsById(empresa.getIdEmpresa())) {
                return empresaRepository.save(empresa);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	

	@Override
	public int eliminar(int idEmpresa) {
		try {
            if (empresaRepository.existsById(idEmpresa)) {
                empresaRepository.deleteById(idEmpresa);
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
	public Empresa buscarUna(int idEmpresa) {
		
		return empresaRepository.findById(idEmpresa).orElse(null);
	}

	@Override
	public List<Empresa> buscarTodas() {
		
		return empresaRepository.findAll() ;
	}


	@Override
	public Empresa buscarPorUsuario(Usuario usuario) {
		
		return empresaRepository.findByUsuario(usuario);
	}


	@Override
	public Empresa buscarPorEmail(String email) {
		
		return empresaRepository.findByUsuarioEmail(email).orElse(null);
	}

}
