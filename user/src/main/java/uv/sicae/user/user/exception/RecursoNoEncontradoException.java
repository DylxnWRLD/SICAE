package uv.sicae.user.user.exception;

/**
 * Excepción utilizada cuando no se encuentra un recurso solicitado.
 * 
 * @author Alvaro
 */
public class RecursoNoEncontradoException extends RuntimeException {
    /**
     * Crea una nueva excepción RecursoNoEncontradoException con el mensaje
     * indicado.
     *
     * @param mensaje parámetro requerido para ejecutar la operación.
     */
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
