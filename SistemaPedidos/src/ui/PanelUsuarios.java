package ui;

import dao.UsuarioDAO;
import models.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelUsuarios extends JPanel {
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private final Usuario usuario;
    
    public PanelUsuarios(Usuario usuario) {
        this.usuario = usuario;
        setLayout(new BorderLayout());
        crearComponentes();
        cargarUsuarios();
    }
    
    private void crearComponentes() {
        // Solo mostrar si tiene permisos de gestión de usuarios
        if (!usuario.puedeGestionarUsuarios()) {
            JLabel lblAccesoDenegado = new JLabel("🚫 ACCESO DENEGADO - No tiene permisos para gestionar usuarios", SwingConstants.CENTER);
            lblAccesoDenegado.setFont(new Font("Arial", Font.BOLD, 16));
            lblAccesoDenegado.setForeground(Color.RED);
            add(lblAccesoDenegado, BorderLayout.CENTER);
            return;
        }
        
        // Panel superior
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelSuperior.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel("GESTIÓN DE USUARIOS (Solo Administradores)");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(52, 73, 94));
        
        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        add(panelSuperior, BorderLayout.NORTH);
        
        // Tabla de usuarios
        crearTabla();
    }
    
    private void crearTabla() {
        String[] columnas = {"ID", "Username", "Nombre", "Email", "Rol", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaUsuarios.setRowHeight(25);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Personalizar header
        tablaUsuarios.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaUsuarios.getTableHeader().setBackground(new Color(52, 73, 94));
        tablaUsuarios.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void cargarUsuarios() {
        modeloTabla.setRowCount(0);
        List<Usuario> usuarios = UsuarioDAO.obtenerTodos();
        
        for (Usuario usuario : usuarios) {
            String estado = usuario.isActivo() ? "✅ Activo" : "❌ Inactivo";
            
            Object[] fila = {
                usuario.getId(),
                usuario.getUsername(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getTextoRol(),
                estado
            };
            modeloTabla.addRow(fila);
        }
    }
}