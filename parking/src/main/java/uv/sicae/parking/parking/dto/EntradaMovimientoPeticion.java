/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.parking.parking.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * DTO utilizado para recibir los datos necesarios al registrar la entrada
 * de un vehículo al estacionamiento.
 *
 * Contiene la clave del usuario, la placa del vehículo, el identificador del
 * espacio asignado y la tarifa por hora que se aplicará al movimiento de
 * entrada. Estos datos son enviados por el cliente en el cuerpo de la solicitud
 * HTTP y se validan antes de procesar el registro.
 *
 * Este objeto permite que el microservicio de estacionamiento trabaje con los
 * datos solicitados por la regla de negocio, mientras que el identificador del
 * vehículo se obtiene internamente mediante la validación con el microservicio
 * de vehículos.
 *
 * @author josec
 */
public class EntradaMovimientoPeticion {

    @NotBlank(message = "La claveUsuario es obligatoria")
    private String claveUsuario;

    @NotBlank(message = "La placa es obligatoria")
    private String placa;

    @NotNull(message = "El idEspacio es obligatorio")
    private Integer idEspacio;

    @NotNull(message = "La tarifa por la hora es obligatoria")
    @DecimalMin(value = "0.01", message = "La tarifa por hora debe ser mayor a 0")
    private BigDecimal tarifaHora;

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

    public Integer getIdEspacio() {
        return idEspacio;
    }

    public void setIdEspacio(Integer idEspacio) {
        this.idEspacio = idEspacio;
    }

    public BigDecimal getTarifaHora() {
        return tarifaHora;
    }

    public void setTarifaHora(BigDecimal tarifaHora) {
        this.tarifaHora = tarifaHora;
    }
}
