package uv.sicae.user.user.service;

import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uv.sicae.user.user.dto.CambiarEstatusUsuarioPeticion;
import uv.sicae.user.user.dto.CatalogoRespuesta;
import uv.sicae.user.user.dto.EditarUsuarioPeticion;
import uv.sicae.user.user.dto.MensajeRespuesta;
import uv.sicae.user.user.dto.RegistroUsuarioPeticion;
import uv.sicae.user.user.exception.CampoObligatorioException;
import uv.sicae.user.user.exception.LargoCampoException;
import uv.sicae.user.user.exception.NoAutorizadoException;
import uv.sicae.user.user.exception.RecursoNoEncontradoException;
import uv.sicae.user.user.exception.RegistroDuplicadoException;
import uv.sicae.user.user.model.DatosToken;
import uv.sicae.user.user.model.UsuarioPerfil;
import uv.sicae.user.user.repository.UserRepository;
import uv.sicae.user.user.seguridad.ServicioJWT;

@Service
public class UserService {

    private static final Pattern FORMATO_CORREO = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServicioJWT servicioJWT;
    private final Random random = new Random();

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ServicioJWT servicioJWT) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.servicioJWT = servicioJWT;
    }

    public MensajeRespuesta registrarUsuario(String authorizationHeader, RegistroUsuarioPeticion peticion) {
        DatosToken token = servicioJWT.validarToken(authorizationHeader);
        validarAdministrador(token);
        validarRegistroUsuario(peticion);

        if (userRepository.existeCorreo(peticion.getCorreo()) > 0) {
            throw new RegistroDuplicadoException("Ya existe un usuario registrado con ese correo");
        }

        if (userRepository.existeUsuario(peticion.getUsuario()) > 0) {
            throw new RegistroDuplicadoException("Ya existe un usuario registrado con ese username");
        }

        validarCatalogos(peticion.getIdRol(), peticion.getIdTipoUsuario(), peticion.getIdProgramaEducativo());

        String claveUsuario = generarClaveUsuario(peticion);
        String contrasenaCifrada = passwordEncoder.encode(peticion.getContrasena());

        userRepository.registrarUsuario(peticion, claveUsuario, contrasenaCifrada);
        return new MensajeRespuesta("Usuario registrado correctamente. Clave generada: " + claveUsuario);
    }

    public MensajeRespuesta editarUsuario(String authorizationHeader, Integer idUsuario,
            EditarUsuarioPeticion peticion) {
        DatosToken token = servicioJWT.validarToken(authorizationHeader);
        validarId(idUsuario, "El identificador del usuario es obligatorio");
        validarEdicionUsuario(peticion);

        UsuarioPerfil existente = userRepository.buscarPerfilPorId(idUsuario);
        if (existente == null) {
            throw new RecursoNoEncontradoException("No se encontró el usuario indicado");
        }

        if (!token.esAdministrador() && !token.getIdUsuario().equals(idUsuario)) {
            throw new NoAutorizadoException("Solo puede editar su propio usuario");
        }

        if (userRepository.existeCorreoEnOtroUsuario(peticion.getCorreo(), idUsuario) > 0) {
            throw new RegistroDuplicadoException("Ya existe otro usuario registrado con ese correo");
        }

        validarCatalogos(peticion.getIdRol(), peticion.getIdTipoUsuario(), peticion.getIdProgramaEducativo());

        userRepository.editarUsuario(idUsuario, peticion);
        return new MensajeRespuesta("Usuario actualizado correctamente");
    }

    public UsuarioPerfil verPerfil(String authorizationHeader, Integer idUsuario) {
        DatosToken token = servicioJWT.validarToken(authorizationHeader);
        validarId(idUsuario, "El identificador del usuario es obligatorio");

        if (!token.esAdministrador() && !token.getIdUsuario().equals(idUsuario)) {
            throw new NoAutorizadoException("Solo puede consultar su propio perfil");
        }

        UsuarioPerfil perfil = userRepository.buscarPerfilPorId(idUsuario);
        if (perfil == null) {
            throw new RecursoNoEncontradoException("No se encontró el usuario indicado");
        }

        return perfil;
    }

    public MensajeRespuesta cambiarEstatus(String authorizationHeader, Integer idUsuario,
            CambiarEstatusUsuarioPeticion peticion) {
        DatosToken token = servicioJWT.validarToken(authorizationHeader);
        validarAdministrador(token);
        validarId(idUsuario, "El identificador del usuario es obligatorio");

        if (peticion == null || peticion.getEstatus() == null) {
            throw new CampoObligatorioException("El estatus es obligatorio");
        }

        UsuarioPerfil existente = userRepository.buscarPerfilPorId(idUsuario);
        if (existente == null) {
            throw new RecursoNoEncontradoException("No se encontró el usuario indicado");
        }

        userRepository.cambiarEstatus(idUsuario, peticion.getEstatus());
        String estatusTexto = peticion.getEstatus() ? "activo" : "inactivo";
        return new MensajeRespuesta("Estatus del usuario actualizado correctamente a " + estatusTexto);
    }

    public List<CatalogoRespuesta> listarRoles(String authorizationHeader) {
        servicioJWT.validarToken(authorizationHeader);
        return userRepository.listarRolesActivos();
    }

    public List<CatalogoRespuesta> listarTiposUsuario(String authorizationHeader) {
        servicioJWT.validarToken(authorizationHeader);
        return userRepository.listarTiposUsuarioActivos();
    }

    public List<CatalogoRespuesta> listarProgramasEducativos(String authorizationHeader) {
        servicioJWT.validarToken(authorizationHeader);
        return userRepository.listarProgramasEducativosActivos();
    }

    private void validarRegistroUsuario(RegistroUsuarioPeticion p) {
        if (p == null) {
            throw new CampoObligatorioException("La petición está vacía");
        }
        validarId(p.getIdRol(), "El rol es obligatorio");
        validarId(p.getIdTipoUsuario(), "El tipo de usuario es obligatorio");
        validarId(p.getIdProgramaEducativo(), "El programa educativo es obligatorio");
        validarTextoObligatorio(p.getNombre(), "El nombre es obligatorio");
        validarTextoObligatorio(p.getApellidoPaterno(), "El apellido paterno es obligatorio");
        validarTextoObligatorio(p.getUsuario(), "El username es obligatorio");
        validarTextoObligatorio(p.getContrasena(), "La contraseña es obligatoria");
        validarTextoObligatorio(p.getCorreo(), "El correo es obligatorio");
        validarTextoObligatorio(p.getTelefono(), "El teléfono es obligatorio");

        validarCamposComunes(p.getNombre(), p.getApellidoPaterno(), p.getApellidoMaterno(), p.getCorreo(),
                p.getTelefono());
        validarLongitud(p.getUsuario(), 30, "El username no debe exceder 30 caracteres");
        validarLongitud(p.getContrasena(), 50, "La contraseña no debe exceder 50 caracteres");
        validarCorreo(p.getCorreo());
    }

    private void validarEdicionUsuario(EditarUsuarioPeticion p) {
        if (p == null) {
            throw new CampoObligatorioException("La petición está vacía");
        }
        validarId(p.getIdRol(), "El rol es obligatorio");
        validarId(p.getIdTipoUsuario(), "El tipo de usuario es obligatorio");
        validarId(p.getIdProgramaEducativo(), "El programa educativo es obligatorio");
        validarTextoObligatorio(p.getNombre(), "El nombre es obligatorio");
        validarTextoObligatorio(p.getApellidoPaterno(), "El apellido paterno es obligatorio");
        validarTextoObligatorio(p.getCorreo(), "El correo es obligatorio");
        validarTextoObligatorio(p.getTelefono(), "El teléfono es obligatorio");

        validarCamposComunes(p.getNombre(), p.getApellidoPaterno(), p.getApellidoMaterno(), p.getCorreo(),
                p.getTelefono());
        validarCorreo(p.getCorreo());
    }

    private void validarCamposComunes(String nombre, String apellidoPaterno, String apellidoMaterno, String correo,
            String telefono) {
        validarLongitud(nombre, 50, "El nombre no debe exceder 50 caracteres");
        validarLongitud(apellidoPaterno, 50, "El apellido paterno no debe exceder 50 caracteres");
        validarLongitud(apellidoMaterno, 50, "El apellido materno no debe exceder 50 caracteres");
        validarLongitud(correo, 255, "El correo no debe exceder 255 caracteres");
        validarLongitud(telefono, 10, "El teléfono no debe exceder 10 caracteres");
        if (telefono != null && !telefono.matches("\\d{10}")) {
            throw new CampoObligatorioException("El teléfono debe contener exactamente 10 dígitos");
        }
    }

    private void validarCatalogos(Integer idRol, Integer idTipoUsuario, Integer idProgramaEducativo) {
        if (userRepository.existeRolActivo(idRol) == 0) {
            throw new RecursoNoEncontradoException("El rol indicado no existe o está inactivo");
        }
        if (userRepository.existeTipoUsuarioActivo(idTipoUsuario) == 0) {
            throw new RecursoNoEncontradoException("El tipo de usuario indicado no existe o está inactivo");
        }
        if (userRepository.existeProgramaEducativoActivo(idProgramaEducativo) == 0) {
            throw new RecursoNoEncontradoException("El programa educativo indicado no existe o está inactivo");
        }
    }

    private void validarAdministrador(DatosToken token) {
        if (!token.esAdministrador()) {
            throw new NoAutorizadoException("Solo un usuario administrador puede realizar esta operación");
        }
    }

    private void validarId(Integer id, String mensaje) {
        if (id == null || id <= 0) {
            throw new CampoObligatorioException(mensaje);
        }
    }

    private void validarTextoObligatorio(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new CampoObligatorioException(mensaje);
        }
    }

    private void validarLongitud(String valor, int max, String mensaje) {
        if (valor != null && valor.length() > max) {
            throw new LargoCampoException(mensaje);
        }
    }

    private void validarCorreo(String correo) {
        if (!FORMATO_CORREO.matcher(correo).matches()) {
            throw new CampoObligatorioException("El formato del correo no es válido");
        }
    }

    private String generarClaveUsuario(RegistroUsuarioPeticion p) {
        String iniciales = obtenerIniciales(p.getNombre(), p.getApellidoPaterno(), p.getApellidoMaterno());
        String clave;
        int intentos = 0;
        do {
            int numero = 100 + random.nextInt(900);
            clave = iniciales + "-" + numero;
            intentos++;
        } while (userRepository.existeClaveUsuario(clave) > 0 && intentos < 20);

        if (userRepository.existeClaveUsuario(clave) > 0) {
            throw new RegistroDuplicadoException("No se pudo generar una clave de usuario única, intente nuevamente");
        }

        return clave;
    }

    private String obtenerIniciales(String nombre, String apellidoPaterno, String apellidoMaterno) {
        StringBuilder iniciales = new StringBuilder();
        iniciales.append(obtenerInicial(nombre));
        iniciales.append(obtenerInicial(apellidoPaterno));
        iniciales.append(obtenerInicial(apellidoMaterno));
        return iniciales.toString().toUpperCase();
    }

    private String obtenerInicial(String texto) {
        if (texto == null || texto.isBlank()) {
            return "X";
        }
        return texto.trim().substring(0, 1);
    }
}
