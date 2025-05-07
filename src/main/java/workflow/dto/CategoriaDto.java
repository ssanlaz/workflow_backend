package workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import workflow.entidades.Categoria;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "nombre")
public class CategoriaDto {

	
    private String nombre;
    private String descripcion;

    public Categoria convertToCategoria() {
        return Categoria.builder()
        	
                .nombre(this.nombre)
                .descripcion(this.descripcion)
                .build();
    }
}
