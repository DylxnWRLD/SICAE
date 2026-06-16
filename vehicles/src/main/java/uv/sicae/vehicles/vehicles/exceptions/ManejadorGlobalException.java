/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.vehicles.vehicles.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;


/*
 * Clase encargada de manejar las excepciones que la app puede lanzar
 * @author Dylxn y jeshu
 */
@RestControllerAdvice
public class ManejadorGlobalException {

    /**
     * Manejador de excepciones para cuando no se cumplen los datos obligatorios
     * @param ex La excepción generada
     * @return Respuesta HTTP dentro de una entidad {@link ResponseEntity} 
     */
    @ExceptionHandler(CampoObligatorioException.class)
    public ResponseEntity<String> manejarCampoObligatorio(CampoObligatorioException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    /**
     * Manejador de excepciones para cuando hay un acceso no autorizado
     * @param ex La excepción generada
     * @return Respuesta HTTP dentro de una entidad {@link ResponseEntity} 
     */
    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<String> manejarCredencialesInvalidas(AccesoDenegadoException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    /**
     * Manejador de excepciones para cuando no se cumple el largo de un parámetro
     * @param ex La excepción generada
     * @return Respuesta HTTP dentro de una entidad {@link ResponseEntity} 
     */
    @ExceptionHandler(LargoCampoException.class)
    public ResponseEntity<String> manjerLargoCampo(LargoCampoException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    /**
     * Manejador de excepciones para cuando un usuario no está autorizado para realizar
     * una acción
     * @param ex La excepción generada
     * @return Respuesta HTTP dentro de una entidad {@link ResponseEntity} 
     */
    @ExceptionHandler(NoAutorizadoException.class)
    public ResponseEntity<String> manejarNoAutorizado(NoAutorizadoException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    /**
     * Manejador de excepciones para cuando no se encontró respuesta a una petición
     * @param ex La excepción generada
     * @return Respuesta HTTP dentro de una entidad {@link ResponseEntity} 
     */
    @ExceptionHandler(ResultadoVacioException.class)
    public ResponseEntity<String> manejarRecursoNoEncontrado(ResultadoVacioException ex) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ex.getMessage());
    }

    /**
     * Manejador de excepciones para cuando hay un vehículo con la misma placa indicada
     * en una petición
     * @param ex La excepción generada
     * @return Respuesta HTTP dentro de una entidad {@link ResponseEntity} 
     */
    @ExceptionHandler(VehiculosConMismaPlacaException.class)
    public ResponseEntity<String> manejarRegistroDuplicado(VehiculosConMismaPlacaException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }
    
    /**
     * Manejador de excepciones para cuando hay un registro duplicado
     * @param ex La excepción generada
     * @return Respuesta HTTP dentro de una entidad {@link ResponseEntity} 
     */
    @ExceptionHandler(VehiculosActivosException.class)
    public ResponseEntity<String> manejarRegistroDuplicado(VehiculosActivosException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }
    
    /**
     * Manejador de excepciones para cuando no hay contenido de respuesta
     * @param ex La excepción generada
     * @return Respuesta HTTP dentro de una entidad {@link ResponseEntity} 
     */
    @ExceptionHandler(RecursoInexistenteException.class)
    public ResponseEntity<String> manejarRecursoInexistente(RecursoInexistenteException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
    
    /**
     * Manejador de excepciones para cuando se recibe un tipo de argumento erroneo o inesperado
     * @param ex La excepción generada
     * @return Respuesta HTTP dentro de una entidad {@link ResponseEntity}
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> manejarErrorArgumentos(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Error en el tipo de argumento recibido");
    }
    
    /**
     * Manejador de excepciones para cuando no se encuentra un recurso
     * @param ex La excepción generada
     * @return Respuesta HTTP dentro de una entidad {@link ResponseEntity}
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> manejarNoEncontrado(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Recurso no encontrado");
    }

    /**
     * Manejador de excepciones para cuando hay un error general en el servidor
     * @param ex La excepción generada
     * @return Respuesta HTTP dentro de una entidad {@link ResponseEntity} 
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarErrorGeneral(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ocurrió un error inesperado en el servidor");
    }
    
}
