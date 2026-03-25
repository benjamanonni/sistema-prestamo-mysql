package DATOS;

import CONEXION.ConexionBD;
import DOMINIO.Sancion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SancionDAO {
    //1-registrar sancion
    public void registrarSancion(Sancion sancion){
        String sql= """
                insert into Sanciones(S_DiaAtraso,S_FechaInicioBloqueo,S_FechaFinBloqueo,U_Legajo,P_id)
                values(?,?,?,?,?)
                """;
        try(//hacemos la conexion con mysql
            Connection conn = ConexionBD.getConnection();
            //le entregamos a mysql el insert
            PreparedStatement ps = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setLong(1,sancion.getDiaAtraso());
            ps.setTimestamp(2, Timestamp.valueOf(sancion.getFechaInicioBloqueo()));
            ps.setTimestamp(3,Timestamp.valueOf(sancion.getFechaFinBloqueo()));
            ps.setString(4,sancion.getUsuariolegajo());
            ps.setInt(5,sancion.getPrestamoid());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    sancion.setIdSancion(idGenerado);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("error al registrar sancion"+e);
        }
    }
    //2-bloquear usuario
    public void bloquearUsuario(String idUsuario,LocalDateTime fechafinbloqueo){
        String sql= """
                update usuarios
                set u_bloqueadoHasta = ?
                where u_legajo= ?
                """;
        try(//hacemos la conexion con mysql
            Connection conn = ConexionBD.getConnection();
            //le entregamos a mysql el insert
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setTimestamp(1,Timestamp.valueOf(fechafinbloqueo));
            ps.setString(2,idUsuario);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("no se pudo bloquear el usuario"+e);
        }
    }
    //3-obtener sanciones
    public ArrayList<Sancion>obtenerSanciones(){
        ArrayList<Sancion>sanciones=new ArrayList<>();
        String sql= """
                select S_Id,S_DiaAtraso,S_FechaInicioBloqueo,S_FechaFinBloqueo,P_Id,U_Legajo
                from sanciones
                """;
        try(//hacemos la conexion con mysql
            Connection conn = ConexionBD.getConnection();
            //le entregamos a mysql el insert
            PreparedStatement ps = conn.prepareStatement(sql)){
            ResultSet rs =ps.executeQuery();
            while(rs.next()){
                int id=rs.getInt("S_Id");
                long diasAtraso=rs.getLong("S_DiaAtraso");
                LocalDateTime fechainicioBloqueo=rs.getTimestamp("S_FechaInicioBloqueo").toLocalDateTime();
                LocalDateTime fechafinBloqueo = rs.getTimestamp("S_FechaFinBloqueo").toLocalDateTime();
                int prestamoId=rs.getInt("P_Id");
                String usuarioLegajo=rs.getString("u_legajo");

                Sancion s=new Sancion(prestamoId,usuarioLegajo,diasAtraso,fechainicioBloqueo,fechafinBloqueo,id);
                sanciones.add(s);
            }
            return sanciones;
        } catch (Exception e) {
            throw new RuntimeException("error al listar sanciones"+e);
        }
    }
}
