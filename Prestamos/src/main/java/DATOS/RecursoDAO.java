package DATOS;

import CONEXION.ConexionBD;
import DOMINIO.Recurso;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class RecursoDAO {
    //1-registrar nuevo recurso
    public void registrarNuevoRecurso(Recurso recurso){
        //definimos la sentencia de mysql
        String sqr = """
                insert into Recursos(R_Nombre, R_Codigo, R_Tipo, R_Estado, R_FechaAlta)
                values(?,?,?,?,?)
                """;
        try(//hacemos la conexion con mysql
            Connection conn = ConexionBD.getConnection();
            //le entregamos a mysql el insert
            PreparedStatement ps = conn.prepareStatement(sqr);
                ){
            ps.setString(1,recurso.getNombre());
            ps.setString(2,recurso.getCodigo());
            //lo convertimos a string
            ps.setString(3,recurso.getTipoRecurso().name());
            ps.setString(4,recurso.getEstado().name());
            //guardamos la fecha como datetime
            ps.setTimestamp(5, Timestamp.valueOf(recurso.getFechaAlta()));
            //le enviamos a mysql el insert
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error técnico al registrar recurso", e);
            }
    }

    //2-listar recurso por estado
    public HashMap<String,Recurso> listarRecursoEstado(TipoEstadoRecurso estado) {
        //necesitamos traer la lista de mysql
        String sqr = """
                select R_Nombre,R_codigo,R_Tipo,R_Estado
                from recursos
                where R_Estado = ?;
                """;
        //hacemos la conexion
        try (Connection conn = ConexionBD.getConnection();
             //le pasamos el statement
             PreparedStatement ps = conn.prepareStatement(sqr)) {

            //como el ? esta vacio nosotros ponemos el indice en ese valor
            ps.setString(1, estado.name());

            try (ResultSet rs = ps.executeQuery()) {
                HashMap<String, Recurso> lista = new HashMap<>();

                while (rs.next()) {
                    String nombre = rs.getString("R_Nombre");
                    String codigo = rs.getString("R_Codigo");
                    TipoEstadoRecurso tipoestado = TipoEstadoRecurso.valueOf(rs.getString("R_Estado"));
                    TipoRecurso tiporecurso = TipoRecurso.valueOf(rs.getString("R_Tipo"));

                    Recurso recurso = new Recurso(nombre, codigo, tiporecurso, tipoestado, LocalDateTime.now());
                    lista.put(codigo, recurso);
                }
                return lista;
            }

        } catch (SQLException e) {
            throw new RuntimeException("error al listar recursos por estado", e);
        }

    }

    //3-obtener recurso por codigo
    public Recurso obtenerRecursoPorCodigo(String codigo){
        //traemos el recurso a travez de codigo
        String sqr= """
                select *
                from recursos
                where R_Codigo= ?;
                """;
        //hacemos la conexion con la base de datos
        try(Connection conn = ConexionBD.getConnection();
            //le entrega la consulta a mysql
            PreparedStatement ps = conn.prepareStatement(sqr);
            ){
            //seteamos en el ? para entregarle el statement,le decimos que valor va en el indice
           ps.setString(1,codigo);
           //hacemos la consulta a mysql
            ResultSet rs = ps.executeQuery();
            //confirma si hay fila devuelve true si hay fila false sino
            if (!rs.next()) {
                return null; // no existe el recurso
            }
            //leemos los datos con rs.string
            String R_codigo = rs.getString("R_codigo");
            String R_nombre = rs.getString("R_Nombre");
            TipoRecurso R_tipo = TipoRecurso.valueOf(rs.getString("R_tipo"));
            TipoEstadoRecurso R_estado = TipoEstadoRecurso.valueOf(rs.getString("R_Estado"));
            //pasamos fecha en string a LocalDameTime
            LocalDateTime R_fechaalta =rs.getTimestamp("r_fechaalta").toLocalDateTime();

            //creamos el objeto
            Recurso r = new Recurso(R_nombre,R_codigo,R_tipo,R_estado,R_fechaalta);
            return r;
            } catch (Exception e) {
                throw new RuntimeException("no se encontro recurso con ese codigo" + e);
            }
        }

    //4-consultar recurso por tipo
    public ArrayList<Recurso>obtenerlistadeRecursoTipo(TipoRecurso tipoRecurso){
        //creamos la lista
        ArrayList<Recurso>recursos=new ArrayList<>();
        String sqr = """
                select R_Nombre, R_Codigo, R_Tipo, R_Estado, R_FechaAlta
                from recursos
                where R_Tipo = ?
                """;
        //hacemos conexion
        try(Connection conn = ConexionBD.getConnection();
            //le entrega la consulta a mysql
            PreparedStatement ps = conn.prepareStatement(sqr);
        ){
            //convertimos tipo de recurso a string,insertamos en ? el tipoRecurso
            ps.setString(1,tipoRecurso.name());
            //ejecutamos la consulta
            ResultSet rs= ps.executeQuery();
            while(rs.next()){
                //obtenemos los datos
                String nombre = rs.getString("R_Nombre");
                String codigo = rs.getString("R_Codigo");
                TipoRecurso tipo = TipoRecurso.valueOf(rs.getString("R_Tipo"));
                TipoEstadoRecurso estado = TipoEstadoRecurso.valueOf(rs.getString("R_Estado"));
                LocalDateTime fechaAlta = rs.getTimestamp("R_FechaAlta").toLocalDateTime();

                //creamos el objeto
                Recurso r = new Recurso(nombre,codigo,tipo,estado,fechaAlta);
                //lo añadimos a la lista
                recursos.add(r);
            }
        } catch (Exception e) {
            throw new RuntimeException("error al buscar la lista de recursos"+ e);
        }
        return recursos;
    }

    //5-cambiar estado del recurso
    public void cambiarEstadoRecurso(String codigo, TipoEstadoRecurso estado) {
        //creamos sentencia
        String sqr = """
                UPDATE recursos
                SET R_estado = ?
                WHERE R_Codigo = ?
                AND R_estado <> 'PRESTADO'
                AND R_estado <>"DADOBAJA"
                """;
        //hacemos conexion con base de datos
        try (Connection conn = ConexionBD.getConnection();
             //le entrega la consulta a mysql
             PreparedStatement ps = conn.prepareStatement(sqr);
        ) {
            //nos paramos en los ? para ingresar los datos
            //cambiamos el tipo de estado a string
            ps.setString(1, estado.name());
            ps.setString(2, codigo);
            //devuelve cantidad de filas afectadas
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("error al cambiar el estado del recurso" + e);
        }
    }

    //6-listar recursos por estado=activo
    public HashMap<String,Recurso> listarRecursoActivo() {
        //necesitamos traer la lista de mysql
        String sqr = """
                select R_Nombre,R_codigo,R_Tipo,R_Estado
                from recursos
                where R_Estado ="DISPONIBLE";
                """;
        //hacemos la conexion
        try (Connection conn = ConexionBD.getConnection();
             //le pasamos el statement
             PreparedStatement ps = conn.prepareStatement(sqr)) {

            try (ResultSet rs = ps.executeQuery()) {
                HashMap<String, Recurso> lista = new HashMap<>();

                while (rs.next()) {
                    String nombre = rs.getString("R_Nombre");
                    String codigo = rs.getString("R_Codigo");
                    TipoEstadoRecurso tipoestado = TipoEstadoRecurso.valueOf(rs.getString("R_Estado"));
                    TipoRecurso tiporecurso = TipoRecurso.valueOf(rs.getString("R_Tipo"));

                    Recurso recurso = new Recurso(nombre, codigo, tiporecurso, tipoestado, LocalDateTime.now());
                    lista.put(codigo, recurso);
                }
                return lista;
            }

        } catch (SQLException e) {
            throw new RuntimeException("error al listar recursos por estado", e);
        }
    }

    //7-listar recursos que su nombre contenga una palabra
    public HashMap<String,Recurso> listarRecursosPorNombre(String nombreAbuscar) {
        HashMap<String,Recurso> recursos = new HashMap<>();
        String sql = """
                select R_Nombre,R_codigo,R_Tipo,R_Estado,R_FechaAlta
                from recursos
                where R_Nombre like ?
                """;
        //hacemos conexion
        try (Connection conn = ConexionBD.getConnection();
             //le pasamos el statement
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" +nombreAbuscar + "%");
            //nos detenemos 1 indice antes de este dato
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                //obtenemos los datos
                String nombre = rs.getString("R_Nombre");
                String codigo = rs.getString("R_Codigo");
                TipoRecurso tipo = TipoRecurso.valueOf(rs.getString("R_Tipo"));
                TipoEstadoRecurso estado = TipoEstadoRecurso.valueOf(rs.getString("R_Estado"));
                LocalDateTime fechaAlta = rs.getTimestamp("R_FechaAlta").toLocalDateTime();

                //creamos el objeto
                Recurso r = new Recurso(nombre,codigo,tipo,estado,fechaAlta);
                //lo añadimos a la lista
                recursos.put(codigo,r);
            }
        } catch (Exception e) {
            throw new RuntimeException("error al buscar por titulo"+e);
        }
        return recursos;
    }
    //8-cambiar estado del recurso
    public boolean cambiarEstadoRecursoPrestado(String legajorecurso){
        String sql= """
               update recursos
               set r_estado= "PRESTADO"
               where r_codigo = ?
                """;
        try(Connection conn = ConexionBD.getConnection();
            //le pasamos el statement
            PreparedStatement ps = conn.prepareStatement(sql)){
            //asignamos valor a ?
            ps.setString(1,legajorecurso);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("error al cambiar estado de recursoa a prestado"+e);
        }
    }
}
