package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:postgresql://localhost:5432/ecommerce_Klicalia";
    private static final String USUARIO = "postgres";
    private static final String CLAVE = "TU_CLAVE_AQUI"; // reemplaza por tu clave local de PostgreSQL antes de correr el proyecto

    private static ConexionBD instancia;
    private Connection conexion;

    private ConexionBD() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                "No se encontró el driver de PostgreSQL. Revisa que el jar esté en WEB-INF/lib.", e);
        }
        conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
    }

    public static synchronized Connection getConexion() throws SQLException {
        if (instancia == null || instancia.conexion == null || instancia.conexion.isClosed()) {
            instancia = new ConexionBD();
        }
        return instancia.conexion;
    }
}