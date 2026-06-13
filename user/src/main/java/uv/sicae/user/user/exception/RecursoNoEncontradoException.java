package uv.sicae.user.user.exception;

/*
    Excepción usada cuando no se encuentra un recurso
    solicitado, como un usuario o catálogo inexistente.
*/
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}