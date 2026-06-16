/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.parking.parking.exception;

/**
 * Excepción utilizada cuando una solicitud no cuenta con autorización válida.
 *
 * Se lanza cuando el token JWT no fue enviado, tiene un formato incorrecto, es
 * inválido o ya expiró. Permite detener el consumo del servicio cuando la
 * petición no cumple con el mecanismo de autenticación requerido.
 *
 * @author josec
 */
public class NoAutorizadoException extends RuntimeException {

    /**
     * Crea una excepción de autorización con un mensaje específico.
     *
     * @param mensaje descripción del motivo por el cual la solicitud no fue
     * autorizada.
     */
    public NoAutorizadoException(String mensaje) {
        super(mensaje);
    }
}
