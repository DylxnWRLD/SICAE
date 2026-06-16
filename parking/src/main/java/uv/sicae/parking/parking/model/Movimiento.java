/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.parking.parking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Modelo que representa un movimiento de estacionamiento.
 *
 * Almacena la información relacionada con la entrada y salida de un vehículo,
 * incluyendo el identificador del movimiento, el vehículo asociado, los tiempos
 * de entrada y salida, los minutos estacionados, las horas cobradas, el costo
 * total, la tarifa aplicada y el espacio utilizado.
 *
 * Esta clase se utiliza para registrar movimientos de entrada, actualizar
 * movimientos de salida y consultar la información necesaria para calcular el
 * tiempo de permanencia y el costo del estacionamiento.
 *
 * @author josec
 */
public class Movimiento {

    private Integer idMovimiento;
    private Integer idVehiculo;
    private LocalDateTime tiempoEntrada;
    private LocalDateTime tiempoSalida;
    private Integer minutosEstacionado;
    private Integer horasCobradas;
    private BigDecimal costoTotal;
    private BigDecimal tarifaHora;
    private LocalDateTime tiempoCreacion;
    private LocalDateTime tiempoActualizacion;
    private Integer idEspacio;
    private String claveEspacio;

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

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public BigDecimal getTarifaHora() {
        return tarifaHora;
    }

    public void setTarifaHora(BigDecimal tarifaHora) {
        this.tarifaHora = tarifaHora;
    }

    public LocalDateTime getTiempoCreacion() {
        return tiempoCreacion;
    }

    public void setTiempoCreacion(LocalDateTime tiempoCreacion) {
        this.tiempoCreacion = tiempoCreacion;
    }

    public LocalDateTime getTiempoActualizacion() {
        return tiempoActualizacion;
    }

    public void setTiempoActualizacion(LocalDateTime tiempoActualizacion) {
        this.tiempoActualizacion = tiempoActualizacion;
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
