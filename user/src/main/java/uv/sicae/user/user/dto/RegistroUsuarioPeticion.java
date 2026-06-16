package uv.sicae.user.user.dto;

/**
 * DTO que contiene los datos necesarios para registrar un nuevo usuario.
 * 
 * @author Alvaro
 */
public class RegistroUsuarioPeticion {
    private Integer idRol;
    private Integer idTipoUsuario;
    private Integer idProgramaEducativo;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String usuario;
    private String contrasena;
    private String correo;
    private String telefono;

    /**
     * Obtiene el valor de idRol.
     *
     * @return resultado generado por la operación.
     */
    public Integer getIdRol() {
        return idRol;
    }

    /**
     * Asigna el valor de idRol.
     *
     * @param idRol parámetro requerido para ejecutar la operación.
     */
    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    /**
     * Obtiene el valor de idTipoUsuario.
     *
     * @return resultado generado por la operación.
     */
    public Integer getIdTipoUsuario() {
        return idTipoUsuario;
    }

    /**
     * Asigna el valor de idTipoUsuario.
     *
     * @param idTipoUsuario parámetro requerido para ejecutar la operación.
     */
    public void setIdTipoUsuario(Integer idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    /**
     * Obtiene el valor de idProgramaEducativo.
     *
     * @return resultado generado por la operación.
     */
    public Integer getIdProgramaEducativo() {
        return idProgramaEducativo;
    }

    /**
     * Asigna el valor de idProgramaEducativo.
     *
     * @param idProgramaEducativo parámetro requerido para ejecutar la operación.
     */
    public void setIdProgramaEducativo(Integer idProgramaEducativo) {
        this.idProgramaEducativo = idProgramaEducativo;
    }

    /**
     * Obtiene el valor de nombre.
     *
     * @return resultado generado por la operación.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el valor de nombre.
     *
     * @param nombre parámetro requerido para ejecutar la operación.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el valor de apellidoPaterno.
     *
     * @return resultado generado por la operación.
     */
    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    /**
     * Asigna el valor de apellidoPaterno.
     *
     * @param apellidoPaterno parámetro requerido para ejecutar la operación.
     */
    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    /**
     * Obtiene el valor de apellidoMaterno.
     *
     * @return resultado generado por la operación.
     */
    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    /**
     * Asigna el valor de apellidoMaterno.
     *
     * @param apellidoMaterno parámetro requerido para ejecutar la operación.
     */
    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    /**
     * Obtiene el valor de usuario.
     *
     * @return resultado generado por la operación.
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Asigna el valor de usuario.
     *
     * @param usuario parámetro requerido para ejecutar la operación.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el valor de contrasena.
     *
     * @return resultado generado por la operación.
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Asigna el valor de contrasena.
     *
     * @param contrasena parámetro requerido para ejecutar la operación.
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Obtiene el valor de correo.
     *
     * @return resultado generado por la operación.
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Asigna el valor de correo.
     *
     * @param correo parámetro requerido para ejecutar la operación.
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Obtiene el valor de telefono.
     *
     * @return resultado generado por la operación.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Asigna el valor de telefono.
     *
     * @param telefono parámetro requerido para ejecutar la operación.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
