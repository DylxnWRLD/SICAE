/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.parking.parking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO utilizado para devolver la respuesta del registro de entrada de un
 * vehículo al estacionamiento.
 *
 * Contiene el mensaje de confirmación y los datos principales del movimiento
 * registrado, como el identificador del movimiento, el vehículo, la hora de
 * entrada, el espacio asignado y la tarifa por hora aplicada.
 *
 * Este objeto se envía como respuesta al cliente cuando la entrada se registra
 * correctamente.
 *
 * @author josec
 */
public class EntradaMovimientoRespuesta {

    private String mensaje;
    private Integer idMovimiento;
    private Integer idVehiculo;
    private LocalDateTime tiempoEntrada;
    private Integer idEspacio;
    private String claveEspacio;
    private BigDecimal tarifaHora;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Integer getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public LocalDateTime getTiempoEntrada() {
        return tiempoEntrada;
    }

    public void setTiempoEntrada(LocalDateTime tiempoEntrada) {
        this.tiempoEntrada = tiempoEntrada;
    }

    public Integer getIdEspacio() {
        return idEspacio;
    }

    public void setIdEspacio(Integer idEspacio) {
        this.idEspacio = idEspacio;
    }

    public String getClaveEspacio() {
        return claveEspacio;
    }

    public void setClaveEspacio(String claveEspacio) {
        this.claveEspacio = claveEspacio;
    }

    public BigDecimal getTarifaHora() {
        return tarifaHora;
    }

    public void setTarifaHora(BigDecimal tarifaHora) {
        this.tarifaHora = tarifaHora;
    }
}
