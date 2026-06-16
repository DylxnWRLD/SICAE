/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.vehicles.vehicles.dto;

/*
    DTO usado para devolver mensajes simples de respuesta
    en operaciones exitosas o con error controlado.
*/
public class MensajeRespuesta {
    private String mensaje;

    public MensajeRespuesta() {
    }

    public MensajeRespuesta(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}