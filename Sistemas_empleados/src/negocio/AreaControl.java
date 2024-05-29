/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import data.AreaDao;
import data.impl.AreaDaoImpl;
import dominio.Area;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USUARIO
 */
public class AreaControl {
    private final AreaDao DATOS;
    private Area obj;
    
    public AreaControl() {
        this.DATOS = new AreaDaoImpl();
        this.obj = new Area();
    }
    
    private DefaultTableModel modeloTabla;
    
    public DefaultTableModel listar(String texto) {
        List<Area> lista = new ArrayList();
        lista.addAll(DATOS.listar(texto));
        //Establecemos la columna del tableModel
        String[] titulos = {"ID", "NOMBRE"};
        //Declaramos un vector que será el que agreguemos como registro al DefaultTableModel
        String[] registro = new String[2];
        //agrego los títulos al DefaultTableModel
        this.modeloTabla = new DefaultTableModel(null, titulos);

        //Recorrer toda mi lista y la pasare al DefaultTableModel
        for (Area item : lista) {
            registro[0] = Integer.toString(item.getId());
            registro[1] = item.getNombre();
            this.modeloTabla.addRow(registro);
        }
        return this.modeloTabla;
    }
    
    public String insertar(Area obj) {
        if (DATOS.insertar(obj)) {
            return "OK";
        } else {
            return "Error en el ingreso de datos.";
        }
    }

    public String actualizar(Area obj) {
        if (DATOS.actualizar(obj)) {
            return "OK";
        } else {
            return "Error en la actualización de datos.";
        }
    }

    public String eliminar(int id) {
        if (DATOS.eliminar(id)) {
            return "OK";
        } else {
            return "Error en la eliminación de datos.";
        }
    }
}
