/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.vehicles.vehicles.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uv.sicae.vehicles.vehicles.dto.MensajeRespuesta;
import uv.sicae.vehicles.vehicles.dto.editarestatusvehiculo.EditarEstatusVehiculoPeticion;
import uv.sicae.vehicles.vehicles.dto.editarvehiculo.EditarVehiculoPeticion;
import uv.sicae.vehicles.vehicles.dto.obtenervehiculos.ObtenerVehiculosPeticion;
import uv.sicae.vehicles.vehicles.dto.registrarvehiculo.RegistrarVehiculoPeticion;
import uv.sicae.vehicles.vehicles.entity.Vehiculo;
import uv.sicae.vehicles.vehicles.service.VehiclesService;

/**
 * Clase Controller REST para recibir las peticiones de los microservicios de vehículos
 * y proporcionar los endpoints 
 *
 * @author jeshu
 */
@RestController
@RequestMapping("/api/vehiculos")
public class VehicleController {

    private final VehiclesService vehicleService;

    /**
     * Constructor de la clase e inyecta el servicio encargado de la lógica de negocio service
     *
     * @param vehiclesService Servicio que contiene la lógica de los vehículos
     */
    public VehicleController(VehiclesService vehiclesService) {
        this.vehicleService = vehiclesService;
    }

    /**
     * Método encargado de recibir un {@link idUsuario} que identifica a un usuario,
     * por el cuál se obtendrá una {@link List<Vehiculo>} donde se guardarán los
     * vehículos asociados al usuario
     * @param authorization String de autorización dentro del header para validación del Token
     * @param peticion El objeto {@link ObtenerVehiculosPeticion} donde se envuelve 
     * el id del usuario a obtener sus vehiculos
     * @return Respuesta HTTP dentro de una entidad {@link ResponseEntity} 
     */
    @GetMapping("/obtenervehiculos")
    public ResponseEntity<List<Vehiculo>> obtenerVehiculosPorIdUsuario(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody ObtenerVehiculosPeticion peticion
    ) {
        return ResponseEntity.ok(vehicleService.obtenerVehiculosPorIdUsuario(authorization, peticion));
    }

    /**
     * Método para registrar un vehículo, de manera que se reciben sus datos dentro
     * de una petición {@link RegistrarVehiculoPeticion} 
     * @param authorization String de autorización dentro del header para validación del Token
     * @param peticion El objeto {@link RegistrarVehiculoPeticion} donde se envuelven los datos del vehículo 
     * a guardar
     * @return Respuesta HTTP dentro de una entidad {@link ResponseEntity} 
     */
    @PostMapping("/registrarse")
    public ResponseEntity<MensajeRespuesta> registrarVehiculo(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody RegistrarVehiculoPeticion peticion) {
        return ResponseEntity.ok(vehicleService.registrarVehiculo(authorization, peticion));
    }
    
    /**
     * Método para editar los datos de un vehículo 
     * @param authorization String de autorización dentro del header para validación del Token
     * @param peticion El objeto {@link EditarVehiculoPeticion} donde se envuelven los datos de edición 
     * del vehículo
     * @return Respuesta HTTP dentro de una entidad {@link ResponseEntity} 
     */
    @PutMapping("/editar")
    public ResponseEntity<MensajeRespuesta> editarVehiculo(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody EditarVehiculoPeticion peticion) {
        return ResponseEntity.ok(vehicleService.editarVehiculo(authorization, peticion));
    }

    /**
     * Método para editar solo el estatus de un vehículo 
     * @param authorization String de autorización dentro del header para validación del Token
     * @param peticion El objeto {@link EditarEstatusVehiculoPeticion} donde se envuelven los datos de
     * edición del vehículo
     * @return Respuesta HTTP dentro de una entidad {@link ResponseEntity} 
     */
    @PutMapping("/editarestatus")
    public ResponseEntity<MensajeRespuesta> editarEstatusVehiculo(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody EditarEstatusVehiculoPeticion peticion) { 
        return ResponseEntity.ok(vehicleService.editarEstatusVehiculo(authorization, peticion));
    }
            

}
