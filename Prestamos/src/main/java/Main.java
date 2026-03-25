import CONEXION.ConexionBD;
import DATOS.*;
import SISTEMA.SistemaPrestamo;

import java.sql.Connection;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        // PASO 1: Crear las herramientas (Las dependencias)
        // Acá es el ÚNICO lugar donde hacemos los "new" de los DAOs
        UsuarioDAO miUsuarioDAO = new UsuarioDAO();
        RecursoDAO miRecursoDAO = new RecursoDAO();
        PrestamoDAO miPrestamoDAO = new PrestamoDAO();
        SancionDAO miSancionDAO = new SancionDAO();

        // PASO 2: La Inyección (Armar el sistema)
        // Le pasamos las herramientas al sistema a través de su constructor
        SistemaPrestamo sistema = new SistemaPrestamo(miUsuarioDAO, miRecursoDAO, miPrestamoDAO, miSancionDAO);
        /*usuarios
        sistema.registrarNuevoUsuario("a","benjamin Manonni",TipoUsuario.ALUMNO,"benjamanonni@gmail.com");
        sistema.registrarNuevoUsuario("b", "Bruno Lopez", TipoUsuario.DOCENTE, "bruno.lopez@gmail.com");
        sistema.registrarNuevoUsuario("c", "Carla Rodriguez", TipoUsuario.ALUMNO, "carla.rodriguez@gmail.com");
        sistema.registrarNuevoUsuario("d", "Diego Fernandez", TipoUsuario.DOCENTE, "diego.fernandez@gmail.com");
        sistema.registrarNuevoUsuario("e", "Elena Gomez", TipoUsuario.ALUMNO, "elena.gomez@gmail.com");
        sistema.registrarNuevoUsuario("f", "Facundo Perez", TipoUsuario.DOCENTE, "facundo.perez@gmail.com");
        sistema.registrarNuevoUsuario("g", "Gabriela Torres", TipoUsuario.ALUMNO, "gabriela.torres@gmail.com");
        sistema.registrarNuevoUsuario("h", "Hernan Silva", TipoUsuario.DOCENTE, "hernan.silva@gmail.com");
        sistema.registrarNuevoUsuario("i", "Ines Romero", TipoUsuario.ALUMNO, "ines.romero@gmail.com");
        sistema.registrarNuevoUsuario("zz", "Julian Castro", TipoUsuario.DOCENTE, "julian.castro@gmail.com");
*/
        sistema.registrarNuevoUsuario("zzzzz", "Juan Co", TipoUsuario.DOCENTE, "cassko@gmail.com");
        //RECURSOS
        LocalDateTime horaRegistro = LocalDateTime.of(2026, 2, 2, 10, 30);
/*
        sistema.registrarNuevoRecurso("notebook","1", TipoRecurso.NOTEBOOK, TipoEstadoRecurso.DISPONIBLE,horaRegistro);
        sistema.registrarNuevoRecurso("notebook","5", TipoRecurso.NOTEBOOK, TipoEstadoRecurso.DISPONIBLE,horaRegistro);
        sistema.registrarNuevoRecurso("kit laboratorio","2", TipoRecurso.KITLABORATORIO, TipoEstadoRecurso.DISPONIBLE,horaRegistro);
        sistema.registrarNuevoRecurso("proyector","3", TipoRecurso.PROYECTOR, TipoEstadoRecurso.DISPONIBLE,horaRegistro);
        sistema.registrarNuevoRecurso("libro","4", TipoRecurso.LIBRO, TipoEstadoRecurso.DISPONIBLE,horaRegistro);
        sistema.registrarNuevoRecurso("libro","6",TipoRecurso.LIBRO,TipoEstadoRecurso.DISPONIBLE,horaRegistro);
        sistema.registrarNuevoRecurso("libro","7",TipoRecurso.LIBRO,TipoEstadoRecurso.DISPONIBLE,horaRegistro);
        sistema.registrarNuevoRecurso("libro","8",TipoRecurso.LIBRO,TipoEstadoRecurso.DISPONIBLE,horaRegistro);
 */
        sistema.registrarNuevoRecurso("libro","9999",TipoRecurso.LIBRO);
        //PRESTAMOS
        LocalDateTime fechaInicioPrestamos = LocalDateTime.of(2026, 2, 2, 12, 30);
        LocalDateTime fechaVencimientoPrestamos = LocalDateTime.of(2026,3, 1, 1, 30);
        //sistema.registrarNuevoPrestamo("a", "1",fechaInicioPrestamos, fechaVencimientoPrestamos);
        //sistema.registrarNuevoPrestamo("b","2",fechaInicioPrestamos,fechaVencimientoPrestamos);
        //sistema.registrarNuevoPrestamo("c","3",fechaInicioPrestamos,fechaVencimientoPrestamos);
        //sistema.registrarNuevoPrestamo("e","4",fechaInicioPrestamos,fechaVencimientoPrestamos);
        //sistema.registrarNuevoPrestamo("j","6",fechaInicioPrestamos,fechaVencimientoPrestamos)
        sistema.registrarNuevoPrestamo("zzzzz","9999",fechaInicioPrestamos,fechaVencimientoPrestamos);
        sistema.listarPrestamos();

        try (Connection con = ConexionBD.getConnection()) {
            System.out.println("Conexión OK: " + !con.isClosed());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    }
