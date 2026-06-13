package uv.sicae.user.user.dto;

/*
    DTO usado para recibir la petición de cambio de estatus de un usuario.
*/
public class CambiarEstatusUsuarioPeticion {
    private Boolean estatus;

    public Boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(Boolean estatus) {
        this.estatus = estatus;
    }
}