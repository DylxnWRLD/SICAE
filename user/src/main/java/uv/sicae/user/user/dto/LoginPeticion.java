/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.user.user.dto;

/**
 * DTO que representa los datos que llegan desde el cliente. 
 * En este caso el usuario y su contraseña.
 * 
 * @author Dylxn
 */
public class LoginPeticion {

    private String usuario;
    private String contrasena;

    public String getUsuario() {
        return this.usuario;
    }

    public void setUsuario(String user) {
        this.usuario = user;
    }

    public String getContrasena() {
        return this.contrasena;
    }

    public void setContrasena(String password) {
        this.contrasena = password;
    }

}
