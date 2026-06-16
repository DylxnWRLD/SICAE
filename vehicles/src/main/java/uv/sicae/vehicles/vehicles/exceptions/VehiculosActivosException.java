/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.vehicles.vehicles.exceptions;

/*
    Excepción usada para cuando hay más de 4 vehiculos del mismo dueño
*/
public class VehiculosActivosException extends RuntimeException {
    public VehiculosActivosException(String mensaje) {
        super(mensaje);
    }
}