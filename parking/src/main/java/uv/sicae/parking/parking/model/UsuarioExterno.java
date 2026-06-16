/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.parking.parking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Modelo utilizado para representar la información básica de un usuario
 * obtenida desde el microservicio de usuarios.
 *
 * Contiene únicamente los datos que el microservicio de estacionamiento
 * necesita para validar sus reglas de negocio, como el identificador del
 * usuario, la clave de usuario y su estatus.
 *
 * Esta clase se utiliza cuando ParkingService consulta UserService para
 * comprobar que el usuario autenticado existe, se encuentra activo y que la
 * claveUsuario enviada en la solicitud corresponde al usuario validado.
 *
 * La anotación JsonIgnoreProperties permite ignorar otros campos que pueda
 * devolver UserService y que no son necesarios para esta validación.
 *
 * @author josec
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioExterno {

    private Integer idUsuario;
    private String claveUsuario;
    private Boolean estatus;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getClaveUsuario() {
        return claveUsuario;
    }

    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
    }

    public Boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(Boolean estatus) {
        this.estatus = estatus;
    }
}
