package ui;

import models.Usuario;
import models.UsuarioSistema;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.List;

public class VentanaPrincipalPedidos extends JFrame {
    private Usuario usuario;
    private JPanel panelContenido;
    private CardLayout cardLayout;
    private JTable tablaUsuarios;
    private List<UsuarioSistema> listaUsuarios;
    
    public VentanaPrincipalPedidos(Usuario usuario) {
        this.usuario = usuario;
        this.listaUsuarios = new ArrayList<>();
        configurarVentana();
        crearComponentes();
    }
    
    private void configurarVentana() {
        setTitle("MAPU CORPORATION - Sistema de Gestión de Pedidos - " + usuario.getNombre() + " (" + usuario.getTextoRol() + ")");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    private void crearComponentes() {
        // Panel principal
        setLayout(new BorderLayout());
        
        // Barra superior
        JPanel barraSuperior = crearBarraSuperior();
        add(barraSuperior, BorderLayout.NORTH);
        
        // Menú lateral
        JPanel menuLateral = crearMenuLateral();
        add(menuLateral, BorderLayout.WEST);
        
        // Panel de contenido
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.setBackground(new Color(30, 30, 40)); // Fondo más oscuro
        
        // Crear paneles según permisos
        if (usuario.puedeGestionarProductos()) {
            panelContenido.add(new PanelProductos(usuario), "PRODUCTOS");
        }
        
        if (usuario.puedeGestionarPedidos()) {
            panelContenido.add(new PanelPedidos(usuario), "PEDIDOS");
        }
        
        if (usuario.puedeGestionarUsuarios()) {
            panelContenido.add(crearPanelUsuarios(), "USUARIOS");
        }
        
        // Panel de dashboard principal MEJORADO
        panelContenido.add(crearPanelDashboard(), "DASHBOARD");
        
        add(panelContenido, BorderLayout.CENTER);
        
        // Mostrar dashboard por defecto
        cardLayout.show(panelContenido, "DASHBOARD");
    }
    
    private JPanel crearBarraSuperior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(new Color(0, 51, 102));
        barra.setPreferredSize(new Dimension(1000, 70));
        barra.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Logo y título
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTitulo.setBackground(new Color(0, 51, 102));
        
        JLabel lblIcono = new JLabel("🏢");
        lblIcono.setFont(new Font("Arial", Font.PLAIN, 24));
        lblIcono.setForeground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel("MAPU CORPORATION - Sistema de Gestión");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        
        panelTitulo.add(lblIcono);
        panelTitulo.add(Box.createHorizontalStrut(10));
        panelTitulo.add(lblTitulo);
        
        // Información del usuario
        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelUsuario.setBackground(new Color(0, 51, 102));
        
        JLabel lblUsuario = new JLabel(usuario.getNombre() + " - " + usuario.getTextoRol());
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUsuario.setForeground(Color.WHITE);
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Arial", Font.PLAIN, 12));
        btnCerrarSesion.setBackground(new Color(204, 0, 0));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        
        panelUsuario.add(lblUsuario);
        panelUsuario.add(Box.createHorizontalStrut(15));
        panelUsuario.add(btnCerrarSesion);
        
        barra.add(panelTitulo, BorderLayout.WEST);
        barra.add(panelUsuario, BorderLayout.EAST);
        
        return barra;
    }
    
    private JPanel crearMenuLateral() {
        JPanel menu = new JPanel();
        menu.setBackground(new Color(44, 62, 80));
        menu.setPreferredSize(new Dimension(250, 0));
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        
        // Espacio superior
        menu.add(Box.createVerticalStrut(30));
        
        // Botón Dashboard
        JButton btnDashboard = crearBotonMenu("DASHBOARD");
        btnDashboard.addActionListener(e -> cardLayout.show(panelContenido, "DASHBOARD"));
        menu.add(btnDashboard);
        menu.add(Box.createVerticalStrut(15));
        
        // Botones del menú según permisos
        if (usuario.puedeGestionarPedidos()) {
            JButton btnPedidos = crearBotonMenu("GESTIÓN DE PEDIDOS");
            btnPedidos.addActionListener(e -> cardLayout.show(panelContenido, "PEDIDOS"));
            menu.add(btnPedidos);
            menu.add(Box.createVerticalStrut(15));
        }
        
        if (usuario.puedeGestionarProductos()) {
            JButton btnProductos = crearBotonMenu("GESTIÓN DE PRODUCTOS");
            btnProductos.addActionListener(e -> cardLayout.show(panelContenido, "PRODUCTOS"));
            menu.add(btnProductos);
            menu.add(Box.createVerticalStrut(15));
        }
        
        if (usuario.puedeGestionarUsuarios()) {
            JButton btnUsuarios = crearBotonMenu(" GESTIÓN DE USUARIOS");
            btnUsuarios.addActionListener(e -> cardLayout.show(panelContenido, "USUARIOS"));
            menu.add(btnUsuarios);
            menu.add(Box.createVerticalStrut(15));
        }
        
        // Espacio flexible
        menu.add(Box.createVerticalGlue());
        
        // Botón salir
        JButton btnSalir = crearBotonMenu("🚪 SALIR DEL SISTEMA");
        btnSalir.setBackground(new Color(204, 0, 0));
        btnSalir.addActionListener(e -> confirmarSalida());
        menu.add(btnSalir);
        menu.add(Box.createVerticalStrut(20));
        
        return menu;
    }
    
    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(220, 50));
        boton.setPreferredSize(new Dimension(220, 50));
        boton.setFont(new Font("Arial", Font.BOLD, 13));
        boton.setBackground(new Color(41, 128, 185));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover
        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(new Color(52, 152, 219));
            }
            public void mouseExited(MouseEvent e) {
                boton.setBackground(new Color(41, 128, 185));
            }
        });
        
        return boton;
    }
private JPanel crearPanelDashboard() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(new Color(20, 20, 30));
    panel.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Título principal con gradiente
    JLabel lblTitulo = new JLabel("DASHBOARD PRINCIPAL", SwingConstants.CENTER);
    lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
    lblTitulo.setForeground(Color.WHITE);
    lblTitulo.setBorder(BorderFactory.createCompoundBorder(
        new EmptyBorder(0, 0, 30, 0),
        new MatteBorder(0, 0, 2, 0, new Color(41, 128, 185))
    ));

    // Panel de estadísticas principal
    JPanel panelStats = new JPanel(new BorderLayout());
    panelStats.setBackground(new Color(20, 20, 30));
    
    // Panel superior con tarjetas de estadísticas
    JPanel panelTarjetas = new JPanel(new GridLayout(1, 3, 20, 20));
    panelTarjetas.setBackground(new Color(20, 20, 30));
    panelTarjetas.setBorder(new EmptyBorder(0, 0, 20, 0));

    // Guardar referencia a esta instancia para usar en los listeners
    VentanaPrincipalPedidos ventanaActual = this;
    
    // Crear tarjetas mejoradas
    panelTarjetas.add(crearTarjetaEstadisticaMejorada("📦", "Total Productos", "125", 
        new Color(41, 128, 185), new Color(52, 152, 219), "PRODUCTOS", ventanaActual));
    panelTarjetas.add(crearTarjetaEstadisticaMejorada("📋", "Pedidos Activos", "24", 
        new Color(39, 174, 96), new Color(46, 204, 113), "PEDIDOS", ventanaActual));
    panelTarjetas.add(crearTarjetaEstadisticaMejorada("👥", "Usuarios Registrados", "15", 
        new Color(243, 156, 18), new Color(255, 168, 36), "USUARIOS", ventanaActual));

    // Panel inferior con gráfico y estadísticas adicionales
    JPanel panelInferior = new JPanel(new GridLayout(1, 2, 20, 20));
    panelInferior.setBackground(new Color(20, 20, 30));

    // Gráfico circular mejorado
    panelInferior.add(crearPanelGraficoCircularMejorado());
    
    // Panel de actividad reciente
    panelInferior.add(crearPanelActividadReciente());

    // Agregar todos los componentes
    panelStats.add(panelTarjetas, BorderLayout.NORTH);
    panelStats.add(panelInferior, BorderLayout.CENTER);
    
    panel.add(lblTitulo, BorderLayout.NORTH);
    panel.add(panelStats, BorderLayout.CENTER);

    return panel;
}

private JPanel crearTarjetaEstadisticaMejorada(String icono, String titulo, String valor, 
                                              Color colorBase, Color colorHover, String destino,
                                              VentanaPrincipalPedidos ventana) {
    JPanel tarjeta = new JPanel(new BorderLayout());
    tarjeta.setBackground(new Color(35, 35, 45));
    
    // Efecto de sombra y elevación
    tarjeta.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createCompoundBorder(
            new LineBorder(new Color(60, 60, 70), 1),
            new EmptyBorder(2, 2, 2, 2)
        ),
        new EmptyBorder(18, 18, 13, 18)
    ));

    // Panel del icono con fondo circular
    JPanel panelIcono = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Fondo circular del icono
            GradientPaint gradient = new GradientPaint(0, 0, colorBase, getWidth(), getHeight(), colorHover);
            g2d.setPaint(gradient);
            g2d.fillOval(0, 0, getWidth(), getHeight());
        }
    };
    panelIcono.setPreferredSize(new Dimension(60, 60));
    panelIcono.setLayout(new BorderLayout());
    panelIcono.setOpaque(false);

    JLabel lblIcono = new JLabel(icono, SwingConstants.CENTER);
    lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
    lblIcono.setForeground(Color.WHITE);
    panelIcono.add(lblIcono, BorderLayout.CENTER);

    // Panel de contenido
    JPanel panelContenidoTarjeta = new JPanel(new BorderLayout(10, 10));
    panelContenidoTarjeta.setBackground(new Color(35, 35, 45));
    panelContenidoTarjeta.setBorder(new EmptyBorder(10, 0, 0, 0));

    JLabel lblTitulo = new JLabel(titulo, SwingConstants.LEFT);
    lblTitulo.setFont(new Font("Arial", Font.PLAIN, 14));
    lblTitulo.setForeground(new Color(200, 200, 200));

    JLabel lblValor = new JLabel(valor, SwingConstants.LEFT);
    lblValor.setFont(new Font("Arial", Font.BOLD, 28));
    lblValor.setForeground(Color.WHITE);

    // Botón de acción mejorado - CORREGIDO: usar referencia explícita
    JButton btnVerDetalles = new JButton("Ver Detalles →");
    btnVerDetalles.setFont(new Font("Arial", Font.BOLD, 12));
    btnVerDetalles.setBackground(colorBase);
    btnVerDetalles.setForeground(Color.WHITE);
    btnVerDetalles.setFocusPainted(false);
    btnVerDetalles.setBorder(BorderFactory.createCompoundBorder(
        new LineBorder(colorBase.darker(), 1),
        new EmptyBorder(10, 20, 10, 20)
    ));
    btnVerDetalles.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
    // CORREGIDO: Usar referencia explícita a la ventana
    btnVerDetalles.addActionListener(e -> {
        if (destino.equals("PRODUCTOS") && ventana.usuario.puedeGestionarProductos()) {
            ventana.cardLayout.show(ventana.panelContenido, "PRODUCTOS");
        } else if (destino.equals("PEDIDOS") && ventana.usuario.puedeGestionarPedidos()) {
            ventana.cardLayout.show(ventana.panelContenido, "PEDIDOS");
        } else if (destino.equals("USUARIOS") && ventana.usuario.puedeGestionarUsuarios()) {
            ventana.cardLayout.show(ventana.panelContenido, "USUARIOS");
        } else {
            JOptionPane.showMessageDialog(ventana,
                "No tiene permisos para acceder a esta sección.",
                "Acceso Denegado",
                JOptionPane.WARNING_MESSAGE);
        }
    });

    // Efectos hover mejorados
    btnVerDetalles.addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            btnVerDetalles.setBackground(colorHover);
            btnVerDetalles.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(colorHover.darker(), 1),
                new EmptyBorder(10, 20, 10, 20)
            ));
            tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                    new LineBorder(colorHover, 2),
                    new EmptyBorder(1, 1, 1, 1)
                ),
                new EmptyBorder(17, 17, 12, 17)
            ));
        }
        public void mouseExited(MouseEvent e) {
            btnVerDetalles.setBackground(colorBase);
            btnVerDetalles.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(colorBase.darker(), 1),
                new EmptyBorder(10, 20, 10, 20)
            ));
            tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(60, 60, 70), 1),
                    new EmptyBorder(2, 2, 2, 2)
                ),
                new EmptyBorder(18, 18, 13, 18)
            ));
        }
    });

    // Deshabilitar botón si no tiene permisos
    if ((destino.equals("PRODUCTOS") && !ventana.usuario.puedeGestionarProductos()) ||
        (destino.equals("PEDIDOS") && !ventana.usuario.puedeGestionarPedidos()) ||
        (destino.equals("USUARIOS") && !ventana.usuario.puedeGestionarUsuarios())) {
        btnVerDetalles.setEnabled(false);
        btnVerDetalles.setBackground(Color.GRAY);
        btnVerDetalles.setForeground(new Color(150, 150, 150));
        btnVerDetalles.setToolTipText("No tiene permisos para acceder a esta sección");
    }

    // Agregar componentes
    panelContenidoTarjeta.add(lblTitulo, BorderLayout.NORTH);
    panelContenidoTarjeta.add(lblValor, BorderLayout.CENTER);
    panelContenidoTarjeta.add(btnVerDetalles, BorderLayout.SOUTH);

    tarjeta.add(panelIcono, BorderLayout.NORTH);
    tarjeta.add(panelContenidoTarjeta, BorderLayout.CENTER);

    return tarjeta;
}

   
    private JPanel crearTarjetaEstadisticaMejorada(String icono, String titulo, String valor, Color colorBase, Color colorHover, String destino) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(new Color(35, 35, 45));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(60, 60, 70), 1),
            new EmptyBorder(20, 20, 15, 20)
        ));
        
        // Efecto de sombra y elevación
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                new LineBorder(new Color(60, 60, 70), 1),
                new EmptyBorder(2, 2, 2, 2)
            ),
            new EmptyBorder(18, 18, 13, 18)
        ));

        // Panel del icono con fondo circular
        JPanel panelIcono = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo circular del icono
                GradientPaint gradient = new GradientPaint(0, 0, colorBase, getWidth(), getHeight(), colorHover);
                g2d.setPaint(gradient);
                g2d.fillOval(0, 0, getWidth(), getHeight());
            }
        };
        panelIcono.setPreferredSize(new Dimension(60, 60));
        panelIcono.setLayout(new BorderLayout());
        panelIcono.setOpaque(false);

        JLabel lblIcono = new JLabel(icono, SwingConstants.CENTER);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        lblIcono.setForeground(Color.WHITE);
        panelIcono.add(lblIcono, BorderLayout.CENTER);

        // Panel de contenido
        JPanel panelContenido = new JPanel(new BorderLayout(10, 10));
        panelContenido.setBackground(new Color(35, 35, 45));
        panelContenido.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.LEFT);
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTitulo.setForeground(new Color(200, 200, 200));

        JLabel lblValor = new JLabel(valor, SwingConstants.LEFT);
        lblValor.setFont(new Font("Arial", Font.BOLD, 28));
        lblValor.setForeground(Color.WHITE);

        // Botón de acción mejorado
        JButton btnVerDetalles = new JButton("Ver Detalles →");
        btnVerDetalles.setFont(new Font("Arial", Font.BOLD, 12));
        btnVerDetalles.setBackground(colorBase);
        btnVerDetalles.setForeground(Color.WHITE);
        btnVerDetalles.setFocusPainted(false);
        btnVerDetalles.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(colorBase.darker(), 1),
            new EmptyBorder(10, 20, 10, 20)
        ));
        btnVerDetalles.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efectos hover mejorados
        btnVerDetalles.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnVerDetalles.setBackground(colorHover);
                btnVerDetalles.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(colorHover.darker(), 1),
                    new EmptyBorder(10, 20, 10, 20)
                ));
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createCompoundBorder(
                        new LineBorder(colorHover, 2),
                        new EmptyBorder(1, 1, 1, 1)
                    ),
                    new EmptyBorder(17, 17, 12, 17)
                ));
            }
            public void mouseExited(MouseEvent e) {
                btnVerDetalles.setBackground(colorBase);
                btnVerDetalles.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(colorBase.darker(), 1),
                    new EmptyBorder(10, 20, 10, 20)
                ));
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(60, 60, 70), 1),
                        new EmptyBorder(2, 2, 2, 2)
                    ),
                    new EmptyBorder(18, 18, 13, 18)
                ));
            }
        });

        // Acción del botón
        btnVerDetalles.addActionListener(e -> {
            if (destino.equals("PRODUCTOS") && usuario.puedeGestionarProductos()) {
                cardLayout.show(panelContenido, "PRODUCTOS");
            } else if (destino.equals("PEDIDOS") && usuario.puedeGestionarPedidos()) {
                cardLayout.show(panelContenido, "PEDIDOS");
            } else if (destino.equals("USUARIOS") && usuario.puedeGestionarUsuarios()) {
                cardLayout.show(panelContenido, "USUARIOS");
            } else {
                JOptionPane.showMessageDialog(VentanaPrincipalPedidos.this,
                    "No tiene permisos para acceder a esta sección.",
                    "Acceso Denegado",
                    JOptionPane.WARNING_MESSAGE);
            }
        });

        // Deshabilitar botón si no tiene permisos
        if ((destino.equals("PRODUCTOS") && !usuario.puedeGestionarProductos()) ||
            (destino.equals("PEDIDOS") && !usuario.puedeGestionarPedidos()) ||
            (destino.equals("USUARIOS") && !usuario.puedeGestionarUsuarios())) {
            btnVerDetalles.setEnabled(false);
            btnVerDetalles.setBackground(Color.GRAY);
            btnVerDetalles.setForeground(new Color(150, 150, 150));
            btnVerDetalles.setToolTipText("No tiene permisos para acceder a esta sección");
        }

        // Agregar componentes
        panelContenido.add(lblTitulo, BorderLayout.NORTH);
        panelContenido.add(lblValor, BorderLayout.CENTER);
        panelContenido.add(btnVerDetalles, BorderLayout.SOUTH);

        tarjeta.add(panelIcono, BorderLayout.NORTH);
        tarjeta.add(panelContenido, BorderLayout.CENTER);

        return tarjeta;
    }
    
    private JPanel crearPanelGraficoCircularMejorado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(35, 35, 45));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(60, 60, 70), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitulo = new JLabel("📊 DISTRIBUCIÓN DEL SISTEMA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Panel del gráfico con mejoras visuales
        JPanel panelGrafico = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                int diameter = Math.min(width, height) - 60;
                int x = (width - diameter) / 2;
                int y = (height - diameter) / 2;
                
                // Datos para el gráfico circular
                int usuarios = 15;
                int pedidos = 24;
                int productos = 125;
                int total = usuarios + pedidos + productos;
                
                // Ángulos
                double anguloUsuarios = 360.0 * usuarios / total;
                double anguloPedidos = 360.0 * pedidos / total;
                double anguloProductos = 360.0 * productos / total;
                
                // Dibujar segmento de productos
                GradientPaint gradientProductos = new GradientPaint(x, y, new Color(41, 128, 185), 
                    x + diameter, y + diameter, new Color(52, 152, 219));
                g2d.setPaint(gradientProductos);
                g2d.fill(new Arc2D.Double(x, y, diameter, diameter, 0, anguloProductos, Arc2D.PIE));
                
                // Dibujar segmento de pedidos
                GradientPaint gradientPedidos = new GradientPaint(x, y, new Color(39, 174, 96), 
                    x + diameter, y + diameter, new Color(46, 204, 113));
                g2d.setPaint(gradientPedidos);
                g2d.fill(new Arc2D.Double(x, y, diameter, diameter, anguloProductos, anguloPedidos, Arc2D.PIE));
                
                // Dibujar segmento de usuarios
                GradientPaint gradientUsuarios = new GradientPaint(x, y, new Color(243, 156, 18), 
                    x + diameter, y + diameter, new Color(255, 168, 36));
                g2d.setPaint(gradientUsuarios);
                g2d.fill(new Arc2D.Double(x, y, diameter, diameter, anguloProductos + anguloPedidos, anguloUsuarios, Arc2D.PIE));
                
                // Dibujar borde del gráfico
                g2d.setColor(new Color(80, 80, 100));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawOval(x, y, diameter, diameter);
                
                // Texto en el centro con efecto
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                String textoTotal = "Total: " + total;
                FontMetrics fm = g2d.getFontMetrics();
                int textoWidth = fm.stringWidth(textoTotal);
                g2d.drawString(textoTotal, (width - textoWidth) / 2, height / 2);
                
                // Sombra del texto
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.drawString(textoTotal, (width - textoWidth) / 2 + 1, height / 2 + 1);
            }
        };
        
        panelGrafico.setBackground(new Color(35, 35, 45));
        panelGrafico.setPreferredSize(new Dimension(250, 250));

        // Leyenda mejorada
        JPanel panelLeyenda = new JPanel(new GridLayout(3, 1, 8, 8));
        panelLeyenda.setBackground(new Color(35, 35, 45));
        panelLeyenda.setBorder(new EmptyBorder(15, 20, 0, 20));

        JLabel lblProductos = crearItemLeyenda("● Productos: 125 (76%)", new Color(41, 128, 185));
        JLabel lblPedidos = crearItemLeyenda("● Pedidos: 24 (15%)", new Color(39, 174, 96));
        JLabel lblUsuarios = crearItemLeyenda("● Usuarios: 15 (9%)", new Color(243, 156, 18));

        panelLeyenda.add(lblProductos);
        panelLeyenda.add(lblPedidos);
        panelLeyenda.add(lblUsuarios);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(panelGrafico, BorderLayout.CENTER);
        panel.add(panelLeyenda, BorderLayout.SOUTH);

        return panel;
    }
    
    private JLabel crearItemLeyenda(String texto, Color color) {
        JLabel label = new JLabel(texto);
        label.setForeground(color);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setBorder(new EmptyBorder(5, 10, 5, 10));
        return label;
    }
    
    private JPanel crearPanelActividadReciente() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(35, 35, 45));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(60, 60, 70), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitulo = new JLabel("📈 ACTIVIDAD RECIENTE", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Lista de actividad reciente
        String[] actividades = {
            "🔄 Nuevo pedido #00125 procesado",
            "✅ Usuario 'maria_g' actualizado",
            "📦 Stock de producto 'Widget X' ajustado",
            "👤 Nuevo usuario registrado: 'carlos_m'",
            "⚠️ Alerta: Stock bajo en 'Componente Y'",
            "📊 Reporte mensual generado",
            "🔄 Pedido #00124 completado",
            "✅ Configuración del sistema actualizada"
        };

        JList<String> listaActividad = new JList<>(actividades);
        listaActividad.setBackground(new Color(45, 45, 55));
        listaActividad.setForeground(Color.WHITE);
        listaActividad.setFont(new Font("Arial", Font.PLAIN, 12));
        listaActividad.setSelectionBackground(new Color(41, 128, 185));
        listaActividad.setSelectionForeground(Color.WHITE);
        listaActividad.setBorder(new EmptyBorder(10, 10, 10, 10));
        listaActividad.setFixedCellHeight(35);

        // Renderer personalizado para la lista
        listaActividad.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(new EmptyBorder(5, 10, 5, 10));
                setBackground(isSelected ? new Color(41, 128, 185) : new Color(45, 45, 55));
                setForeground(isSelected ? Color.WHITE : new Color(220, 220, 220));
                setFont(new Font("Arial", Font.PLAIN, 12));
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(listaActividad);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 70)));
        scrollPane.getViewport().setBackground(new Color(45, 45, 55));

        // Panel de estadísticas rápidas
        JPanel panelStats = new JPanel(new GridLayout(2, 2, 10, 10));
        panelStats.setBackground(new Color(35, 35, 45));
        panelStats.setBorder(new EmptyBorder(15, 0, 0, 0));

        panelStats.add(crearMiniEstadistica("Hoy", "8", new Color(41, 128, 185)));
        panelStats.add(crearMiniEstadistica("Esta semana", "42", new Color(39, 174, 96)));
        panelStats.add(crearMiniEstadistica("Este mes", "156", new Color(243, 156, 18)));
        panelStats.add(crearMiniEstadistica("Total", "892", new Color(155, 89, 182)));

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelStats, BorderLayout.SOUTH);

        return panel;
    }
    
    private JPanel crearMiniEstadistica(String titulo, String valor, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(45, 45, 55));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 11));
        lblTitulo.setForeground(new Color(180, 180, 180));

        JLabel lblValor = new JLabel(valor, SwingConstants.CENTER);
        lblValor.setFont(new Font("Arial", Font.BOLD, 16));
        lblValor.setForeground(color);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblValor, BorderLayout.CENTER);

        return panel;
    }

    // Mantener el método original para compatibilidad
    private JPanel crearTarjetaEstadistica(String icono, String titulo, String valor, Color color) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(new Color(45, 45, 55));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color, 2),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel lblIcono = new JLabel(icono, SwingConstants.CENTER);
        lblIcono.setFont(new Font("Arial", Font.PLAIN, 36));
        lblIcono.setForeground(color);
        
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(Color.WHITE);
        
        JLabel lblValor = new JLabel(valor, SwingConstants.CENTER);
        lblValor.setFont(new Font("Arial", Font.BOLD, 20));
        lblValor.setForeground(color);
        
        JPanel panelContenido = new JPanel(new BorderLayout(0, 10));
        panelContenido.setBackground(new Color(45, 45, 55));
        panelContenido.add(lblIcono, BorderLayout.NORTH);
        panelContenido.add(lblTitulo, BorderLayout.CENTER);
        panelContenido.add(lblValor, BorderLayout.SOUTH);
        
        tarjeta.add(panelContenido, BorderLayout.CENTER);
        
        return tarjeta;
    }
    
    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(new Color(25, 25, 35)); // Fondo más oscuro para gestión de usuarios
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel lblTitulo = new JLabel("GESTIÓN DE USUARIOS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Panel de búsqueda y filtros
        JPanel panelBusqueda = crearPanelBusqueda();
        
        // Tabla de usuarios
        JScrollPane scrollPane = crearTablaUsuarios();
        
        // Panel de botones de acción
        JPanel panelAcciones = crearPanelAcciones();
        
        // Agregar componentes al panel principal
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(panelBusqueda, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelAcciones, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(new Color(35, 35, 45));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(80, 80, 100), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblBusqueda = new JLabel("Buscar usuario:");
        lblBusqueda.setFont(new Font("Arial", Font.BOLD, 12));
        lblBusqueda.setForeground(Color.WHITE);
        
        JTextField txtBusqueda = new JTextField(25);
        txtBusqueda.setFont(new Font("Arial", Font.PLAIN, 14));
        txtBusqueda.setPreferredSize(new Dimension(200, 35));
        txtBusqueda.setBackground(new Color(60, 60, 70));
        txtBusqueda.setForeground(Color.WHITE);
        txtBusqueda.setCaretColor(Color.WHITE);
        txtBusqueda.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(100, 100, 120), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        
        JButton btnBuscar = crearBotonBusqueda("🔍 Buscar", new Color(41, 128, 185));
        JButton btnLimpiar = crearBotonBusqueda("Limpiar", new Color(108, 117, 125));
        
        panel.add(lblBusqueda);
        panel.add(txtBusqueda);
        panel.add(btnBuscar);
        panel.add(btnLimpiar);
        
        return panel;
    }
    
    private JButton crearBotonBusqueda(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        boton.setPreferredSize(new Dimension(120, 35));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                boton.setBackground(color);
            }
        });
        
        return boton;
    }
    
    private JScrollPane crearTablaUsuarios() {
        inicializarUsuariosEjemplo();
        
        String[] columnas = {"ID", "Usuario", "Nombre", "Rol", "Estado", "Último Acceso", "Acciones"};
        
        // Crear tabla con modelo personalizado
        tablaUsuarios = new JTable(new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Solo la columna de acciones es editable
            }
        });
        
        // Llenar la tabla con datos
        DefaultTableModel model = (DefaultTableModel) tablaUsuarios.getModel();
        for (UsuarioSistema user : listaUsuarios) {
            model.addRow(new Object[]{
                user.getId(),
                user.getUsuario(),
                user.getNombre(),
                user.getRol(),
                user.getEstado(),
                user.getUltimoAcceso(),
                ""
            });
        }

        // Configurar apariencia de la tabla para tema oscuro
        tablaUsuarios.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaUsuarios.setBackground(new Color(45, 45, 55));
        tablaUsuarios.setForeground(Color.WHITE);
        tablaUsuarios.setGridColor(new Color(80, 80, 100));
        tablaUsuarios.setSelectionBackground(new Color(41, 128, 185));
        tablaUsuarios.setSelectionForeground(Color.WHITE);
        
        // Configurar header de la tabla
        tablaUsuarios.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaUsuarios.getTableHeader().setBackground(new Color(60, 60, 70));
        tablaUsuarios.getTableHeader().setForeground(Color.WHITE);
        
        tablaUsuarios.setRowHeight(40);
        
        // Configurar ancho de columnas
        tablaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(60);  // ID
        tablaUsuarios.getColumnModel().getColumn(1).setPreferredWidth(100); // Usuario
        tablaUsuarios.getColumnModel().getColumn(2).setPreferredWidth(200); // Nombre
        tablaUsuarios.getColumnModel().getColumn(3).setPreferredWidth(120); // Rol
        tablaUsuarios.getColumnModel().getColumn(4).setPreferredWidth(80);  // Estado
        tablaUsuarios.getColumnModel().getColumn(5).setPreferredWidth(150); // Último Acceso
        tablaUsuarios.getColumnModel().getColumn(6).setPreferredWidth(250); // Acciones
        
        // Agregar botones de acción a la tabla
        tablaUsuarios.getColumnModel().getColumn(6).setCellRenderer(new BotonesTablaRenderer());
        tablaUsuarios.getColumnModel().getColumn(6).setCellEditor(new BotonesTablaEditor());
        
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(80, 80, 100), 1),
            new EmptyBorder(10, 0, 10, 0)
        ));
        scrollPane.getViewport().setBackground(new Color(45, 45, 55));
        
        return scrollPane;
    }
    
    private void inicializarUsuariosEjemplo() {
        listaUsuarios.clear();
        listaUsuarios.add(new UsuarioSistema(1, "admbr", "Administrador Principal", "Administrador", "Activo", "2024-01-15 10:30"));
        listaUsuarios.add(new UsuarioSistema(2, "usando", "Usuario Standard", "Usuario", "Activo", "2024-01-14 14:22"));
        listaUsuarios.add(new UsuarioSistema(3, "opensador", "Operador Sistema", "Operador", "Inactivo", "2024-01-10 09:15"));
        listaUsuarios.add(new UsuarioSistema(4, "supervisor1", "Supervisor Turno", "Supervisor", "Activo", "2024-01-15 08:45"));
        listaUsuarios.add(new UsuarioSistema(5, "usuario2", "Usuario Secundario", "Usuario", "Activo", "2024-01-13 16:30"));
    }
    
    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panel.setBackground(new Color(25, 25, 35));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 0, 0, 0, new Color(80, 80, 100)),
            new EmptyBorder(15, 0, 0, 0)
        ));
        
        JButton btnNuevoUsuario = crearBotonAccion("➕ Nuevo Usuario", new Color(39, 174, 96));
        JButton btnExportar = crearBotonAccion("📊 Exportar Reporte", new Color(41, 128, 185));
        JButton btnActualizar = crearBotonAccion("🔄 Actualizar Lista", new Color(243, 156, 18));
        JButton btnEstadisticas = crearBotonAccion("📈 Estadísticas", new Color(155, 89, 182));
        
        panel.add(btnNuevoUsuario);
        panel.add(btnExportar);
        panel.add(btnActualizar);
        panel.add(btnEstadisticas);
        
        return panel;
    }
    
    private JButton crearBotonAccion(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color.darker(), 1),
            new EmptyBorder(12, 20, 12, 20)
        ));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                boton.setBackground(color);
            }
        });
        
        return boton;
    }
    
    // Renderer para botones en la tabla - MEJORADO
    private class BotonesTablaRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private JPanel panelBotones;
        
        public BotonesTablaRenderer() {
            setLayout(new BorderLayout());
            setBackground(new Color(45, 45, 55));
            
            panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
            panelBotones.setBackground(new Color(45, 45, 55));
            panelBotones.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            // Crear botones de ejemplo para el renderer
            JButton btn1 = crearBotonTabla("🔄 Rol", new Color(41, 128, 185));
            JButton btn2 = crearBotonTabla("✏️ Editar", new Color(243, 156, 18));
            JButton btn3 = crearBotonTabla("✅ Habilitar", new Color(39, 174, 96));
            
            panelBotones.add(btn1);
            panelBotones.add(btn2);
            panelBotones.add(btn3);
            
            add(panelBotones, BorderLayout.CENTER);
        }
        
        private JButton crearBotonTabla(String texto, Color color) {
            JButton boton = new JButton(texto);
            boton.setFont(new Font("Arial", Font.PLAIN, 11));
            boton.setBackground(color);
            boton.setForeground(Color.WHITE);
            boton.setFocusPainted(false);
            boton.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
            boton.setPreferredSize(new Dimension(80, 30));
            return boton;
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                                                     boolean isSelected, boolean hasFocus, 
                                                     int row, int column) {
            return this;
        }
    }
    
    // Editor para botones en la tabla - MEJORADO
    private class BotonesTablaEditor extends AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private JPanel panel;
        private JButton btnCambiarRol, btnEditar, btnHabilitar;
        private int currentRow;
        
        public BotonesTablaEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
            panel.setBackground(new Color(45, 45, 55));
            panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            btnCambiarRol = crearBotonEditor("🔄 Rol", new Color(41, 128, 185));
            btnEditar = crearBotonEditor("✏️ Editar", new Color(243, 156, 18));
            btnHabilitar = crearBotonEditor("✅ Habilitar", new Color(39, 174, 96));
            
            // Acciones de los botones
            btnCambiarRol.addActionListener(e -> {
                cambiarRolUsuario(currentRow);
                fireEditingStopped();
            });
            
            btnEditar.addActionListener(e -> {
                editarUsuario(currentRow);
                fireEditingStopped();
            });
            
            btnHabilitar.addActionListener(e -> {
                habilitarUsuario(currentRow);
                fireEditingStopped();
            });
            
            panel.add(btnCambiarRol);
            panel.add(btnEditar);
            panel.add(btnHabilitar);
        }
        
        private JButton crearBotonEditor(String texto, Color color) {
            JButton boton = new JButton(texto);
            boton.setFont(new Font("Arial", Font.PLAIN, 11));
            boton.setBackground(color);
            boton.setForeground(Color.WHITE);
            boton.setFocusPainted(false);
            boton.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
            boton.setPreferredSize(new Dimension(80, 30));
            boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Efecto hover
            boton.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    boton.setBackground(color.brighter());
                }
                public void mouseExited(MouseEvent e) {
                    boton.setBackground(color);
                }
            });
            
            return boton;
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                   boolean isSelected, int row, int column) {
            currentRow = row;
            
            // Actualizar texto del botón según el estado
            String estado = (String) table.getValueAt(row, 4);
            btnHabilitar.setText(estado.equals("Activo") ? "❌ Desactivar" : "✅ Activar");
            btnHabilitar.setBackground(estado.equals("Activo") ? new Color(231, 76, 60) : new Color(39, 174, 96));
            
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
    
    private void cambiarRolUsuario(int fila) {
        if (fila >= 0 && fila < listaUsuarios.size()) {
            UsuarioSistema usuario = listaUsuarios.get(fila);
            String[] roles = {"Administrador", "Supervisor", "Usuario", "Operador", "Invitado"};
            String rolActual = usuario.getRol();
            
            String rolSeleccionado = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione el nuevo rol para el usuario " + usuario.getNombre() + ":",
                "Cambiar Rol de Usuario",
                JOptionPane.QUESTION_MESSAGE,
                null,
                roles,
                rolActual
            );
            
            if (rolSeleccionado != null) {
                // Actualizar el objeto en la lista
                usuario.setRol(rolSeleccionado);
                
                // Actualizar la tabla
                DefaultTableModel model = (DefaultTableModel) tablaUsuarios.getModel();
                model.setValueAt(rolSeleccionado, fila, 3);
                
                JOptionPane.showMessageDialog(this,
                    "<html><b>Rol actualizado exitosamente</b><br>" +
                    "El usuario " + usuario.getNombre() + " ahora tiene el rol: <b>" + rolSeleccionado + "</b></html>",
                    "Cambio de Rol Exitoso",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void editarUsuario(int fila) {
        if (fila >= 0 && fila < listaUsuarios.size()) {
            UsuarioSistema usuarioEditar = listaUsuarios.get(fila);
            
            JPanel panelEdicion = new JPanel(new GridLayout(4, 2, 10, 10));
            panelEdicion.setBorder(new EmptyBorder(10, 10, 10, 10));
            panelEdicion.setBackground(new Color(45, 45, 55));
            
            JTextField txtUsuario = new JTextField(usuarioEditar.getUsuario());
            JTextField txtNombre = new JTextField(usuarioEditar.getNombre());
            JTextField txtEmail = new JTextField(usuarioEditar.getUsuario() + "@mapucorp.com");
            
            // Configurar campos para tema oscuro
            configurarCampoTexto(txtUsuario);
            configurarCampoTexto(txtNombre);
            configurarCampoTexto(txtEmail);
            
            JLabel lblUsuario = new JLabel("Usuario:");
            JLabel lblNombre = new JLabel("Nombre:");
            JLabel lblEmail = new JLabel("Email:");
            JLabel lblRol = new JLabel("Rol:");
            
            lblUsuario.setForeground(Color.WHITE);
            lblNombre.setForeground(Color.WHITE);
            lblEmail.setForeground(Color.WHITE);
            lblRol.setForeground(Color.WHITE);
            
            panelEdicion.add(lblUsuario);
            panelEdicion.add(txtUsuario);
            panelEdicion.add(lblNombre);
            panelEdicion.add(txtNombre);
            panelEdicion.add(lblEmail);
            panelEdicion.add(txtEmail);
            panelEdicion.add(lblRol);
            
            String[] roles = {"Administrador", "Supervisor", "Usuario", "Operador"};
            JComboBox<String> cmbRol = new JComboBox<>(roles);
            cmbRol.setSelectedItem(usuarioEditar.getRol());
            configurarComboBox(cmbRol);
            panelEdicion.add(cmbRol);
            
            int resultado = JOptionPane.showConfirmDialog(this,
                panelEdicion,
                "Editar Usuario - " + usuarioEditar.getNombre(),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
                
            if (resultado == JOptionPane.OK_OPTION) {
                // Actualizar el objeto en la lista
                usuarioEditar.setUsuario(txtUsuario.getText());
                usuarioEditar.setNombre(txtNombre.getText());
                usuarioEditar.setRol(cmbRol.getSelectedItem().toString());
                
                // Actualizar la tabla
                DefaultTableModel model = (DefaultTableModel) tablaUsuarios.getModel();
                model.setValueAt(txtUsuario.getText(), fila, 1);
                model.setValueAt(txtNombre.getText(), fila, 2);
                model.setValueAt(cmbRol.getSelectedItem().toString(), fila, 3);
                
                JOptionPane.showMessageDialog(this,
                    "<html><b>Usuario editado exitosamente</b><br>" +
                    "Usuario: " + txtUsuario.getText() + "<br>" +
                    "Nombre: " + txtNombre.getText() + "<br>" +
                    "Rol: " + cmbRol.getSelectedItem().toString() + "</html>",
                    "Edición Completada",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void configurarCampoTexto(JTextField campo) {
        campo.setBackground(new Color(60, 60, 70));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(100, 100, 120), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
    }
    
    private void configurarComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(new Color(60, 60, 70));
        comboBox.setForeground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(100, 100, 120), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(new Color(60, 60, 70));
                setForeground(Color.WHITE);
                if (isSelected) {
                    setBackground(new Color(41, 128, 185));
                }
                return this;
            }
        });
    }
    
    private void habilitarUsuario(int fila) {
        if (fila >= 0 && fila < listaUsuarios.size()) {
            UsuarioSistema usuario = listaUsuarios.get(fila);
            String estadoActual = usuario.getEstado();
            String nuevoEstado = estadoActual.equals("Activo") ? "Inactivo" : "Activo";
            
            int opcion = JOptionPane.showConfirmDialog(this,
                "<html><b>¿Cambiar estado del usuario?</b><br>" +
                "Usuario: <b>" + usuario.getNombre() + "</b><br>" +
                "Estado actual: <b>" + estadoActual + "</b><br>" +
                "Nuevo estado: <b>" + nuevoEstado + "</b></html>",
                "Confirmar Cambio de Estado",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (opcion == JOptionPane.YES_OPTION) {
                // Actualizar el objeto en la lista
                usuario.setEstado(nuevoEstado);
                
                // Actualizar la tabla
                DefaultTableModel model = (DefaultTableModel) tablaUsuarios.getModel();
                model.setValueAt(nuevoEstado, fila, 4);
                
                JOptionPane.showMessageDialog(this,
                    "<html>Estado del usuario actualizado exitosamente<br>" +
                    "Nuevo estado: <b>" + nuevoEstado + "</b></html>",
                    "Estado Actualizado",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro que desea cerrar sesión?",
            "Confirmar cierre de sesión",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (opcion == JOptionPane.YES_OPTION) {
            dispose();
            new LoginSistemaPedidos().setVisible(true);
        }
    }
    
    private void confirmarSalida() {
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro que desea salir del sistema?",
            "Confirmar salida",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (opcion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}