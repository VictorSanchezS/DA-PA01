package data;

import java.util.List;

public interface CargosDao<T> {
    public List<T> listar(String texto);
    public boolean insertar(T obj);
    public boolean actualizar(T obj);
    public boolean eliminar(int id);
}
