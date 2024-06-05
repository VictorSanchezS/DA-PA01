
package data.impl;

import database.Conexion;
import dominio.Area;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import data.AreaDao;
import javax.swing.JOptionPane;

/**
 *
 * @author USUARIO
 */
public class AreaDaoImpl implements AreaDao<Area>{
    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;
    
    public AreaDaoImpl() {
        CON = Conexion.getInstancia();
    }

    @Override
    public List<Area> listar(String texto) {
        List<Area> registros = new ArrayList();    
        try {
            ps = CON.conectar().prepareStatement("Select * from areas where nombre like ?");
            ps.setString(1, "%" + texto + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Area(rs.getInt(1), rs.getString(2)));
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
    public boolean insertar(Area obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("INSERT INTO areas (nombre) VALUES (?)");
            ps.setString(1, obj.getNombre());
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Error al insertar areas: "+ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }

    @Override
    public boolean actualizar(Area obj) {
        boolean resp = false;
        try {
            ps = CON.conectar().prepareStatement("UPDATE areas SET nombre=? WHERE id=?");
            ps.setString(1, obj.getNombre());
            ps.setInt(2, obj.getId());
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage());
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
            ps = CON.conectar().prepareStatement("DELETE FROM areas WHERE id = ?");
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Error al eliminar area: "+ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }
    
    public List<Area> seleccionar() {
        List<Area> registros = new ArrayList();
        try {
            ps = CON.conectar().prepareStatement("SELECT id, nombre FROM areas ORDER BY nombre ASC");
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Area(rs.getInt(1), rs.getString(2)));
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
