/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.user.user.dto;

/**
 * DTO que representa la respuesta enviada al cliente después de una
 * autenticación exitosa.
 *
 *
 * @author Dylxn
 */
public class LoginRespuesta {
    
    private Integer idUsuario;
    private Integer idRol;
    private String rol;
    private String usuario;
    private String nombreCompleto;
    private Integer idTipoUsuario;
    private String tipoUsuario;
    private String token;
    
    
    /**
     * Datos que conforman el cuerpo de la respuesta
     * 
     * @param idUsuario identificador del usuario
     * @param idRol identificador del rol asignado al usuario
     * @param rol nombre del rol del usuario
     * @param usuario nombre de usuario
     * @param nombreCompleto nombre completo del usuario
     * @param idTipoUsuario identificador del tipo de usuario
     * @param tipoUsuario nombre del tipo de usuario
     * @param token token JTW generado después de la autenticación
     */
    public LoginRespuesta(Integer idUsuario, Integer idRol, String rol,  String usuario, 
            String nombreCompleto, Integer idTipoUsuario, String tipoUsuario,
    String token){
        this.idUsuario = idUsuario;
        this.idRol = idRol;
        this.rol = rol;
        this.usuario = usuario;
        this.nombreCompleto = nombreCompleto;
        this.idTipoUsuario = idTipoUsuario;
        this.tipoUsuario = tipoUsuario;
        this.token = token;
    }

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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    
    
    
}
