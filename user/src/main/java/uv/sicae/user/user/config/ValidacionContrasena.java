/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.user.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;



/**
Clase de configuración que permite crear un objeto de tipo
{@link PasswordEncoder} para trabajar con las contraseñas
* 
* @author Dylxn
*/
@Configuration
public class ValidacionContrasena {
    
    /**
     * Crea el códificador de contraseñas utilizando BCrypt
     * @return instancia de {@link PasswordEncoder} 
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
}
