/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.vehicles.vehicles.dto.editarestatusvehiculo;

/**
 * Clase DTO utilizada para envolver los datos de una petición para editar 
 * el estatus de un vehículo
 * @author jeshu
 */
public class EditarEstatusVehiculoPeticion {
    private Integer estatus;
    private Integer idVehiculo;
    private Integer idUsuario;

    public Integer getEstatus() {
        return estatus;
    }

    public void setEstatus(Integer estatus) {
        this.estatus = estatus;
    }


    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    
}
