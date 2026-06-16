/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.parking.parking.cliente;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uv.sicae.parking.parking.exception.ReglaNegocioException;
import uv.sicae.parking.parking.model.VehiculoExterno;

/**
 * Cliente encargado de comunicarse con el microservicio de vehículos desde
 * ParkingService.
 *
 * Realiza una petición HTTP hacia VehicleService para obtener la lista de
 * vehículos asociados al usuario autenticado, utilizando el token recibido en
 * la solicitud original. Esta información permite validar que la placa enviada
 * pertenezca al usuario y que el vehículo cumpla con las reglas necesarias para
 * entrar o salir del estacionamiento.
 *
 * Esta clase apoya la comunicación entre microservicios sin acceder
 * directamente a la base de datos de vehículos.
 *
 * @author josec
 */
@Service
public class VehiculoCliente {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${servicios.vehiculos.url}")
    private String urlVehiculos;

    /**
     * Obtiene la lista de vehículos asociados a un usuario desde el
     * microservicio de vehículos.
     *
     * Envía una solicitud GET a VehicleService utilizando el identificador del
     * usuario y el encabezado Authorization con el token JWT. Si la consulta es
     * exitosa, devuelve los vehículos necesarios para que ParkingService valide
     * la placa, la asociación usuario-vehículo y el estatus del vehículo.
     *
     * @param idUsuario identificador del usuario cuyos vehículos serán
     * consultados.
     * @param authorizationHeader encabezado Authorization con el token JWT
     * recibido en la solicitud original.
     * @return lista de vehículos asociados al usuario.
     * @throws ReglaNegocioException si no es posible comunicarse con
     * VehicleService o si ocurre un error durante la validación del vehículo.
     */
    public List<VehiculoExterno> obtenerVehiculosUsuario(Integer idUsuario, String authorizationHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);

        HttpEntity<Void> peticion = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<VehiculoExterno>> respuesta = restTemplate.exchange(
                    urlVehiculos + "/api/vehiculos/" + idUsuario,
                    HttpMethod.GET,
                    peticion,
                    new ParameterizedTypeReference<List<VehiculoExterno>>() {
            }
            );

            return respuesta.getBody();

        } catch (Exception e) {
            throw new ReglaNegocioException("No fue posible validar el vehículo en VehicleService");
        }
    }
}
