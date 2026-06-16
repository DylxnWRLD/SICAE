/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.vehicles.vehicles.exceptions;

/**
 * Excepción personalizada lanzada cuando el resultado de una petición no tiene contenido
 * @author jeshu
 */
public class ResultadoVacioException extends RuntimeException {
    /**
     * Método para mostrar el mensaje indicado como parámetro
     * @param mensaje El String indicado como mensaje
     */
    public ResultadoVacioException(String mensaje){
        super(mensaje);
    }
}
