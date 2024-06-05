/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

/**
 *
 * @author walex
 */
public class Usuario {
    
   int idUser;
   String usuario;
   String password;
   String tipoUsuario;
   
   public Usuario(){
       
   }    

    public Usuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
   
   

    public Usuario(int idUser, String usuario, String password, String tipoUsuario) {
        this.idUser = idUser;
        this.usuario = usuario;
        this.password = password;
        this.tipoUsuario = tipoUsuario;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
   
   
   
}
