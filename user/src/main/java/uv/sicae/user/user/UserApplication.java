package uv.sicae.user.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del microservicio de usuarios de SICAE. Inicia la aplicación Spring Boot.
 * 
 * @author Alvaro
 */
@SpringBootApplication
public class UserApplication {

	/**
	 * Punto de entrada para ejecutar la clase.
	 *
	 * @param args parámetro requerido para ejecutar la operación.
	 */
	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

}
