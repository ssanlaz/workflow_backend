package workflow.dto;





import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import workflow.entidades.Usuario;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "email")
public class UsuarioDto {

    private String email;
    private String nombre;
    private String apellidos;
    private String password;
    private int enabled;
    private Date fechaRegistro;
    private String rol;

    public Usuario convertToUsuario() {
        return Usuario.builder()
                .email(this.email)
                .nombre(this.nombre)
                .apellidos(this.apellidos)
                .password(this.password)
                .enabled(this.enabled)
                .fechaRegistro(this.fechaRegistro)
                .rol(this.rol)
                .build();
    }
}
