package DATOS;

import CONEXION.ConexionBD;
import DOMINIO.Usuario;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class UsuarioDAO {

    //1-registrar usuario
    public void registrarUsuario(Usuario usuario) {
        //definimos la pregunta que le vamos a mandar a mysql,los ? es donde van a ir los valores de estos
        String sql = """
        INSERT INTO Usuarios (U_Legajo, U_Nombre, U_Correo, U_TipoUsuario,U_BloqueadoHasta)
        VALUES (?, ?, ?, ?,?)
        """;
        try (
                //hacemos la conexion con mysql
                Connection conn = ConexionBD.getConnection();
                //le entregamos a mysql el insert
                PreparedStatement ps = conn.prepareStatement(sql);
        ) {
//seteamos los 4 parametros cada ? del insert lo remplazamos con un valor real
            ps.setString(1, usuario.getLegajo());
            ps.setString(2, usuario.getNombreCompleto());
            ps.setString(3, usuario.getCorreo());
            //convierte el enum a texto
            ps.setString(4, usuario.getTipoUsuario().name());
            ps.setTimestamp(5,null);
            //se le envia a mysql el insert,los devuelve 1 si es true osea si se se inserto
           ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error técnico al registrar usuario", e);
        }
    }

    //2-listar usuarios,necesitamos almacenar la lista en algo temporal osea traerla de mysql
    public ArrayList<Usuario> listarUsuarios() {
        //creamos la consulta sql
        String sql = """
                Select U_Legajo,U_Nombre,U_Correo,U_TipoUsuario,U_bloqueadoHasta
                from Usuarios
                """;
        //hacemos conexion
        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
        ) {
            //almacenamiento de lista temporal
            ArrayList<Usuario>lista = new ArrayList<>();
            //vamos moviendo el puntero hasta que sea false,osea no haya mas elementos
            while (rs.next()) {
                String legajo = rs.getString("U_Legajo");
                String nombre = rs.getString("U_Nombre");
                String correo = rs.getString("U_Correo");
                //convertimos el objeto a ENUM
                TipoUsuario tipo = TipoUsuario.valueOf(rs.getString("U_TipoUsuario"));
                //si bloqueado hasta es null no podemos usar localDateTime para eso primero debemos revisar que no sea null
                Timestamp asegurar = rs.getTimestamp("u_bloqueadoHasta");
                LocalDateTime bloqueadoHasta;

                if(asegurar==null) {
                     bloqueadoHasta = null;
                }else{
                     bloqueadoHasta=asegurar.toLocalDateTime();
                }

                //creamos el objeto usuario
                Usuario u = new Usuario(legajo, nombre, tipo, correo,bloqueadoHasta);
                lista.add(u);
            }
            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("error tecnico al listar usuario",e);
        }
    }

    //3-consultar usuario por legajo
    public Usuario buscarPorLegajo(String legajoBuscar){
        //guardamos la consulta que vamos a usar mas adelanta,le ponemos ? porque no representa un dato en especifico
        String sql = """
        SELECT U_Legajo, U_Nombre, U_Correo, U_TipoUsuario,u_bloqueadoHasta
        FROM Usuarios
        WHERE U_Legajo = ?
        """;

        try (
                //hacemos la conexion con la base de dato
                Connection conn = ConexionBD.getConnection();
                //le entrega la consulta a mysql
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            //seteamos el parametro donde va el ?,donde esta el ?coloca ese string
            ps.setString(1, legajoBuscar);

            //hace la consulta a mysql,rs contiene la fila entera
            try (ResultSet rs = ps.executeQuery()) {

                //confirma si hay fila devuelve true si hay fila false sino
                if (!rs.next()) {
                    return null; // no existe el usuario
                }

                //leemos los datos con rs.getString y lo guardamos en la variable
                String legajo = rs.getString("U_Legajo");
                String nombre = rs.getString("U_Nombre");
                String correo = rs.getString("U_Correo");
                //convertimos el string a ENUM
                TipoUsuario tipo = TipoUsuario.valueOf(rs.getString("U_TipoUsuario"));
                Timestamp asegurar = rs.getTimestamp("u_bloqueadoHasta");
                LocalDateTime bloqueadoHasta;
                if(asegurar==null){
                    bloqueadoHasta=null;
                }else{
                    bloqueadoHasta=asegurar.toLocalDateTime();
                }
                //creamos el objeto
                return new Usuario(legajo, nombre, tipo, correo,bloqueadoHasta);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error técnico al buscar usuario por legajo en la BD", e);
        }
    }
}
