/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.parking.parking.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO utilizado para recibir los datos necesarios al registrar la salida de un
 * vehículo del estacionamiento.
 *
 * Contiene la clave del usuario y la placa del vehículo que desea salir. Estos
 * datos son enviados por el cliente en el cuerpo de la solicitud HTTP y se
 * validan antes de procesar la salida.
 *
 * Este objeto permite identificar la operación solicitada sin recibir
 * directamente el identificador del vehículo, ya que dicho identificador se
 * obtiene internamente mediante la consulta y validación con el microservicio
 * de vehículos.
 *
 * @author josec
 */
public class SalidaMovimientoPeticion {

    @NotBlank(message = "La claveUsuario es obligatoria")
    private String claveUsuario;

    @NotBlank(message = "La placa es obligatoria")
    private String placa;

    public String getClaveUsuario() {
        return claveUsuario;
    }

    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
}
