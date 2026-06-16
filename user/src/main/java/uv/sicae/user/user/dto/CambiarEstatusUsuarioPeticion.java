package uv.sicae.user.user.dto;

/**
 * DTO que representa la petición para cambiar el estatus de un usuario.
 * 
 * @author Alvaro
 */
public class CambiarEstatusUsuarioPeticion {
    private Boolean estatus;

    /**
     * Obtiene el valor de estatus.
     *
     * @return resultado generado por la operación.
     */
    public Boolean getEstatus() {
        return estatus;
    }

    /**
     * Asigna el valor de estatus.
     *
     * @param estatus parámetro requerido para ejecutar la operación.
     */
    public void setEstatus(Boolean estatus) {
        this.estatus = estatus;
    }
}