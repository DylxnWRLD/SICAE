/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.user.user.dto;

/**
 *
 * @author Dylxn
 */

/*
Clase que representa los datos que llegan desde el cliente. En este
caso el usuario y su contraseña.
*/
public class LoginPeticion {
    
    private String usuario;
    private String contrasena;
    
    public String getUser(){
        return this.usuario;
    }
    
    public void setUser(String user){
        this.usuario = user;
    }
    
    public String getPassword(){
        return this.contrasena;
    }
    
    public void setPassword (String password){
        this.contrasena = password;
    }
    
}
