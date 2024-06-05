package data.impl;

import database.Conexion;
import dominio.Cargo;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import data.CargoDao;

public class CargoDaoImpl implements CargoDao<Cargo> {

    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public CargoDaoImpl() {
        CON = Conexion.getInstancia();
    }
    
    @Override
    public List<Cargo> listar(String texto) {
        List<Cargo> registros = new ArrayList();
        try {
            ps = CON.conectar().prepareStatement("Select * from cargos where nombre like ?");
            ps.setString(1, "%" + texto + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Cargo(rs.getInt(1), rs.getString(2),rs.getString(3)));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return registros;
    }

    @Override
    public boolean insertar(Cargo obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("INSERT INTO cargos (nombre,descripcion) VALUES (?,?)");
            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getDescripcion());
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
    public boolean actualizar(Cargo obj) {
        boolean resp = false;
        try {
            ps = CON.conectar().prepareStatement("UPDATE cargos SET nombre=?, descripcion=? WHERE id=?");
            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getDescripcion());
            ps.setInt(3, obj.getId());
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
            ps = CON.conectar().prepareStatement("DELETE FROM cargos WHERE id = ?");
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
        CargoDao datos = new CargoDaoImpl();
        System.out.println(datos.listar("").size());
        System.out.println(datos.listar("").get(0));
    }
    
    public List<Cargo> seleccionar() {
        List<Cargo> registros = new ArrayList();
        try {
            ps = CON.conectar().prepareStatement("Select id,nombre from cargos ORDER BY nombre ASC");
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Cargo(rs.getInt(1), rs.getString(2)));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return registros;
    }

}
