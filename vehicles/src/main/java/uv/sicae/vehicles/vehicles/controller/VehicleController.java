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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uv.sicae.vehicles.vehicles.dto.MensajeRespuesta;
import uv.sicae.vehicles.vehicles.dto.editarestatusvehiculo.EditarEstatusVehiculoPeticion;
import uv.sicae.vehicles.vehicles.dto.editarvehiculo.EditarVehiculoPeticion;
import uv.sicae.vehicles.vehicles.dto.obtenervehiculosid.ObtenerVehiculosPorIdRespuesta;
import uv.sicae.vehicles.vehicles.dto.registrarvehiculo.RegistrarVehiculoPeticion;
import uv.sicae.vehicles.vehicles.entity.Vehiculo;
import uv.sicae.vehicles.vehicles.service.VehiclesService;

/**
 * Controlador para enrutar peticiones de vehiculos jeje
 *
 * @author jeshu
 */
@RestController
@RequestMapping("/api/vehiculos")
public class VehicleController {

    private final VehiclesService vehicleService;

    public VehicleController(VehiclesService vehiclesService) {
        this.vehicleService = vehiclesService;
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<Vehiculo>> obtenerVehiculosPorIdUsuario(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Integer idUsuario
    ) {
        return ResponseEntity.ok(vehicleService.obtenerVehiculosPorIdUsuario(authorization, idUsuario));
    }

    @PostMapping("/registrarse")
    public ResponseEntity<MensajeRespuesta> registrarVehiculo(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody RegistrarVehiculoPeticion peticion) {
        return ResponseEntity.ok(vehicleService.registrarVehiculo(authorization, peticion));
    }
    
    @PostMapping("/editar")
    public ResponseEntity<MensajeRespuesta> editarVehiculo(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody EditarVehiculoPeticion peticion) {
        return ResponseEntity.ok(vehicleService.editarVehiculo(authorization, peticion));
    }

    @PostMapping("/editarestatus")
    public ResponseEntity<MensajeRespuesta> editarEstatusVehiculo(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody EditarEstatusVehiculoPeticion peticion) { 
        return ResponseEntity.ok(vehicleService.editarEstatusVehiculo(authorization, peticion));
    }
            

}
