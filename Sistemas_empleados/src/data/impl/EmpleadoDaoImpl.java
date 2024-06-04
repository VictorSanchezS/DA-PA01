/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data.impl;

import data.EmpleadoDao;
import database.Conexion;
import dominio.Empleado;
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
public class EmpleadoDaoImpl implements EmpleadoDao<Empleado> {

    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public EmpleadoDaoImpl() {
        CON = Conexion.getInstancia();
    }

    @Override
    public List<Empleado> listar(String texto) {
        List<Empleado> registros = new ArrayList<>();
        try {
            String query = "SELECT e.id, e.nombre, e.apellido, e.tipoDoc, e.documento, e.area_id, a.nombre as areaNombre, e.cargo_id, c.nombre as cargoNombre, e.telefono, e.correo "
                    + "FROM empleados e "
                    + "INNER JOIN areas a ON e.area_id = a.id "
                    + "INNER JOIN cargos c ON e.cargo_id = c.id "
                    + "WHERE e.nombre LIKE ? "
                    + "ORDER BY e.id ASC";
            ps = CON.conectar().prepareStatement(query);
            ps.setString(1, "%" + texto + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Empleado(
                        rs.getInt("id"), // 1
                        rs.getString("nombre"), // 2
                        rs.getString("apellido"), // 3
                        rs.getString("tipoDoc"), // 4
                        rs.getString("documento"), // 5
                        rs.getInt("area_id"), // 6
                        rs.getString("areaNombre"),// 7
                        rs.getInt("cargo_id"), // 8
                        rs.getString("cargoNombre"),// 9
                        rs.getString("telefono"), // 10
                        rs.getString("correo") // 11
                ));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al listar empleados: " + ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return registros;
    }

    @Override
    public boolean insertar(Empleado obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("INSERT INTO empleados (nombre,apellido,tipoDoc,documento,area_id,cargo_id,telefono,correo) VALUES (?,?,?,?,?,?,?,?)");
            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getApellido());
            ps.setString(3, obj.getTipoDoc());
            ps.setString(4, obj.getDocumento());
            ps.setInt(5, obj.getAreaId());
            ps.setInt(6, obj.getCargoId());
            ps.setString(7, obj.getTelefono());
            ps.setString(8, obj.getCorreo());
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al insertar empleado: " + ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }

    @Override
    public boolean actualizar(Empleado obj) {
        boolean resp = false;
        try {
            ps = CON.conectar().prepareStatement("Update empleados set nombre=?, apellido=?,tipoDoc=?,documento=?,area_id=?,cargo_id=?,telefono=?,correo=? where id=?");
            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getApellido());
            ps.setString(3, obj.getTipoDoc());
            ps.setString(4, obj.getDocumento());
            ps.setInt(5, obj.getAreaId());
            ps.setInt(6, obj.getCargoId());
            ps.setString(7, obj.getTelefono());
            ps.setString(8, obj.getCorreo());
            ps.setInt(9, obj.getId());
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
            ps = CON.conectar().prepareStatement("DELETE FROM empleados WHERE id = ?");
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

    public List<Empleado> seleccionar() {
        List<Empleado> registros = new ArrayList();
        try {
            ps = CON.conectar().prepareStatement("SELECT id, nombre FROM empleados ORDER BY nombre ASC");
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Empleado(rs.getInt(1), rs.getString(2)));
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
}
