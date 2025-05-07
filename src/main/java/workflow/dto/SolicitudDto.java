package workflow.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import workflow.entidades.Solicitud;
import workflow.entidades.Usuario;
import workflow.entidades.Vacante;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "idVacante") // o puedes usar algún otro campo clave si aplica
public class SolicitudDto {

	private int idSolicitud;
    private Date fecha;
    private String archivo;
    private String comentarios;
    private boolean estado;
    private String curriculum;
    private int idVacante;
    private String nombreVacante;
    private String nombreEmpresa;
    private String emailUsuario;
   

    public Solicitud convertToSolicitud(Vacante vacante, Usuario usuario) {
        return Solicitud.builder()
        		.idSolicitud(this.idSolicitud)
                .fecha(this.fecha)
                .archivo(this.archivo)
                .comentarios(this.comentarios)
                .estado(this.estado)
                .curriculum(this.curriculum)
                .vacante(vacante)
                .usuario(usuario)
                .build();
    }
}
