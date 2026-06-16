/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.vehicles.vehicles.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import uv.sicae.vehicles.vehicles.dto.MensajeRespuesta;
import uv.sicae.vehicles.vehicles.dto.editarestatusvehiculo.EditarEstatusVehiculoPeticion;
import uv.sicae.vehicles.vehicles.dto.editarvehiculo.EditarVehiculoPeticion;
import uv.sicae.vehicles.vehicles.dto.obtenervehiculosid.ObtenerVehiculosPorIdRespuesta;
import uv.sicae.vehicles.vehicles.dto.registrarvehiculo.RegistrarVehiculoPeticion;
import uv.sicae.vehicles.vehicles.exceptions.CampoObligatorioException;
import uv.sicae.vehicles.vehicles.entity.DatosToken;
import uv.sicae.vehicles.vehicles.entity.Vehiculo;
import uv.sicae.vehicles.vehicles.exceptions.AccesoDenegadoException;
import uv.sicae.vehicles.vehicles.exceptions.LargoCampoException;
import uv.sicae.vehicles.vehicles.exceptions.RecursoInexistenteException;
import uv.sicae.vehicles.vehicles.exceptions.ResultadoVacioException;
import uv.sicae.vehicles.vehicles.exceptions.VehiculosActivosException;
import uv.sicae.vehicles.vehicles.exceptions.VehiculosConMismaPlacaException;
import uv.sicae.vehicles.vehicles.repository.VehicleRepository;
import uv.sicae.vehicles.vehicles.security.ServicioJWT;

/**
 * Clase service para los vehiculos
 *
 * @author jeshu
 */
@Service
public class VehiclesService {

    private final VehicleRepository vehicleRepository;
    private final ServicioJWT servicioJWT;

    public VehiclesService(VehicleRepository vehicleRepository, ServicioJWT servicioJWT) {
        this.vehicleRepository = vehicleRepository;
        this.servicioJWT = servicioJWT;
    }

    /**
     * Servicio para obtener los vehículos de un usuario por medio de su ID
     *
     * @param authorizationHeader
     * @param idUsuario
     * @return
     */
    public List<Vehiculo> obtenerVehiculosPorIdUsuario(String authorizationHeader,
            Integer idUsuario) {
        // Validar el Token del usuario
        DatosToken token = servicioJWT.validarToken(authorizationHeader);

        if (!token.getIdUsuario().equals(idUsuario)) {
            throw new AccesoDenegadoException("No puedes acceder a vehículos de otro usuario");
        }

        // Validar campo obligatorio de Id
        if (idUsuario == null) {
            throw new CampoObligatorioException("El ID es un campo obligatorio");
        }

        // Llamar a repository
        List<Vehiculo> vehiculos = vehicleRepository.ObtenerVehiculosPorId(idUsuario);


        // Validar si el usuario no tiene vehiculos
        if (vehiculos.isEmpty()) {
            throw new ResultadoVacioException("El usuario indicado no tiene vehículos asociados");
        }

        return vehiculos;
    }

    /**
     * Servicio para registrar un vehículo
     *
     * @param authorizationHeader
     * @param peticion
     * @return
     */
    public MensajeRespuesta registrarVehiculo(String authorizationHeader,
            RegistrarVehiculoPeticion peticion) {
        // Validar el Token del usuario
        DatosToken token = servicioJWT.validarToken(authorizationHeader);

        //Validar los campos obligatorios
        validarCamposObligatoriosRegistro(peticion);

        // Estatus activo por defecto por si no se especifica
        if (peticion.getEstatus() == null) {
            peticion.setEstatus(true);
        }

        // Validar que el usuario no tenga más de 4 vehículos activos
        int vehiculosActivos = vehicleRepository.contarVehiculosActivos(peticion.getIdUsuario());
    

        if (vehiculosActivos >= 4) {
            throw new VehiculosActivosException("No puede haber más de 4 vehículos de un mismo dueño");
        }

        // Validar que no haya un vehículo con la misma placa
        int vehiculosConMismaPlaca = vehicleRepository.contarVehiculosPorPlaca(peticion.getPlaca());

        if (vehiculosConMismaPlaca > 0) {
            throw new VehiculosConMismaPlacaException("Ya existe un vehículo con la placa "
                    + peticion.getPlaca());
        }

        // Validar el largo de los campos
        validarLongitud(peticion.getClaveVehiculo(), 10);
        validarLongitud(peticion.getPlaca(), 7);
        validarLongitud(peticion.getColor(), 20);
        validarLongitud(peticion.getDescripcion(), 255);

        // Llamar al repository
        int registroVehiculo = vehicleRepository.registrarVehiculo(peticion);

        return new MensajeRespuesta("Vehiculo añadido exitosamente");

    }

    /**
     * Servicio para editar un vehículo
     *
     * @param authorizationHeader
     * @param peticion
     * @return
     */
    public MensajeRespuesta editarVehiculo(String authorizationHeader,
            EditarVehiculoPeticion peticion) {
        // Validar el Token del usuario
        DatosToken token = servicioJWT.validarToken(authorizationHeader);

        // Validar que no modifique los vehiculos de otro usuario
        if (!token.getIdUsuario().equals(peticion.getIdUsuario())) {
            throw new AccesoDenegadoException("No puedes modificar los vehículos de otro usuario");
        }

        // Validar campos obligatorios
        validarCamposObligatoriosEdicion(peticion);

        // Obtener el Id del auto con la misma placa de la petición
        Integer vehiculosConMismaPlaca = vehicleRepository.obtenerIdPorPlaca(peticion.getPlaca());
        
        // Validar de una vez si existe algún vehículo con los IDs de la petición
        if (vehiculosConMismaPlaca == null){
            throw new RecursoInexistenteException("No se encontró ningún vehiculo con idUsuario " 
            + peticion.getIdUsuario() + ", idVehiculo " + peticion.getIdVehiculo() + " y idModelo "
            + peticion.getIdModelo());
        }

        // Validar si estamos editando a un vehículo con la misma placa
        if (!vehiculosConMismaPlaca.equals(peticion.getIdVehiculo())) {
            throw new VehiculosConMismaPlacaException("No puede haber más de un vehiculo con la misma placa");
        }
        
        // Validar el largo de los campos
        validarLongitud(peticion.getPlaca(), 7);
        validarLongitud(peticion.getColor(), 20);
        validarLongitud(peticion.getDescripcion(), 255);

        // Llamar a Repository
        Integer edicionVehiculo = vehicleRepository.editarVehiculo(peticion);
        
        // Validar que se edito el vehículo
        if (edicionVehiculo == null){
            throw new RecursoInexistenteException("No se encontró ningún vehiculo para editar");
        }

        return new MensajeRespuesta("Vehiculo editado correctamente");

    }

    /**
     * Servicio para editar el estatus de un vehículo
     *
     * @param authorizationHeader
     * @param peticion
     * @return
     */
    public MensajeRespuesta editarEstatusVehiculo(String authorizationHeader,
            EditarEstatusVehiculoPeticion peticion) {
        // Validar el Token del usuario
        DatosToken token = servicioJWT.validarToken(authorizationHeader);
        
        // Validar que este editando sus autos
        if (!token.getIdUsuario().equals(peticion.getIdUsuario())) {
            throw new AccesoDenegadoException("No puedes modificar los vehículos de otro usuario");
        }
        
        //Validar campos obligatorios
        validarCamposObligatoriosEdicionEstatus(peticion);
        
        // Validar que el usuario no tenga más de 4 vehiculos activos
        Integer vehiculosActivos = vehicleRepository.contarVehiculosActivos(peticion.getIdUsuario());
        
        // Validar que existan vehiculos activos con el idUsuario de la petición
        if (vehiculosActivos == null){
            throw new RecursoInexistenteException("No se encontró ningún vehículo con idUsuario "
            + peticion.getIdUsuario() + " y idVehiculo " + peticion.getIdVehiculo()) ;
        }
        

        if (vehiculosActivos >= 4 && peticion.getEstatus() == true) {
            throw new VehiculosActivosException("No puede haber más de 4 vehículos activos de un mismo dueño");
        }
        
        int actualizacionEstatusVehiculo = vehicleRepository.actualizarEstatusVehiculo(peticion);
        
        System.out.println(actualizacionEstatusVehiculo);
        
        // Validar que se realizó el cambio
        if (actualizacionEstatusVehiculo == 0){
            throw new RecursoInexistenteException("No se encontró ningún vehículo con idUsuario "
            + peticion.getIdUsuario() + " y idVehiculo " + peticion.getIdVehiculo()) ;
        }

        return new MensajeRespuesta("Estatus actualizado a " + peticion.getEstatus());
    }

    private void validarCamposObligatoriosRegistro(RegistrarVehiculoPeticion peticion) {
        if (peticion.getIdUsuario() == null || peticion.getIdUsuario() <= 0) {
            throw new CampoObligatorioException("El id del usuario es obligatorio");
        }

        if (peticion.getIdVehiculo() == null || peticion.getIdVehiculo() <= 0) {
            throw new CampoObligatorioException("El id del vehiculo es obligatorio");
        }

        if (peticion.getIdModelo() == null || peticion.getIdModelo() <= 0) {
            throw new CampoObligatorioException("El id del usuario es obligatorio");
        }

        if (peticion.getPlaca() == null || peticion.getPlaca().trim().isEmpty()) {
            throw new CampoObligatorioException("La placa es obligatoria");
        }

        if (peticion.getColor() == null || peticion.getColor().trim().isEmpty()) {
            throw new CampoObligatorioException("El color es obligatorio");
        }

        if (peticion.getAnio() == null) {
            throw new CampoObligatorioException("El año es obligatorio");
        }

        if (peticion.getDescripcion() == null || peticion.getDescripcion().trim().isEmpty()) {
            throw new CampoObligatorioException("La descripción es obligatoria");
        }

        if (peticion.getClaveVehiculo() == null || peticion.getClaveVehiculo().trim().isEmpty()) {
            throw new CampoObligatorioException("La clave del vehículo es obligatoria");
        }

    }

    private void validarCamposObligatoriosEdicion(EditarVehiculoPeticion peticion) {
        if (peticion.getIdUsuario() == null || peticion.getIdUsuario() <= 0) {
            throw new CampoObligatorioException("El id del usuario es obligatorio");
        }

        if (peticion.getIdVehiculo() == null || peticion.getIdVehiculo() <= 0) {
            throw new CampoObligatorioException("El id del vehiculo es obligatorio");
        }

        if (peticion.getIdModelo() == null || peticion.getIdModelo() <= 0) {
            throw new CampoObligatorioException("El id del usuario es obligatorio");
        }

        if (peticion.getPlaca() == null || peticion.getPlaca().trim().isEmpty()) {
            throw new CampoObligatorioException("La placa es obligatoria");
        }

        if (peticion.getColor() == null || peticion.getColor().trim().isEmpty()) {
            throw new CampoObligatorioException("El color es obligatorio");
        }

        if (peticion.getAnio() == null) {
            throw new CampoObligatorioException("El año es obligatorio");
        }

        if (peticion.getDescripcion() == null || peticion.getDescripcion().trim().isEmpty()) {
            throw new CampoObligatorioException("La descripción es obligatoria");
        }

    }
    
    private void validarCamposObligatoriosEdicionEstatus(EditarEstatusVehiculoPeticion peticion) {
        if (peticion.getIdUsuario() == null || peticion.getIdUsuario() <= 0) {
            throw new CampoObligatorioException("El id del usuario es obligatorio");
        }

        if (peticion.getIdVehiculo() == null || peticion.getIdVehiculo() <= 0) {
            throw new CampoObligatorioException("El id del vehiculo es obligatorio");
        }

    }
    
    

    private void validarLongitud(String valor, int max) {
        if (valor.length() > max) {
            String mensaje = "El campo " + valor + " no debe ser mayor a " + max
                    + " caracteres";
            throw new LargoCampoException(mensaje);
        }
    }

}
