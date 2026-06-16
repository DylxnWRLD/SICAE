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

/**
 * Servicio encargado de concentrar la lógica de negocio relacionada con la
 * administración de usuarios del sistema SICAE.
 *
 * En esta clase se validan las reglas solicitadas para el dominio de usuarios:
 * autenticación mediante token JWT, autorización por rol administrador,
 * validación de campos obligatorios, revisión de formatos, control de registros
 * duplicados, validación de catálogos activos, generación de clave única de
 * usuario, cifrado de contraseñas mediante BCrypt y baja lógica mediante cambio
 * de estatus.
 *
 * Esta clase forma parte de la capa Service dentro de la arquitectura en capas.
 * Recibe las solicitudes desde los controladores, aplica las reglas de negocio
 * y delega a UserRepository únicamente las operaciones de persistencia.
 *
 * @author Alvaro
 */
@Service
public class UserService {

    private static final Pattern FORMATO_CORREO = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServicioJWT servicioJWT;
    private final Random random = new Random();

    /**
     * Crea el servicio de usuarios mediante inyección de dependencias.
     *
     * @param userRepository  repositorio encargado de consultar y modificar
     *                        usuarios y catálogos en la base de datos.
     * @param passwordEncoder componente utilizado para cifrar contraseñas con
     *                        BCrypt antes de almacenarlas.
     * @param servicioJWT     servicio encargado de validar tokens JWT y obtener los
     *                        datos del usuario autenticado.
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ServicioJWT servicioJWT) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.servicioJWT = servicioJWT;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * Valida el token JWT y confirma que el solicitante tenga rol de
     * administrador. Después valida campos obligatorios, longitudes, formato de
     * correo, teléfono, duplicidad de correo, duplicidad de username y existencia
     * de los catálogos relacionados. Si todas las validaciones son correctas,
     * genera una clave única de usuario, cifra la contraseña y solicita al
     * repositorio la inserción del registro.
     *
     * @param authorizationHeader encabezado Authorization con el token JWT del
     *                            usuario autenticado.
     * @param peticion            objeto con los datos del usuario que se desea
     *                            registrar.
     * @return mensaje de confirmación con la clave generada para el usuario.
     */
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

    /**
     * Edita la información permitida de un usuario existente.
     *
     * Valida el token JWT, el identificador recibido y los datos editables. La
     * operación verifica que el usuario exista, que el solicitante sea
     * administrador o dueño del perfil y que el nuevo correo no esté registrado
     * por otro usuario. No permite modificar directamente username, contraseña
     * ni clave de usuario, ya que esos campos están restringidos por las reglas
     * del proyecto.
     *
     * @param authorizationHeader encabezado Authorization con el token JWT.
     * @param idUsuario           identificador del usuario que será actualizado.
     * @param peticion            objeto con los datos editables del usuario.
     * @return mensaje de confirmación de actualización correcta.
     */
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

    /**
     * Consulta el perfil completo de un usuario por identificador.
     *
     * Valida el token JWT y aplica control de acceso. Un administrador puede
     * consultar cualquier perfil, mientras que un usuario sin privilegios solo
     * puede consultar el suyo. Si el identificador no corresponde a un registro
     * existente, se genera una excepción de recurso no encontrado.
     *
     * @param authorizationHeader encabezado Authorization con el token JWT.
     * @param idUsuario           identificador del usuario consultado.
     * @return información completa del perfil del usuario.
     */
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

    /**
     * Cambia el estatus activo o inactivo de un usuario.
     *
     * Valida que la solicitud tenga token JWT válido y que el solicitante sea
     * administrador. Posteriormente comprueba que el usuario exista y actualiza
     * su estatus sin eliminar físicamente el registro, cumpliendo la regla de
     * baja lógica del proyecto.
     *
     * @param authorizationHeader encabezado Authorization con el token JWT.
     * @param idUsuario           identificador del usuario al que se le cambiará el
     *                            estatus.
     * @param peticion            objeto que contiene el nuevo valor de estatus.
     * @return mensaje de confirmación con el estatus aplicado.
     */
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

    /**
     * Obtiene los roles activos disponibles para registrar o editar usuarios.
     *
     * Valida el token JWT antes de consultar el catálogo para asegurar que solo
     * usuarios autenticados consuman información del sistema.
     *
     * @param authorizationHeader encabezado Authorization con el token JWT.
     * @return lista de roles activos registrados en la base de datos.
     */
    public List<CatalogoRespuesta> listarRoles(String authorizationHeader) {
        servicioJWT.validarToken(authorizationHeader);
        return userRepository.listarRolesActivos();
    }

    /**
     * Obtiene los tipos de usuario activos disponibles en el sistema.
     *
     * @param authorizationHeader encabezado Authorization con el token JWT.
     * @return lista de tipos de usuario activos.
     */
    public List<CatalogoRespuesta> listarTiposUsuario(String authorizationHeader) {
        servicioJWT.validarToken(authorizationHeader);
        return userRepository.listarTiposUsuarioActivos();
    }

    /**
     * Obtiene los programas educativos activos disponibles en el sistema.
     *
     * @param authorizationHeader encabezado Authorization con el token JWT.
     * @return lista de programas educativos activos.
     */
    public List<CatalogoRespuesta> listarProgramasEducativos(String authorizationHeader) {
        servicioJWT.validarToken(authorizationHeader);
        return userRepository.listarProgramasEducativosActivos();
    }

    /**
     * Valida los datos necesarios para registrar un usuario nuevo.
     *
     * Comprueba que la petición no sea nula, que los identificadores de
     * catálogos estén presentes, que los campos obligatorios tengan valor, que
     * las longitudes sean válidas, que el teléfono contenga diez dígitos y que
     * el correo tenga formato correcto.
     *
     * @param p petición de registro recibida desde el controlador.
     */
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

    /**
     * Valida los datos permitidos para editar un usuario existente.
     *
     * La edición excluye username, contraseña y clave de usuario. Por ello, este
     * método valida únicamente los datos administrativos, personales y de
     * contacto que sí pueden modificarse.
     *
     * @param p petición de edición recibida desde el controlador.
     */
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

    /**
     * Valida longitudes y formato de los campos personales comunes.
     *
     * @param nombre          nombre del usuario.
     * @param apellidoPaterno apellido paterno del usuario.
     * @param apellidoMaterno apellido materno del usuario, opcional.
     * @param correo          correo electrónico del usuario.
     * @param telefono        teléfono del usuario; debe contener exactamente diez
     *                        dígitos.
     */
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

    /**
     * Valida que los catálogos relacionados existan y se encuentren activos.
     *
     * @param idRol               identificador del rol asignado al usuario.
     * @param idTipoUsuario       identificador del tipo de usuario asignado.
     * @param idProgramaEducativo identificador del programa educativo asociado.
     */
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

    /**
     * Valida que el usuario autenticado tenga rol de administrador.
     *
     * @param token datos extraídos del JWT del usuario autenticado.
     */
    private void validarAdministrador(DatosToken token) {
        if (!token.esAdministrador()) {
            throw new NoAutorizadoException("Solo un usuario administrador puede realizar esta operación");
        }
    }

    /**
     * Valida que un identificador obligatorio exista y sea mayor que cero.
     *
     * @param id      identificador recibido en la petición.
     * @param mensaje mensaje de error que se mostrará si el identificador es
     *                inválido.
     */
    private void validarId(Integer id, String mensaje) {
        if (id == null || id <= 0) {
            throw new CampoObligatorioException(mensaje);
        }
    }

    /**
     * Valida que un texto obligatorio no sea nulo ni vacío.
     *
     * @param valor   texto recibido en la petición.
     * @param mensaje mensaje de error que se mostrará si el texto está vacío.
     */
    private void validarTextoObligatorio(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new CampoObligatorioException(mensaje);
        }
    }

    /**
     * Valida que un texto no exceda la longitud máxima permitida.
     *
     * @param valor   texto que será evaluado.
     * @param max     longitud máxima aceptada para el texto.
     * @param mensaje mensaje de error que se mostrará si se excede la longitud.
     */
    private void validarLongitud(String valor, int max, String mensaje) {
        if (valor != null && valor.length() > max) {
            throw new LargoCampoException(mensaje);
        }
    }

    /**
     * Valida que el correo electrónico tenga un formato aceptado por el sistema.
     *
     * @param correo correo electrónico recibido en la petición.
     */
    private void validarCorreo(String correo) {
        if (!FORMATO_CORREO.matcher(correo).matches()) {
            throw new CampoObligatorioException("El formato del correo no es válido");
        }
    }

    /**
     * Genera una clave única de usuario a partir de iniciales y número aleatorio.
     *
     * La clave se compone de las iniciales del nombre y apellidos, seguidas de
     * un número aleatorio de tres dígitos. Antes de devolverla, se verifica en
     * la base de datos que no exista una clave igual.
     *
     * @param p petición de registro con los datos personales del usuario.
     * @return clave única generada para el usuario.
     */
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

    /**
     * Obtiene las iniciales utilizadas para formar la clave de usuario.
     *
     * @param nombre          nombre del usuario.
     * @param apellidoPaterno apellido paterno del usuario.
     * @param apellidoMaterno apellido materno del usuario.
     * @return cadena con las iniciales en mayúsculas.
     */
    private String obtenerIniciales(String nombre, String apellidoPaterno, String apellidoMaterno) {
        StringBuilder iniciales = new StringBuilder();
        iniciales.append(obtenerInicial(nombre));
        iniciales.append(obtenerInicial(apellidoPaterno));
        iniciales.append(obtenerInicial(apellidoMaterno));
        return iniciales.toString().toUpperCase();
    }

    /**
     * Obtiene la primera letra de un texto para formar iniciales.
     *
     * Si el texto es nulo o vacío, devuelve la letra X para evitar errores al
     * construir la clave de usuario.
     *
     * @param texto texto del que se obtendrá la inicial.
     * @return primera letra del texto o X cuando el texto no tenga valor.
     */
    private String obtenerInicial(String texto) {
        if (texto == null || texto.isBlank()) {
            return "X";
        }
        return texto.trim().substring(0, 1);
    }
}
