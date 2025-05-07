package workflow.restcontroller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import workflow.dto.EmpresaDto;
import workflow.dto.RegistroDto;
import workflow.dto.RegistroEmpresaDto;
import workflow.dto.UsuarioDto;
import workflow.entidades.Empresa;
import workflow.entidades.Usuario;
import workflow.services.EmpresaService;
import workflow.services.UsuarioService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	   @Autowired
	    private UsuarioService usuarioService;

	    @Autowired
	    private EmpresaService empresaService;

	    @Autowired
	    private PasswordEncoder passwordEncoder;
	    
	    @Autowired
	    private ModelMapper modelMapper;
	    
	    
	    
	                                                     //EMPRESAS
	    
	   //1.Metodo para listar todas las empresas 
	    @GetMapping("/todas/empresas")
		public ResponseEntity<?> todas(HttpSession session) {
	    	 Usuario usuario = (Usuario) session.getAttribute("usuario");
	 		if (usuario == null || !usuario.getRol().equals("EMPRESA") &&
	                 				!usuario.getRol().equals("ADMON") &&
	                 				!usuario.getRol().equals("CLIENTE")) {
	 					return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
	 		    }
	 		
	 		 	List<Empresa> listaEmpresas = empresaService.buscarTodas();
			
	 		   List<EmpresaDto> listaDto = listaEmpresas.stream().map(empresa -> {
	 			   EmpresaDto dto = new EmpresaDto();
	 			    dto.setIdEmpresa(empresa.getIdEmpresa());
	 			   	dto.setNombreEmpresa(empresa.getNombreEmpresa());
	 			   	dto.setCif(empresa.getCif());
	 			   	dto.setEmail(empresa.getUsuario().getEmail());
	 			   	dto.setDireccionFiscal(empresa.getDireccionFiscal());
	 			   	dto.setCiudad(empresa.getCiudad());
	 			   	dto.setPais(empresa.getPais());

	 			   	if (empresa.getUsuario() != null) {
	 	            dto.setEmail(empresa.getUsuario().getEmail());
	 			   	}

	 			   	return dto;
			 	    }).toList();

	 		   	return ResponseEntity.ok(listaDto);
	 				}
	    
	    
	    
	    
	
	   //2.Metodo para dar de alta las empresas por parte del administrador 
	    @PostMapping("/alta/empresa")
	    public ResponseEntity<?> altaEmpresa(@RequestBody RegistroEmpresaDto dto,HttpSession session) {
	    	
	    	 Usuario usu = (Usuario) session.getAttribute("usuario");
	 		if (usu == null || !usu.getRol().equals("ADMON")) {
	                 				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
	 		    }
	 		
	 		 EmpresaDto dtos = dto.getEmpresaDto();
	 	    RegistroDto registroDto = dto.getRegistroDto();
		
	 		if (usuarioService.buscarUno(registroDto.getEmail()) != null) {
			return ResponseEntity.internalServerError().body(Map.of("mensaje", "Error, ya existe un Usuario"));
	 		}
	 		
	 		// Creamos el usuario con rol EMPRESA
	 		Usuario usuario = modelMapper.map(registroDto, Usuario.class);
	 		usuario.setNombre(dtos.getNombreEmpresa());
	 		usuario.setApellidos("Empresa");
	 		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
	 		usuario.setRol("EMPRESA");
	 		usuario.setEnabled(1);
	 		usuario.setFechaRegistro(new Date());
		
	 		//Guardamos el usuario
	 		Usuario guardado = usuarioService.alta(usuario);
		    if (guardado == null) {
		    	return ResponseEntity.internalServerError().body(Map.of("mensaje", "Error al guardar el usuario"));
		    }
		
		    //Creamos la empresa
		     Empresa empresa = dtos.convertToEmpresa(guardado);
		     Empresa nueva = empresaService.alta(empresa);
		     
		    if( nueva == null) {
		    	return ResponseEntity.internalServerError().body(Map.of("mensaje", "Error al guardar la empresa"));
		    }
		    return ResponseEntity.ok(Map.of("mensaje", "Empresa registrada con exito "));
	    	}
	
	
	
	    
	
		//3.Metodo para verDetalles de las empresas por su idEmpresa
		    @GetMapping("/empresa/{idEmpresa}")
		    public ResponseEntity<?>verDetallesEmpresa(@PathVariable Integer idEmpresa,HttpSession session){
		    	
		    	 Usuario usu = (Usuario) session.getAttribute("usuario");
		    	 if (usu == null || !usu.getRol().equals("EMPRESA") &&
             			!usu.getRol().equals("ADMON"))  {
		 			 return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
		 		}
		   
		 		
		    	Empresa empresa = empresaService.buscarUna(idEmpresa);
				if (empresa != null)
					return new ResponseEntity<>(empresa, HttpStatus.OK);
				else
					return new ResponseEntity<>("Empresa no existe", HttpStatus.NOT_FOUND);
					}
		    
		    
		    
		    
		    
		   //4.Metodo para modificar los datos de las empresas 
		    @PutMapping("/editar/empresa/{idEmpresa}")
			public ResponseEntity<?> modificar(@PathVariable Integer idEmpresa, @RequestBody EmpresaDto empresaDto,HttpSession session) {
		    	
		    	 Usuario usu = (Usuario) session.getAttribute("usuario");
		    	 if (usu == null || !usu.getRol().equals("EMPRESA") &&
	             			!usu.getRol().equals("ADMON")) {
			 			 return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
			 		}
			   
		    	
		    	
			 Empresa empresaExistente = empresaService.buscarUna(idEmpresa);
			 if (empresaExistente == null) {
			        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Empresa no encontrada"));
			    }
			 
			 Usuario usuario = usuarioService.buscarUno(empresaDto.getEmail());
			 if (usuario== null) {
				 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", "Usuario asociado no encontrado"));
			 }
			 
			 Empresa empresa = empresaDto.convertToEmpresa(usuario);
			 empresa.setIdEmpresa(idEmpresa);
			 Empresa empresaEditada = empresaService.modificar(empresa);
			 return ResponseEntity.ok(Map.of("mensaje", "Empresa modificada con éxito", "empresa", empresaEditada));
		    }
		 
		    
		    
		    
		   //5.Metodo para eliminar una Empresa 
		    @DeleteMapping("/eliminar/empresa/{idEmpresa}")
		    public ResponseEntity<?> eliminarEmpresa(@PathVariable Integer idEmpresa,HttpSession session) {
		    	
		    	 Usuario usuario = (Usuario) session.getAttribute("usuario");
			 		if (usuario == null || !usuario.getRol().equals("ADMON")) {
			 			 return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
			 		}
			   
			 int resultado = empresaService.eliminar(idEmpresa);

			    switch (resultado) {
			        case 1:
			            return ResponseEntity.ok(Map.of("mensaje", "Empresa eliminada con éxito"));
			        case 0:
			            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Empresa no encontrada"));
			        default: 
			            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("mensaje", "Error al intentar eliminar la empresa"));
			    	}
				}
		    
		    
		    
		    
		    					//USUARIOS CLIENTE
		    
		    //6.Listar todos los usuarios por Rol Cliente 
		    	@GetMapping("/todos/clientes")
		    	public ResponseEntity<?> listarClientes(HttpSession session) {
		    		
		    		 Usuario usuario = (Usuario) session.getAttribute("usuario");
				 		if (usuario == null || !usuario.getRol().equals("ADMON")) {
				 			 return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
				 		}
				   
		    		
		        List<Usuario> clientes = usuarioService.buscarPorRol("CLIENTE")
		        .stream()
		        .filter(u -> u.getEnabled() == 1)
		        .collect(Collectors.toList());

		        if (clientes.isEmpty()) {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "No hay usuarios con rol CLIENTE"));
		        }

		        return ResponseEntity.ok(Map.of("mensaje", "Usuarios cliente encontrados","usuarios", clientes));
		    	}
		    
		    
		    
		    	
		    
		    //7.Metodo para dar de baja un usuario y cambiar su estado a enabled(0)
		    	@PutMapping("/modificar/usuario/{email}")
		    	public ResponseEntity<?> darDeBajaUsuario(@PathVariable String email,HttpSession session) {
		    		
		    		 Usuario usu = (Usuario) session.getAttribute("usuario");
				 		if (usu == null || !usu.getRol().equals("ADMON")) {
				 			 return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
				 		}
				   
		    		
			    Usuario usuario = usuarioService.buscarUno(email);
			    if (usuario == null) {
			        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Usuario no encontrado"));
			    }
			    
			    if (usuario.getEnabled() == 0) {
			        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", "El usuario ya está dado de baja"));
			    }
		 
			    usuario.setEnabled(0);
			    Usuario actualizado = usuarioService.modificar(usuario);

			    if (actualizado == null) {
			        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("mensaje", "Error al dar de baja el usuario"));
			    }

			    return ResponseEntity.ok(Map.of ("mensaje", "Usuario dado de baja correctamente","usuario", actualizado));
		    	}
		 
		    	
		    									//ADMINISTRADORES 
		 
		 
		    
		    //8.Metodo para listar todos los administradores por su rol 
		    	@GetMapping("/todos")
		    	public ResponseEntity<?> listarAdmin(HttpSession session) {
		    		
		    		 Usuario usuario = (Usuario) session.getAttribute("usuario");
				 		if (usuario == null || !usuario.getRol().equals("ADMON")) {
				 			 return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
				 		}
				   
		    		
		        List<Usuario> admins = usuarioService.buscarPorRol("ADMON")
		        .stream()
		        .filter(admin -> admin.getEnabled() == 1)
		        .collect(Collectors.toList());
		        return ResponseEntity.ok(Map.of("mensaje", "Listado de administradores", "admins", admins));
		    	}
		     
		    	
		    	
		    	
		    	
		    	
		    	
		    
		    	
		    //9.Metodo para dar de alta un administrador
		    	@PostMapping("/alta")
		    	public ResponseEntity<?> altaAdmin(@RequestBody RegistroDto dto,HttpSession session) {
		    		
		    		 Usuario usuario = (Usuario) session.getAttribute("usuario");
				 		if (usuario == null || !usuario.getRol().equals("ADMON")) {
				 			 return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
				 		}
				   
		    		
		    		if (usuarioService.buscarUno(dto.getEmail()) != null) {
		    			return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", "Ya existe un usuario con ese email"));
		    		}
		    	Usuario nuevo = modelMapper.map(dto, Usuario.class);
		    	nuevo.setPassword(passwordEncoder.encode(dto.getPassword()));
		    	nuevo.setRol("ADMON");
		    	nuevo.setEnabled(1);
		    	nuevo.setFechaRegistro(new Date());

		    	Usuario guardado = usuarioService.alta(nuevo);
		 
		    	if (guardado == null) {
		    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("mensaje", "Error al registrar administrador"));
		    		}
		    		return ResponseEntity.ok(Map.of("mensaje", "Administrador registrado con éxito"));
		    		}
		
		    	
		    	
		      //10.Editar un administrador 
		    	  @PutMapping("/editar/{email}")
		    	   public ResponseEntity<?> editarAdmin(@PathVariable String email, @RequestBody UsuarioDto dto,HttpSession session) {
		    		  
		    		  Usuario usuario = (Usuario) session.getAttribute("usuario");
				 		if (usuario == null || !usuario.getRol().equals("ADMON")) {
				 			 return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
				 		}
				   
		    		  
		    		  
		    		  
		    	    Usuario existente = usuarioService.buscarUno(email);
		    	    if (existente == null || !"ADMON".equals(existente.getRol())) {
		    	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Administrador no encontrado"));
		    	    }

		    	    existente.setNombre(dto.getNombre());
		    	    existente.setApellidos(dto.getApellidos());
		    	    existente.setEnabled(dto.getEnabled());

		    	    Usuario actualizado = usuarioService.modificar(existente);
		    	    return ResponseEntity.ok(Map.of("mensaje", "Administrador actualizado con éxito", "admin", actualizado));
		    	    }
		    
		    	
		       //11.Eliminar un administrador
		    	   @DeleteMapping("/eliminar/{email}")
		    	    public ResponseEntity<?> eliminarAdmin(@PathVariable String email,HttpSession session) {
		    		   
		    		   Usuario usuario = (Usuario) session.getAttribute("usuario");
				 		if (usuario == null || !usuario.getRol().equals("ADMON")) {
				 			 return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
				 		}
				   
		    	    Usuario admin = usuarioService.buscarUno(email);
		    	    if (admin == null || !"ADMON".equals(admin.getRol())) {
		    	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Administrador no encontrado"));
		    	    }

		    	    admin.setEnabled(0);
		    	    usuarioService.modificar(admin);
		    	    return ResponseEntity.ok(Map.of("mensaje", "Administrador deshabilitado"));
		    	}
		
		
		    	   //12.Buscar un admin por su email
		    	   @GetMapping("/{email}")
		    	   public ResponseEntity<?> buscarAdmin(@PathVariable String email,HttpSession session) {
		    	   
		    		   Usuario usuario = (Usuario) session.getAttribute("usuario");
				 		if (usuario == null || !usuario.getRol().equals("ADMON")) {
				 			 return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
				 		}
				 		
				 	   Usuario admin = usuarioService.buscarUno(email);
				 	    if (admin == null || !admin.getRol().equals("ADMON")) {
				 	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Administrador no encontrado"));
				 	    }
				 	    
				 	   UsuarioDto dto = UsuarioDto.builder()
				 			  .email(admin.getEmail())
				 			    .nombre(admin.getNombre())
				 			    .apellidos(admin.getApellidos())
				 			    .build();
;				 	    return ResponseEntity.ok(dto);
				 		
		    	   }
		
}	
		
		
	
	
	
	
	

