/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.parking.parking.cliente;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uv.sicae.parking.parking.exception.ReglaNegocioException;
import uv.sicae.parking.parking.model.UsuarioExterno;

/**
 * Cliente encargado de comunicarse con el microservicio de usuarios desde
 * ParkingService.
 *
 * Realiza una petición HTTP hacia UserService para obtener la información
 * básica del usuario autenticado, utilizando el token recibido en la solicitud
 * original. Esta información permite validar que el usuario exista, esté activo
 * y que la claveUsuario enviada corresponda al usuario que realiza la
 * operación.
 *
 * Esta clase apoya la comunicación entre microservicios sin acceder
 * directamente a la base de datos de usuarios.
 *
 * @author josec
 */
@Service
public class UsuarioCliente {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${servicios.usuario.url}")
    private String urlUsuario;

    /**
     * Obtiene la información básica de un usuario desde el microservicio de
     * usuarios.
     *
     * Envía una solicitud GET a UserService utilizando el identificador del
     * usuario y el encabezado Authorization con el token JWT. Si la consulta es
     * exitosa, devuelve los datos necesarios para que ParkingService valide las
     * reglas de negocio relacionadas con el usuario.
     *
     * @param idUsuario identificador del usuario que se desea consultar.
     * @param authorizationHeader encabezado Authorization con el token JWT
     * recibido en la solicitud original.
     * @return objeto UsuarioExterno con la información básica del usuario.
     * @throws ReglaNegocioException si no es posible comunicarse con
     * UserService o si ocurre un error durante la validación del usuario.
     */
    public UsuarioExterno obtenerUsuario(Integer idUsuario, String authorizationHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);

        HttpEntity<Void> peticion = new HttpEntity<>(headers);

        try {
            ResponseEntity<UsuarioExterno> respuesta = restTemplate.exchange(
                    urlUsuario + "/api/usuarios/" + idUsuario,
                    HttpMethod.GET,
                    peticion,
                    UsuarioExterno.class
            );

            return respuesta.getBody();

        } catch (Exception e) {
            throw new ReglaNegocioException("No fue posible validar el usuario en UserService");
        }
    }
}
