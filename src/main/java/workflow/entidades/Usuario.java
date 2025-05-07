package workflow.entidades;


import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(of = "email")

@Entity
@Table(name = "Usuarios")
public class Usuario implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    private String email;

    private String nombre;

    private String apellidos;

    private String password;

    private int enabled;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_Registro")
    private Date fechaRegistro;

    private String rol;//admon , cliente , empresa
	
	
}
