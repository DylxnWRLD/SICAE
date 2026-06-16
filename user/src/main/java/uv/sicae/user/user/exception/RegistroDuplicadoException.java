package uv.sicae.user.user.exception;

/**
 * Excepción utilizada cuando se intenta registrar información duplicada.
 * 
 * @author Alvaro
 */
public class RegistroDuplicadoException extends RuntimeException {
    /**
     * Crea una nueva excepción RegistroDuplicadoException con el mensaje indicado.
     *
     * @param mensaje parámetro requerido para ejecutar la operación.
     */
    public RegistroDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
