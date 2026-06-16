package uv.sicae.user.user.model;

import java.time.LocalDateTime;

/**
 * Modelo que representa la información completa del perfil de un usuario.
 * 
 * @author Alvaro
 */
public class UsuarioPerfil {
    private Integer idUsuario;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombreCompleto;
    private String claveUsuario;
    private String email;
    private String telefono;
    private String username;
    private Boolean estatus;
    private Integer idRol;
    private String rol;
    private Integer idTipoUsuario;
    private String tipoUsuario;
    private Integer idProgramaEducativo;
    private String programaEducativo;
    private LocalDateTime tiempoCreacion;
    private LocalDateTime tempoActualizacion;

    /**
     * Obtiene el valor de idUsuario.
     *
     * @return resultado generado por la operación.
     */
    public Integer getIdUsuario() {
        return idUsuario;
    }

    /**
     * Asigna el valor de idUsuario.
     *
     * @param idUsuario parámetro requerido para ejecutar la operación.
     */
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
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
     * Obtiene el valor de nombreCompleto.
     *
     * @return resultado generado por la operación.
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * Asigna el valor de nombreCompleto.
     *
     * @param nombreCompleto parámetro requerido para ejecutar la operación.
     */
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    /**
     * Obtiene el valor de claveUsuario.
     *
     * @return resultado generado por la operación.
     */
    public String getClaveUsuario() {
        return claveUsuario;
    }

    /**
     * Asigna el valor de claveUsuario.
     *
     * @param claveUsuario parámetro requerido para ejecutar la operación.
     */
    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
    }

    /**
     * Obtiene el valor de email.
     *
     * @return resultado generado por la operación.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Asigna el valor de email.
     *
     * @param email parámetro requerido para ejecutar la operación.
     */
    public void setEmail(String email) {
        this.email = email;
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

    /**
     * Obtiene el valor de username.
     *
     * @return resultado generado por la operación.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Asigna el valor de username.
     *
     * @param username parámetro requerido para ejecutar la operación.
     */
    public void setUsername(String username) {
        this.username = username;
    }

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
     * Obtiene el valor de rol.
     *
     * @return resultado generado por la operación.
     */
    public String getRol() {
        return rol;
    }

    /**
     * Asigna el valor de rol.
     *
     * @param rol parámetro requerido para ejecutar la operación.
     */
    public void setRol(String rol) {
        this.rol = rol;
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
     * Obtiene el valor de tipoUsuario.
     *
     * @return resultado generado por la operación.
     */
    public String getTipoUsuario() {
        return tipoUsuario;
    }

    /**
     * Asigna el valor de tipoUsuario.
     *
     * @param tipoUsuario parámetro requerido para ejecutar la operación.
     */
    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
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
     * Obtiene el valor de programaEducativo.
     *
     * @return resultado generado por la operación.
     */
    public String getProgramaEducativo() {
        return programaEducativo;
    }

    /**
     * Asigna el valor de programaEducativo.
     *
     * @param programaEducativo parámetro requerido para ejecutar la operación.
     */
    public void setProgramaEducativo(String programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    /**
     * Obtiene el valor de tiempoCreacion.
     *
     * @return resultado generado por la operación.
     */
    public LocalDateTime getTiempoCreacion() {
        return tiempoCreacion;
    }

    /**
     * Asigna el valor de tiempoCreacion.
     *
     * @param tiempoCreacion parámetro requerido para ejecutar la operación.
     */
    public void setTiempoCreacion(LocalDateTime tiempoCreacion) {
        this.tiempoCreacion = tiempoCreacion;
    }

    /**
     * Obtiene el valor de tempoActualizacion.
     *
     * @return resultado generado por la operación.
     */
    public LocalDateTime getTempoActualizacion() {
        return tempoActualizacion;
    }

    /**
     * Asigna el valor de tempoActualizacion.
     *
     * @param tempoActualizacion parámetro requerido para ejecutar la operación.
     */
    public void setTempoActualizacion(LocalDateTime tempoActualizacion) {
        this.tempoActualizacion = tempoActualizacion;
    }
}
