package data.impl;

import database.Conexion;
import dominio.Usuarios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import data.UsuarioDao;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDaoImpl implements UsuarioDao<Usuarios> {

    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public UsuarioDaoImpl() {
        this.CON = Conexion.getInstancia();
    }

    public Usuarios login(String user, String pass) {
        Usuarios us = new Usuarios();
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND password = ? ";
        try {
            Connection con = CON.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, pass);
            rs = ps.executeQuery();
            if (rs.next()) {
                us = new Usuarios();
                us.setIdUser(rs.getInt(1));
                us.setNombre(rs.getString(2));
                us.setUsuario(rs.getString(3));
                us.setPassword(rs.getString(4));
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

    // Implement other methods from UsuarioDao if necessary

    @Override
    public List<Usuarios> listar(String texto) {
        List<Usuarios> registros = new ArrayList();    
        try {
            ps = CON.conectar().prepareStatement("Select * from usuarios where nombre like ?");
            ps.setString(1, "%" + texto + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Usuarios(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getNString(4)));
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
    public boolean insertar(Usuarios obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("INSERT INTO usuarios (nombre,usuario,password) VALUES (?,?,?)");
            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getUsuario());
            ps.setString(3, obj.getPassword());
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

    @Override
    public boolean actualizar(Usuarios obj) {
        boolean resp = false;
        try {
            ps = CON.conectar().prepareStatement("UPDATE usuarios SET nombre=?, descripcion=? WHERE id=?");
            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getUsuario());
            ps.setString(3, obj.getPassword());
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
