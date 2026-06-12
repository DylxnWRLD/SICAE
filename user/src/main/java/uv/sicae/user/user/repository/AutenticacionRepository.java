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
 *
 * @author Dylxn
 */

/*
Interfaz para acceder a los datos para la autenticación
*/
@Mapper
public interface AutenticacionRepository {
    
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
