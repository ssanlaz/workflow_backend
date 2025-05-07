package workflow.restcontroller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import workflow.dto.EmpresaDto;

import workflow.entidades.Empresa;
import workflow.entidades.Usuario;
import workflow.services.EmpresaService;
import workflow.services.UsuarioService;

@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/empresas")
public class EmpresaController {
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private UsuarioService usuarioService;

	
	
	
	@GetMapping("/")
	public ResponseEntity<?> todas(HttpSession session) {
		 Usuario usuario = (Usuario) session.getAttribute("usuario");
		if (usuario == null || !usuario.getRol().equals("EMPRESA") &&
                				!usuario.getRol().equals("ADMON") &&
                				!usuario.getRol().equals("CLIENTE")) {
					return ResponseEntity.status(HttpStatus.FORBIDDEN)
								.body("Acceso no permitido");
		    }
		return new ResponseEntity<>(empresaService.buscarTodas(), HttpStatus.OK);
	}
	
	
	@GetMapping("/{idEmpresa}")
	public ResponseEntity<?> una(@PathVariable int idEmpresa,HttpSession session) {
		
		 Usuario usuario = (Usuario) session.getAttribute("usuario");
			if (usuario == null || !usuario.getRol().equals("EMPRESA") &&
	                				!usuario.getRol().equals("ADMON") &&
	                				!usuario.getRol().equals("CLIENTE")) {
						return ResponseEntity.status(HttpStatus.FORBIDDEN)
									.body("Acceso no permitido");
			    }
		Empresa empresa = empresaService.buscarUna(idEmpresa);
		if (empresa != null)
			return new ResponseEntity<>(empresa, HttpStatus.OK);
		else
			return new ResponseEntity<>("Empresa no existe", HttpStatus.NOT_FOUND);
	}
	


	
	
	//1.LAS EMPRESAS TAMBIEN PUEDE EDITARSE ASI MISMAS 
		@PutMapping("/modificar/{idEmpresa}")
		public ResponseEntity<?> modificar(@PathVariable int idEmpresa, @RequestBody EmpresaDto empresaDto,HttpSession session) {
			
			 Usuario usu = (Usuario) session.getAttribute("usuario");
				if (usu == null || !usu.getRol().equals("EMPRESA") &&
		                			!usu.getRol().equals("ADMON") ) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
				    }
			
	    Empresa empresaExistente = empresaService.buscarUna(idEmpresa);
	    if (empresaExistente == null) {
	        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	    }

	    // Buscar el usuario para asociar
	    Usuario usuario = usuarioService.buscarUno(empresaDto.getEmail());
	    if (usuario == null) {
	        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); 
	    }

	    Empresa empresa = empresaDto.convertToEmpresa(usuario);
	    empresa.setIdEmpresa(idEmpresa); // Asegurar que se actualiza y no se crea

	    Empresa empresaModificada = empresaService.modificar(empresa);
	    return new ResponseEntity<>(empresaModificada, HttpStatus.OK);
		}

	
	
	
	
	
	
    }
    
    


	
