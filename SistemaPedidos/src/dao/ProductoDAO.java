package dao;

import models.Producto;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import sistemapedidos.DatabaseConfig;

public class ProductoDAO {
    
    public static List<Producto> obtenerTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, precio, stock, categoria, activo FROM productos WHERE activo = true ORDER BY nombre";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecio(rs.getBigDecimal("precio"));
                producto.setStock(rs.getInt("stock"));
                producto.setCategoria(rs.getString("categoria"));
                producto.setActivo(rs.getBoolean("activo"));
                productos.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo productos: " + e.getMessage());
            e.printStackTrace();
        }
        return productos;
    }
    
    public static Producto obtenerPorId(int id) {
        String sql = "SELECT id, nombre, descripcion, precio, stock, categoria, activo FROM productos WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecio(rs.getBigDecimal("precio"));
                producto.setStock(rs.getInt("stock"));
                producto.setCategoria(rs.getString("categoria"));
                producto.setActivo(rs.getBoolean("activo"));
                return producto;
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo producto: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean insertar(Producto producto) {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, stock, categoria) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getDescripcion());
            pstmt.setBigDecimal(3, producto.getPrecio());
            pstmt.setInt(4, producto.getStock());
            pstmt.setString(5, producto.getCategoria());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error insertando producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre=?, descripcion=?, precio=?, stock=?, categoria=? WHERE id=?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getDescripcion());
            pstmt.setBigDecimal(3, producto.getPrecio());
            pstmt.setInt(4, producto.getStock());
            pstmt.setString(5, producto.getCategoria());
            pstmt.setInt(6, producto.getId());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error actualizando producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean actualizarStock(int productoId, int cantidad) {
        String sql = "UPDATE productos SET stock = stock - ? WHERE id = ? AND stock >= ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cantidad);
            pstmt.setInt(2, productoId);
            pstmt.setInt(3, cantidad);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error actualizando stock: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean eliminar(int id) {
        String sql = "UPDATE productos SET activo = false WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error eliminando producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<Producto> buscar(String criterio) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, precio, stock, categoria, activo FROM productos " +
                    "WHERE (nombre LIKE ? OR descripcion LIKE ? OR categoria LIKE ?) AND activo = true ORDER BY nombre";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String likeCriterio = "%" + criterio + "%";
            pstmt.setString(1, likeCriterio);
            pstmt.setString(2, likeCriterio);
            pstmt.setString(3, likeCriterio);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecio(rs.getBigDecimal("precio"));
                producto.setStock(rs.getInt("stock"));
                producto.setCategoria(rs.getString("categoria"));
                producto.setActivo(rs.getBoolean("activo"));
                productos.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("Error buscando productos: " + e.getMessage());
            e.printStackTrace();
        }
        return productos;
    }
    
    public static List<Producto> obtenerConStockBajo(int limite) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, precio, stock, categoria, activo FROM productos " +
                    "WHERE stock <= ? AND activo = true ORDER BY stock ASC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limite);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecio(rs.getBigDecimal("precio"));
                producto.setStock(rs.getInt("stock"));
                producto.setCategoria(rs.getString("categoria"));
                producto.setActivo(rs.getBoolean("activo"));
                productos.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo productos con stock bajo: " + e.getMessage());
            e.printStackTrace();
        }
        return productos;
    }
    
    // Método adicional para incrementar stock
    public static boolean incrementarStock(int productoId, int cantidad) {
        String sql = "UPDATE productos SET stock = stock + ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cantidad);
            pstmt.setInt(2, productoId);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error incrementando stock: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Método para verificar si existe un producto con el mismo nombre
    public static boolean existeProducto(String nombre) {
        String sql = "SELECT COUNT(*) FROM productos WHERE nombre = ? AND activo = true";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error verificando producto: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}