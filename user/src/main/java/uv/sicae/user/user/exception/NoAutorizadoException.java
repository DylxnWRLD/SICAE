package uv.sicae.user.user.exception;

/**
 * Excepción utilizada cuando el usuario no cuenta con autorización para
 * realizar una operación.
 * 
 * @author Alvaro
 */
public class NoAutorizadoException extends RuntimeException {
    /**
     * Crea una nueva excepción NoAutorizadoException con el mensaje indicado.
     *
     * @param mensaje parámetro requerido para ejecutar la operación.
     */
    public NoAutorizadoException(String mensaje) {
        super(mensaje);
    }
}
