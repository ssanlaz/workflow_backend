package workflow.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import workflow.entidades.Solicitud;
import workflow.repository.SolicitudRepository;
@Service
public class SolicitudServiceImpl implements SolicitudService {
	
	@Autowired
	private SolicitudRepository solicitudRepository;

	@Override
	public Solicitud alta(Solicitud solicitud) {
		try {
            return solicitudRepository.save(solicitud);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	@Override
		public Solicitud modificar(Solicitud solicitud) {
	        try {
	            if (solicitudRepository.existsById(solicitud.getIdSolicitud())) {
	                return solicitudRepository.save(solicitud);
	            } else {
	                return null;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	@Override
	public int eliminar(int idSolicitud) {
		try {
            if (solicitudRepository.existsById(idSolicitud)) {
                solicitudRepository.deleteById(idSolicitud);
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
	public Solicitud buscarUna(int idSolicitud) {
		
		return solicitudRepository.findById(idSolicitud).orElse(null);
	}

	@Override
	public List<Solicitud> buscarTodas() {
		
		return solicitudRepository.findAll();
	}

	@Override
	public Solicitud buscarPorVacanteYUsuario(int idVacante, String email) {
		try {
	        return solicitudRepository.findByVacanteIdVacanteAndUsuarioEmail(idVacante, email).orElse(null);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
		
	}

	@Override
	public List<Solicitud> buscarPorIdVacante(int idVacante) {
		
		return solicitudRepository.findByVacanteIdVacante(idVacante);
	}

	@Override
	public List<Solicitud> buscarPorEmailUsuario(String email) {
		
		return solicitudRepository.findByUsuarioEmail(email);
	}

	@Override
	public List<Solicitud> buscarPorEmpresa(int idEmpresa) {
		
		return solicitudRepository.findByEmpresaId(idEmpresa);
	}

}
