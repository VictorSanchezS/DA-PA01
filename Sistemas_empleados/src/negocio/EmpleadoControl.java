/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import data.EmpleadoDao;
import data.impl.AreaDaoImpl;
import data.impl.CargoDaoImpl;
import data.impl.EmpleadoDaoImpl;
import dominio.Area;
import dominio.Cargo;
import dominio.Empleado;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USUARIO
 */
public class EmpleadoControl {

    private final EmpleadoDao DATOS;
    private final AreaDaoImpl DATOSARE;
    private final CargoDaoImpl DATOSCAR;
    private Empleado obj;

    public EmpleadoControl() {
        this.DATOS = new EmpleadoDaoImpl();
        this.obj = new Empleado();
        this.DATOSARE = new AreaDaoImpl();
        this.DATOSCAR = new CargoDaoImpl();
    }

    private DefaultTableModel modeloTabla;

    public DefaultTableModel listar(String texto) {
        List<Empleado> lista = new ArrayList<>();
        lista.addAll(DATOS.listar(texto));
        // Establecemos la columna del tableModel
        String[] titulos = {"ID", "NOMBRE", "APELLIDO", "TIPO DOCUMENTO", "DOCUMENTO", "AREA ID", "AREA", "CARGO ID", "CARGO", "TELEFONO", "CORREO"};
        // Declaramos un vector que será el que agreguemos como registro al DefaultTableModel
        String[] registro = new String[11];
        // Agrego los títulos al DefaultTableModel
        this.modeloTabla = new DefaultTableModel(null, titulos);

        // Recorrer toda mi lista y la pasare al DefaultTableModel
        for (Empleado item : lista) {
            registro[0] = Integer.toString(item.getId());
            registro[1] = item.getNombre();
            registro[2] = item.getApellido();
            registro[3] = item.getTipoDoc();
            registro[4] = item.getDocumento();
            registro[5] = Integer.toString(item.getAreaId());
            registro[6] = item.getAreaNombre();
            registro[7] = Integer.toString(item.getCargoId());
            registro[8] = item.getCargoNombre();
            registro[9] = item.getTelefono();
            registro[10] = item.getCorreo();

            this.modeloTabla.addRow(registro);
        }
        return this.modeloTabla;
    }

    public String insertar(Empleado obj) {
        if (DATOS.insertar(obj)) {
            return "OK";
        } else {
            return "Error en el ingreso de datos.";
        }
    }

    public String actualizar(Empleado obj) {
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

    public DefaultComboBoxModel seleccionarAreas(){
        DefaultComboBoxModel items= new DefaultComboBoxModel();
        List<Area> lista=new ArrayList();
        lista=DATOSARE.seleccionar();
        for (Area item: lista){
            items.addElement(new Area(item.getId(),item.getNombre()));
        }
        return items;
    }
    
    public DefaultComboBoxModel seleccionarCargos(){
        DefaultComboBoxModel items= new DefaultComboBoxModel();
        List<Cargo> lista=new ArrayList();
        lista=DATOSCAR.seleccionar();
        for (Cargo item: lista){
            items.addElement(new Cargo(item.getId(),item.getNombre()));
        }
        return items;
    }
    
}
