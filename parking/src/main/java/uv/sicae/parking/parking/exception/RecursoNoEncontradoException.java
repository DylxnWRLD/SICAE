/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.parking.parking.exception;

/**
 * Excepción utilizada cuando un recurso solicitado no existe.
 *
 * Se lanza cuando no se encuentra información necesaria para completar una
 * operación del microservicio de estacionamiento, por ejemplo, cuando no existe
 * un espacio de estacionamiento con el identificador recibido o cuando no se
 * localiza una entrada activa para un vehículo.
 *
 * @author josec
 */
public class RecursoNoEncontradoException extends RuntimeException {

    /**
     * Crea una excepción de recurso no encontrado con un mensaje específico.
     *
     * @param mensaje descripción del recurso que no fue localizado durante la
     * operación.
     */
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
