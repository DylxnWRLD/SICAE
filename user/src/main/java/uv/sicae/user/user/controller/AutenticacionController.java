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
 * Clase encargada de recibir las peticiones del microservicio de autenticación
 * 
 * @author Dylxn
 */
@RestController
@RequestMapping("/api/autenticacion")
public class AutenticacionController {

    private final AutenticacionService autenticacionService;

    /**
     * Crea el controlador de autenticación e inyecta el servicio encargado de
     * procesar el inicio de sesión.
     *
     * @param autenticacionService servicio que contiene la lógica de
     * autenticación.
     */
    public AutenticacionController(AutenticacionService autenticacionService) {
        this.autenticacionService = autenticacionService;
    }

    /**
     * Encargado de recibir una {@link LoginPeticion} que contiene el nombre de
     * usuario y la contraseña del usuario para realizar el inicio de sesión
     *
     * @param peticion que contiene nombre de usuario y contraseña
     * @return Código http {@link ResponseEntity} con {@link LoginRespuesta} como parte del cuerpo
     */
    @PostMapping("/login")
    public ResponseEntity<LoginRespuesta> login(@RequestBody LoginPeticion peticion) {
        LoginRespuesta respuesta = autenticacionService.login(peticion);
        return ResponseEntity.ok(respuesta);
    }
}
