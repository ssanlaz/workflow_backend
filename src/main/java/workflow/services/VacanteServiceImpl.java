package workflow.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import workflow.entidades.Vacante;
import workflow.repository.VacanteRepository;
@Service
public class VacanteServiceImpl implements VacanteService {
	
	@Autowired
	private VacanteRepository vacanteRepository;

	@Override
	public Vacante alta(Vacante vacante) {
		try {
            return vacanteRepository.save(vacante);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	@Override
	public Vacante modificar(Vacante vacante) {
		try {
            if (vacanteRepository.existsById(vacante.getIdVacante())) {
                return vacanteRepository.save(vacante);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	@Override
	public int eliminar(int idVacante) {
		try {
            if (vacanteRepository.existsById(idVacante)) {
                vacanteRepository.deleteById(idVacante);
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
	public Vacante buscarUna(int idVacante) {
		
		return vacanteRepository.findById(idVacante).orElse(null);
	}

	@Override
	public List<Vacante> buscarTodas() {
		
		return vacanteRepository.findAll();
	}

	@Override
	public List<Vacante> buscarPorEmpresa(int idEmpresa) {
		
		return vacanteRepository.buscarPorEmpresa(idEmpresa);
	}

}
