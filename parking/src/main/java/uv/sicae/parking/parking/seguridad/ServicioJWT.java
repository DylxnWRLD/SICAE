/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.parking.parking.seguridad;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;
import uv.sicae.parking.parking.exception.NoAutorizadoException;
import uv.sicae.parking.parking.model.DatosToken;

/**
 * Servicio encargado de validar tokens JWT dentro del microservicio de
 * estacionamiento.
 *
 * Verifica que el encabezado Authorization exista, que utilice el formato
 * Bearer y que el token recibido sea válido. Cuando la validación es correcta,
 * extrae la información principal del usuario autenticado y la almacena en un
 * objeto DatosToken.
 *
 * La información obtenida del token se utiliza posteriormente para validar
 * reglas del estacionamiento, como el usuario autenticado, su rol, su tipo de
 * usuario y la relación con las operaciones de entrada y salida.
 *
 * @author josec
 */
@Service
public class ServicioJWT {

    private static final String llave = "SICAE-jwt-inicio-sesion-generacion-2026";

    /**
     * Genera la llave secreta utilizada para validar la firma del token JWT.
     *
     * La llave se construye a partir de una cadena definida dentro del
     * microservicio. Esta cadena debe coincidir con la utilizada por el
     * servicio de autenticación al momento de generar el token, ya que de lo
     * contrario la validación no será correcta.
     *
     * @return llave secreta utilizada para verificar la firma del token JWT.
     */
    private SecretKey getLlave() {
        return Keys.hmacShaKeyFor(llave.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Valida el token JWT recibido en el encabezado Authorization.
     *
     * Primero verifica que el encabezado exista y que tenga el formato correcto
     * con la palabra Bearer. Después obtiene el token, valida su firma y extrae
     * los datos principales del usuario autenticado, como el nombre de usuario,
     * identificador de usuario, rol, tipo de usuario y sus identificadores
     * relacionados.
     *
     * Si el token no fue enviado, tiene un formato incorrecto, es inválido o ya
     * expiró, se lanza una excepción de autorización para impedir el consumo
     * del servicio.
     *
     * @param authorizationHeader encabezado Authorization recibido en la
     * solicitud HTTP.
     * @return objeto DatosToken con la información obtenida del token validado.
     * @throws NoAutorizadoException si el token no fue enviado, tiene formato
     * incorrecto, es inválido o expiró.
     */
    public DatosToken validarToken(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new NoAutorizadoException("Debe proporcionar el token de autenticación");
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new NoAutorizadoException("El token debe enviarse con el fomrato: Bearer token");
        }

        String token = authorizationHeader.substring(7);

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getLlave())
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
        } catch (Exception e) {
            throw new NoAutorizadoException("Token inválido o expirado");
        }
    }
}
