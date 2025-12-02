package dao;

import models.Pedido;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import sistemapedidos.DatabaseConfig;

public class PedidoDAO {
    
    public static List<Pedido> obtenerTodos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.id, p.cliente_nombre, p.cliente_telefono, p.usuario_id, p.fecha_pedido, " +
                    "p.estado, p.total, p.observaciones, u.nombre as usuario_nombre " +
                    "FROM pedidos p " +
                    "LEFT JOIN usuarios u ON p.usuario_id = u.id " +
                    "ORDER BY p.fecha_pedido DESC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setId(rs.getInt("id"));
                pedido.setClienteNombre(rs.getString("cliente_nombre"));
                pedido.setClienteTelefono(rs.getString("cliente_telefono"));
                pedido.setUsuarioId(rs.getInt("usuario_id"));
                pedido.setFechaPedido(rs.getTimestamp("fecha_pedido"));
                pedido.setEstado(rs.getString("estado"));
                pedido.setTotal(rs.getBigDecimal("total"));
                pedido.setObservaciones(rs.getString("observaciones"));
                pedido.setUsuarioNombre(rs.getString("usuario_nombre"));
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo pedidos: " + e.getMessage());
        }
        return pedidos;
    }
    
    public static Pedido obtenerPorId(int id) {
        String sql = "SELECT p.id, p.cliente_nombre, p.cliente_telefono, p.usuario_id, p.fecha_pedido, " +
                    "p.estado, p.total, p.observaciones, u.nombre as usuario_nombre " +
                    "FROM pedidos p " +
                    "LEFT JOIN usuarios u ON p.usuario_id = u.id " +
                    "WHERE p.id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setId(rs.getInt("id"));
                pedido.setClienteNombre(rs.getString("cliente_nombre"));
                pedido.setClienteTelefono(rs.getString("cliente_telefono"));
                pedido.setUsuarioId(rs.getInt("usuario_id"));
                pedido.setFechaPedido(rs.getTimestamp("fecha_pedido"));
                pedido.setEstado(rs.getString("estado"));
                pedido.setTotal(rs.getBigDecimal("total"));
                pedido.setObservaciones(rs.getString("observaciones"));
                pedido.setUsuarioNombre(rs.getString("usuario_nombre"));
                return pedido;
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo pedido: " + e.getMessage());
        }
        return null;
    }
    
    public static boolean insertar(Pedido pedido) {
        String sql = "INSERT INTO pedidos (cliente_nombre, cliente_telefono, usuario_id, total, observaciones) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, pedido.getClienteNombre());
            pstmt.setString(2, pedido.getClienteTelefono());
            pstmt.setInt(3, pedido.getUsuarioId());
            pstmt.setBigDecimal(4, pedido.getTotal());
            pstmt.setString(5, pedido.getObservaciones());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error insertando pedido: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean actualizarEstado(int pedidoId, String estado) {
        String sql = "UPDATE pedidos SET estado = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, estado);
            pstmt.setInt(2, pedidoId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizando estado del pedido: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean actualizarPedido(Pedido pedido) {
        String sql = "UPDATE pedidos SET cliente_nombre = ?, cliente_telefono = ?, total = ?, observaciones = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, pedido.getClienteNombre());
            pstmt.setString(2, pedido.getClienteTelefono());
            pstmt.setBigDecimal(3, pedido.getTotal());
            pstmt.setString(4, pedido.getObservaciones());
            pstmt.setInt(5, pedido.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizando pedido: " + e.getMessage());
            return false;
        }
    }
    
    public static List<Pedido> buscarPorCliente(String nombreCliente) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.id, p.cliente_nombre, p.cliente_telefono, p.usuario_id, p.fecha_pedido, " +
                    "p.estado, p.total, p.observaciones, u.nombre as usuario_nombre " +
                    "FROM pedidos p " +
                    "LEFT JOIN usuarios u ON p.usuario_id = u.id " +
                    "WHERE p.cliente_nombre LIKE ? " +
                    "ORDER BY p.fecha_pedido DESC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + nombreCliente + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setId(rs.getInt("id"));
                pedido.setClienteNombre(rs.getString("cliente_nombre"));
                pedido.setClienteTelefono(rs.getString("cliente_telefono"));
                pedido.setUsuarioId(rs.getInt("usuario_id"));
                pedido.setFechaPedido(rs.getTimestamp("fecha_pedido"));
                pedido.setEstado(rs.getString("estado"));
                pedido.setTotal(rs.getBigDecimal("total"));
                pedido.setObservaciones(rs.getString("observaciones"));
                pedido.setUsuarioNombre(rs.getString("usuario_nombre"));
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            System.err.println("Error buscando pedidos: " + e.getMessage());
        }
        return pedidos;
    }
    
    public static int obtenerTotalPedidos() {
        String sql = "SELECT COUNT(*) as total FROM pedidos";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo total de pedidos: " + e.getMessage());
        }
        return 0;
    }
    
    public static double obtenerVentasTotales() {
        String sql = "SELECT COALESCE(SUM(total), 0) as ventas_totales FROM pedidos WHERE estado = 'COMPLETADO'";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble("ventas_totales");
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo ventas totales: " + e.getMessage());
        }
        return 0;
    }
    
    public static int obtenerCantidadPorEstado(String estado) {
        String sql = "SELECT COUNT(*) as cantidad FROM pedidos WHERE estado = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, estado);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("cantidad");
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo cantidad por estado: " + e.getMessage());
        }
        return 0;
    }
}