package uv.sicae.user.user.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import uv.sicae.user.user.dto.CatalogoRespuesta;
import uv.sicae.user.user.dto.EditarUsuarioPeticion;
import uv.sicae.user.user.dto.RegistroUsuarioPeticion;
import uv.sicae.user.user.model.UsuarioPerfil;

/**
 * Repositorio MyBatis encargado de realizar las operaciones de persistencia del
 * dominio de usuarios.
 *
 * Esta interfaz pertenece a la capa Repository y contiene las consultas SQL
 * necesarias para validar duplicados, revisar catálogos activos, registrar
 * usuarios, actualizar información editable, cambiar estatus y consultar
 * perfiles completos. La lógica de negocio no se implementa aquí; únicamente se
 * ejecutan operaciones contra la base de datos PostgreSQL del microservicio de
 * usuarios.
 *
 * @author Alvaro
 */
@Mapper
public interface UserRepository {

    /**
     * Cuenta cuántos usuarios existen con el correo indicado.
     *
     * Se utiliza durante el registro para evitar correos duplicados dentro de la
     * base de datos.
     *
     * @param correo correo electrónico que se desea validar.
     * @return cantidad de usuarios encontrados con ese correo.
     */
    @Select("""
            SELECT COUNT(*)
            FROM usuario
            WHERE LOWER(email) = LOWER(#{correo})
            """)
    int existeCorreo(@Param("correo") String correo);

    /**
     * Cuenta usuarios con el mismo correo excluyendo al usuario que se edita.
     *
     * Permite validar duplicidad de correo durante la edición sin considerar el
     * correo actual del propio usuario.
     *
     * @param correo    correo electrónico que se desea validar.
     * @param idUsuario identificador del usuario que está siendo editado.
     * @return cantidad de usuarios distintos que ya utilizan ese correo.
     */
    @Select("""
            SELECT COUNT(*)
            FROM usuario
            WHERE LOWER(email) = LOWER(#{correo})
              AND "idUsuario" <> #{idUsuario}
            """)
    int existeCorreoEnOtroUsuario(@Param("correo") String correo, @Param("idUsuario") Integer idUsuario);

    /**
     * Cuenta cuántos usuarios existen con el username indicado.
     *
     * Se utiliza durante el registro para impedir que dos cuentas compartan el
     * mismo nombre de usuario.
     *
     * @param usuario username que se desea validar.
     * @return cantidad de usuarios encontrados con ese username.
     */
    @Select("""
            SELECT COUNT(*)
            FROM usuario
            WHERE LOWER(username) = LOWER(#{usuario})
            """)
    int existeUsuario(@Param("usuario") String usuario);

    /**
     * Cuenta usuarios que ya tienen asignada la clave de usuario indicada.
     *
     * Se utiliza para garantizar que la clave generada internamente sea única.
     *
     * @param claveUsuario clave generada que será verificada.
     * @return cantidad de usuarios que poseen esa clave.
     */
    @Select("""
            SELECT COUNT(*)
            FROM usuario
            WHERE "claveUsuario" = #{claveUsuario}
            """)
    int existeClaveUsuario(@Param("claveUsuario") String claveUsuario);

    /**
     * Verifica si existe un rol activo con el identificador indicado.
     *
     * @param idRol identificador del rol consultado.
     * @return cantidad de roles activos encontrados con ese identificador.
     */
    @Select("SELECT COUNT(*) FROM rol WHERE idrol = #{idRol} AND estatus = B'1'")
    int existeRolActivo(@Param("idRol") Integer idRol);

    /**
     * Verifica si existe un tipo de usuario activo con el identificador indicado.
     *
     * @param idTipoUsuario identificador del tipo de usuario consultado.
     * @return cantidad de tipos de usuario activos encontrados.
     */
    @Select("SELECT COUNT(*) FROM \"tipoUsuario\" WHERE \"idTipo\" = #{idTipoUsuario} AND estatus = B'1'")
    int existeTipoUsuarioActivo(@Param("idTipoUsuario") Integer idTipoUsuario);

    /**
     * Verifica si existe un programa educativo activo con el identificador
     * indicado.
     *
     * @param idProgramaEducativo identificador del programa educativo consultado.
     * @return cantidad de programas educativos activos encontrados.
     */
    @Select("SELECT COUNT(*) FROM \"programaEducativo\" WHERE \"idPrograma\" = #{idProgramaEducativo} AND estatus = B'1'")
    int existeProgramaEducativoActivo(@Param("idProgramaEducativo") Integer idProgramaEducativo);

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * La contraseña recibida ya llega cifrada desde la capa de servicio y el
     * usuario se registra activo por defecto. También se almacena la clave de
     * usuario generada internamente y la fecha de creación.
     *
     * @param peticion          datos capturados para el registro del usuario.
     * @param claveUsuario      clave única generada para el usuario.
     * @param contrasenaCifrada contraseña cifrada mediante BCrypt.
     * @return número de registros insertados.
     */
    @Insert("""
            INSERT INTO usuario (
                nombre,
                "apellidoPaterno",
                "apellidoMaterno",
                "claveUsuario",
                email,
                telefono,
                username,
                password,
                estatus,
                "idRol",
                "idTipoUsuario",
                "idProgramaEducativo",
                "tiempoCreacion",
                "tempoActualizacion"
            ) VALUES (
                #{peticion.nombre},
                #{peticion.apellidoPaterno},
                #{peticion.apellidoMaterno},
                #{claveUsuario},
                #{peticion.correo},
                #{peticion.telefono},
                #{peticion.usuario},
                #{contrasenaCifrada},
                B'1',
                #{peticion.idRol},
                #{peticion.idTipoUsuario},
                #{peticion.idProgramaEducativo},
                CURRENT_TIMESTAMP,
                NULL
            )
            """)
    int registrarUsuario(@Param("peticion") RegistroUsuarioPeticion peticion,
            @Param("claveUsuario") String claveUsuario,
            @Param("contrasenaCifrada") String contrasenaCifrada);

    /**
     * Actualiza los datos editables de un usuario existente.
     *
     * @param idUsuario identificador del usuario que se desea actualizar.
     * @param peticion  datos editables del usuario.
     * @return número de registros actualizados.
     */
    @Update("""
            UPDATE usuario
            SET
                nombre = #{peticion.nombre},
                "apellidoPaterno" = #{peticion.apellidoPaterno},
                "apellidoMaterno" = #{peticion.apellidoMaterno},
                email = #{peticion.correo},
                telefono = #{peticion.telefono},
                "idRol" = #{peticion.idRol},
                "idTipoUsuario" = #{peticion.idTipoUsuario},
                "idProgramaEducativo" = #{peticion.idProgramaEducativo},
                "tempoActualizacion" = CURRENT_TIMESTAMP
            WHERE "idUsuario" = #{idUsuario}
            """)
    int editarUsuario(@Param("idUsuario") Integer idUsuario,
            @Param("peticion") EditarUsuarioPeticion peticion);

    /**
     * Actualiza el estatus activo o inactivo de un usuario.
     *
     * Esta operación realiza la baja lógica del usuario, ya que no elimina el
     * registro de la base de datos. También actualiza la fecha de modificación.
     *
     * @param idUsuario identificador del usuario al que se le cambiará el estatus.
     * @param estatus   nuevo valor de estatus; true para activo y false para
     *                  inactivo.
     * @return número de registros actualizados.
     */
    @Update("""
            UPDATE usuario
            SET estatus = CASE WHEN #{estatus} THEN B'1' ELSE B'0' END,
                "tempoActualizacion" = CURRENT_TIMESTAMP
            WHERE "idUsuario" = #{idUsuario}
            """)
    int cambiarEstatus(@Param("idUsuario") Integer idUsuario, @Param("estatus") Boolean estatus);

    /**
     * Busca la información completa del perfil de un usuario por identificador.
     *
     * La consulta une usuario, rol, tipo de usuario y programa educativo para
     * construir una respuesta completa con datos personales, datos de contacto,
     * estatus, clave de usuario y fechas de creación o actualización.
     *
     * @param idUsuario identificador del usuario consultado.
     * @return perfil completo del usuario, o null si no existe.
     */
    @Select("""
            SELECT
                u."idUsuario" AS "idUsuario",
                u.nombre AS nombre,
                u."apellidoPaterno" AS "apellidoPaterno",
                u."apellidoMaterno" AS "apellidoMaterno",
                CONCAT(u.nombre, ' ', u."apellidoPaterno", ' ', COALESCE(u."apellidoMaterno", '')) AS "nombreCompleto",
                u."claveUsuario" AS "claveUsuario",
                u.email AS email,
                u.telefono AS telefono,
                u.username AS username,
                CASE WHEN u.estatus = B'1' THEN true ELSE false END AS estatus,
                u."idRol" AS "idRol",
                r.nombre AS rol,
                u."idTipoUsuario" AS "idTipoUsuario",
                tu.nombre AS "tipoUsuario",
                u."idProgramaEducativo" AS "idProgramaEducativo",
                pe.nombre AS "programaEducativo",
                u."tiempoCreacion" AS "tiempoCreacion",
                u."tempoActualizacion" AS "tempoActualizacion"
            FROM usuario u
            INNER JOIN rol r ON r.idrol = u."idRol"
            INNER JOIN "tipoUsuario" tu ON tu."idTipo" = u."idTipoUsuario"
            INNER JOIN "programaEducativo" pe ON pe."idPrograma" = u."idProgramaEducativo"
            WHERE u."idUsuario" = #{idUsuario}
            """)
    UsuarioPerfil buscarPerfilPorId(@Param("idUsuario") Integer idUsuario);

    /**
     * Consulta los roles activos registrados en la base de datos.
     *
     * @return lista de roles activos ordenados por identificador.
     */
    @Select("""
            SELECT idrol AS id, nombre
            FROM rol
            WHERE estatus = B'1'
            ORDER BY idrol
            """)
    List<CatalogoRespuesta> listarRolesActivos();

    /**
     * Consulta los tipos de usuario activos registrados en la base de datos.
     *
     * @return lista de tipos de usuario activos ordenados por identificador.
     */
    @Select("""
            SELECT "idTipo" AS id, nombre
            FROM "tipoUsuario"
            WHERE estatus = B'1'
            ORDER BY "idTipo"
            """)
    List<CatalogoRespuesta> listarTiposUsuarioActivos();

    /**
     * Consulta los programas educativos activos registrados en la base de datos.
     *
     * @return lista de programas educativos activos ordenados por identificador.
     */
    @Select("""
            SELECT "idPrograma" AS id, nombre
            FROM "programaEducativo"
            WHERE estatus = B'1'
            ORDER BY "idPrograma"
            """)
    List<CatalogoRespuesta> listarProgramasEducativosActivos();
}
