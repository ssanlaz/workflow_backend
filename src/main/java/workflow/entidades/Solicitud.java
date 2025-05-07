package workflow.entidades;

import java.io.Serializable;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity
@Builder
@EqualsAndHashCode(of="idSolicitud")
@Table(name = "Solicitudes")
public class Solicitud implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_solicitud")
	private int idSolicitud;
	
	@Temporal(TemporalType.DATE)
	private Date fecha;
	
	private String archivo;
	private String comentarios;
	
	private boolean estado; // true= adjudicado, false = presentada,java lo traduce en la bbdd como 1/0, tinyint es mas pequeño, valores de 0 a 1
	private String curriculum;
	
	 @ManyToOne
	    @JoinColumn(name = "id_Vacante")
	    private Vacante vacante;
	
	 @ManyToOne
	    @JoinColumn(name = "email")
	    private Usuario usuario;
}
