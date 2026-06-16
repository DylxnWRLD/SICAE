/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.parking.parking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO utilizado para devolver la respuesta del registro de salida de un
 * vehículo del estacionamiento.
 *
 * Contiene el mensaje de confirmación y los datos principales del movimiento
 * actualizado, como el identificador del movimiento, el vehículo, el tiempo de
 * entrada, el tiempo de salida, los minutos estacionados, las horas cobradas,
 * la tarifa aplicada, el costo total y el espacio liberado.
 *
 * Este objeto se envía como respuesta al cliente cuando la salida se registra
 * correctamente.
 *
 * @author josec
 */
public class SalidaMovimientoRespuesta {

    private String mensaje;
    private Integer idMovimiento;
    private Integer idVehiculo;
    private LocalDateTime tiempoEntrada;
    private LocalDateTime tiempoSalida;
    private Integer minutosEstacionado;
    private Integer horasCobradas;
    private BigDecimal tarifaHora;
    private BigDecimal costoTotal;
    private Integer idEspacio;
    private String claveEspacio;

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

    public LocalDateTime getTiempoSalida() {
        return tiempoSalida;
    }

    public void setTiempoSalida(LocalDateTime tiempoSalida) {
        this.tiempoSalida = tiempoSalida;
    }

    public Integer getMinutosEstacionado() {
        return minutosEstacionado;
    }

    public void setMinutosEstacionado(Integer minutosEstacionado) {
        this.minutosEstacionado = minutosEstacionado;
    }

    public Integer getHorasCobradas() {
        return horasCobradas;
    }

    public void setHorasCobradas(Integer horasCobradas) {
        this.horasCobradas = horasCobradas;
    }

    public BigDecimal getTarifaHora() {
        return tarifaHora;
    }

    public void setTarifaHora(BigDecimal tarifaHora) {
        this.tarifaHora = tarifaHora;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
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
}
