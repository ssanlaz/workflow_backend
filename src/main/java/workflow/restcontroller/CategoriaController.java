package workflow.restcontroller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import workflow.dto.CategoriaDto;
import workflow.entidades.Categoria;
import workflow.entidades.Usuario;
import workflow.services.CategoriaService; 

@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    //METODOS USADOS POR EL ADMIN PARA EL CRUD DE LAS CATEGORIAS , DAMOS PERMISOS EN LA CONFIGURACION DE SEGURIDAD PARA EL ROL ADMON
    
    
    
    //1.Metodo listar todas las categorias
    	@GetMapping("/todas")
    	public ResponseEntity<?> todas(HttpSession session) {
    		 Usuario usuario = (Usuario) session.getAttribute("usuario");
		 		if (usuario == null || !usuario.getRol().equals("ADMON")) {
		 			 return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
		 		}
        return new ResponseEntity<>(categoriaService.buscarTodas(), HttpStatus.OK);
    	}

    
    
    
    //2.Metodo para buscar categorias por su iD
    	@GetMapping("/{idCategoria}")
    	public ResponseEntity<?> una(@PathVariable Integer idCategoria,HttpSession session) {
    		 Usuario usuario = (Usuario) session.getAttribute("usuario");
		 		if (usuario == null || !usuario.getRol().equals("ADMON")) {
		 			 return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
		 		}
        Categoria categoria = categoriaService.buscarUna(idCategoria);
        if (categoria != null) {
            return new ResponseEntity<>(categoria, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Categoría no existe", HttpStatus.NOT_FOUND);
        }
    	}
    
    
    
    //3.Metodo para dar de alta una categoria
    	@PostMapping("/alta")
    	public ResponseEntity<?> alta(@RequestBody CategoriaDto categoriaDto,HttpSession session) {
    		 Usuario usuario = (Usuario) session.getAttribute("usuario");
		 		if (usuario == null || !usuario.getRol().equals("ADMON")) {
		 			 return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
		 		}
        Categoria categoria = categoriaDto.convertToCategoria();
        Categoria nuevaCategoria = categoriaService.alta(categoria);
        if (nuevaCategoria == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("mensaje", "Error al crear la categoría"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensaje", "Categoría creada con éxito", "categoria", nuevaCategoria));
    	}
    

    
    
    
    //4.Metodo para modificar una categoria por su id
    	@PutMapping("/modificar/{idCategoria}")
    	public ResponseEntity<?> modificar(@PathVariable Integer idCategoria, @RequestBody Categoria categoria,HttpSession session) {
    		 Usuario usuario = (Usuario) session.getAttribute("usuario");
		 		if (usuario == null || !usuario.getRol().equals("ADMON")) {
		 			 return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
		 		}
        Categoria existente = categoriaService.buscarUna(idCategoria);
        if (existente == null) {
        	 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("mensaje", "Error al modificar la categoría"));
        }

        categoria.setIdCategoria(idCategoria);
        Categoria categoriaModificada = categoriaService.modificar(categoria);
        return ResponseEntity.status(HttpStatus.CREATED)		.body(Map.of("mensaje", "Categoría modificafa con éxito"));
    	}

    
    
    //5.Metodo para eliminar una categoria por su id
    	@DeleteMapping("/eliminar/{idCategoria}")
    	public ResponseEntity<?> eliminar(@PathVariable Integer idCategoria,HttpSession session) {
    		 Usuario usuario = (Usuario) session.getAttribute("usuario");
		 		if (usuario == null || !usuario.getRol().equals("ADMON")) {
		 			 return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no permitido");
		 		}
        switch (categoriaService.eliminar(idCategoria)) {
            case 1:
            	 return ResponseEntity.ok(Map.of("mensaje", "Categoría eliminada correctamente"));
            case 0:
            	 return ResponseEntity.ok(Map.of("mensaje", "Categoría eliminada correctamente"));
            case -1:
            	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("mensaje", "Error en la base de datos. Contacte con soporte técnico."));
            default:
            	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("mensaje", "Error desconocido"));
        }
    }
    
    
    
    
}
