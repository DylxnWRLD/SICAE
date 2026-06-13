package uv.sicae.user.user.dto;

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