/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.user.user.exception;

/**
 * Excepción personalizada lanzada cuando el usuario
 * no se encuentra activo dentro del sistema
 * 
 * @author Dylxn
 */
public class UsuarioInactivoException extends RuntimeException {

    
    /**
     * Crea una excepción con el mensaje que describe la causa.
     * 
     * @param mensaje que describe el error ocurrido
     */
    public UsuarioInactivoException(String mensaje) {
        super(mensaje);
    }
}
