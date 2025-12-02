package ui;

import sistemapedidos.DatabaseConfig;
import dao.UsuarioDAO;
import models.Usuario;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class LoginSistemaPedidos extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnSalir;
    private JLabel lblMensaje;
    private JLabel lblConexion;
    
    public LoginSistemaPedidos() {
        configurarVentana();
        crearComponentes();
        verificarConexion();
    }
    
    private void configurarVentana() {
        setTitle("MAPU CORPORATION - Sistema de Gestión");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        getContentPane().setBackground(new Color(20, 20, 30));
        setShape(new RoundRectangle2D.Double(0, 0, 500, 700, 30, 30));
    }
    
    private void crearComponentes() {
        setLayout(null);
        
        // Panel superior con gradiente
        JPanel panelSuperior = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(41, 128, 185), 
                    0, getHeight(), new Color(52, 152, 219));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelSuperior.setBounds(0, 0, 500, 200);
        panelSuperior.setLayout(null);
        panelSuperior.setOpaque(false);
        
        // Icono de la empresa
        JLabel lblIcono = new JLabel("🏢");
        lblIcono.setBounds(0, 30, 500, 60);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        lblIcono.setForeground(Color.WHITE);
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
        panelSuperior.add(lblIcono);
        
        JLabel lblTitulo = new JLabel("MAPU CORPORATION", SwingConstants.CENTER);
        lblTitulo.setBounds(0, 90, 500, 40);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);
        
        JLabel lblSubtitulo = new JLabel("Sistema de Gestión Integral", SwingConstants.CENTER);
        lblSubtitulo.setBounds(0, 135, 500, 25);
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSubtitulo.setForeground(new Color(240, 240, 240));
        panelSuperior.add(lblSubtitulo);
        
        // Botón cerrar (X)
        JButton btnCerrar = new JButton("×");
        btnCerrar.setBounds(460, 10, 30, 30);
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 20));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBackground(new Color(231, 76, 60));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder());
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> confirmarSalida());
        
        // Efecto hover para botón cerrar
        btnCerrar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnCerrar.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(MouseEvent e) {
                btnCerrar.setBackground(new Color(231, 76, 60));
            }
        });
        panelSuperior.add(btnCerrar);
        
        add(panelSuperior);
        
        // Estado de conexión con diseño mejorado
        lblConexion = new JLabel("⏳ Verificando conexión con la base de datos...", SwingConstants.CENTER);
        lblConexion.setBounds(50, 220, 400, 30);
        lblConexion.setFont(new Font("Arial", Font.BOLD, 13));
        lblConexion.setForeground(new Color(243, 156, 18));
        lblConexion.setOpaque(true);
        lblConexion.setBackground(new Color(45, 45, 55));
        lblConexion.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(80, 80, 100), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        add(lblConexion);
        
        // Panel de formulario con diseño moderno
        JPanel panelFormulario = new JPanel();
        panelFormulario.setBounds(50, 270, 400, 280);
        panelFormulario.setBackground(new Color(35, 35, 45));
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(60, 60, 70), 2),
            new EmptyBorder(25, 25, 25, 25)
        ));
        panelFormulario.setLayout(null);
        
        JLabel lblLogin = new JLabel("🔐 INICIAR SESIÓN", SwingConstants.CENTER);
        lblLogin.setBounds(0, 0, 350, 30);
        lblLogin.setFont(new Font("Arial", Font.BOLD, 20));
        lblLogin.setForeground(Color.WHITE);
        panelFormulario.add(lblLogin);
        
        JLabel lblUsuario = new JLabel("👤 Usuario:");
        lblUsuario.setBounds(25, 45, 300, 20);
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 12));
        lblUsuario.setForeground(new Color(200, 200, 200));
        panelFormulario.add(lblUsuario);
        
        txtUsuario = new JTextField();
        txtUsuario.setBounds(25, 70, 300, 45);
        txtUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUsuario.setBackground(new Color(60, 60, 70));
        txtUsuario.setForeground(Color.WHITE);
        txtUsuario.setCaretColor(Color.WHITE);
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(100, 100, 120), 1),
            new EmptyBorder(10, 15, 10, 15)
        ));
        panelFormulario.add(txtUsuario);
        
        JLabel lblPassword = new JLabel("🔒 Contraseña:");
        lblPassword.setBounds(25, 130, 300, 20);
        lblPassword.setFont(new Font("Arial", Font.BOLD, 12));
        lblPassword.setForeground(new Color(200, 200, 200));
        panelFormulario.add(lblPassword);
        
        txtPassword = new JPasswordField();
        txtPassword.setBounds(25, 155, 300, 45);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword.setBackground(new Color(60, 60, 70));
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setCaretColor(Color.WHITE);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(100, 100, 120), 1),
            new EmptyBorder(10, 15, 10, 15)
        ));
        panelFormulario.add(txtPassword);
        
        btnLogin = new JButton("🚀 ENTRAR AL SISTEMA");
        btnLogin.setBounds(25, 215, 300, 50);
        btnLogin.setBackground(new Color(39, 174, 96));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(46, 204, 113), 1),
            new EmptyBorder(12, 20, 12, 20)
        ));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> intentarLogin());
        
        // Efecto hover para botón login
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(46, 204, 113));
                btnLogin.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(39, 174, 96), 1),
                    new EmptyBorder(12, 20, 12, 20)
                ));
            }
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(new Color(39, 174, 96));
                btnLogin.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(46, 204, 113), 1),
                    new EmptyBorder(12, 20, 12, 20)
                ));
            }
        });
        panelFormulario.add(btnLogin);
        
        add(panelFormulario);
        
        // Mensaje con diseño mejorado
        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setBounds(50, 565, 400, 40);
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 13));
        lblMensaje.setOpaque(true);
        lblMensaje.setBackground(new Color(45, 45, 55));
        lblMensaje.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(80, 80, 100), 1),
            new EmptyBorder(10, 15, 10, 15)
        ));
        add(lblMensaje);    
        
        // Evento Enter para login
        txtPassword.addActionListener(e -> intentarLogin());
        
        // Evento ESC para salir
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "salir");
        getRootPane().getActionMap().put("salir", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmarSalida();
            }
        });
        
        // Hacer la ventana arrastrable
        makeDraggable();
    }
    
    private void makeDraggable() {
        final Point[] dragOffset = new Point[1];
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                dragOffset[0] = e.getPoint();
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point currentLocation = getLocation();
                setLocation(currentLocation.x + e.getX() - dragOffset[0].x, 
                           currentLocation.y + e.getY() - dragOffset[0].y);
            }
        });
    }
    
    private void verificarConexion() {
        new Thread(() -> {
            try {
                Thread.sleep(800); // Pausa para efecto visual
                DatabaseConfig.getConnection();
                SwingUtilities.invokeLater(() -> {
                    lblConexion.setText("🟢 Conectado a la base de datos");
                    lblConexion.setForeground(new Color(39, 174, 96));
                    lblConexion.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(39, 174, 96), 1),
                        new EmptyBorder(5, 10, 5, 10)
                    ));
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    lblConexion.setText("🔴 Error de conexión a la base de datos");
                    lblConexion.setForeground(new Color(231, 76, 60));
                    lblConexion.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(231, 76, 60), 1),
                        new EmptyBorder(5, 10, 5, 10)
                    ));
                });
            }
        }).start();
    }
    
    private void intentarLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarMensaje("⚠️ Por favor complete todos los campos", new Color(243, 156, 18));
            return;
        }
        
        // Deshabilitar botón durante login
        btnLogin.setEnabled(false);
        btnLogin.setText("⏳ VERIFICANDO...");
        btnLogin.setBackground(new Color(108, 117, 125));
        mostrarMensaje("🔍 Autenticando credenciales...", new Color(41, 128, 185));
        
        new Thread(() -> {
            try {
                Usuario user = UsuarioDAO.autenticar(usuario, password);
                System.out.println(user);
                SwingUtilities.invokeLater(() -> {
                    if (user != null) {
                        String rolMensaje = "";
                        Color colorMensaje = new Color(39, 174, 96);
                        
                        switch (user.getRol()) {
                            case Usuario.ROL_ADMINISTRADOR:
                                rolMensaje = "👑 Acceso como Administrador";
                                colorMensaje = new Color(41, 128, 185);
                                break;
                            case Usuario.ROL_SUPERVISOR:
                                rolMensaje = "📊 Acceso como Supervisor";
                                colorMensaje = new Color(243, 156, 18);
                                break;
                            case Usuario.ROL_VENDEDOR:
                                rolMensaje = "💼 Acceso como Vendedor";
                                colorMensaje = new Color(39, 174, 96);
                                break;
                            default:
                                rolMensaje = "Acceso concedido - " + user.getRol();
                        }
                        
                        mostrarMensaje("✅ " + rolMensaje, colorMensaje);
                        
                        // Abrir ventana principal después de breve delay
                        Timer timer = new Timer(1500, e -> {
                            abrirVentanaPrincipal(user);
                        });
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        mostrarMensaje("❌ Usuario o contraseña incorrectos", new Color(231, 76, 60));
                        reiniciarFormulario();
                    }
                });
                
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    mostrarMensaje("💥 Error de conexión con la base de datos", new Color(231, 76, 60));
                    reiniciarFormulario();
                });
            }
        }).start();
    }
    
    private void reiniciarFormulario() {
        btnLogin.setEnabled(true);
        btnLogin.setText("🚀 ENTRAR AL SISTEMA");
        btnLogin.setBackground(new Color(39, 174, 96));
        txtPassword.setText("");
    }
    
    private void mostrarMensaje(String mensaje, Color color) {
        lblMensaje.setText(mensaje);
        lblMensaje.setForeground(color);
        lblMensaje.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color, 1),
            new EmptyBorder(10, 15, 10, 15)
        ));
    }
    
    private void confirmarSalida() {
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "<html><b>¿Está seguro que desea salir del sistema?</b><br>" +
            "Se cerrará la aplicación completamente.</html>",
            "Confirmar Salida",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (opcion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    private void abrirVentanaPrincipal(Usuario usuario) {
        dispose(); // Cerrar login
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipalPedidos(usuario).setVisible(true);
        });
    }
}