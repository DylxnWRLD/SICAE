/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.parking.parking.model;

/**
 * Modelo que representa un espacio físico dentro del estacionamiento.
 *
 * Contiene la información principal de cada cajón registrado en la base de
 * datos, incluyendo su identificador, clave, tipo, estado de ocupación y
 * estatus. Esta clase se utiliza para consultar y devolver los espacios
 * disponibles u ocupados dentro del microservicio de estacionamiento.
 *
 * @author josec
 */
public class EspacioEstacionamiento {

    private Integer idEspacio;
    private String claveEspacio;
    private String tipo;
    private Integer ocupado;
    private Integer estatus;

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getOcupado() {
        return ocupado;
    }

    public void setOcupado(Integer ocupado) {
        this.ocupado = ocupado;
    }

    public Integer getEstatus() {
        return estatus;
    }

    public void setEstatus(Integer estatus) {
        this.estatus = estatus;
    }
}
