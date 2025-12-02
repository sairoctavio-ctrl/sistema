package dao;

import models.Usuario;
import java.sql.*;
import java.util.List;
import sistemapedidos.DatabaseConfig;

public class UsuarioDAO {
    
    public static Usuario autenticar(String usuario, String password) {
        // Primero intentar con la base de datos real
        
        Boolean DevMode = false;
        Usuario user = autenticarEnBD(usuario, password);
        if (user != null && !DevMode) {
            System.out.println("user");
            return user;
        }
        
        // Si no encuentra en BD, usar usuarios de prueba
        if(DevMode){
        return autenticarUsuariosPrueba(usuario, password);
        }
        return null;
    }
    
    private static Usuario autenticarEnBD(String usuario, String password) {
        String sql = "SELECT id, username, nombre, rol, activo FROM usuarios WHERE username = ? AND password = ? AND activo = '1'";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.print(rs.getString("username"));
                return new Usuario(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("nombre"),
                    rs.getString("rol"),
                    rs.getString("activo")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error en autenticación BD: " + e.getMessage());
        }
        return null;
    }
    
    private static Usuario autenticarUsuariosPrueba(String usuario, String password) {
        /* Usuarios de prueba predefinidos
        if ("admin".equals(usuario) && "admin123".equals(password)) {
            return new Usuario(1, "admin", "Administrador Principal", Usuario.ROL_ADMINISTRADOR, "Activo");
        }
        else if ("supervisor1".equals(usuario) && "super123".equals(password)) {
            return new Usuario(2, "supervisor1", "Supervisor General", Usuario.ROL_SUPERVISOR, "Activo");
        }
        else if ("vendedor1".equals(usuario) && "vendedor123".equals(password)) {
            return new Usuario(3, "vendedor1", "Vendedor Principal", Usuario.ROL_VENDEDOR, "Activo");
        }
        else if ("operador1".equals(usuario) && "operador123".equals(password)) {
            return new Usuario(4, "operador1", "Operador Sistema", Usuario.ROL_OPERADOR, "Activo");
        }*/
        return null;
    }

    public static List<Usuario> obtenerTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}