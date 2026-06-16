/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.vehicles.vehicles.dto.obtenervehiculos;

/**
 * Clase DTO utilizada para envolver una petición para obtener vehiculos por ID
 * @author jeshu
 */
public class ObtenerVehiculosPeticion {
    //El id del usuario
    private Integer idUsuario;

    //Constructor
    public ObtenerVehiculosPeticion() {
    }

    //Getter y setter
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    
}
