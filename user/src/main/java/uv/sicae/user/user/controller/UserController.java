package uv.sicae.user.user.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uv.sicae.user.user.dto.CambiarEstatusUsuarioPeticion;
import uv.sicae.user.user.dto.CatalogoRespuesta;
import uv.sicae.user.user.dto.EditarUsuarioPeticion;
import uv.sicae.user.user.dto.MensajeRespuesta;
import uv.sicae.user.user.dto.RegistroUsuarioPeticion;
import uv.sicae.user.user.model.UsuarioPerfil;
import uv.sicae.user.user.service.UserService;

/**
 * Controlador REST encargado de exponer los endpoints del módulo de usuarios
 * dentro del microservicio de identidad de SICAE.
 *
 * Recibe las solicitudes HTTP relacionadas con el registro, edición, consulta
 * de perfil, cambio de estatus y consulta de catálogos necesarios para la
 * administración de usuarios. Cada operación recibe el encabezado
 * Authorization para que la capa de servicio valide el token JWT antes de
 * ejecutar la lógica correspondiente.
 *
 * Esta clase forma parte de la capa Controller dentro de la arquitectura en
 * capas del microservicio. Su responsabilidad es recibir las peticiones,
 * obtener los datos enviados por el cliente y delegar el procesamiento de
 * reglas de negocio a UserService.
 *
 * @author Alvaro
 */
@RestController
@RequestMapping("/api/usuarios")
public class UserController {

    private final UserService userService;

    /**
     * Crea el controlador de usuarios mediante inyección de dependencias.
     *
     * @param userService servicio encargado de ejecutar la lógica de negocio
     * relacionada con usuarios, validación de permisos y consulta de catálogos.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registra un nuevo usuario dentro del sistema SICAE.
     *
     * Recibe el token JWT del usuario autenticado y los datos requeridos para
     * crear una cuenta. La operación se delega a la capa de servicio, donde se
     * valida que el usuario autenticado tenga rol de administrador, que no
     * existan correo ni username duplicados, que los catálogos enviados sean
     * válidos y que la contraseña sea cifrada antes de persistirse.
     *
     * @param authorization encabezado Authorization que contiene el token JWT
     * enviado por el cliente.
     * @param peticion objeto con los datos necesarios para registrar el usuario.
     * @return respuesta HTTP con un mensaje de confirmación o error de negocio.
     */
    @PostMapping
    public ResponseEntity<MensajeRespuesta> registrarUsuario(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody RegistroUsuarioPeticion peticion) {
        return ResponseEntity.ok(userService.registrarUsuario(authorization, peticion));
    }

    /**
     * Actualiza la información editable de un usuario existente.
     *
     * Recibe el identificador del usuario a modificar y los nuevos datos
     * editables. La capa de servicio valida el token JWT, comprueba permisos,
     * verifica que el usuario exista, revisa que el correo no pertenezca a otro
     * usuario y evita modificar datos restringidos como username, contraseña y
     * clave de usuario.
     *
     * @param authorization encabezado Authorization que contiene el token JWT.
     * @param idUsuario identificador del usuario que será actualizado.
     * @param peticion objeto con los datos editables del usuario.
     * @return respuesta HTTP con el mensaje de actualización correcta.
     */
    @PutMapping("/{idUsuario}")
    public ResponseEntity<MensajeRespuesta> editarUsuario(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Integer idUsuario,
            @RequestBody EditarUsuarioPeticion peticion) {
        return ResponseEntity.ok(userService.editarUsuario(authorization, idUsuario, peticion));
    }

    /**
     * Consulta la información completa del perfil de un usuario.
     *
     * Valida el token JWT recibido y solicita a la capa de servicio la búsqueda
     * del usuario indicado. Un administrador puede consultar cualquier perfil,
     * mientras que un usuario sin privilegios solo puede consultar su propia
     * información.
     *
     * @param authorization encabezado Authorization que contiene el token JWT.
     * @param idUsuario identificador del usuario cuyo perfil será consultado.
     * @return respuesta HTTP con los datos completos del perfil del usuario.
     */
    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioPerfil> verPerfil(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Integer idUsuario) {
        return ResponseEntity.ok(userService.verPerfil(authorization, idUsuario));
    }

    /**
     * Cambia el estatus activo o inactivo de un usuario.
     *
     * Esta operación representa la baja lógica solicitada por el proyecto, ya
     * que los usuarios no se eliminan físicamente de la base de datos. La capa
     * de servicio valida que el token pertenezca a un administrador y que el
     * usuario objetivo exista antes de actualizar el campo de estatus.
     *
     * @param authorization encabezado Authorization que contiene el token JWT.
     * @param idUsuario identificador del usuario al que se le modificará el
     * estatus.
     * @param peticion objeto con el nuevo estatus del usuario.
     * @return respuesta HTTP con el mensaje de cambio de estatus correcto.
     */
    @PatchMapping("/{idUsuario}/estatus")
    public ResponseEntity<MensajeRespuesta> cambiarEstatus(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Integer idUsuario,
            @RequestBody CambiarEstatusUsuarioPeticion peticion) {
        return ResponseEntity.ok(userService.cambiarEstatus(authorization, idUsuario, peticion));
    }

    /**
     * Consulta los roles activos registrados en el catálogo del sistema.
     *
     * Valida el token JWT recibido y devuelve los roles que pueden utilizarse
     * durante el registro o edición de usuarios.
     *
     * @param authorization encabezado Authorization que contiene el token JWT.
     * @return respuesta HTTP con la lista de roles activos.
     */
    @GetMapping("/catalogos/roles")
    public ResponseEntity<List<CatalogoRespuesta>> listarRoles(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ResponseEntity.ok(userService.listarRoles(authorization));
    }

    /**
     * Consulta los tipos de usuario activos registrados en el catálogo.
     *
     * Valida el token JWT recibido y devuelve los tipos de usuario disponibles
     * para relacionar una cuenta con su clasificación dentro del sistema.
     *
     * @param authorization encabezado Authorization que contiene el token JWT.
     * @return respuesta HTTP con la lista de tipos de usuario activos.
     */
    @GetMapping("/catalogos/tipos-usuario")
    public ResponseEntity<List<CatalogoRespuesta>> listarTiposUsuario(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ResponseEntity.ok(userService.listarTiposUsuario(authorization));
    }

    /**
     * Consulta los programas educativos activos registrados en el catálogo.
     *
     * Valida el token JWT recibido y devuelve los programas educativos que
     * pueden asociarse a un usuario durante su registro o edición.
     *
     * @param authorization encabezado Authorization que contiene el token JWT.
     * @return respuesta HTTP con la lista de programas educativos activos.
     */
    @GetMapping("/catalogos/programas-educativos")
    public ResponseEntity<List<CatalogoRespuesta>> listarProgramasEducativos(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ResponseEntity.ok(userService.listarProgramasEducativos(authorization));
    }
}
