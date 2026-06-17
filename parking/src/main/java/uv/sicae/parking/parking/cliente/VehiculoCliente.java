/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.parking.parking.cliente;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;
import uv.sicae.parking.parking.exception.ReglaNegocioException;
import uv.sicae.parking.parking.model.VehiculoExterno;

/**
 * Cliente encargado de comunicarse con el microservicio de vehículos desde
 * ParkingService.
 *
 * Realiza una petición HTTP hacia VehicleService para obtener la lista de
 * vehículos asociados al usuario autenticado. Para adaptarse al endpoint actual
 * del microservicio de vehículos, envía el idUsuario dentro del cuerpo de una
 * solicitud GET y conserva el encabezado Authorization con el token JWT.
 *
 * La información obtenida permite validar que la placa enviada pertenezca al
 * usuario autenticado y que el vehículo cumpla con las reglas necesarias para
 * entrar o salir del estacionamiento.
 *
 * Esta clase apoya la comunicación entre microservicios sin acceder
 * directamente a la base de datos de vehículos.
 *
 * @author josec
 */
@Service
public class VehiculoCliente {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${servicios.vehiculos.url}")
    private String urlVehiculos;

    /**
     * Obtiene la lista de vehículos asociados a un usuario desde el
     * microservicio de vehículos.
     *
     * Construye un cuerpo JSON con el idUsuario recibido y lo envía a
     * VehicleService mediante una solicitud GET con body. También agrega el
     * encabezado Authorization para conservar el token JWT recibido en la
     * solicitud original.
     *
     * Si VehicleService responde correctamente, convierte la respuesta JSON en
     * una lista de objetos VehiculoExterno. Si no existen vehículos, devuelve
     * una lista vacía. En caso de error, lanza una excepción de regla de
     * negocio con el detalle de la respuesta recibida.
     *
     * @param idUsuario identificador del usuario cuyos vehículos serán
     * consultados.
     * @param authorizationHeader encabezado Authorization con el token JWT
     * recibido en la solicitud original.
     * @return lista de vehículos asociados al usuario.
     * @throws ReglaNegocioException si no es posible comunicarse con
     * VehicleService o si ocurre un error durante la validación de los
     * vehículos.
     */
    public List<VehiculoExterno> obtenerVehiculosUsuario(Integer idUsuario, String authorizationHeader) {
        try {
            Map<String, Integer> cuerpo = new HashMap<>();
            cuerpo.put("idUsuario", idUsuario);

            String json = objectMapper.writeValueAsString(cuerpo);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlVehiculos + "/api/vehiculos/obtenervehiculos"))
                    .header("Authorization", authorizationHeader)
                    .header("Content-Type", "application/json")
                    .method("GET", HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> respuesta = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (respuesta.statusCode() == 204) {
                return List.of();
            }

            if (respuesta.statusCode() < 200 || respuesta.statusCode() >= 300) {
                throw new ReglaNegocioException(
                        "No fue posible validar el vehículo en VehicleService: "
                        + respuesta.statusCode() + " - " + respuesta.body()
                );
            }

            VehiculoExterno[] vehiculos = objectMapper.readValue(
                    respuesta.body(),
                    VehiculoExterno[].class
            );

            return Arrays.asList(vehiculos);

        } catch (ReglaNegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new ReglaNegocioException(
                    "No fue posible validar el vehículo en VehicleService: " + e.getMessage()
            );
        }
    }
}
