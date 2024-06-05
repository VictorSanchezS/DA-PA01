/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import data.AsistenciaDao;
import data.impl.AsistenciaDaoImpl;
import data.impl.EmpleadoDaoImpl;
import dominio.Asistencia;
import dominio.Empleado;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

import java.text.SimpleDateFormat;

public class AsistenciaControl {
    
    private final AsistenciaDao DATOS;
    private final EmpleadoDaoImpl DATOSEMP;
    private Empleado obj;

    public AsistenciaControl() {
        this.DATOS = new AsistenciaDaoImpl();
        this.obj = new Empleado();
        this.DATOSEMP = new EmpleadoDaoImpl();
    }
    
    private DefaultTableModel modeloTabla;
    
    public DefaultTableModel listar(String texto) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

        List<Asistencia> lista = new ArrayList<>();
        lista.addAll(DATOS.listar(texto));
        // Establecemos la columna del tableModel
        String[] titulos = {"ID", "EMPLEADO ID", "EMPLEADO NOMBRE", "FECHA", "HORA ENTRADA", "HORA SALIDA"};
        // Declaramos un vector que será el que agreguemos como registro al DefaultTableModel
        String[] registro = new String[6]; // Cambiado a 7 para que coincida con el número de títulos
        // Agrego los títulos al DefaultTableModel
        this.modeloTabla = new DefaultTableModel(null, titulos);

        // Recorrer toda mi lista y la pasare al DefaultTableModel
        for (Asistencia item : lista) {
            registro[0] = Integer.toString(item.getId());
            registro[1] = Integer.toString(item.getEmpleadoId());
            registro[2] = item.getEmpleadoNombre();
            registro[3] = formatter.format(item.getFecha());
            registro[4] = timeFormatter.format(item.getHoraEntrada());
            registro[5] = timeFormatter.format(item.getHoraSalida());

            this.modeloTabla.addRow(registro);
        }
        return this.modeloTabla;
    }
    
     public String insertar(Asistencia obj) {
        if (DATOS.insertar(obj)) {
            return "OK";
        } else {
            return "Error en el ingreso de datos.";
        }
    }
     
     public String actualizar(Asistencia obj) {
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

    public DefaultComboBoxModel seleccionarEmpleados() {
        DefaultComboBoxModel items = new DefaultComboBoxModel();
        List<Empleado> lista = new ArrayList();
        lista = DATOSEMP.seleccionar();
        for (Empleado item : lista) {
            items.addElement(new Empleado(item.getId(), item.getNombre()));
        }
        return items;
    }
}
