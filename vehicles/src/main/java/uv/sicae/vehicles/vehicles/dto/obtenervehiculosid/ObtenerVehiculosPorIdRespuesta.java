/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.vehicles.vehicles.dto.obtenervehiculosid;

import java.util.List;
import uv.sicae.vehicles.vehicles.entity.Vehiculo;

/**
 *
 * @author jeshu
 */
public class ObtenerVehiculosPorIdRespuesta {
    private List<Vehiculo> vehiculos; 

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }
    
    public void addVehiculo(Vehiculo vehiculo){
        vehiculos.add(vehiculo);
    }
    
    

    

   
}
