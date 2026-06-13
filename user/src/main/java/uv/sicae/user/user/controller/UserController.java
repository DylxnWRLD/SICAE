package uv.sicae.user.user.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uv.sicae.user.user.dto.CambiarEstatusUsuarioPeticion;
import uv.sicae.user.user.dto.CatalogoRespuesta;
import uv.sicae.user.user.dto.EditarUsuarioPeticion;
import uv.sicae.user.user.dto.MensajeRespuesta;
import uv.sicae.user.user.dto.RegistroUsuarioPeticion;
import uv.sicae.user.user.model.UsuarioPerfil;
import uv.sicae.user.user.service.UserService;

@RestController
@RequestMapping("/api/usuarios")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<MensajeRespuesta> registrarUsuario(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody RegistroUsuarioPeticion peticion) {
        return ResponseEntity.ok(userService.registrarUsuario(authorization, peticion));
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<MensajeRespuesta> editarUsuario(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Integer idUsuario,
            @RequestBody EditarUsuarioPeticion peticion) {
        return ResponseEntity.ok(userService.editarUsuario(authorization, idUsuario, peticion));
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioPerfil> verPerfil(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Integer idUsuario) {
        return ResponseEntity.ok(userService.verPerfil(authorization, idUsuario));
    }

    @PatchMapping("/{idUsuario}/estatus")
    public ResponseEntity<MensajeRespuesta> cambiarEstatus(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Integer idUsuario,
            @RequestBody CambiarEstatusUsuarioPeticion peticion) {
        return ResponseEntity.ok(userService.cambiarEstatus(authorization, idUsuario, peticion));
    }

    @GetMapping("/catalogos/roles")
    public ResponseEntity<List<CatalogoRespuesta>> listarRoles(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ResponseEntity.ok(userService.listarRoles(authorization));
    }

    @GetMapping("/catalogos/tipos-usuario")
    public ResponseEntity<List<CatalogoRespuesta>> listarTiposUsuario(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ResponseEntity.ok(userService.listarTiposUsuario(authorization));
    }

    @GetMapping("/catalogos/programas-educativos")
    public ResponseEntity<List<CatalogoRespuesta>> listarProgramasEducativos(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ResponseEntity.ok(userService.listarProgramasEducativos(authorization));
    }
}
