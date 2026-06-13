package uv.sicae.user.user.exception;

/*
    Excepción usada cuando se intenta registrar o actualizar
    información que ya existe, como usuario o correo duplicado.
*/
public class RegistroDuplicadoException extends RuntimeException {
    public RegistroDuplicadoException(String mensaje) {
        super(mensaje);
    }
}