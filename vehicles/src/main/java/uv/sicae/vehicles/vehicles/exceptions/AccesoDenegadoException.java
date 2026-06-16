/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.vehicles.vehicles.exceptions;

/**
 * Excepción personalizada lanzada cuando a un usuario no le corresponde un token
 * @author jeshu
 */
public class AccesoDenegadoException extends RuntimeException {
    /**
     * Método para mostrar el mensaje indicado como parámetro
     * @param mensaje El String indicado como mensaje
     */
    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}

