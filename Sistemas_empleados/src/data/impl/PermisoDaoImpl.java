/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data.impl;

import data.PermisoDao;
import database.Conexion;
import dominio.Permiso;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author USUARIO
 */
public class PermisoDaoImpl implements PermisoDao<Permiso> {

    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public PermisoDaoImpl() {
        CON = Conexion.getInstancia();
    }

    @Override
    public List<Permiso> listar(String texto) {
        List<Permiso> registros = new ArrayList<>();
        try {
            String query = "SELECT p.id, p.empleado_id, e.nombre as empleadoNombre, p.fecha_inicio, p.fecha_fin, p.tipo, p.descripcion "
                    + "FROM permisos p "
                    + "INNER JOIN empleados e ON e.id = p.empleado_id "
                    + "WHERE e.nombre LIKE ? "
                    + "ORDER BY p.id ASC";
            ps = CON.conectar().prepareStatement(query);
            ps.setString(1, "%" + texto + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Permiso(
                        rs.getInt("id"), // 1
                        rs.getInt("empleado_id"), // 2
                        rs.getString("empleadoNombre"), // 3
                        rs.getDate("fecha_inicio"), // 4
                        rs.getDate("fecha_fin"), // 5
                        rs.getString("tipo"),// 6
                        rs.getString("descripcion")// 7
                ));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al listar Permisos: " + ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return registros;
    }

    @Override
    public boolean insertar(Permiso obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("INSERT INTO permisos (empleado_id,fecha_inicio,fecha_fin,tipo,descripcion) VALUES (?,?,?,?,?)");
            ps.setInt(1, obj.getEmpleadoId());
            ps.setDate(2, new java.sql.Date(obj.getFechaInicio().getTime())); // Corrección aquí
            ps.setDate(3, new java.sql.Date(obj.getFechaFin().getTime())); // Corrección aquí
            ps.setString(4, obj.getTipo());
            ps.setString(5, obj.getDescripcion());
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al insertar permiso: " + ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }

    @Override
    public boolean actualizar(Permiso obj) {
        boolean resp = false;
        try {
            ps = CON.conectar().prepareStatement("Update permisos set empleado_id=?, fecha_inicio=?,fecha_fin=?,tipo=?,descripcion=? where id=?");
            ps.setInt(1, obj.getEmpleadoId());
            ps.setDate(2, new java.sql.Date(obj.getFechaInicio().getTime())); // Corrección aquí
            ps.setDate(3, new java.sql.Date(obj.getFechaFin().getTime())); // Corrección aquí
            ps.setString(4, obj.getTipo());
            ps.setString(5, obj.getDescripcion());
            ps.setInt(6, obj.getId());
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
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
            ps = CON.conectar().prepareStatement("DELETE FROM permisos WHERE id = ?");
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar area: " + ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }

}
