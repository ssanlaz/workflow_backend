package workflow.entidades;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Builder
@EqualsAndHashCode(of = "idVacante")

@Entity
@Table(name = "Vacantes")
public class Vacante implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
  
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name="id_vacante")
	    private int idVacante;

	    private String nombre;
	    
	    @Column(columnDefinition = "text")
	    private String descripcion;
	    
	    @Temporal(TemporalType.DATE)
	    private Date fecha;
	    
	    private double salario;
	    
	    @Column(name = "estatus")
	    @Enumerated(EnumType.STRING)
	    private TipoEstado estado; // CREADA, CUBIERTA, CANCELADA

	    private boolean destacado;

	    private String imagen;

	    @Column(columnDefinition = "text")
	    private String detalles;
	    
	    @ManyToOne
	    @JoinColumn(name = "id_categoria")
	    private Categoria categoria;

	   
	    @ManyToOne
	    @JoinColumn(name = "id_empresa")
	    private Empresa empresa;

}
