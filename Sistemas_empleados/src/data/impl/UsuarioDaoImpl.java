package data.impl;

import database.Conexion;
import dominio.Usuarios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import data.UsuarioDao;
import java.util.List;

public class UsuarioDaoImpl implements UsuarioDao<Usuarios> {

    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;

    public UsuarioDaoImpl() {
        this.CON = Conexion.getInstancia();
    }

    public Usuarios login(String user, String pass) {
        Usuarios us = null;
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND pass = aes_encrypt(?, 'clave')";
        try {
            Connection con = CON.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, pass);
            rs = ps.executeQuery();
            if (rs.next()) {
                us = new Usuarios();
                us.setIdUser(rs.getInt("id_user"));
                //us.setIdempleado(rs.getInt("id_empleado"));
                us.setUsuario(rs.getString("usuario"));
                us.setPassword(rs.getString("pass"));
                //us.setTipo(rs.getString("tipo"));
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean insertar(Usuarios obj) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean actualizar(Usuarios obj) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean eliminar(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
