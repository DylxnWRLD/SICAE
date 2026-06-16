/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.vehicles.vehicles.exceptions;

/**
 * Excepción personalizada lanzada cuando un campo está vacío
 * o no tiene contenido válido
 * 
 * @author Dylxn
 */
public class CampoObligatorioException extends RuntimeException {

    /**
     * Crea una excepción con el mensaje que describe el campo obligatorio faltante.
     *
     * @param mensaje descripción del error ocurrido.
     */
    public CampoObligatorioException(String mensaje) {
        super(mensaje);
    }
}