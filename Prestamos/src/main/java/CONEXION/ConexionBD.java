package CONEXION;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL =
            "jdbc:mysql://localhost:3306/SISTEMAPRESTAMO";
    private static final String USER = "app_prestamos";
    private static final String PASSWORD ="AppPrestamos123!";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
