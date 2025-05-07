package workflow.restcontroller;



import java.util.Date;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import workflow.dto.FiltroDto;
import workflow.dto.RegistroDto;
import workflow.dto.SolicitudDto;
import workflow.dto.UsuarioDto;
import workflow.dto.VacanteDto;
import workflow.entidades.Solicitud;
import workflow.entidades.TipoEstado;
import workflow.entidades.Usuario;
import workflow.entidades.Vacante;
import workflow.services.SolicitudService;
import workflow.services.UsuarioService;
import workflow.services.VacanteService;

@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private VacanteService vacanteService;
    
    @Autowired
    private SolicitudService solicitudService;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
	private PasswordEncoder passwordEncoder;
    
    
    
    
    
    	//1.Registro de usuario con rol cliente 
    	@PostMapping("/registro")
    	public ResponseEntity<?> registrarCliente(@RequestBody RegistroDto dto) {
	
		if(usuarioService.buscarUno(dto.getEmail()) !=null) {
			return ResponseEntity.internalServerError().body(Map.of("mensaje", "Usuario no registrado correctamente"));
		}
		
		Usuario nuevo = modelMapper.map(dto, Usuario.class);
		// Valores que no deben venir del cliente
	    nuevo.setPassword(passwordEncoder.encode(dto.getPassword()));
	    nuevo.setRol("CLIENTE");
	    nuevo.setEnabled(1);
	    nuevo.setFechaRegistro(new Date());
    
	    usuarioService.alta(nuevo);
	    return ResponseEntity.ok(Map.of("mensaje", "Usuario registrado correctamente"));
    	}
    
    
    	
    	
    	
    	//2.Busqueda de vacantes por su estado CREADA
    	@PostMapping("/vacantes/busqueda")
    	public ResponseEntity<?> buscarVacantesFiltro(@RequestBody FiltroDto filtros,HttpSession session) {
    		Usuario usuario = (Usuario) session.getAttribute("usuario");
    		if (usuario == null || !usuario.getRol().equals("EMPRESA") &&
    									!usuario.getRol().equals("ADMON") &&
    											!usuario.getRol().equals("CLIENTE")) {
    		        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
    		    }
    		
    	    List<Vacante> todas = vacanteService.buscarTodas();

    	    List<Vacante> filtradas = todas.stream()
    	    		.filter(v -> v.getEstado() == TipoEstado.CREADA)
    	            .filter(v -> filtros.getNombreEmpresa() == null || 
    	                         v.getEmpresa().getNombreEmpresa().equalsIgnoreCase(filtros.getNombreEmpresa()))
    	            .filter(v -> filtros.getCategoria() == null || 
    	                         v.getCategoria().getNombre().equalsIgnoreCase(filtros.getCategoria()))
    	            .filter(v -> filtros.getCiudad() == null || 
    	                         v.getEmpresa().getCiudad().equalsIgnoreCase(filtros.getCiudad()))
    	            .filter(v -> filtros.getSalario() == null || 
    	                         v.getSalario() >= filtros.getSalario())
    	            .toList();
    	    
    	    	List<VacanteDto> dtos = filtradas.stream()
    	            .map(v -> {
    	            	VacanteDto dto = modelMapper.map(v, VacanteDto.class);
    	            	dto.setIdVacante(v.getIdVacante());
    	            	dto.setIdCategoria(v.getCategoria().getIdCategoria());
    	            	 dto.setNombreCategoria(v.getCategoria().getNombre());
    	                 dto.setNombreEmpresa(v.getEmpresa().getNombreEmpresa());
    	                 dto.setCiudad(v.getEmpresa().getCiudad());
    	            	return dto;
    	            })
    	            .toList();
    
    	    	return ResponseEntity.ok(Map.of("mensaje", "Vacantes filtradas con éxito","vacantes", dtos));
    	    		}
    	
    	
    	
    	
    	
    		
    	  //3. Usuario cliente postulandose a una vacante , mandando cv y pudiendo poner detalles(front)
    	    @PostMapping("/solicitudVacante")
    		public ResponseEntity<?> mandarSolicitud(@RequestBody SolicitudDto dto, HttpSession session) {
    	    	Usuario usu = (Usuario) session.getAttribute("usuario");
      		  if (usu == null || !usu.getRol().equals("CLIENTE")) {
      		        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
      		    }
      		
    		Vacante vacante = vacanteService.buscarUna(dto.getIdVacante());
    		if(vacante == null) {
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", "Vacante no encontrada"));
    			 }
    			
    		Usuario usuario = usu;
    		   
    		Solicitud solicitud = dto.convertToSolicitud(vacante, usuario);
    		solicitud.setEstado(false);// cambiar estado a presentada
    			
    			
    		Solicitud creada = solicitudService.alta(solicitud);
    		   if (creada == null) {
    		       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("mensaje", "Error al crear la solicitud"));
    		    }
    		    
    		    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensaje", "Solicitud enviada con éxito", "solicitud", creada));
    		    
    			}
    	
    	
    	    
    	    
    	    
    	    
    	  //4.Metodo para sacar todas las solicitudes del Usuario
    		@GetMapping("/solicitudes")
    		public ResponseEntity<?> verSolicitudesUsuario(HttpSession session) {
    			
    			Usuario usu = (Usuario) session.getAttribute("usuario");
        		  if (usu == null || !usu.getRol().equals("CLIENTE")) {
        		        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
        		  }
        		  
        		  String email = usu.getEmail();        	  	
    	        
    	    List<Solicitud> solicitudes = solicitudService.buscarPorEmailUsuario(email);
    	     if (solicitudes.isEmpty()) {
    	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "No tienes solicitudes registradas"));
    	        }
    		
    	    List<SolicitudDto> dtos = solicitudes.stream()
    	            .map(s -> {
    	                 SolicitudDto dto = modelMapper.map(s, SolicitudDto.class);
    	                 dto.setIdSolicitud(s.getIdSolicitud());
    	                 dto.setEmailUsuario(s.getUsuario().getEmail());
    	                 dto.setIdVacante(s.getVacante().getIdVacante());
    	                 dto.setNombreVacante(s.getVacante().getNombre());
    	                 dto.setNombreEmpresa(s.getVacante().getEmpresa().getNombreEmpresa());
    	                 dto.setEstado(s.isEstado());
    	                 return dto;
    	                   })
    	            .toList();
    	                
    	        return ResponseEntity.ok(Map.of("mensaje", "Solicitudes del usuario", "solicitudes", dtos));
    		
    					}
    		
    		
    		
    		
    		
    		
    		//5.Mostrar los detalles de la solicitud por usuario logueado
    		  @GetMapping("/solicitudes/detalle/{idSolicitud}")
    		  public ResponseEntity<?> verDetalleSolicitud(@PathVariable Integer idSolicitud, HttpSession session) {
    			  
    			  Usuario usuario = (Usuario) session.getAttribute("usuario");
        		  if (usuario == null || !usuario.getRol().equals("CLIENTE")) {
        		        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
        		    }
    			  String email = usuario.getEmail();
    			  
    		  Solicitud solicitud = solicitudService.buscarUna(idSolicitud);
    			 if (solicitud == null || !solicitud.getUsuario().getEmail().equals(email)) {
    			     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Solicitud no encontrada o no pertenece al usuario"));
    			    }
    		  
    			 SolicitudDto dto = modelMapper.map(solicitud, SolicitudDto.class);
    			    dto.setEmailUsuario(solicitud.getUsuario().getEmail());
    			    dto.setIdVacante(solicitud.getVacante().getIdVacante());
    			    dto.setEstado(solicitud.isEstado());
    			    dto.setFecha(solicitud.getFecha());
    			    dto.setNombreEmpresa(solicitud.getVacante().getEmpresa().getNombreEmpresa());
    			    dto.setNombreVacante(solicitud.getVacante().getNombre());
    			    dto.setComentarios(solicitud.getComentarios());
    			  
    			    
    			    return ResponseEntity.ok(Map.of("mensaje", "Detalle de la solicitud","solicitud", dto));
    		  }
    		  
    		  
    		  
    		  
    		  
    		  //6.Cancelar la solicitud , solo si es de ese usuario logueado
    		  @DeleteMapping("/solicitudes/cancelar/{idSolicitud}")
    		  public ResponseEntity<?> cancelarSolicitud(@PathVariable Integer idSolicitud, HttpSession session) {
    			  //Sacar sesion del usuario
    			  Usuario usu = (Usuario) session.getAttribute("usuario");
        		  if (usu == null || !usu.getRol().equals("CLIENTE")) {
        		        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
        		  }
        		  
        		  String email = usu.getEmail();   

    		      Solicitud solicitud = solicitudService.buscarUna(idSolicitud);
    		      if (solicitud == null || !solicitud.getUsuario().getEmail().equals(email)) {
    		          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Solicitud no encontrada o no pertenece al usuario"));
    		      }

    		      int resultado = solicitudService.eliminar(idSolicitud);
    		      if (resultado == 1) {
    		          return ResponseEntity.ok(Map.of("mensaje", "Solicitud cancelada correctamente"));
    		      }

    		      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("mensaje", "Error al cancelar la solicitud"));
    		  		}
    		  
    		  
    		  
    		  
    		  
    		
   
    

   
    
}
