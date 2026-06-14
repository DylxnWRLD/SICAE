package uv.sicae.user.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uv.sicae.user.user.dto.LoginPeticion;
import uv.sicae.user.user.dto.LoginRespuesta;
import uv.sicae.user.user.exception.CampoObligatorioException;
import uv.sicae.user.user.exception.CredencialesInvalidasException;
import uv.sicae.user.user.exception.LargoCampoException;
import uv.sicae.user.user.exception.UsuarioInactivoException;
import uv.sicae.user.user.model.UsuarioAutenticacion;
import uv.sicae.user.user.repository.AutenticacionRepository;
import uv.sicae.user.user.seguridad.ServicioJWT;

/**
 * Servicio encargado de procesar la lógica de autenticación de usuarios.
 *
 * Valida los datos recibidos en la petición de inicio de sesión,
 * consulta la información del usuario, verifica el estado,
 * compara la contraseña ingresada con la almacenada y genera un token
 * JWT cuando la autenticación es exitosa.
 *
 * @author Dylxn
 */
@Service
public class AutenticacionService {

    private final AutenticacionRepository autenticacionRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServicioJWT servicioJWT;

    /**
     * Crea el servicio de autenticación mediante inyección de dependencias.
     *
     * @param autenticacionRepository repositorio utilizado para consultar los datos
     *        del usuario en la base de datos.
     * @param passwordEncoder codificador utilizado para comparar la contraseña
     *        ingresada con el hash almacenado.
     * @param servicioJWT servicio utilizado para generar el token JWT después de
     *        una autenticación exitosa.
     */
    public AutenticacionService(AutenticacionRepository autenticacionRepository,
            PasswordEncoder passwordEncoder, ServicioJWT servicioJWT) {
        this.autenticacionRepository = autenticacionRepository;
        this.passwordEncoder = passwordEncoder;
        this.servicioJWT = servicioJWT;
    }

    /**
     * Procesa el inicio de sesión de un usuario.
     *
     * Valida que la petición, el nombre de usuario y la contraseña no estén
     * vacíos, verifica que no excedan la longitud permitida, consulta al usuario
     * en la base de datos, comprueba que la cuenta esté activa y valida la
     * contraseña ingresada contra el hash almacenado. Si todas las validaciones
     * son correctas, genera un token JWT y construye la respuesta.
     *
     * @param peticion datos de inicio de sesión enviados por el cliente.
     * @return objeto {@link LoginRespuesta} con los datos del usuario autenticado
     *         y el token generado.
     * @throws CampoObligatorioException si la petición, el usuario o la contraseña
     *         no fueron proporcionados.
     * @throws LargoCampoException si el usuario o la contraseña exceden la longitud
     *         permitida.
     * @throws CredencialesInvalidasException si el usuario no existe o la contraseña
     *         no coincide.
     * @throws UsuarioInactivoException si la cuenta del usuario se encuentra inactiva.
     */
    public LoginRespuesta login(LoginPeticion peticion) {
        if (peticion == null) {
            throw new CampoObligatorioException("La petición está vacía");
        }

        if (peticion.getUsuario() == null || peticion.getUsuario().isBlank()) {
            throw new CampoObligatorioException("El usuario es obligatorio");
        }

        if (peticion.getContrasena() == null || peticion.getContrasena().isBlank()) {
            throw new CampoObligatorioException("La contraseña es obligatoria");
        }

        if (peticion.getUsuario().length() > 30) {
            throw new LargoCampoException("Se excedió el límite de tamaño del nombre de usuario");
        }

        if (peticion.getContrasena().length() > 50) {
            throw new LargoCampoException("La contraseña supera el tamaño permitido");
        }

        UsuarioAutenticacion usuario = autenticacionRepository.buscarUsuario(peticion.getUsuario());

        if (usuario == null) {
            throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
        }

        if (!usuario.isEstado()) {
            throw new UsuarioInactivoException("El usuario se encuentra inactivo");
        }

        boolean contrasenaValida = passwordEncoder.matches(
                peticion.getContrasena(),
                usuario.getContrasena()
        );

        if (!contrasenaValida) {
            throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
        }

        String token = servicioJWT.generarToken(usuario);

        return new LoginRespuesta(
                usuario.getIdUsuario(),
                usuario.getIdRol(),
                usuario.getRol(),
                usuario.getUsuario(),
                usuario.getNombreCompleto(),
                usuario.getIdTipoUsuario(),
                usuario.getTipoUsuario(),
                token
        );
    }
}