package workflow.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import workflow.entidades.Vacante;
import workflow.entidades.Categoria;
import workflow.entidades.Empresa;
import workflow.entidades.TipoEstado; 

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "nombre")
public class VacanteDto {

    private String nombre;
    private String descripcion;
    private Date fecha;
    private double salario;
    private TipoEstado estado;
    private boolean destacado;
    private String imagen;
    private String detalles;
    private int idVacante;
    private int idCategoria;
    private int idEmpresa;
    private String nombreEmpresa;
    private String nombreCategoria;
    private String ciudad;

    public Vacante convertToVacante(Categoria categoria, Empresa empresa) {
        return Vacante.builder()
                .nombre(this.nombre)
                .descripcion(this.descripcion)
                .fecha(this.fecha)
                .salario(this.salario)
                .estado(this.estado)
                .destacado(this.destacado)
                .imagen(this.imagen)
                .detalles(this.detalles)
                .idVacante(this.idVacante)
                .categoria(categoria)
                .empresa(empresa)
                .build();
    }
}
