package workflow.restcontroller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import workflow.dto.VacanteDto;
import workflow.entidades.Categoria;
import workflow.entidades.Empresa;
import workflow.entidades.Solicitud;
import workflow.entidades.TipoEstado;
import workflow.entidades.Usuario;
import workflow.entidades.Vacante;
import workflow.repository.CategoriaRepository;
import workflow.repository.EmpresaRepository;
import workflow.services.CategoriaService;
import workflow.services.EmpresaService;
import workflow.services.SolicitudService;
import workflow.services.UsuarioService;
import workflow.services.VacanteService;

@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/vacantes")
public class VacanteController {

    @Autowired
    private VacanteService vacanteService;
    
    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private EmpresaService empresaService;
    
    @Autowired
    private SolicitudService solicitudService;
    
   

    
    //METODOS PARA LAS VACANTES, GESTIONADAS POR LAS EMPRESAS
    
    	//1.Buscar todas las vacantes
    	@GetMapping("/todas")
    	public ResponseEntity<List<Vacante>> todas() {
        return new ResponseEntity<>(vacanteService.buscarTodas(), HttpStatus.OK);
    	}
    
    
    	
    	
    	//2.Buscar vacante por su id ,para ver detalles de ella
    	@GetMapping("/{idVacante}")
    	public ResponseEntity<?> una(@PathVariable int idVacante,HttpSession session) {
    	Usuario usuario = (Usuario) session.getAttribute("usuario");
		  if (usuario == null || (!usuario.getRol().equals("CLIENTE") && !usuario.getRol().equals("EMPRESA"))) {
		        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
		    }
    	
        Vacante vacante = vacanteService.buscarUna(idVacante);
        if (vacante == null) {
        	return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vacante);
        }
    
    	
    	//Buscar vacantes por empresa 
    	@GetMapping("/empresa")
    	public ResponseEntity<?> verVacantesEmpresa(HttpSession session) {
    		Usuario usuario = (Usuario) session.getAttribute("usuario");
  		  if (usuario == null || !usuario.getRol().equals("EMPRESA")) {
  		        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
  		    }
      	
    		
    	    Empresa empresa = empresaService.buscarPorUsuario(usuario) ;
    	    List<Vacante> vacantes = vacanteService.buscarPorEmpresa(empresa.getIdEmpresa());

    	    return ResponseEntity.ok(Map.of("vacantes", vacantes));
    	}

    	
    
    	//3.Dar de alta una vacante nueva , cambiar el estado a Creada
    	@PostMapping("/alta")
    	public ResponseEntity<?> alta(@RequestBody VacanteDto vacanteDto,HttpSession session) {
    		
    		Usuario usuario = (Usuario) session.getAttribute("usuario");
  		  if (usuario == null || !usuario.getRol().equals("EMPRESA")) {
  		        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
  		    }
  		  
        Categoria categoria = categoriaService.buscarUna(vacanteDto.getIdCategoria());

        if (categoria == null ) {
        	 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", "Categoría no encontrada"));
        }
       
        Empresa empresa = empresaService.buscarPorEmail(usuario.getEmail());
        if (empresa == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", "Empresa no encontrada"));
        }

        Vacante vacante = vacanteDto.convertToVacante(categoria, empresa);
        vacante.setFecha(new Date());
        vacante.setEstado(TipoEstado.CREADA);
        
        Vacante nueva = vacanteService.alta(vacante);
       
        	if (nueva == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("mensaje", "Error al crear la vacante"));
        	}   
        		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensaje", "Vacante creada con éxito", "vacante", nueva));
    		}


    	
    	
    	
    	//4.Modificar el estado a cancelada, cuando se quiere eliminar 
    	@PutMapping("/cancelar/{idVacante}")
    	public ResponseEntity<?> modificar(@PathVariable Integer idVacante,HttpSession session) {
    		
    		Usuario usuario = (Usuario) session.getAttribute("usuario");
  		  if (usuario == null || !usuario.getRol().equals("EMPRESA")) {
  		        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
  		    }
  		  
        Vacante existente = vacanteService.buscarUna(idVacante);
        if (existente == null) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Vacante no encontrada"));
        }
        
        existente.setEstado(TipoEstado.CANCELADA);
        Vacante cancelada = vacanteService.modificar(existente);
        
        return ResponseEntity.ok(Map.of("mensaje", "Vacante cancelada con éxito", "vacante", cancelada));
        
    	}
    	
    	
    	
    	
    	
    	//5.Editar una vacante 
    	@PutMapping("/editar/{idVacante}")
    	public ResponseEntity<?> editarVacante(@PathVariable Integer idVacante, @RequestBody VacanteDto dto,HttpSession session) {
    		
    		Usuario usuario = (Usuario) session.getAttribute("usuario");
  		  if (usuario == null || !usuario.getRol().equals("EMPRESA")) {
  		        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
  		    }
    		
    	    Vacante existente = vacanteService.buscarUna(idVacante);
    	    if (existente == null) {
    	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Vacante no encontrada"));
    	    }

    	    Categoria categoria = categoriaService.buscarUna(dto.getIdCategoria());
    	    if (categoria == null) {
    	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", "Categoría no encontrada"));
    	    }

    	    Empresa empresa = empresaService.buscarUna(dto.getIdEmpresa());
    	    if (empresa == null) {
    	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", "Empresa no encontrada"));
    	    }

    	    Vacante modificada = dto.convertToVacante(categoria, empresa);
    	    modificada.setIdVacante(idVacante); 
    	 
    	    Vacante guardada = vacanteService.modificar(modificada);

    	    return ResponseEntity.ok(Map.of("mensaje", "Vacante actualizada con éxito", "vacante", guardada));
    		}

    	
    	
    	
    	//6.Asignar una vacante a un candidato
    		@PutMapping("/asigna/{idVacante}/{email}")
    		public ResponseEntity<?> asignarVacante(@PathVariable int idVacante, @PathVariable String email,HttpSession session) {
    			
    			Usuario usuario = (Usuario) session.getAttribute("usuario");
    			  if (usuario == null || !usuario.getRol().equals("EMPRESA")) {
    			        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
    			    }
    	
    		 Vacante vacante = vacanteService.buscarUna(idVacante);
    		    if (vacante == null) {
    		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Vacante no encontrada"));
    		    }
    	
    		    vacante.setEstado(TipoEstado.CUBIERTA);
    		    vacanteService.modificar(vacante);
    	
    		    Solicitud solicitud = solicitudService.buscarPorVacanteYUsuario(idVacante, email);
    		    if (solicitud == null) {
    		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Solicitud no encontrada para ese usuario y vacante"));
    		    }
    		    solicitud.setEstado(true); //estado adjudicada
    		    solicitudService.modificar(solicitud);

    		    return ResponseEntity.ok(Map.of("mensaje", "Vacante asignada correctamente"));
    	}  	
   
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
}
