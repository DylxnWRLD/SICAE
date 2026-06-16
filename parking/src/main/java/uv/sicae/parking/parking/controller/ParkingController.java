/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.parking.parking.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uv.sicae.parking.parking.dto.EntradaMovimientoPeticion;
import uv.sicae.parking.parking.dto.EntradaMovimientoRespuesta;
import uv.sicae.parking.parking.dto.SalidaMovimientoPeticion;
import uv.sicae.parking.parking.dto.SalidaMovimientoRespuesta;
import uv.sicae.parking.parking.model.DatosToken;
import uv.sicae.parking.parking.model.EspacioEstacionamiento;
import uv.sicae.parking.parking.seguridad.ServicioJWT;
import uv.sicae.parking.parking.service.ParkingService;

/**
 * Controlador REST encargado de exponer los endpoints del microservicio de
 * estacionamiento.
 *
 * Recibe las solicitudes HTTP relacionadas con la consulta de espacios
 * disponibles, el registro de entrada de vehículos y el registro de salida del
 * estacionamiento. Antes de procesar las operaciones principales, valida el
 * token JWT recibido en el encabezado Authorization y obtiene los datos del
 * usuario autenticado.
 *
 * Esta clase forma parte de la capa Controller dentro de la arquitectura en
 * capas del microservicio y se encarga de recibir las peticiones, validar los
 * datos de entrada y delegar la lógica de negocio a ParkingService.
 *
 * @author josec
 */
@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    private final ParkingService parkingService;
    private final ServicioJWT servicioJWT;

    /**
     * Crea el controlador de estacionamiento mediante inyección de
     * dependencias.
     *
     * @param parkingService servicio encargado de procesar la lógica de negocio
     * relacionada con espacios, entradas y salidas del estacionamiento.
     * @param servicioJWT servicio encargado de validar el token JWT recibido en
     * las solicitudes HTTP.
     */
    public ParkingController(ParkingService parkingService, ServicioJWT servicioJWT) {
        this.parkingService = parkingService;
        this.servicioJWT = servicioJWT;
    }

    /**
     * Verifica que el microservicio de estacionamiento se encuentre
     * funcionando.
     *
     * Este endpoint se utiliza como una prueba simple para confirmar que el
     * controlador responde correctamente y que el microservicio fue levantado
     * dentro del entorno de ejecución.
     *
     * @return mensaje de confirmación del funcionamiento del microservicio.
     */
    @GetMapping("/ping")
    public String ping() {
        return "Microservicio parking funcionando";
    }

    /**
     * Consulta los espacios disponibles del estacionamiento.
     *
     * Valida el token JWT recibido en el encabezado Authorization. Si el token
     * es válido, solicita a la capa de servicio la lista de espacios activos y
     * libres registrados en la base de datos de estacionamiento.
     *
     * @param authorizationHeader encabezado Authorization que contiene el token
     * JWT enviado por el cliente.
     * @return respuesta HTTP con la lista de espacios disponibles.
     */
    @GetMapping("/espacios")
    public ResponseEntity<List<EspacioEstacionamiento>> consultarEspacios(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        servicioJWT.validarToken(authorizationHeader);

        List<EspacioEstacionamiento> espacios = parkingService.consultarEspacios();

        return ResponseEntity.ok(espacios);
    }

    /**
     * Registra la entrada de un vehículo al estacionamiento.
     *
     * Valida el token JWT recibido en la solicitud y obtiene los datos del
     * usuario autenticado. También recibe y valida el cuerpo de la petición, el
     * cual debe contener la claveUsuario, la placa del vehículo, el espacio
     * asignado y la tarifa por hora.
     *
     * Posteriormente delega la operación a la capa de servicio, donde se
     * validan las reglas de negocio relacionadas con el usuario, el vehículo,
     * la asociación usuario-vehículo, el límite de vehículos dentro del
     * estacionamiento, el espacio disponible y el registro del movimiento de
     * entrada.
     *
     * @param authorizationHeader encabezado Authorization que contiene el token
     * JWT enviado por el cliente.
     * @param peticion objeto con los datos necesarios para registrar la entrada
     * del vehículo.
     * @return respuesta HTTP con la información del movimiento de entrada
     * registrado.
     */
    @PostMapping("/entrada")
    public ResponseEntity<EntradaMovimientoRespuesta> registrarEntrada(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @Valid @RequestBody EntradaMovimientoPeticion peticion) {

        DatosToken datosToken = servicioJWT.validarToken(authorizationHeader);

        EntradaMovimientoRespuesta respuesta = parkingService.registrarEntrada(
                authorizationHeader,
                datosToken,
                peticion
        );

        return ResponseEntity.ok(respuesta);
    }

    /**
     * Registra la salida de un vehículo del estacionamiento.
     *
     * Valida el token JWT recibido en la solicitud y obtiene los datos del
     * usuario autenticado. También recibe y valida el cuerpo de la petición, el
     * cual debe contener la claveUsuario y la placa del vehículo que saldrá del
     * estacionamiento.
     *
     * Posteriormente delega la operación a la capa de servicio, donde se valida
     * la relación entre el usuario y el vehículo, se busca el movimiento
     * abierto, se calcula el tiempo estacionado, las horas cobradas, el costo
     * total y se libera el espacio ocupado.
     *
     * @param authorizationHeader encabezado Authorization que contiene el token
     * JWT enviado por el cliente.
     * @param peticion objeto con los datos necesarios para registrar la salida
     * del vehículo.
     * @return respuesta HTTP con la información del movimiento de salida
     * actualizado.
     */
    @PutMapping("/salida")
    public ResponseEntity<SalidaMovimientoRespuesta> registrarSalida(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @Valid @RequestBody SalidaMovimientoPeticion peticion) {

        DatosToken datosToken = servicioJWT.validarToken(authorizationHeader);

        SalidaMovimientoRespuesta respuesta = parkingService.registrarSalida(
                authorizationHeader,
                datosToken,
                peticion
        );

        return ResponseEntity.ok(respuesta);
    }
}
