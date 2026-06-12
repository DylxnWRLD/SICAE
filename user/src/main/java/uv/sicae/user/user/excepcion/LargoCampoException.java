/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.user.user.excepcion;

/**
 *
 * @author Dylxn
 */

/*
Excepción personalizada para cuando se exceda el límte de caracteres de un campo
 */
public class LargoCampoException extends RuntimeException {

    public LargoCampoException(String mensaje) {
        super(mensaje);

    }
}
