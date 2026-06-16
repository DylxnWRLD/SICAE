/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.vehicles.vehicles.service;

import java.util.List;

import org.springframework.stereotype.Service;

import uv.sicae.vehicles.vehicles.dto.MensajeRespuesta;
import uv.sicae.vehicles.vehicles.dto.editarestatusvehiculo.EditarEstatusVehiculoPeticion;
import uv.sicae.vehicles.vehicles.dto.editarvehiculo.EditarVehiculoPeticion;
import uv.sicae.vehicles.vehicles.dto.registrarvehiculo.RegistrarVehiculoPeticion;
import uv.sicae.vehicles.vehicles.entity.DatosToken;
import uv.sicae.vehicles.vehicles.entity.Vehiculo;
import uv.sicae.vehicles.vehicles.exceptions.AccesoDenegadoException;
import uv.sicae.vehicles.vehicles.exceptions.CampoObligatorioException;
import uv.sicae.vehicles.vehicles.exceptions.LargoCampoException;
import uv.sicae.vehicles.vehicles.exceptions.NoAutorizadoException;
import uv.sicae.vehicles.vehicles.exceptions.RecursoInexistenteException;
import uv.sicae.vehicles.vehicles.exceptions.ResultadoVacioException;
import uv.sicae.vehicles.vehicles.exceptions.VehiculosActivosException;
import uv.sicae.vehicles.vehicles.exceptions.VehiculosConMismaPlacaException;
import uv.sicae.vehicles.vehicles.repository.VehicleRepository;
import uv.sicae.vehicles.vehicles.security.ServicioJWT;

/**
 * Clase service para los vehiculos, incluyendo la lógica de negocio y
 * validaciones, las cuales se basan en el dominio exclusivo de los vehículos y
 * requieren autenticación de un token
 *
 * @author jeshu
 */
@Service
public class VehiclesService {

    private final VehicleRepository vehicleRepository;
    // Para trabajar con los tokens
    private final ServicioJWT servicioJWT;

    /**
     * Constructor con la dependencia del repository y el servicio de los tokens
     *
     * @param vehicleRepository Repository para interactuar con la bd
     * @param servicioJWT Servicio de validación de los tokens
     */
    public VehiclesService(VehicleRepository vehicleRepository, ServicioJWT servicioJWT) {
        this.vehicleRepository = vehicleRepository;
        this.servicioJWT = servicioJWT;
    }

    /**
     * Servicio para obtener los vehículos de un usuario por medio de su ID
     *
     * @param authorizationHeader String de autorización dentro del header para
     * validación del Token
     * @param idUsuario El id del usuario para buscar sus vehículos asociados
     * @return Una lista de vehiculos {@link vehiculos} con todos los vehículos
     * del usuario
     *
     * @throws CampoObligatorioException Si el id es null
     * @throws NoAutorizadoException Si el token es invalido (desde ServicioJWT)
     * @throws AccesoDenegadoException Si el token no corresponde al id del
     * usuario
     * @throws ResultadoVacioException Si no hay vehiculos para mostrar
     */
    public List<Vehiculo> obtenerVehiculosPorIdUsuario(String authorizationHeader,
            Integer idUsuario) {

        // Validar campo obligatorio de Id
        if (idUsuario == null) {
            throw new CampoObligatorioException("El ID es un campo obligatorio");
        }

        // Validar el Token del usuario
        DatosToken token = servicioJWT.validarToken(authorizationHeader);

        if (!token.getIdUsuario().equals(idUsuario)) {
            throw new AccesoDenegadoException("No puedes acceder a vehículos de otro usuario");
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
     * Servicio para registrar un vehículo recibiendo todos los datos envueltos
     * en un objeto {@link RegistrarVehiculoPeticion}
     *
     * @param authorizationHeader String de autorización dentro del header para
     * validación del Token
     * @param peticion Objeto {@link RegistrarVehiculoPeticion} donde se
     * envuelven todos los datos del vehiculo a registrar
     * @return Un mensaje de respuesta indicando si se añadió correctamente
     *
     * @throws CampoObligatorioException Si idUsuario, idVehiculo, idModelo,
     * placa, color anio, estatus o descripción no se encuentra en la petición
     * @throws NoAutorizadoException Si el token es invalido (desde ServicioJWT)
     * @throws VehiculosActivosException Si el usuario cuyo ID se encuentra en
     * la petición ya tiene 4 autos activos
     * @throws VehiculosConMismaPlacaException Si ya existe un vehiculo con la
     * misma placa
     * @throws LargoCampoException Si alguno de los campos excede la longitud
     * especificada en la BD
     */
    public MensajeRespuesta registrarVehiculo(String authorizationHeader,
            RegistrarVehiculoPeticion peticion) {

        //Validar los campos obligatorios
        validarCamposObligatoriosRegistro(peticion);

        // Validar el Token del usuario
        DatosToken token = servicioJWT.validarToken(authorizationHeader);

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

        // Valor de estatus activo por defecto 
        if (peticion.getEstatus() == null) {
            peticion.setEstatus(1);
        }

        // Llamar al repository
        int registroVehiculo = vehicleRepository.registrarVehiculo(peticion);

        return new MensajeRespuesta("Vehiculo añadido exitosamente");

    }

    /**
     * Servicio para editar un vehículo recibiendo los datos para su edición
     * envueltos en una petición {@link EditarVehiculoPeticion}
     *
     * @param authorizationHeader String de autorización dentro del header para
     * validación del Token
     * @param peticion Objeto {@link EditarVehiculoPeticion} donde se envuelven
     * los datos para la edición del vehiculo
     * @return Un mensaje de respuesta si se añadió correctamente
     *
     * @throws CampoObligatorioException Si idUsuario, idVehiculo, idModelo,
     * placa, color anio o descripción no se encuentra en la petición
     * @throws NoAutorizadoException Si el token es invalido (desde ServicioJWT)
     * @throws AccesoDenegadoException Si el token no corresponde al id del
     * usuario
     * @throws RecursoInexistenteException Si no existe algún vehículo que
     * coincida con el idUsuario, idVehiculo o idModelo de la petición en la BD
     * @throws VehiculosConMismaPlacaException Si en la petición se indica que
     * se quiere editar la placa al valor de una placa que ya existe
     * @throws LargoCampoException Si alguno de los campos excede la longitud
     * especificada en la BD
     * @throws RecursoInexistenteException Si no existe el vehículo que se
     * quiere editar
     */
    public MensajeRespuesta editarVehiculo(String authorizationHeader,
            EditarVehiculoPeticion peticion) {

        // Validar campos obligatorios
        validarCamposObligatoriosEdicion(peticion);

        // Validar el Token del usuario
        DatosToken token = servicioJWT.validarToken(authorizationHeader);

        // Validar que no modifique los vehiculos de otro usuario
        if (!token.getIdUsuario().equals(peticion.getIdUsuario())) {
            throw new AccesoDenegadoException("No puedes modificar los vehículos de otro usuario");
        }

        // Validar de una vez si existe algún vehículo con los IDs de la petición
        int existeVehiculo = vehicleRepository.verificarExistenciaVehiculo(peticion.getIdUsuario(),
                peticion.getIdVehiculo(), peticion.getIdModelo());

        if (existeVehiculo < 1) {
            throw new RecursoInexistenteException("No se encontró ningún vehiculo con idUsuario "
                    + peticion.getIdUsuario() + ", idVehiculo " + peticion.getIdVehiculo() + " y idModelo "
                    + peticion.getIdModelo());
        }

        // Obtener el Id del auto con la misma placa de la petición
        Integer vehiculosConMismaPlaca = vehicleRepository.obtenerIdPorPlaca(peticion.getPlaca());

        // Si existe un vehículo con esa placa, verificar que sea el mismo que estamos editando
        if (vehiculosConMismaPlaca != null && !vehiculosConMismaPlaca.equals(peticion.getIdVehiculo())) {
            throw new VehiculosConMismaPlacaException("No puede haber más de un vehiculo con la misma placa");
        }

        // Validar el largo de los campos
        validarLongitud(peticion.getPlaca(), 7);
        validarLongitud(peticion.getColor(), 20);
        validarLongitud(peticion.getDescripcion(), 255);

        // Llamar a Repository
        Integer edicionVehiculo = vehicleRepository.editarVehiculo(peticion);

        // Validar que se editó el vehículo
        if (edicionVehiculo == null) {
            throw new RecursoInexistenteException("No se encontró ningún vehiculo para editar");
        }

        return new MensajeRespuesta("Vehiculo editado correctamente");

    }

    /**
     * Servicio para editar el estatus de un vehículo de activo a inactivo, por
     * medio de una petición {@link EditarEstatusVehiculoPeticion}
     *
     * @param authorizationHeader String de autorización dentro del header para
     * validación del Token
     * @param peticion Objeto {@link EditarEstatusVehiculoPeticion} donde se
     * envuelven los datos de la petición para editar el estatus del vehículo
     * indicado dentro de la misma
     * @return Un mensaje de respuesta si se añadió correctamente
     *
     * @throws CampoObligatorioException Si idUsuario ó idVehiculo no se
     * encuentran en la petición
     * @throws NoAutorizadoException Si el token es invalido (desde ServicioJWT)
     * @throws AccesoDenegadoException Si el token no corresponde al id del
     * usuario
     * @throws RecursoInexistenteException Si no existe algún vehículo que
     * coincida con el idUsuario ó idVehiculo de la petición en la BD
     * @throws VehiculosActivosException Si el usuario ya tiene 4 vehículos
     * activos y en su petición indica que quiere cambiar el estatus de otro a
     * activo
     * @throws RecursoInexistenteException Si la BD retorno 0, indicando que no
     * se editó nada porque no encontró un vehículo que coincidiera con el
     * idUsuario o idVehiculo de la petición
     *
     */
    public MensajeRespuesta editarEstatusVehiculo(String authorizationHeader,
            EditarEstatusVehiculoPeticion peticion) {

        //Validar campos obligatorios
        validarCamposObligatoriosEdicionEstatus(peticion);

        // Validar el Token del usuario
        DatosToken token = servicioJWT.validarToken(authorizationHeader);

        // Validar que este editando sus autos
        if (!token.getIdUsuario().equals(peticion.getIdUsuario())) {
            throw new AccesoDenegadoException("No puedes modificar los vehículos de otro usuario");
        }

        // Validar que el usuario no tenga más de 4 vehiculos activos
        Integer vehiculosActivos = vehicleRepository.contarVehiculosActivos(peticion.getIdUsuario());

        // Validar que existan vehiculos activos con el idUsuario de la petición
        if (vehiculosActivos == null) {
            throw new RecursoInexistenteException("No se encontró ningún vehículo con idUsuario "
                    + peticion.getIdUsuario() + " y idVehiculo " + peticion.getIdVehiculo());
        }

        // Validar que no tenga 4 vehiculos activos y quiera activar uno más
        if (vehiculosActivos >= 4 && peticion.getEstatus() == 1) {
            throw new VehiculosActivosException("No puede haber más de 4 vehículos activos de un mismo dueño");
        }

        // Llamar a repository
        int actualizacionEstatusVehiculo = vehicleRepository.actualizarEstatusVehiculo(peticion);

        // Validar que se realizó el cambio
        if (actualizacionEstatusVehiculo == 0) {
            throw new RecursoInexistenteException("No se encontró ningún vehículo con idUsuario "
                    + peticion.getIdUsuario() + " y idVehiculo " + peticion.getIdVehiculo());
        }

        return new MensajeRespuesta("Estatus actualizado a " + peticion.getEstatus());
    }

    /**
     *
     * Métodos para las validaciones de las peticiones
     *
     */
    /**
     * Método para validar los campos obligatorios de una petición
     * {@link RegistrarVehiculoPeticion}, donde se valida que ninguno sea nulo,
     * los valores de id no sean menores o iguales a cero y las cadenas de
     * caracteres no sean solo espacios
     *
     * @param peticion El objeto que envuelve todos los valores de una petición
     * {@link RegistrarVehiculoPeticion}
     *
     * @throws CampoObligatorioException Si el campo es nulo, si es menor o
     * igual a cero para los id ó que no sea solo espacios para las cadenas de
     * caracteres
     */
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

    /**
     * Método para validar los campos obligatorios de una petición
     * {@link EditarVehiculoPeticion}, donde se valida que ninguno sea nulo, los
     * valores de id no sean menores o iguales a cero y las cadenas de
     * caracteres no sean solo espacios
     *
     * @param peticion El objeto que envuelve todos los valores de una petición
     * {@link EditarVehiculoPeticion}
     *
     * @throws CampoObligatorioException Si el campo es nulo, si es menor o
     * igual a cero para los id ó que no sea solo espacios para las cadenas de
     * caracteres
     */
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

    /**
     * Método para validar los campos obligatorios de una petición
     * {@link EditarEstatusVehiculoPeticion}, donde se valida que ninguno sea
     * nulo y los valores de id no sean menores o iguales a cero
     *
     * @param peticion El objeto que envuelve todos los valores de una petición
     * {@link EditarEstatusVehiculoPeticion}
     *
     * @throws CampoObligatorioException Si el campo es nulo y si es menor o
     * igual a cero para los id
     */
    private void validarCamposObligatoriosEdicionEstatus(EditarEstatusVehiculoPeticion peticion) {
        if (peticion.getIdUsuario() == null || peticion.getIdUsuario() <= 0) {
            throw new CampoObligatorioException("El id del usuario es obligatorio");
        }

        if (peticion.getIdVehiculo() == null || peticion.getIdVehiculo() <= 0) {
            throw new CampoObligatorioException("El id del vehiculo es obligatorio");
        }

    }

    /**
     * Método para validar la longitud de un valor que recibe como parámetro
     *
     * @param valor El valor a validar su longitud
     * @param max El máximo de longitud aceptada
     *
     * @throws LargoCampoException Si se excede la longitud indicada
     */
    private void validarLongitud(String valor, int max) {
        if (valor.length() > max) {
            String mensaje = "El campo " + valor + " no debe ser mayor a " + max
                    + " caracteres";
            throw new LargoCampoException(mensaje);
        }
    }

}
