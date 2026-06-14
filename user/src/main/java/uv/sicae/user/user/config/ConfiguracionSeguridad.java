/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.user.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Configura la seguridad del microservicio
 * El login queda público para que cualquier persona pueda acceder y
 * realizar la autenticación para obtener el token JWT.
 * 
 * @author Dylxn
 */
@Configuration
public class ConfiguracionSeguridad {
    
    /**
     *
     * Permite el acceso público al login y desactiva CSRF para facilitar las
     * pruebas de autenticación desde el cliente.
     *
     * @param http configuración de seguridad HTTP proporcionada por Spring.
     * @return cadena de filtros de seguridad configurada.
     * @throws Exception si la configuración no puede construirse correctamente.
 */
   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       return http
               .csrf(csrf -> csrf.disable())
               .authorizeHttpRequests(auth -> auth
               .requestMatchers("/api/autenticacion/login").permitAll()
               .anyRequest().permitAll()
               )
               .build();
   }
    
}
