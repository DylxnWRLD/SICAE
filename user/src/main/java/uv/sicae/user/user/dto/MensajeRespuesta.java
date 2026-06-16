package uv.sicae.user.user.dto;

/**
 * DTO utilizado para devolver mensajes simples de éxito o error controlado.
 * 
 * @author Alvaro
 */
public class MensajeRespuesta {
    private String mensaje;

    /**
     * Crea una instancia de MensajeRespuesta e inicializa sus dependencias o datos
     * principales.
     */
    public MensajeRespuesta() {
    }

    /**
     * Crea una instancia de MensajeRespuesta e inicializa sus dependencias o datos
     * principales.
     *
     * @param mensaje parámetro requerido para ejecutar la operación.
     */
    public MensajeRespuesta(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * Obtiene el valor de mensaje.
     *
     * @return resultado generado por la operación.
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Asigna el valor de mensaje.
     *
     * @param mensaje parámetro requerido para ejecutar la operación.
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
