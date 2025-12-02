package sistemapedidos;

import java.sql.*;

public class DatabaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "sistema_pedidos";
    private static final String FULL_URL = URL + DB_NAME;
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error cargando driver MySQL: " + e.getMessage());
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(FULL_URL, USER, PASSWORD);
    }
    
    public static void inicializarBaseDatos() {
        System.out.println("=== INICIALIZANDO SISTEMA DE PEDIDOS ===");
        verificarConexion();
    }
    
    private static void verificarConexion() {
        new Thread(() -> {
            try {
                Thread.sleep(500); // Pequeña pausa para efecto visual
                Connection conn = getConnection();
                System.out.println("✓ Conexión a MySQL establecida");
                conn.close();
            } catch (Exception e) {
                System.err.println("✗ Error de conexión: " + e.getMessage());
                System.err.println("Asegúrese que:");
                System.err.println("• XAMPP esté ejecutándose");
                System.err.println("• MySQL esté activo"); 
                System.err.println("• La base de datos 'sistema_pedidos' exista");
            }
        }).start();
    }
}