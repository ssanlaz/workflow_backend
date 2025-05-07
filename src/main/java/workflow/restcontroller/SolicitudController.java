package workflow.restcontroller;

import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import workflow.dto.SolicitudDto;
import workflow.entidades.Empresa;
import workflow.entidades.Solicitud;
import workflow.entidades.Usuario;
import workflow.entidades.Vacante;
import workflow.services.EmpresaService;
import workflow.services.SolicitudService;
import workflow.services.UsuarioService;
import workflow.services.VacanteService;

@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/solicitudes")
public class SolicitudController {

    @Autowired
    private SolicitudService solicitudService;
    
    @Autowired
    private VacanteService vacanteService;

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private EmpresaService emser;
    
    @Autowired
    private ModelMapper modelMapper;
    
    
    
    	//1.Mostrar las solicitudes por Vacante 
    	@GetMapping("/vacante/{idVacante}")
    	public ResponseEntity<?> listarSolicitudesPorVacante(@PathVariable int idVacante,HttpSession session) {
    		
    		Usuario usuario = (Usuario) session.getAttribute("usuario");
  		  if (usuario == null || !usuario.getRol().equals("EMPRESA")) {
  		        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
  		    }
        List<Solicitud> solicitudes = solicitudService.buscarPorIdVacante(idVacante);

        if (solicitudes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "No hay solicitudes para esta vacante"));
        }
        
        List<SolicitudDto> dtos = solicitudes.stream()
        		.map(s -> {
        	        SolicitudDto dto = modelMapper.map(s, SolicitudDto.class);
        	        dto.setIdVacante(s.getVacante().getIdVacante());
        	        dto.setEmailUsuario(s.getUsuario().getEmail());
        	        return dto;
        	    })
        	    .toList();
        return ResponseEntity.ok(Map.of("mensaje", "Solicitudes encontradas","solicitudes", dtos));
    	}
    
    
    	
    	
    	//2.Modificar, gestionar las solicitudes.
    	@PutMapping("/adjudicar/{idSolicitud}")
    	public ResponseEntity<?> adjudicarSolicitud(@PathVariable int idSolicitud,HttpSession session) {
    		
    		Usuario usuario = (Usuario) session.getAttribute("usuario");
  		  if (usuario == null || !usuario.getRol().equals("EMPRESA")) {
  		        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
  		    }
  		  
        Solicitud solicitud = solicitudService.buscarUna(idSolicitud);

        if (solicitud == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Solicitud no encontrada"));
        }

        solicitud.setEstado(true);
        Solicitud actualizada = solicitudService.modificar(solicitud);

        return ResponseEntity.ok(Map.of("mensaje", "Solicitud adjudicada", "solicitud", actualizada));
    	}

    
    
    	
    
    
 
      //3.Mostrar todas las solicitudes
      @GetMapping("/todas")
       public ResponseEntity<?> todas(HttpSession session) {
    	
    	Usuario usuario = (Usuario) session.getAttribute("usuario");
		  if (usuario == null || !usuario.getRol().equals("EMPRESA")) {
		        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
		    }
        return new ResponseEntity<>(solicitudService.buscarTodas(), HttpStatus.OK);
    }

  
    //4.Mostrar las solicitudes por Empresa
      @GetMapping("/empresa")
      public ResponseEntity<?> obtenerSolicitudesEmpresa(HttpSession session) {
          Usuario usu = (Usuario) session.getAttribute("usuario");

          if (usu == null || !usu.getRol().equals("EMPRESA")) {
              return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado");
          }

          Empresa empresa = emser.buscarPorEmail(usu.getEmail());
          if (empresa == null) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa no encontrada");
          }

          List<Solicitud> solicitudes = solicitudService.buscarPorEmpresa(empresa.getIdEmpresa());
          return ResponseEntity.ok(solicitudes);
      }
  



    
}
