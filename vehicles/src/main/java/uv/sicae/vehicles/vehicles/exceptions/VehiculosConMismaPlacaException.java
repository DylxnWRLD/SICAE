/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.vehicles.vehicles.exceptions;

/**
 * Excepción personalizada lanzada cuando ya existe un vehículo con una placa indicada
 * en una petición
 * @author jeshu
 */
public class VehiculosConMismaPlacaException extends RuntimeException {
    /**
     * Método para mostrar el mensaje indicado como parámetro
     * @param mensaje El String indicado como mensaje
     */
    public VehiculosConMismaPlacaException(String mensaje) {
        super(mensaje);
    } 
    
}
