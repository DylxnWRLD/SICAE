/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.user.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uv.sicae.user.user.dto.LoginPeticion;
import uv.sicae.user.user.dto.LoginRespuesta;
import uv.sicae.user.user.excepcion.CampoObligatorioException;
import uv.sicae.user.user.excepcion.CredencialesInvalidasException;
import uv.sicae.user.user.excepcion.UsuarioInactivoException;
import uv.sicae.user.user.model.UsuarioAutenticacion;
import uv.sicae.user.user.repository.AutenticacionRepository;
import uv.sicae.user.user.seguridad.ServicioJWT;

/**
 *
 * @author Dylxn
 */

/*
Clase que contiene la lógica de negocio para validación del login
 */
@Service
public class AutenticacionService {

    private final AutenticacionRepository autenticacionRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServicioJWT servicioJWT;

    public AutenticacionService(AutenticacionRepository autenticacionRepository,
            PasswordEncoder passwordEncoder, ServicioJWT servicioJWT) {
        this.autenticacionRepository = autenticacionRepository;
        this.passwordEncoder = passwordEncoder;
        this.servicioJWT = servicioJWT;
    }

    public LoginRespuesta login(LoginPeticion peticion) {
        if (peticion == null) {
            throw new CampoObligatorioException("La petición está vacía");
        }

        if (peticion.getUsuario() == null || peticion.getUsuario().isBlank()) {
            throw new CampoObligatorioException("EL usuario es obligatorio");
        }
        if (peticion.getContrasena() == null || peticion.getContrasena().isBlank()) {
            throw new CampoObligatorioException("La contraseña es obligatoria");
        }

        UsuarioAutenticacion usuario = autenticacionRepository.buscarUsuario(peticion.getUsuario());

        if (usuario == null) {
            throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
        }

        if (!usuario.isEstado()) {
            throw new UsuarioInactivoException("El usuario se encuentra inactivo");
        }

        boolean contrasenaValida = passwordEncoder.matches(peticion.getContrasena(), usuario.getContrasena());

        if (!contrasenaValida) {
            throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
        }

        String token = servicioJWT.generarToken(usuario);

        return new LoginRespuesta(
                usuario.getIdUsuario(),
                usuario.getIdRol(),
                usuario.getRol(),
                usuario.getUsuario(),
                usuario.getNombreCompleto(),
                usuario.getIdTipoUsuario(),
                usuario.getTipoUsuario(),
                token
        );

    }

}
