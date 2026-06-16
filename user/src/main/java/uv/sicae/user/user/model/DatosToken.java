package uv.sicae.user.user.model;

/**
 * Modelo que representa la información extraída de un token JWT válido.
 *
 * Contiene los datos necesarios para identificar al usuario autenticado y
 * aplicar reglas de autorización dentro de la capa de servicio, como validar si
 * el usuario tiene rol de administrador.
 * 
 * @author Alvaro
 */
public class DatosToken {
    private Integer idUsuario;
    private Integer idRol;
    private String rol;
    private String usuario;
    private Integer idTipoUsuario;
    private String tipoUsuario;

    /**
     * Obtiene el valor de idUsuario.
     *
     * @return valor solicitado por el método.
     */
    public Integer getIdUsuario() {
        return idUsuario;
    }

    /**
     * Asigna el valor de idUsuario.
     *
     * @param idUsuario nuevo valor que será asignado al objeto.
     */
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el valor de idRol.
     *
     * @return valor solicitado por el método.
     */
    public Integer getIdRol() {
        return idRol;
    }

    /**
     * Asigna el valor de idRol.
     *
     * @param idRol nuevo valor que será asignado al objeto.
     */
    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    /**
     * Obtiene el valor de rol.
     *
     * @return valor solicitado por el método.
     */
    public String getRol() {
        return rol;
    }

    /**
     * Asigna el valor de rol.
     *
     * @param rol nuevo valor que será asignado al objeto.
     */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * Obtiene el valor de usuario.
     *
     * @return valor solicitado por el método.
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Asigna el valor de usuario.
     *
     * @param usuario nuevo valor que será asignado al objeto.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el valor de idTipoUsuario.
     *
     * @return valor solicitado por el método.
     */
    public Integer getIdTipoUsuario() {
        return idTipoUsuario;
    }

    /**
     * Asigna el valor de idTipoUsuario.
     *
     * @param idTipoUsuario nuevo valor que será asignado al objeto.
     */
    public void setIdTipoUsuario(Integer idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    /**
     * Obtiene el valor de tipoUsuario.
     *
     * @return valor solicitado por el método.
     */
    public String getTipoUsuario() {
        return tipoUsuario;
    }

    /**
     * Asigna el valor de tipoUsuario.
     *
     * @param tipoUsuario nuevo valor que será asignado al objeto.
     */
    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    /**
     * Indica si los datos del token corresponden a un usuario administrador.
     *
     * @return valor solicitado por el método.
     */
    public boolean esAdministrador() {
        return (idRol != null && idRol == 1) || (rol != null && rol.equalsIgnoreCase("administrador"));
    }
}
