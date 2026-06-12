/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.user.user.seguridad;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Service;
import uv.sicae.user.user.model.UsuarioAutenticacion;

/**
 *
 * @author Dylxn
 */


/*
Configuración para el token de autenticación
*/

@Service
public class ServicioJWT {
    
    private static final String clave="SICAE-jwt-inicio-sesion-generacion-2026";
    private static final long expiracion = 1000*60*60;
    
    private Key getClave(){
        return Keys.hmacShaKeyFor(clave.getBytes(StandardCharsets.UTF_8));
    }
    
    public String generarToken(UsuarioAutenticacion usuario){
        Date fechaActual = new Date();
        Date fechaExpiracion = new Date(fechaActual.getTime() + expiracion);
        
        return Jwts.builder()
                .subject(usuario.getUsuario())
                .claim("idUsuario", usuario.getIdUsuario())
                .claim("idRol", usuario.getIdRol())
                .claim("rol", usuario.getRol())
                .claim("idTipoUsuario", usuario.getIdTipoUsuario())
                .claim("tipoUsuario", usuario.getTipoUsuario())
                .issuedAt(fechaActual)
                .expiration(fechaExpiracion)
                .signWith(getClave())
                .compact();
    }
    
}
