/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package data.impl;

import data.AsistenciaDao;
import database.Conexion;
import dominio.Asistencia;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


public class AsistenciaDaoImpl implements AsistenciaDao<Asistencia>{
      private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public AsistenciaDaoImpl() {
        CON = Conexion.getInstancia();
    }

    @Override
    public List<Asistencia> listar(String texto) {
        List<Asistencia> registros = new ArrayList<>();
        try {
            String query = "SELECT a.id, a.empleado_id, e.nombre as empleadoNombre, a.fecha, a.hora_entrada, a.hora_salida "
                    + "FROM asistencias a "
                    + "INNER JOIN empleados e ON e.id = a.empleado_id "
                    + "WHERE e.nombre LIKE ? "
                    + "ORDER BY a.id ASC";
            ps = CON.conectar().prepareStatement(query);
            ps.setString(1, "%" + texto + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Asistencia(
                        rs.getInt("id"), // 1
                        rs.getInt("empleado_id"), // 2
                        rs.getString("empleadoNombre"), // 3
                        rs.getDate("fecha"), // 4
                        rs.getTime("hora_entrada"), // 5
                        rs.getTime("hora_salida")// 6
                )
                );
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al listar Asistencias: " + ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return registros;
        }

    @Override
    public boolean insertar(Asistencia obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("INSERT INTO asistencias (empleado_id, fecha, hora_entrada, hora_salida) VALUES (?, ?, ?, ?)");
            ps.setInt(1, obj.getEmpleadoId());
            ps.setDate(2, new java.sql.Date(obj.getFecha().getTime()));
            ps.setTime(3, obj.getHoraEntrada());
            ps.setTime(4, obj.getHoraSalida());
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al insertar asistencia: " + ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
        
    }

    @Override
public boolean actualizar(Asistencia obj) {
    boolean resp = false;
    try {
        // La consulta UPDATE correcta
        String sql = "UPDATE asistencias SET empleado_id = ?, fecha = ?, hora_entrada = ?, hora_salida = ? WHERE id = ?";
        
        // Preparar la declaración
        PreparedStatement ps = CON.conectar().prepareStatement(sql);
        
        // Establecer los valores para los parámetros
        ps.setInt(1, obj.getEmpleadoId());
        ps.setDate(2, new java.sql.Date(obj.getFecha().getTime()));
        ps.setTime(3, obj.getHoraEntrada());
        ps.setTime(4, obj.getHoraSalida());
        ps.setInt(5, obj.getId()); // Asegúrate de pasar el ID del registro que deseas actualizar
        
        // Ejecutar la actualización
        if (ps.executeUpdate() > 0) {
            resp = true;
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage());
    } finally {
        // Asegurarse de cerrar el PreparedStatement y la conexión
        try {
            if (ps != null) ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        CON.desconectar();
    }
    return resp;
}

    
    @Override
    public boolean eliminar(int id) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("DELETE FROM asistencias WHERE id = ?");
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar asistencias: " + ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }
    
}
