/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.user.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uv.sicae.user.user.dto.LoginPeticion;
import uv.sicae.user.user.dto.LoginRespuesta;
import uv.sicae.user.user.service.AutenticacionService;

/**
 *
 * @author Dylxn
 */

/*
Clase encargada de recibir las peticiones de autenticación
*/

@RestController
@RequestMapping("/api/autenticacion")
public class AutenticacionController {
    
    private final AutenticacionService autenticacionService;
    
    public AutenticacionController(AutenticacionService autenticacionService){
        this.autenticacionService = autenticacionService;
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginRespuesta> login(@RequestBody LoginPeticion peticion){
        LoginRespuesta respuesta = autenticacionService.login(peticion);
        return ResponseEntity.ok(respuesta);
    }
}
