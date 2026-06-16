/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.vehicles.vehicles.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Service;
import uv.sicae.vehicles.vehicles.exceptions.NoAutorizadoException;
import uv.sicae.vehicles.vehicles.entity.DatosToken;

/**
 * Clase encargada de generar y validar el token de autenticación.
 *
 * @author Dylxn y Alvaro
 */
@Service
public class ServicioJWT {

    private static final String llave = "SICAE-jwt-inicio-sesion-generacion-2026";
    private static final long expiracion = 1000 * 60 * 60;

    /**
     * Obtiene la llave utilizada para firmar y verificar los tokens JWT.
     *
     * @return llave generada a partir de la cadena secreta.
     */
    private Key getLlave() {
        return Keys.hmacShaKeyFor(llave.getBytes(StandardCharsets.UTF_8));
    }

    

    public DatosToken validarToken(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new NoAutorizadoException("Debe proporcionar el token de autenticación");
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new NoAutorizadoException("El token debe enviarse con el formato: Bearer token");
        }

        String token = authorizationHeader.substring(7);

        try {
            Claims claims = Jwts.parser()
                    .verifyWith((javax.crypto.SecretKey) getLlave())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            DatosToken datos = new DatosToken();
            datos.setUsuario(claims.getSubject());
            datos.setIdUsuario(claims.get("idUsuario", Integer.class));
            datos.setIdRol(claims.get("idRol", Integer.class));
            datos.setRol(claims.get("rol", String.class));
            datos.setIdTipoUsuario(claims.get("idTipoUsuario", Integer.class));
            datos.setTipoUsuario(claims.get("tipoUsuario", String.class));
            return datos;
        } catch (Exception ex) {
            throw new NoAutorizadoException("Token inválido o expirado");
        }
    }

}
