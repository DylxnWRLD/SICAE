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
    Excepción usada cuando falta un campo obligatorio
*/
public class CampoObligatorioException extends RuntimeException {

    public CampoObligatorioException(String mensaje) {
        super(mensaje);
    }
}
