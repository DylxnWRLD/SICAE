/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.parking.parking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Clase de configuración de seguridad del microservicio de estacionamiento.
 *
 * Define la configuración principal de Spring Security para permitir que las
 * solicitudes HTTP lleguen a los controladores del microservicio. En este caso,
 * se desactiva la protección CSRF debido a que el sistema funciona mediante una
 * API REST consumida desde herramientas como Postman.
 *
 * La validación del token JWT se realiza posteriormente dentro de los
 * controladores, por lo que esta configuración permite el acceso a las rutas
 * para que la lógica personalizada de autenticación pueda ejecutarse.
 *
 * @author josec
 */
@Configuration
public class ConfiguracionSeguridad {

    /**
     * Configura la cadena de filtros de seguridad de Spring Security.
     *
     * Desactiva CSRF y permite todas las solicitudes entrantes para evitar que
     * Spring Security bloquee las peticiones antes de que lleguen a los
     * controladores. La validación real del token se realiza dentro de la
     * lógica del microservicio.
     *
     * @param http objeto utilizado para construir la configuración de
     * seguridad.
     * @return cadena de filtros de seguridad configurada.
     * @throws Exception si ocurre un error al construir la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
                )
                .build();
    }
}
