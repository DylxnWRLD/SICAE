/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.vehicles.vehicles.exceptions;

/**
 * Excepción personalizada lanzada cuando un usuario tiene 4 vehículos activos e intenta
 * registrar / cambiar de estatus a activo uno más
 * @author jeshu
 */
public class VehiculosActivosException extends RuntimeException {
    /**
     * Método para mostrar el mensaje indicado como parámetro
     * @param mensaje El String indicado como mensaje
     */
    public VehiculosActivosException(String mensaje) {
        super(mensaje);
    }
}