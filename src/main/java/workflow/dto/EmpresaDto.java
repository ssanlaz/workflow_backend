package workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import workflow.entidades.Empresa;
import workflow.entidades.Usuario;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "cif")
public class EmpresaDto {
	
	private Integer idEmpresa;
    private String cif;
    private String nombreEmpresa;
    private String direccionFiscal;
    private String pais;
    private String ciudad;
    private String email; // Para asociar con Usuario

    /**
     * Convierte el DTO a entidad Empresa, dejando preparado el objeto Usuario (vacío).
     * El usuario real debe ser inyectado desde fuera (Service o Controller).
     */
    public Empresa convertToEmpresa(Usuario usuario) {
        Empresa empresa = new Empresa();
        empresa.setCif(this.cif);
        empresa.setNombreEmpresa(this.nombreEmpresa);
        empresa.setDireccionFiscal(this.direccionFiscal);
        empresa.setPais(this.pais);
        empresa.setCiudad(this.ciudad);
        empresa.setUsuario(usuario); // ✅ ahora seteamos el objeto Usuario

        return empresa;
    }
}
