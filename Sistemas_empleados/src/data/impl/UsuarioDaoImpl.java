package data.impl;

import database.Conexion;
import dominio.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import data.UsuarioDao;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UsuarioDaoImpl implements UsuarioDao<Usuario> {

    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public UsuarioDaoImpl() {
        this.CON = Conexion.getInstancia();
    }
    
    public Usuario login(String user, String pass) {
        Usuario us = new Usuario();
        String sql = "SELECT * FROM usuarios WHERE usuario = ? ";
        try {
            Connection con = CON.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, user);
            rs = ps.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password"); // Obtener el hash almacenado
                String storedSalt = rs.getString("salt"); // Obtener el salt almacenado
                boolean passwordMatch = PasswordEncryptor.verifyPassword(pass, storedHash, storedSalt);
                
                if( passwordMatch ){
                    us.setIdUser(rs.getInt(1));
                    us.setUsuario(rs.getString(2));
                    us.setPassword(storedHash);
                    us.setTipoUsuario(rs.getString(4));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (CON != null) CON.desconectar();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        return us;
    }
/*
    public Usuario login(String user, String pass) {
        Usuario us = new Usuario();
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND password = ? ";
        try {
            Connection con = CON.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, user);
            //ps.setString(2, pass); // se 
            rs = ps.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password"); // Obtener el hash almacenado
                String storedSalt = rs.getString("salt"); // Obtener el salt almacenado
                boolean passwordMatch = PasswordEncryptor.verifyPassword(pass, storedHash, storedSalt);
                
                if( passwordMatch ){
                    us.setIdUser(rs.getInt(1));
                    us.setUsuario(rs.getString(2));
                    us.setPassword(rs.getString(3));
                    us.setTipoUsuario(rs.getString(4));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (CON != null) CON.desconectar();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        return us;
    }
  */
    /*
    public Usuario login(String user, String pass) {
        Usuario us = new Usuario();
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND password = ? ";
        try {
            Connection con = CON.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, pass);  
            rs = ps.executeQuery();
            if (rs.next()) {               
                us.setIdUser(rs.getInt(1));
                us.setUsuario(rs.getString(2));
                us.setPassword(rs.getString(3));
                us.setTipoUsuario(rs.getString(4));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (CON != null) CON.desconectar();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        return us;
    }
*/
    
    // Implement other methods from UsuarioDao if necessary

    @Override
    public List<Usuario> listar(String texto) {
        List<Usuario> registros = new ArrayList();    
        try {
            ps = CON.conectar().prepareStatement("Select * from usuarios where usuario like ?");
            ps.setString(1, "%" + texto + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Usuario(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getNString(4)));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Error al listar areas: "+ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return registros;
    }

    @Override
    public boolean insertar(Usuario obj) {
        resp = false;
        try {
            
            byte[] salt = PasswordEncryptor.generateSalt();
            String encryptedPassword = PasswordEncryptor.encryptPassword(obj.getPassword(), salt);
            String base64Salt = Base64.getEncoder().encodeToString(salt);

            ps = CON.conectar().prepareStatement("INSERT INTO usuarios (usuario,password,tipo_usuario,salt) VALUES (?,?,?,?)");
            ps.setString(1, obj.getUsuario());
            ps.setString(2, encryptedPassword);
            ps.setString(3, obj.getTipoUsuario());
            ps.setString(4, base64Salt);
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Error al insertar usuarios: "+ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }

    public boolean actualizar(Usuario obj) {
        boolean resp = false;
        try {
            // Verificar si se proporcionó una nueva contraseña
            if (obj.getPassword() != null && !obj.getPassword().isEmpty()) {
                // Generar un nuevo salt y encriptar la nueva contraseña
                byte[] salt = PasswordEncryptor.generateSalt();
                String encryptedPassword = PasswordEncryptor.encryptPassword(obj.getPassword(), salt);
                String base64Salt = Base64.getEncoder().encodeToString(salt);

                // Actualizar el registro con la nueva contraseña encriptada y el nuevo salt
                ps = CON.conectar().prepareStatement("UPDATE usuarios SET usuario=?, password=?, tipo_usuario=?, salt=? WHERE id=?");
                ps.setString(1, obj.getUsuario());
                ps.setString(2, encryptedPassword);
                ps.setString(3, obj.getTipoUsuario());
                ps.setString(4, base64Salt);
                ps.setInt(5, obj.getIdUser());
            } else {
                // No se proporcionó una nueva contraseña, mantener la contraseña existente
                ps = CON.conectar().prepareStatement("UPDATE usuarios SET usuario=?, tipo_usuario=? WHERE id=?");
                ps.setString(1, obj.getUsuario());
                ps.setString(2, obj.getTipoUsuario());
                ps.setInt(3, obj.getIdUser());
            }

            // Ejecutar la actualización en la base de datos
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }   
 
    
/*
    @Override
    public boolean actualizar(Usuario obj) {
        boolean resp = false;
        try {
            byte[] salt = PasswordEncryptor.generateSalt();
            String encryptedPassword = PasswordEncryptor.encryptPassword(obj.getPassword(), salt);
            String base64Salt = Base64.getEncoder().encodeToString(salt);
            
            ps = CON.conectar().prepareStatement("UPDATE usuarios SET nombre=?, descripcion=? WHERE id=?");
            ps.setString(1, obj.getUsuario());
            ps.setString(2, encryptedPassword);
            ps.setString(3, obj.getTipoUsuario());
            ps.setString(4, base64Salt); // Almacenar el nuevo salt
            ps.setInt(5, obj.getIdUser());
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }
*/
    @Override
    public boolean eliminar(int id) {
        boolean resp = false;
        try {
            ps = CON.conectar().prepareStatement("DELETE FROM usuarios WHERE id = ?");
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }    
    
    public static void main(String[] args) {
        UsuarioDao datos = new UsuarioDaoImpl();
        System.out.println(datos.listar("").size());
        System.out.println(datos.listar("").get(0));
    }
}
