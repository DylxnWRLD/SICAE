package uv.sicae.parking.parking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del microservicio de estacionamiento.
 *
 * Esta clase se encarga de iniciar la aplicación Spring Boot correspondiente al
 * dominio de estacionamiento dentro del sistema SICAE. A partir de esta clase
 * se cargan las configuraciones, controladores, servicios, repositorios y demás
 * componentes necesarios para ejecutar el microservicio.
 *
 * @author josec
 */
@SpringBootApplication
public class ParkingApplication {

    /**
     * Método principal de ejecución del microservicio.
     *
     * Inicia la aplicación Spring Boot y levanta el servidor embebido para que
     * el microservicio de estacionamiento pueda recibir solicitudes HTTP.
     *
     * @param args argumentos recibidos al iniciar la aplicación.
     */
    public static void main(String[] args) {
        SpringApplication.run(ParkingApplication.class, args);
    }
    
}
