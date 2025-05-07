package workflow.restcontroller;




import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import workflow.dto.LoginDto;
import workflow.entidades.Usuario;
import workflow.services.UsuarioService;





@RestController
public class HomeController {
	
	@Autowired
	private UsuarioService user;
	
	@Autowired
	private PasswordEncoder passwordEncoder;


        //1.LOGUEAR USUARIO
		@PostMapping("/login")
		public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpSession session) {
	    System.out.println("EMAIL ENVIADO: " + loginDto.getEmail());
	    System.out.println("PASS ENVIADA: " + loginDto.getPassword());

	    Usuario usuario = user.buscarUno(loginDto.getEmail());
	    if (usuario == null || !passwordEncoder.matches(loginDto.getPassword(), usuario.getPassword())) { //Encriptadas

	    	
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensaje", "Credenciales incorrectas"));
	    }

	 
	   
	    session.setAttribute("usuario", usuario);//guardamos sesion de usuario en servidor
	    
	    return ResponseEntity.ok(Map.of("mensaje", "Login correcto","rol", usuario.getRol(),"nombre", usuario.getNombre()));
		}


		
		
		//2.Para autorizaciones de usuarios 
        @GetMapping("/auth/usuario")
        
        public ResponseEntity<?> getUsuario(HttpSession session) {
            Usuario usuario = (Usuario) session.getAttribute("usuario");

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sesión no activa");
            }

            return ResponseEntity.ok(Map.of(
                "email", usuario.getEmail(),
                "rol", usuario.getRol()
            ));
        }
     
                
        
        
        //3.VER ESTADO DE LA SESION
        @GetMapping("/session-status")
        public ResponseEntity<?> checkSession(HttpServletRequest request) {
            HttpSession session = request.getSession(false);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sesión no activa");
            }
            return ResponseEntity.ok("Sesión activa");
        }
    
        
        
        
        //4.CERRAR SESION 
         @PostMapping("/logout")
         public ResponseEntity<?> logout(HttpSession session) {
            session.invalidate(); 
            return ResponseEntity.ok(Map.of("mensaje", "Sesión cerrada"));
         }
            
          

    
    
}

