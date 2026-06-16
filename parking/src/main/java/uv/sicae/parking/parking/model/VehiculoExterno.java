/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.parking.parking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Modelo utilizado para representar la información básica de un vehículo
 * obtenida desde el microservicio de vehículos.
 *
 * Contiene únicamente los datos que el microservicio de estacionamiento
 * necesita para validar sus reglas de negocio, como el identificador del
 * vehículo, el usuario asociado, la placa y el estatus del vehículo.
 *
 * Esta clase se utiliza cuando ParkingService consulta VehicleService para
 * comprobar que la placa enviada pertenece al usuario autenticado y, cuando
 * corresponde, que el vehículo se encuentra activo.
 *
 * La anotación JsonIgnoreProperties permite ignorar otros campos que pueda
 * devolver VehicleService y que no son necesarios para esta validación.
 *
 * @author josec
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VehiculoExterno {

    private Integer idVehiculo;
    private Integer idUsuario;
    private String placa;
    private Boolean estatus;

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

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(Boolean estatus) {
        this.estatus = estatus;
    }
}
