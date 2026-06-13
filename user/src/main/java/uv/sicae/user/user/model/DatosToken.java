package uv.sicae.user.user.model;

/*
    Modelo usado para almacenar los datos principales
    obtenidos del token JWT del usuario autenticado.
*/
public class DatosToken {
    private Integer idUsuario;
    private Integer idRol;
    private String rol;
    private String usuario;
    private Integer idTipoUsuario;
    private String tipoUsuario;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getIdTipoUsuario() {
        return idTipoUsuario;
    }

    public void setIdTipoUsuario(Integer idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public boolean esAdministrador() {
        return (idRol != null && idRol == 1) || (rol != null && rol.equalsIgnoreCase("administrador"));
    }
}