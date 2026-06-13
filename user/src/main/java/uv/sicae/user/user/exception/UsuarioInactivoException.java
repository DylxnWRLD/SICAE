/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.user.user.exception;

/**
 *
 * @author Dylxn
 */

/*
    Excepción usada cuando el usuario existe, pero está inactivo
*/
public class UsuarioInactivoException extends RuntimeException {

    public UsuarioInactivoException(String mensaje) {
        super(mensaje);
    }
}
