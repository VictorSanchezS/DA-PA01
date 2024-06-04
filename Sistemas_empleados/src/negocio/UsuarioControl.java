package negocio;

import dominio.Usuarios;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import data.UsuarioDao;
import data.impl.UsuarioDaoImpl;

public class UsuarioControl {

    private final UsuarioDao DATOS;
    private Usuarios obj;

    public UsuarioControl() {
        this.DATOS = new UsuarioDaoImpl();
        this.obj = new Usuarios();
    }
    private DefaultTableModel modeloTabla;

    public DefaultTableModel listar(String texto) {
        List<Usuarios> lista = new ArrayList();
        lista.addAll(DATOS.listar(texto));
        //Establecemos la columna del tableModel
        String[] titulos = {"ID", "USUARIO","PASSWORD","TIPO USUARIO"};
        //Declaramos un vector que será el que agreguemos como registro al DefaultTableModel
        String[] registro = new String[4];
        //agrego los títulos al DefaultTableModel
        this.modeloTabla = new DefaultTableModel(null, titulos);

        //Recorrer toda mi lista y la pasare al DefaultTableModel
        for (Usuarios item : lista) {
            registro[0] = Integer.toString(item.getIdUser());
            registro[1] = item.getUsuario();
            registro[2] = item.getPassword();
            registro[3] = item.getTipoUsuario();
            this.modeloTabla.addRow(registro);
        }
        return this.modeloTabla;
    }
    
    public String insertar(Usuarios obj) {
        if (DATOS.insertar(obj)) {
            return "OK";
        } else {
            return "Error en el ingreso de datos.";
        }
    }

    public String actualizar(Usuarios obj) {
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
