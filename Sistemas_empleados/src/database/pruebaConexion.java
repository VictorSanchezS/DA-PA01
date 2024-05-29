
package database;

public class pruebaConexion {
    public static void main( String[] args ){
        Conexion con = new Conexion();
        con.conectar();
        if( con.con != null ){
            System.out.println("Conectado");
        }else{
            System.out.println("Deconectado");
        }
    }
}
