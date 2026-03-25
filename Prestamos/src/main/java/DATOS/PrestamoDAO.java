package DATOS;

import CONEXION.ConexionBD;
import DOMINIO.Prestamo;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PrestamoDAO{
    //1-registrar prestamo
    public boolean registrarNuevoPrestamo(Prestamo prestamo) {
        //creamos el stament
        String sql = """
                insert into Prestamos(P_FechaInicio,P_FechaVencimiento,P_FechaDevolucion,P_Estado,U_LegajoHace,R_Codigo)
                values(?,?,?,?,?,?)
                """;
        //conexion a base de dato
        try (//hacemos la conexion con mysql
             Connection conn = ConexionBD.getConnection();
             //le entregamos a mysql el insert
             PreparedStatement ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            //guardamos la fecha como dameTime
            ps.setTimestamp(1, Timestamp.valueOf(prestamo.getFechaInicio()));
            ps.setTimestamp(2, Timestamp.valueOf(prestamo.getFechaVencimiento()));
            //marcamos fecha devolucion vacia
            ps.setTimestamp(3, null);
            ps.setString(4, TipoEstadoPrestamo.ACTIVO.name());
            ps.setString(5, prestamo.getLegajoUsuario());
            ps.setString(6, prestamo.getCodigoRecurso());
            ps.executeUpdate();
            //obtener id generado
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    prestamo.setId(idGenerado);
                }
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException("error al registrar nuevo prestamo" + e);
        }
    }
    //2-listar prestamos
    public ArrayList<Prestamo> listarPrestamos() {
        ArrayList<Prestamo> prestamos = new ArrayList<>();
        String sql = """
                select P_FechaInicio,P_FechaVencimiento,P_FechaDevolucion,P_Estado,U_LegajoHace,R_Codigo,P_Id
                from Prestamos
                """;
        //conectamos base de dato
        try (Connection conn = ConexionBD.getConnection();
             //le entregamos a mysql el insert
             PreparedStatement ps = conn.prepareStatement(sql)) {
            //nos paramos un indice antes del dato
            ResultSet rs = ps.executeQuery();
            //nos movemos con while
            while (rs.next()) {
                LocalDateTime P_FechaInicio = rs.getTimestamp("P_FechaInicio").toLocalDateTime();
                LocalDateTime P_FechaVencimiento = rs.getTimestamp("P_FechaVencimiento").toLocalDateTime();
                //trabajamos fechaDevolucion como si fuera null o no

                Timestamp tsDev = rs.getTimestamp("P_FechaDevolucion");
                LocalDateTime P_FechaDevolucion;

                if (tsDev != null) {
                    P_FechaDevolucion = tsDev.toLocalDateTime();
                } else {
                    P_FechaDevolucion = null;
                }

                TipoEstadoPrestamo P_Estado = TipoEstadoPrestamo.valueOf(rs.getString("P_Estado"));
                String legajoAlumno = rs.getString("U_LegajoHace");
                String codigoRecurso = rs.getString("R_Codigo");
                int id = rs.getInt("P_Id");

                Prestamo p = new Prestamo(legajoAlumno, codigoRecurso, P_FechaInicio, P_FechaVencimiento, P_FechaDevolucion, id, P_Estado);
                prestamos.add(p);
            }
            return prestamos;

        } catch (Exception e) {
            throw new RuntimeException("error al listar prestamos" + e);
        }
    }

    //3-listar prestamos por usuario
    public ArrayList<Prestamo> listarPrestamosPorUsuario(String legajo) {
        ArrayList<Prestamo> prestamos = new ArrayList<>();
        String sql = """
                select P_FechaInicio,P_FechaVencimiento,P_FechaDevolucion,P_Estado,U_LegajoHace,R_Codigo,P_Id
                from prestamos
                where U_LegajoHace= ?
                """;
        //conectamos base de dato
        try (Connection conn = ConexionBD.getConnection();
             //le entregamos a mysql el insert
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LocalDateTime P_FechaInicio = rs.getTimestamp("P_FechaInicio").toLocalDateTime();
                LocalDateTime P_FechaVencimiento = rs.getTimestamp("P_FechaVencimiento").toLocalDateTime();
                //trabajamos fechaDevolucion como si fuera null o no

                Timestamp tsDev = rs.getTimestamp("P_FechaDevolucion");
                LocalDateTime P_FechaDevolucion;
                if (tsDev != null) {
                    P_FechaDevolucion = tsDev.toLocalDateTime();
                } else {
                    P_FechaDevolucion = null;
                }

                TipoEstadoPrestamo P_Estado = TipoEstadoPrestamo.valueOf(rs.getString("P_Estado"));
                String legajoAlumno = rs.getString("U_LegajoHace");
                String codigoRecurso = rs.getString("R_Codigo");
                int id = rs.getInt("P_Id");

                Prestamo p = new Prestamo(legajoAlumno, codigoRecurso, P_FechaInicio, P_FechaVencimiento, P_FechaDevolucion, id, P_Estado);
                prestamos.add(p);
            }
            return prestamos;
        } catch (Exception e) {
            throw new RuntimeException("error al listar prestamos por usuario" + e);
        }
    }

    //4-listar prestamos por recurso
    public ArrayList<Prestamo> listarPrestamoPorRecurso(String codigoRecurso) {
        ArrayList<Prestamo> prestamos = new ArrayList<>();
        String sql = """
                select P_FechaInicio,P_FechaVencimiento,P_FechaDevolucion,P_Estado,U_LegajoHace,R_Codigo,P_Id
                from prestamos
                where R_Codigo = ?
                """;
        //conectamos base de dato
        try (Connection conn = ConexionBD.getConnection();
             //le entregamos el stament
             PreparedStatement ps = conn.prepareStatement(sql)) {
            //nos detiene un indice antes  y guardamos ese indice
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LocalDateTime P_FechaInicio = rs.getTimestamp("P_FechaInicio").toLocalDateTime();
                LocalDateTime P_FechaVencimiento = rs.getTimestamp("P_FechaVencimiento").toLocalDateTime();
                //trabajamos fechaDevolucion como si fuera null o no

                Timestamp tsDev = rs.getTimestamp("P_FechaDevolucion");
                LocalDateTime P_FechaDevolucion;
                if (tsDev != null) {
                    P_FechaDevolucion = tsDev.toLocalDateTime();
                } else {
                    P_FechaDevolucion = null;
                }

                TipoEstadoPrestamo P_Estado = TipoEstadoPrestamo.valueOf(rs.getString("P_Estado"));
                String legajoAlumno = rs.getString("U_LegajoHace");
                String codigo = rs.getString("R_Codigo");
                int id = rs.getInt("P_Id");

                Prestamo p = new Prestamo(legajoAlumno, codigo, P_FechaInicio, P_FechaVencimiento, P_FechaDevolucion, id, P_Estado);
                prestamos.add(p);
            }
            return prestamos;
        } catch (Exception e) {
            throw new RuntimeException("error al listar prestamos por usuario" + e);
        }
    }

    //5-listar prestamo por id
    public Prestamo listarprestamoPorId(int idBuscar) {
        String sql = """
                select P_FechaInicio,P_FechaVencimiento,P_FechaDevolucion,P_Estado,U_LegajoHace,R_Codigo,P_Id
                from prestamos
                where P_id = ?
                """;
        //conectamos base de dato
        try (Connection conn = ConexionBD.getConnection();
             //le entregamos el stament
             PreparedStatement ps = conn.prepareStatement(sql)) {
            //nos detiene un indice antes  y guardamos ese indice
            ps.setInt(1, idBuscar);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                LocalDateTime P_FechaInicio = rs.getTimestamp("P_FechaInicio").toLocalDateTime();
                LocalDateTime P_FechaVencimiento = rs.getTimestamp("P_FechaVencimiento").toLocalDateTime();
                //trabajamos fechaDevolucion como si fuera null o no

                Timestamp tsDev = rs.getTimestamp("P_FechaDevolucion");
                LocalDateTime P_FechaDevolucion;
                if (tsDev != null) {
                    P_FechaDevolucion = tsDev.toLocalDateTime();
                } else {
                    P_FechaDevolucion = null;
                }

                TipoEstadoPrestamo P_Estado = TipoEstadoPrestamo.valueOf(rs.getString("P_Estado"));
                String legajoAlumno = rs.getString("U_LegajoHace");
                String codigo = rs.getString("R_Codigo");
                int id = rs.getInt("P_Id");

                Prestamo p = new Prestamo(legajoAlumno, codigo, P_FechaInicio, P_FechaVencimiento, P_FechaDevolucion, id, P_Estado);
                return p;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("error al listar prestamos por usuario" + e);
        }
    }

    //6-registrar devolucion
    public boolean registrarDevolucion(int idBuscar) {
        String sql = """
                UPDATE prestamos p
                JOIN recursos r ON p.R_Codigo = r.R_Codigo
                SET
                    p.P_Estado = 'DEVUELTO',
                    p.P_FechaDevolucion = now(),
                    r.R_Estado = 'DISPONIBLE'
                WHERE p.P_ID = ?;
                """;
        try (//hacemos la conexion con mysql
             Connection conn = ConexionBD.getConnection();
             //le entregamos a mysql el insert
             PreparedStatement ps = conn.prepareStatement(sql)) {
            //al signo ? le pasamos el parametro del id
            ps.setInt(1, idBuscar);
            //mandamos el statement
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("error al registrar prestamo" + e);
        }
    }

    //7-listar prestamos vencidos
    public ArrayList<Prestamo> listarPrestamosVencidos(){
        ArrayList<Prestamo>prestamos=new ArrayList<>();
        String sql= """
                select P_FechaInicio,P_FechaVencimiento,P_FechaDevolucion,P_Estado,U_LegajoHace,R_Codigo,P_Id
                from prestamos
                where P_Estado = "VENCIDO"
                """;
        //conectamos base de dato
        try (Connection conn = ConexionBD.getConnection();
             //le entregamos el stament
             PreparedStatement ps = conn.prepareStatement(sql)) {
            //nos detiene un indice antes  y guardamos ese indice
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LocalDateTime P_FechaInicio = rs.getTimestamp("P_FechaInicio").toLocalDateTime();
                LocalDateTime P_FechaVencimiento = rs.getTimestamp("P_FechaVencimiento").toLocalDateTime();
                //trabajamos fechaDevolucion como si fuera null o no

                Timestamp tsDev = rs.getTimestamp("P_FechaDevolucion");
                LocalDateTime P_FechaDevolucion;
                if (tsDev != null) {
                    P_FechaDevolucion = tsDev.toLocalDateTime();
                } else {
                    P_FechaDevolucion = null;
                }

                TipoEstadoPrestamo P_Estado = TipoEstadoPrestamo.valueOf(rs.getString("P_Estado"));
                String legajoAlumno = rs.getString("U_LegajoHace");
                String codigo = rs.getString("R_Codigo");
                int id = rs.getInt("P_Id");

                Prestamo p = new Prestamo(legajoAlumno, codigo, P_FechaInicio, P_FechaVencimiento, P_FechaDevolucion, id, P_Estado);
                prestamos.add(p);
            }
            return prestamos;
        } catch (Exception e) {
            throw new RuntimeException("error al listar prestamos vencidos" + e);
        }
    }

    //8-pasar prestamos a vencidos,este lo pasa automaticamente
    public void pasarPrestamosVencidos(){
        String sql= """
                UPDATE prestamos
                SET P_Estado = "VENCIDO"
                WHERE P_Estado = 'ACTIVO'
                AND P_FechaVencimiento < NOW();
                """;
        try(Connection conn = ConexionBD.getConnection();
            //le entregamos el stament
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("error al pasar los prestamos a vencidos"+ e);
        }
    }


}
