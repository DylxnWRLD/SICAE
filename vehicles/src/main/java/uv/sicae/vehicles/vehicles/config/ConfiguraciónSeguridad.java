/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.vehicles.vehicles.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Clase para permitir que se acceda al microservicio
 * @author jeshu
 */

@Configuration
public class ConfiguraciónSeguridad {
    
    /**
     *
     * Permite el acceso público a /api/vehiculos y desactiva CSRF
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
               .requestMatchers("/api/vehiculos").permitAll()
               .anyRequest().permitAll()
               )
               .build();
   }
}
