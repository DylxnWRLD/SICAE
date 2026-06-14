/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.user.user.exception;


/**
 * Excepción personalizada lanzada cuando las credenciales no coinciden
 * con lo que se tiene en la base de datos
 * 
 * 
 * @author Dylxn
 */
public class CredencialesInvalidasException extends RuntimeException {

    /**
     * Crea una excepción con el mensaje que describe la causa.
     * 
     * @param mensaje descripción del error ocurrido 
     */
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
