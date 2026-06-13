package uv.sicae.user.user.exception;

/*
    Excepción usada cuando un usuario no tiene permisos
    para realizar una operación protegida.
*/
public class NoAutorizadoException extends RuntimeException {
    public NoAutorizadoException(String mensaje) {
        super(mensaje);
    }
}