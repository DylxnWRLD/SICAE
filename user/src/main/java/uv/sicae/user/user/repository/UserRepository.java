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

@Mapper
public interface UserRepository {

        @Select("""
                        SELECT COUNT(*)
                        FROM usuario
                        WHERE LOWER(email) = LOWER(#{correo})
                        """)
        int existeCorreo(@Param("correo") String correo);

        @Select("""
                        SELECT COUNT(*)
                        FROM usuario
                        WHERE LOWER(email) = LOWER(#{correo})
                          AND "idUsuario" <> #{idUsuario}
                        """)
        int existeCorreoEnOtroUsuario(@Param("correo") String correo, @Param("idUsuario") Integer idUsuario);

        @Select("""
                        SELECT COUNT(*)
                        FROM usuario
                        WHERE LOWER(username) = LOWER(#{usuario})
                        """)
        int existeUsuario(@Param("usuario") String usuario);

        @Select("""
                        SELECT COUNT(*)
                        FROM usuario
                        WHERE "claveUsuario" = #{claveUsuario}
                        """)
        int existeClaveUsuario(@Param("claveUsuario") String claveUsuario);

        @Select("SELECT COUNT(*) FROM rol WHERE idrol = #{idRol} AND estatus = B'1'")
        int existeRolActivo(@Param("idRol") Integer idRol);

        @Select("SELECT COUNT(*) FROM \"tipoUsuario\" WHERE \"idTipo\" = #{idTipoUsuario} AND estatus = B'1'")
        int existeTipoUsuarioActivo(@Param("idTipoUsuario") Integer idTipoUsuario);

        @Select("SELECT COUNT(*) FROM \"programaEducativo\" WHERE \"idPrograma\" = #{idProgramaEducativo} AND estatus = B'1'")
        int existeProgramaEducativoActivo(@Param("idProgramaEducativo") Integer idProgramaEducativo);

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

        @Update("""
                        UPDATE usuario
                        SET estatus = CASE WHEN #{estatus} THEN B'1' ELSE B'0' END,
                            "tempoActualizacion" = CURRENT_TIMESTAMP
                        WHERE "idUsuario" = #{idUsuario}
                        """)
        int cambiarEstatus(@Param("idUsuario") Integer idUsuario, @Param("estatus") Boolean estatus);

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

        @Select("""
                        SELECT idrol AS id, nombre
                        FROM rol
                        WHERE estatus = B'1'
                        ORDER BY idrol
                        """)
        List<CatalogoRespuesta> listarRolesActivos();

        @Select("""
                        SELECT "idTipo" AS id, nombre
                        FROM "tipoUsuario"
                        WHERE estatus = B'1'
                        ORDER BY "idTipo"
                        """)
        List<CatalogoRespuesta> listarTiposUsuarioActivos();

        @Select("""
                        SELECT "idPrograma" AS id, nombre
                        FROM "programaEducativo"
                        WHERE estatus = B'1'
                        ORDER BY "idPrograma"
                        """)
        List<CatalogoRespuesta> listarProgramasEducativosActivos();
}
