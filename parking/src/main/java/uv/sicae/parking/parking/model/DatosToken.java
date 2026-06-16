/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.parking.parking.model;

/**
 * Modelo utilizado para almacenar la información obtenida a partir de un token
 * JWT válido.
 *
 * Contiene los datos principales del usuario autenticado, como su
 * identificador, rol, nombre de usuario, tipo de usuario y demás información
 * necesaria para validar el acceso a los servicios protegidos del microservicio
 * de estacionamiento.
 *
 * @author josec
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
}
