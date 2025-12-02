package ui;

import models.Usuario;
import models.Producto;
import models.Pedido;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PanelPedidos extends JPanel {
    private Usuario usuario;
    private JTable tablaPedidos;
    private PedidoTableModel tableModel;
    private List<Pedido> listaPedidos;
    private List<Producto> listaProductos;
    private JTextField txtBusqueda;
    private JComboBox<String> cmbEstadoFiltro;

    public PanelPedidos(Usuario usuario) {
        this.usuario = usuario;
        this.listaPedidos = new ArrayList<>();
        this.listaProductos = new ArrayList<>();
        inicializarDatosEjemplo();
        configurarPanel();
    }

    private void inicializarDatosEjemplo() {
        // Primero inicializamos algunos productos de ejemplo
        listaProductos.add(new Producto(1, "Laptop Gamer Pro", "Laptop para gaming de alta gama", 
            new BigDecimal("1599.99"), 25, "TECNOLOGIA", true));
        listaProductos.add(new Producto(2, "Mouse Inalámbrico", "Mouse ergonómico inalámbrico", 
            new BigDecimal("29.99"), 150, "TECNOLOGIA", true));
        listaProductos.add(new Producto(3, "Teclado Mecánico", "Teclado mecánico RGB", 
            new BigDecimal("89.99"), 75, "TECNOLOGIA", true));

        // Ahora inicializamos pedidos de ejemplo
        // CORREGIDO: Usar BigDecimal para el total
        listaPedidos.add(new Pedido(1, "2024-01-15", "Cliente Ejemplo 1", 
            "Pendiente", new BigDecimal("1599.99")));
        listaPedidos.add(new Pedido(2, "2024-01-14", "Cliente Ejemplo 2", 
            "Procesando", new BigDecimal("119.98")));
        listaPedidos.add(new Pedido(3, "2024-01-13", "Cliente Ejemplo 3", 
            "Completado", new BigDecimal("89.99")));
        listaPedidos.add(new Pedido(4, "2024-01-12", "Cliente Ejemplo 4", 
            "Cancelado", new BigDecimal("299.99")));
    }

    private void configurarPanel() {
        setLayout(new BorderLayout(0, 15));
        setBackground(new Color(25, 25, 35));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título
        JLabel lblTitulo = new JLabel("GESTIÓN DE PEDIDOS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Panel de búsqueda y filtros
        JPanel panelBusqueda = crearPanelBusqueda();

        // Tabla de pedidos
        JScrollPane scrollPane = crearTablaPedidos();

        // Panel de botones de acción
        JPanel panelAcciones = crearPanelAcciones();

        // Agregar componentes al panel principal
        add(lblTitulo, BorderLayout.NORTH);
        add(panelBusqueda, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.SOUTH);
    }

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(new Color(35, 35, 45));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(80, 80, 100), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));

        // Campo de búsqueda
        JLabel lblBusqueda = new JLabel("Buscar pedido:");
        lblBusqueda.setFont(new Font("Arial", Font.BOLD, 12));
        lblBusqueda.setForeground(Color.WHITE);

        txtBusqueda = new JTextField(20);
        configurarCampoTexto(txtBusqueda);
        txtBusqueda.setPreferredSize(new Dimension(200, 35));
        
        // Acción de búsqueda al presionar Enter
        txtBusqueda.addActionListener(e -> buscarPedidos());

        // Filtro por estado
        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setFont(new Font("Arial", Font.BOLD, 12));
        lblEstado.setForeground(Color.WHITE);

        String[] estados = {"TODOS", "Pendiente", "Procesando", "Completado", "Cancelado"};
        cmbEstadoFiltro = new JComboBox<>(estados);
        configurarComboBox(cmbEstadoFiltro);
        cmbEstadoFiltro.addActionListener(e -> filtrarPedidos());

        // Botones de acción de búsqueda
        JButton btnBuscar = crearBotonBusqueda("🔍 Buscar", new Color(41, 128, 185));
        btnBuscar.addActionListener(e -> buscarPedidos());

        JButton btnLimpiar = crearBotonBusqueda("🔄 Limpiar", new Color(108, 117, 125));
        btnLimpiar.addActionListener(e -> limpiarFiltros());

        panel.add(lblBusqueda);
        panel.add(txtBusqueda);
        panel.add(btnBuscar);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(lblEstado);
        panel.add(cmbEstadoFiltro);
        panel.add(btnLimpiar);

        return panel;
    }

    private JScrollPane crearTablaPedidos() {
        tableModel = new PedidoTableModel(listaPedidos);
        tablaPedidos = new JTable(tableModel);
        
        // Configurar apariencia de la tabla
        tablaPedidos.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaPedidos.setBackground(new Color(45, 45, 55));
        tablaPedidos.setForeground(Color.WHITE);
        tablaPedidos.setGridColor(new Color(80, 80, 100));
        tablaPedidos.setSelectionBackground(new Color(41, 128, 185));
        tablaPedidos.setSelectionForeground(Color.WHITE);
        tablaPedidos.setRowHeight(40);

        // Configurar header
        tablaPedidos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaPedidos.getTableHeader().setBackground(new Color(60, 60, 70));
        tablaPedidos.getTableHeader().setForeground(Color.WHITE);

        // Configurar ancho de columnas
        tablaPedidos.getColumnModel().getColumn(0).setPreferredWidth(60);   // ID
        tablaPedidos.getColumnModel().getColumn(1).setPreferredWidth(120);  // Fecha
        tablaPedidos.getColumnModel().getColumn(2).setPreferredWidth(200);  // Cliente
        tablaPedidos.getColumnModel().getColumn(3).setPreferredWidth(120);  // Estado
        tablaPedidos.getColumnModel().getColumn(4).setPreferredWidth(120);  // Total
        tablaPedidos.getColumnModel().getColumn(5).setPreferredWidth(200);  // Acciones

        // Agregar botones de acción a la tabla
        tablaPedidos.getColumnModel().getColumn(5).setCellRenderer(new BotonesPedidoRenderer());
        tablaPedidos.getColumnModel().getColumn(5).setCellEditor(new BotonesPedidoEditor());

        JScrollPane scrollPane = new JScrollPane(tablaPedidos);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(80, 80, 100), 1),
            new EmptyBorder(10, 0, 10, 0)
        ));
        scrollPane.getViewport().setBackground(new Color(45, 45, 55));

        return scrollPane;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panel.setBackground(new Color(25, 25, 35));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 0, 0, 0, new Color(80, 80, 100)),
            new EmptyBorder(15, 0, 0, 0)
        ));

        JButton btnNuevoPedido = crearBotonAccion("➕ Nuevo Pedido", new Color(39, 174, 96));
        btnNuevoPedido.addActionListener(e -> mostrarDialogoNuevoPedido());

        JButton btnExportar = crearBotonAccion("📊 Exportar Reporte", new Color(41, 128, 185));
        btnExportar.addActionListener(e -> exportarReporte());

        JButton btnActualizar = crearBotonAccion("🔄 Actualizar Lista", new Color(243, 156, 18));
        btnActualizar.addActionListener(e -> actualizarLista());

        JButton btnEstadisticas = crearBotonAccion("📈 Estadísticas", new Color(155, 89, 182));
        btnEstadisticas.addActionListener(e -> mostrarEstadisticas());

        panel.add(btnNuevoPedido);
        panel.add(btnExportar);
        panel.add(btnActualizar);
        panel.add(btnEstadisticas);

        return panel;
    }

    // Métodos de funcionalidad para los botones

    private void buscarPedidos() {
        String textoBusqueda = txtBusqueda.getText().trim().toLowerCase();
        String estadoFiltro = cmbEstadoFiltro.getSelectedItem().toString();

        List<Pedido> pedidosFiltrados = new ArrayList<>();

        for (Pedido pedido : listaPedidos) {
            boolean coincideBusqueda = textoBusqueda.isEmpty() ||
                pedido.getCliente().toLowerCase().contains(textoBusqueda) ||
                String.valueOf(pedido.getId()).contains(textoBusqueda);

            boolean coincideEstado = estadoFiltro.equals("TODOS") ||
                pedido.getEstado().equals(estadoFiltro);

            if (coincideBusqueda && coincideEstado) {
                pedidosFiltrados.add(pedido);
            }
        }

        tableModel.actualizarDatos(pedidosFiltrados);
        
        if (pedidosFiltrados.isEmpty() && (!textoBusqueda.isEmpty() || !estadoFiltro.equals("TODOS"))) {
            JOptionPane.showMessageDialog(this,
                "No se encontraron pedidos que coincidan con los criterios de búsqueda.",
                "Búsqueda sin resultados",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void filtrarPedidos() {
        buscarPedidos(); // Reutilizamos la misma lógica
    }

    private void limpiarFiltros() {
        txtBusqueda.setText("");
        cmbEstadoFiltro.setSelectedIndex(0);
        tableModel.actualizarDatos(listaPedidos);
    }

    private void mostrarDialogoNuevoPedido() {
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nuevo Pedido", true);
        dialogo.setLayout(new BorderLayout());
        dialogo.setSize(600, 500);
        dialogo.setLocationRelativeTo(this);

        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));
        panelFormulario.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelFormulario.setBackground(new Color(45, 45, 55));

        // Campos del formulario
        JTextField txtCliente = new JTextField();
        JComboBox<String> cmbProducto = new JComboBox<>();
        JTextField txtCantidad = new JTextField("1");
        JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Pendiente", "Procesando"});

        // Llenar combo box de productos
        for (Producto producto : listaProductos) {
            if (producto.isActivo()) {
                cmbProducto.addItem(producto.getNombre() + " - $" + producto.getPrecio() + " (Stock: " + producto.getStock() + ")");
            }
        }

        // Configurar campos
        configurarCampoTexto(txtCliente);
        configurarComboBox(cmbProducto);
        configurarCampoTexto(txtCantidad);
        configurarComboBox(cmbEstado);

        // Etiquetas
        agregarCampoFormulario(panelFormulario, "Cliente:", txtCliente);
        agregarCampoFormulario(panelFormulario, "Producto:", cmbProducto);
        agregarCampoFormulario(panelFormulario, "Cantidad:", txtCantidad);
        agregarCampoFormulario(panelFormulario, "Estado:", cmbEstado);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(new Color(45, 45, 55));
        
        JButton btnGuardar = crearBotonAccion("💾 Guardar", new Color(39, 174, 96));
        JButton btnCancelar = crearBotonAccion("❌ Cancelar", new Color(231, 76, 60));

        btnGuardar.addActionListener(e -> {
            if (validarFormulario(txtCliente, txtCantidad)) {
                guardarNuevoPedido(
                    txtCliente.getText(),
                    cmbProducto.getSelectedIndex(),
                    Integer.parseInt(txtCantidad.getText()),
                    cmbEstado.getSelectedItem().toString()
                );
                dialogo.dispose();
            }
        });

        btnCancelar.addActionListener(e -> dialogo.dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        dialogo.add(panelFormulario, BorderLayout.CENTER);
        dialogo.add(panelBotones, BorderLayout.SOUTH);
        dialogo.setVisible(true);
    }

    private void agregarCampoFormulario(JPanel panel, String etiqueta, JComponent campo) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lbl);
        panel.add(campo);
    }

    private boolean validarFormulario(JTextField cliente, JTextField cantidad) {
        if (cliente.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del cliente es obligatorio.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            int cantidadValor = Integer.parseInt(cantidad.getText());
            if (cantidadValor <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número válido.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void guardarNuevoPedido(String cliente, int productoIndex, int cantidad, String estado) {
        if (productoIndex >= 0 && productoIndex < listaProductos.size()) {
            Producto productoSeleccionado = listaProductos.get(productoIndex);
            
            // CORREGIDO: Usar BigDecimal para el total
            BigDecimal total = productoSeleccionado.getPrecio().multiply(new BigDecimal(cantidad));
            
            int nuevoId = listaPedidos.size() + 1;
            String fechaActual = java.time.LocalDate.now().toString();
            
            Pedido nuevoPedido = new Pedido(nuevoId, fechaActual, cliente, estado, total);
            listaPedidos.add(nuevoPedido);
            tableModel.actualizarDatos(listaPedidos);
            
            JOptionPane.showMessageDialog(this,
                "<html><b>Pedido creado exitosamente</b><br>" +
                "ID: " + nuevoId + "<br>" +
                "Cliente: " + cliente + "<br>" +
                "Total: $" + total + "</html>",
                "Pedido Creado",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void exportarReporte() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar Reporte de Pedidos");
        fileChooser.setSelectedFile(new java.io.File("reporte_pedidos.xlsx"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(this,
                "<html><b>Reporte exportado exitosamente</b><br>" +
                "Archivo: " + fileToSave.getAbsolutePath() + "<br>" +
                "Total de pedidos: " + listaPedidos.size() + "</html>",
                "Exportación Completada",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarLista() {
        tableModel.actualizarDatos(listaPedidos);
        limpiarFiltros();
        JOptionPane.showMessageDialog(this,
            "Lista de pedidos actualizada correctamente.",
            "Actualización Completada",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarEstadisticas() {
        int totalPedidos = listaPedidos.size();
        int pedidosPendientes = 0;
        int pedidosProcesando = 0;
        int pedidosCompletados = 0;
        int pedidosCancelados = 0;
        BigDecimal totalVentas = BigDecimal.ZERO;

        for (Pedido pedido : listaPedidos) {
            switch (pedido.getEstado()) {
                case "Pendiente" -> pedidosPendientes++;
                case "Procesando" -> pedidosProcesando++;
                case "Completado" -> {
                    pedidosCompletados++; 
                    totalVentas = totalVentas.add(pedido.getTotal());
                }
                case "Cancelado" -> pedidosCancelados++;
            }
        }

        String mensaje = "<html><div style='text-align: center;'>" +
            "<h2>📊 ESTADÍSTICAS DE PEDIDOS</h2>" +
            "<table border='0' style='margin: 0 auto; text-align: left;'>" +
            "<tr><td><b>Total de Pedidos:</b></td><td>" + totalPedidos + "</td></tr>" +
            "<tr><td><b>Pedidos Pendientes:</b></td><td>" + pedidosPendientes + "</td></tr>" +
            "<tr><td><b>Pedidos Procesando:</b></td><td>" + pedidosProcesando + "</td></tr>" +
            "<tr><td><b>Pedidos Completados:</b></td><td>" + pedidosCompletados + "</td></tr>" +
            "<tr><td><b>Pedidos Cancelados:</b></td><td>" + pedidosCancelados + "</td></tr>" +
            "<tr><td><b>Total en Ventas:</b></td><td>$" + String.format("%.2f", totalVentas) + "</td></tr>" +
            "</table></html>";

        JOptionPane.showMessageDialog(this,
            mensaje,
            "Estadísticas de Pedidos",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void editarPedido(int fila) {
        if (fila >= 0 && fila < listaPedidos.size()) {
            Pedido pedido = listaPedidos.get(fila);
            
            JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Pedido", true);
            dialogo.setLayout(new BorderLayout());
            dialogo.setSize(500, 400);
            dialogo.setLocationRelativeTo(this);

            JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
            panelFormulario.setBorder(new EmptyBorder(20, 20, 20, 20));
            panelFormulario.setBackground(new Color(45, 45, 55));

            // Campos del formulario con datos actuales
            JTextField txtCliente = new JTextField(pedido.getCliente());
            JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Pendiente", "Procesando", "Completado", "Cancelado"});
            cmbEstado.setSelectedItem(pedido.getEstado());
            JLabel lblTotal = new JLabel("$" + pedido.getTotal());
            lblTotal.setForeground(Color.WHITE);
            lblTotal.setFont(new Font("Arial", Font.BOLD, 12));

            // Configurar campos
            configurarCampoTexto(txtCliente);
            configurarComboBox(cmbEstado);

            // Etiquetas
            agregarCampoFormulario(panelFormulario, "Cliente:", txtCliente);
            agregarCampoFormulario(panelFormulario, "Estado:", cmbEstado);
            agregarCampoFormulario(panelFormulario, "Total:", lblTotal);

            // Panel de botones
            JPanel panelBotones = new JPanel(new FlowLayout());
            panelBotones.setBackground(new Color(45, 45, 55));
            
            JButton btnGuardar = crearBotonAccion("💾 Guardar Cambios", new Color(39, 174, 96));
            JButton btnCancelar = crearBotonAccion("❌ Cancelar", new Color(231, 76, 60));

            btnGuardar.addActionListener(e -> {
                if (validarFormularioEdicion(txtCliente)) {
                    actualizarPedido(fila, 
                        txtCliente.getText(),
                        cmbEstado.getSelectedItem().toString()
                    );
                    dialogo.dispose();
                }
            });

            btnCancelar.addActionListener(e -> dialogo.dispose());

            panelBotones.add(btnGuardar);
            panelBotones.add(btnCancelar);

            dialogo.add(panelFormulario, BorderLayout.CENTER);
            dialogo.add(panelBotones, BorderLayout.SOUTH);
            dialogo.setVisible(true);
        }
    }

    private boolean validarFormularioEdicion(JTextField cliente) {
        if (cliente.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del cliente es obligatorio.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void actualizarPedido(int fila, String cliente, String estado) {
        Pedido pedido = listaPedidos.get(fila);
        pedido.setCliente(cliente);
        pedido.setEstado(estado);
        
        tableModel.actualizarDatos(listaPedidos);
        
        JOptionPane.showMessageDialog(this,
            "Pedido actualizado exitosamente.",
            "Actualización Completada",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void cambiarEstadoPedido(int fila) {
        if (fila >= 0 && fila < listaPedidos.size()) {
            Pedido pedido = listaPedidos.get(fila);
            String estadoActual = pedido.getEstado();
            
            String[] opciones = {"Pendiente", "Procesando", "Completado", "Cancelado"};
            String nuevoEstado = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione el nuevo estado para el pedido:",
                "Cambiar Estado del Pedido",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                estadoActual
            );
            
            if (nuevoEstado != null && !nuevoEstado.equals(estadoActual)) {
                pedido.setEstado(nuevoEstado);
                tableModel.actualizarDatos(listaPedidos);
                
                JOptionPane.showMessageDialog(this,
                    "<html>Estado del pedido actualizado exitosamente<br>" +
                    "Nuevo estado: <b>" + nuevoEstado + "</b></html>",
                    "Estado Actualizado",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void eliminarPedido(int fila) {
        if (fila >= 0 && fila < listaPedidos.size()) {
            Pedido pedido = listaPedidos.get(fila);
            
            int opcion = JOptionPane.showConfirmDialog(this,
                "<html><b>¿Eliminar pedido permanentemente?</b><br>" +
                "Pedido ID: <b>" + pedido.getId() + "</b><br>" +
                "Cliente: <b>" + pedido.getCliente() + "</b><br>" +
                "<font color='red'>Esta acción no se puede deshacer.</font></html>",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (opcion == JOptionPane.YES_OPTION) {
                listaPedidos.remove(fila);
                tableModel.actualizarDatos(listaPedidos);
                
                JOptionPane.showMessageDialog(this,
                    "Pedido eliminado exitosamente.",
                    "Eliminación Completada",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // Métodos auxiliares para configuración de componentes
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

    // Clases internas para la tabla
    private class PedidoTableModel extends AbstractTableModel {
        private final String[] columnNames = {"ID", "Fecha", "Cliente", "Estado", "Total", "Acciones"};
        private List<Pedido> datos;

        public PedidoTableModel(List<Pedido> datos) {
            this.datos = new ArrayList<>(datos);
        }

        public void actualizarDatos(List<Pedido> nuevosDatos) {
            this.datos = new ArrayList<>(nuevosDatos);
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return datos.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Pedido pedido = datos.get(rowIndex);
            switch (columnIndex) {
                case 0: return pedido.getId();
                case 1: return pedido.getFecha();
                case 2: return pedido.getCliente();
                case 3: return pedido.getEstado();
                case 4: return String.format("$%.2f", pedido.getTotal());
                case 5: return ""; // Para botones de acción
                default: return null;
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 5; // Solo la columna de acciones es editable
        }
    }

    private class BotonesPedidoRenderer extends JPanel implements TableCellRenderer {
        private JPanel panelBotones;
        
        public BotonesPedidoRenderer() {
            setLayout(new BorderLayout());
            setBackground(new Color(45, 45, 55));
            
            panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
            panelBotones.setBackground(new Color(45, 45, 55));
            panelBotones.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            // Crear botones de ejemplo para el renderer
            JButton btn1 = crearBotonTabla("✏️ Editar", new Color(243, 156, 18));
            JButton btn2 = crearBotonTabla("🔄 Estado", new Color(41, 128, 185));
            JButton btn3 = crearBotonTabla("❌ Eliminar", new Color(231, 76, 60));
            
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

    private class BotonesPedidoEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton btnEditar, btnEstado, btnEliminar;
        private int currentRow;
        
        public BotonesPedidoEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
            panel.setBackground(new Color(45, 45, 55));
            panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            btnEditar = crearBotonEditor("✏️ Editar", new Color(243, 156, 18));
            btnEstado = crearBotonEditor("🔄 Estado", new Color(41, 128, 185));
            btnEliminar = crearBotonEditor("❌ Eliminar", new Color(231, 76, 60));
            
            // Acciones de los botones
            btnEditar.addActionListener(e -> {
                editarPedido(currentRow);
                fireEditingStopped();
            });
            
            btnEstado.addActionListener(e -> {
                cambiarEstadoPedido(currentRow);
                fireEditingStopped();
            });
            
            btnEliminar.addActionListener(e -> {
                eliminarPedido(currentRow);
                fireEditingStopped();
            });
            
            panel.add(btnEditar);
            panel.add(btnEstado);
            panel.add(btnEliminar);
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
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
}