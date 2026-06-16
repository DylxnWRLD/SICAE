/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.parking.parking.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones del microservicio de estacionamiento.
 *
 * Centraliza el manejo de errores generados durante el consumo de la API,
 * permitiendo devolver respuestas claras en formato JSON cuando ocurre una
 * excepción relacionada con autorización, reglas de negocio, recursos no
 * encontrados o validación de datos enviados por el cliente.
 *
 * Esta clase evita que las excepciones se muestren como errores internos sin
 * formato y permite que el cliente reciba un mensaje entendible junto con el
 * código HTTP correspondiente.
 *
 * @author josec
 */
@RestControllerAdvice
public class ManejadorGlobalException {

    /**
     * Maneja las excepciones generadas cuando una solicitud no cuenta con
     * autorización válida.
     *
     * Se ejecuta cuando el token JWT no fue enviado, tiene un formato
     * incorrecto, es inválido o ya expiró. Devuelve una respuesta HTTP 401 con
     * el mensaje correspondiente.
     *
     * @param e excepción de autorización generada durante la validación del
     * token.
     * @return respuesta HTTP con código 401 y mensaje de error.
     */
    @ExceptionHandler(NoAutorizadoException.class)
    public ResponseEntity<Map<String, String>> manejarNoAutorizado(NoAutorizadoException e) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
    }

    /**
     * Maneja las excepciones generadas cuando no se cumple una regla de
     * negocio.
     *
     * Se ejecuta cuando una operación no puede completarse por condiciones
     * propias del estacionamiento, como intentar ocupar un espacio ya ocupado,
     * registrar una entrada duplicada para un vehículo, usar una placa no
     * asociada al usuario o exceder el límite de vehículos permitidos dentro
     * del estacionamiento.
     *
     * @param e excepción de regla de negocio generada durante el proceso.
     * @return respuesta HTTP con código 400 y mensaje de error.
     */
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<Map<String, String>> manejarReglaNegocio(ReglaNegocioException e) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    /**
     * Maneja las excepciones generadas cuando un recurso solicitado no existe.
     *
     * Se ejecuta cuando no se encuentra información necesaria para completar la
     * operación, como un espacio de estacionamiento inexistente, un usuario no
     * localizado o un movimiento abierto no encontrado para un vehículo.
     *
     * @param e excepción generada cuando no se encuentra el recurso solicitado.
     * @return respuesta HTTP con código 404 y mensaje de error.
     */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> manejarNoEncontrado(RecursoNoEncontradoException e) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    /**
     * Maneja los errores de validación de datos recibidos en la solicitud.
     *
     * Se ejecuta cuando algún campo obligatorio del cuerpo de la petición no
     * fue enviado o no cumple con las restricciones definidas en los DTO, como
     * claveUsuario, placa, idEspacio o tarifaHora.
     *
     * Obtiene el primer error de validación detectado y lo devuelve en una
     * respuesta JSON con un mensaje claro para el cliente.
     *
     * @param e excepción generada cuando los datos enviados no cumplen las
     * validaciones establecidas.
     * @return respuesta HTTP con código 400 y mensaje de validación.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidaciones(MethodArgumentNotValidException e) {
        Map<String, String> respuesta = new HashMap<>();

        FieldError error = e.getBindingResult().getFieldError();

        if (error != null) {
            respuesta.put("mensaje", error.getDefaultMessage());
        } else {
            respuesta.put("mensaje", "Los datos enviados no son válidos");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }
}
