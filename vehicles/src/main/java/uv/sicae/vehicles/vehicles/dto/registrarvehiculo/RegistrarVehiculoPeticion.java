/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.vehicles.vehicles.dto.registrarvehiculo;

/**
 * Clase DTO utilizada para envolver los datos de una petición para registrar un 
 * nuevo vehículo
 * @author jeshu
 */
public class RegistrarVehiculoPeticion {
    private Integer idUsuario;
    private Integer idVehiculo;
    private Integer idModelo;
    private String claveVehiculo;
    private String placa;
    private String color;
    private Integer anio;
    private Integer estatus;
    private String descripcion;


    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Integer getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(Integer idModelo) {
        this.idModelo = idModelo;
    }

    public String getClaveVehiculo() {
        return claveVehiculo;
    }

    public void setClaveVehiculo(String claveVehiculo) {
        this.claveVehiculo = claveVehiculo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getEstatus() {
        return estatus;
    }

    public void setEstatus(Integer estatus) {
        this.estatus = estatus;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    

    
    
    
}
