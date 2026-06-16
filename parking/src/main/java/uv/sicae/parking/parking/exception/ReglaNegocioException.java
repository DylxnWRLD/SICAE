/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.parking.parking.exception;

/**
 * Excepción utilizada cuando no se cumple una regla de negocio del
 * microservicio de estacionamiento.
 *
 * Se lanza cuando una operación no puede realizarse debido a las condiciones
 * definidas para el funcionamiento del estacionamiento, por ejemplo, cuando un
 * espacio ya se encuentra ocupado, cuando un espacio está inactivo o cuando un
 * vehículo ya tiene una entrada activa.
 *
 * @author josec
 */
public class ReglaNegocioException extends RuntimeException {

    /**
     * Crea una excepción de regla de negocio con un mensaje específico.
     *
     * @param mensaje descripción de la regla de negocio que no se cumplió
     * durante la operación.
     */
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
