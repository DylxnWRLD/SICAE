/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.vehicles.vehicles.exceptions;

/**
 * Excepción personalizada lanzada cuando no hay respuesta a una petición
 * @author jeshu
 */
public class ErrorBDException extends RuntimeException{
    /**
     * Método para mostrar el mensaje indicado como parámetro
     * @param mensaje El String indicado como mensaje
     */
    public ErrorBDException(String mensaje){
        super(mensaje);
    }
}
