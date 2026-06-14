/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.user.user.exception;

/**
 * Excepción personalizada lanzada cuando los campos 
 * exceden el límite de caracteres asignado
 * 
 * @author Dylxn
 */
public class LargoCampoException extends RuntimeException {

    
    /**
     * Crea una excepción con el mensaje que describe la causa.
     * 
     * @param mensaje que describe el error ocurrido
     */
    public LargoCampoException(String mensaje) {
        super(mensaje);

    }
}
