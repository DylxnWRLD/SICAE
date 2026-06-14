/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package uv.sicae.user.user.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import uv.sicae.user.user.model.UsuarioAutenticacion;

/**
 * Interfaz de la capa Repository encargada de consultar la información
 * necesaria para el proceso de autenticación de usuarios.
 *
 * Utiliza MyBatis para ejecutar consultas SQL y mapear los resultados
 * al modelo {@link UsuarioAutenticacion}, el cual posteriormente es usado
 * por la capa Service para realizar las validaciones correspondientes.
 *
 * @author Dylxn
 */
@Mapper
public interface AutenticacionRepository {
    
    
    /**
     * Busca en la base de datos la información del usuario que intenta
     * iniciar sesión.
     *
     * La consulta obtiene datos como identificadores, rol, tipo de usuario,
     * nombre de usuario, contraseña almacenada, nombre completo y estado.
     * Estos datos son utilizados posteriormente por la capa Service
     * para validar la autenticación.
     *
     * @param usuario nombre de usuario ingresado en la petición de inicio de sesión.
     * @return objeto {@link UsuarioAutenticacion} con los datos del usuario encontrado.
     */
    @Select("""
            SELECT
                u."idUsuario" AS "idUsuario",
                u."idRol" AS "idRol",
                r.nombre AS "rol",
                u."idTipoUsuario" AS "idTipoUsuario",
                tu.nombre AS "tipoUsuario",
                u.username AS "usuario",
                u.password AS "contrasena",
                CONCAT(
                    u.nombre,
                    ' ',
                    u."apellidoPaterno",
                    ' ',
                    COALESCE(u."apellidoMaterno", '')
                ) AS "nombreCompleto",
                CASE
                    WHEN u.estatus = B'1' THEN true
                    ELSE false
                END AS "estado"
            
            FROM usuario u
          
            INNER JOIN "tipoUsuario" tu
                ON u."idTipoUsuario" = tu."idTipo"
            
            INNER JOIN rol r
                ON u."idRol" = r.idrol
            
            WHERE u.username = #{usuario}   
            """)
    UsuarioAutenticacion buscarUsuario(@Param("usuario") String usuario);
}
