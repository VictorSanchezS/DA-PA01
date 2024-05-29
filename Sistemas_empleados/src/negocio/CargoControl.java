package negocio;

import data.impl.CargosDaoImpl;
import dominio.Cargos;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import data.CargosDao;

public class CargoControl {

    private final CargosDao DATOS;
    private Cargos obj;

    public CargoControl() {
        this.DATOS = new CargosDaoImpl();
        this.obj = new Cargos();
    }
    private DefaultTableModel modeloTabla;

    public DefaultTableModel listar(String texto) {
        List<Cargos> lista = new ArrayList();
        lista.addAll(DATOS.listar(texto));
        //Establecemos la columna del tableModel
        String[] titulos = {"ID", "NOMBRE", "DESCRIPCION"};
        //Declaramos un vector que será el que agreguemos como registro al DefaultTableModel
        String[] registro = new String[3];
        //agrego los títulos al DefaultTableModel
        this.modeloTabla = new DefaultTableModel(null, titulos);

        //Recorrer toda mi lista y la pasare al DefaultTableModel
        for (Cargos item : lista) {
            registro[0] = Integer.toString(item.getId());
            registro[1] = item.getNombre();
            registro[2] = item.getDescripcion();
            this.modeloTabla.addRow(registro);
        }
        return this.modeloTabla;
    }

    public String insertar(Cargos obj) {
        if (DATOS.insertar(obj)) {
            return "OK";
        } else {
            return "Error en el ingreso de datos.";
        }
    }

    public String actualizar(Cargos obj) {
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
