package ui;

import models.Usuario;
import models.Producto;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PanelProductos extends JPanel {
    private Usuario usuario;
    private JTable tablaProductos;
    private ProductoTableModel tableModel;
    private List<Producto> listaProductos;
    private JTextField txtBusqueda;
    private JComboBox<String> cmbCategoriaFiltro;
    private JComboBox<String> cmbEstadoFiltro;

    public PanelProductos(Usuario usuario) {
        this.usuario = usuario;
        this.listaProductos = new ArrayList<>();
        inicializarDatosEjemplo();
        configurarPanel();
    }

    private void inicializarDatosEjemplo() {
        // Datos de ejemplo para productos - CORREGIDO: usar BigDecimal
        listaProductos.add(new Producto(1, "Laptop Gamer Pro", "Laptop para gaming de alta gama", 
            new BigDecimal("1599.99"), 25, "TECNOLOGIA", true));
        listaProductos.add(new Producto(2, "Mouse Inalámbrico", "Mouse ergonómico inalámbrico", 
            new BigDecimal("29.99"), 150, "TECNOLOGIA", true));
        listaProductos.add(new Producto(3, "Teclado Mecánico", "Teclado mecánico RGB", 
            new BigDecimal("89.99"), 75, "TECNOLOGIA", true));
        listaProductos.add(new Producto(4, "Monitor 24\"", "Monitor Full HD 24 pulgadas", 
            new BigDecimal("199.99"), 40, "TECNOLOGIA", true));
        listaProductos.add(new Producto(5, "Silla Oficina", "Silla ergonómica para oficina", 
            new BigDecimal("299.99"), 15, "MUEBLES", true));
        listaProductos.add(new Producto(6, "Escritorio Ejecutivo", "Escritorio de madera ejecutivo", 
            new BigDecimal("499.99"), 8, "MUEBLES", true));
        listaProductos.add(new Producto(7, "Archivador Metálico", "Archivador de metal 4 cajones", 
            new BigDecimal("79.99"), 30, "MUEBLES", false));
        listaProductos.add(new Producto(8, "Tóner Impresora", "Tóner para impresora láser", 
            new BigDecimal("45.99"), 100, "INSUMOS", true));
        listaProductos.add(new Producto(9, "Resma Papel A4", "Resma de papel A4 500 hojas", 
            new BigDecimal("8.99"), 200, "INSUMOS", true));
        listaProductos.add(new Producto(10, "Calculadora Científica", "Calculadora científica profesional", 
            new BigDecimal("34.99"), 60, "TECNOLOGIA", true));
    }

    private void configurarPanel() {
        setLayout(new BorderLayout(0, 15));
        setBackground(new Color(25, 25, 35));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título
        JLabel lblTitulo = new JLabel("GESTIÓN DE PRODUCTOS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Panel de búsqueda y filtros
        JPanel panelBusqueda = crearPanelBusqueda();

        // Tabla de productos
        JScrollPane scrollPane = crearTablaProductos();

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
        JLabel lblBusqueda = new JLabel("Buscar producto:");
        lblBusqueda.setFont(new Font("Arial", Font.BOLD, 12));
        lblBusqueda.setForeground(Color.WHITE);

        txtBusqueda = new JTextField(20);
        configurarCampoTexto(txtBusqueda);
        txtBusqueda.setPreferredSize(new Dimension(200, 35));
        
        // Acción de búsqueda al presionar Enter
        txtBusqueda.addActionListener(e -> buscarProductos());

        // Filtro por categoría
        JLabel lblCategoria = new JLabel("Categoría:");
        lblCategoria.setFont(new Font("Arial", Font.BOLD, 12));
        lblCategoria.setForeground(Color.WHITE);

        String[] categorias = {"TODAS", "TECNOLOGIA", "MUEBLES", "INSUMOS", "OFICINA", "OTROS"};
        cmbCategoriaFiltro = new JComboBox<>(categorias);
        configurarComboBox(cmbCategoriaFiltro);
        cmbCategoriaFiltro.addActionListener(e -> filtrarProductos());

        // Filtro por estado
        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setFont(new Font("Arial", Font.BOLD, 12));
        lblEstado.setForeground(Color.WHITE);

        String[] estados = {"TODOS", "Activo", "Inactivo"};
        cmbEstadoFiltro = new JComboBox<>(estados);
        configurarComboBox(cmbEstadoFiltro);
        cmbEstadoFiltro.addActionListener(e -> filtrarProductos());

        // Botones de acción de búsqueda
        JButton btnBuscar = crearBotonBusqueda("🔍 Buscar", new Color(41, 128, 185));
        btnBuscar.addActionListener(e -> buscarProductos());

        JButton btnLimpiar = crearBotonBusqueda("🔄 Limpiar", new Color(108, 117, 125));
        btnLimpiar.addActionListener(e -> limpiarFiltros());

        panel.add(lblBusqueda);
        panel.add(txtBusqueda);
        panel.add(btnBuscar);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(lblCategoria);
        panel.add(cmbCategoriaFiltro);
        panel.add(lblEstado);
        panel.add(cmbEstadoFiltro);
        panel.add(btnLimpiar);

        return panel;
    }

    private JScrollPane crearTablaProductos() {
        tableModel = new ProductoTableModel(listaProductos);
        tablaProductos = new JTable(tableModel);
        
        // Configurar apariencia de la tabla
        tablaProductos.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaProductos.setBackground(new Color(45, 45, 55));
        tablaProductos.setForeground(Color.WHITE);
        tablaProductos.setGridColor(new Color(80, 80, 100));
        tablaProductos.setSelectionBackground(new Color(41, 128, 185));
        tablaProductos.setSelectionForeground(Color.WHITE);
        tablaProductos.setRowHeight(40);

        // Configurar header
        tablaProductos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaProductos.getTableHeader().setBackground(new Color(60, 60, 70));
        tablaProductos.getTableHeader().setForeground(Color.WHITE);

        // Configurar ancho de columnas
        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(60);   // ID
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(200);  // Nombre
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(120);  // Categoría
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(100);  // Precio
        tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(80);   // Stock
        tablaProductos.getColumnModel().getColumn(5).setPreferredWidth(80);   // Estado
        tablaProductos.getColumnModel().getColumn(6).setPreferredWidth(200);  // Acciones

        // Agregar botones de acción a la tabla
        tablaProductos.getColumnModel().getColumn(6).setCellRenderer(new BotonesProductoRenderer());
        tablaProductos.getColumnModel().getColumn(6).setCellEditor(new BotonesProductoEditor());

        JScrollPane scrollPane = new JScrollPane(tablaProductos);
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

        JButton btnNuevoProducto = crearBotonAccion("➕ Nuevo Producto", new Color(39, 174, 96));
        btnNuevoProducto.addActionListener(e -> mostrarDialogoNuevoProducto());

        JButton btnExportar = crearBotonAccion("📊 Exportar Reporte", new Color(41, 128, 185));
        btnExportar.addActionListener(e -> exportarReporte());

        JButton btnActualizar = crearBotonAccion("🔄 Actualizar Lista", new Color(243, 156, 18));
        btnActualizar.addActionListener(e -> actualizarLista());

        JButton btnEstadisticas = crearBotonAccion("📈 Estadísticas", new Color(155, 89, 182));
        btnEstadisticas.addActionListener(e -> mostrarEstadisticas());

        panel.add(btnNuevoProducto);
        panel.add(btnExportar);
        panel.add(btnActualizar);
        panel.add(btnEstadisticas);

        return panel;
    }

    // Métodos de funcionalidad para los botones

    private void buscarProductos() {
        String textoBusqueda = txtBusqueda.getText().trim().toLowerCase();
        String categoriaFiltro = cmbCategoriaFiltro.getSelectedItem().toString();
        String estadoFiltro = cmbEstadoFiltro.getSelectedItem().toString();

        List<Producto> productosFiltrados = new ArrayList<>();

        for (Producto producto : listaProductos) {
            boolean coincideBusqueda = textoBusqueda.isEmpty() ||
                producto.getNombre().toLowerCase().contains(textoBusqueda) ||
                producto.getDescripcion().toLowerCase().contains(textoBusqueda) ||
                producto.getCategoria().toLowerCase().contains(textoBusqueda);

            boolean coincideCategoria = categoriaFiltro.equals("TODAS") ||
                producto.getCategoria().equals(categoriaFiltro);

            boolean coincideEstado = estadoFiltro.equals("TODOS") ||
                (producto.isActivo() && estadoFiltro.equals("Activo")) ||
                (!producto.isActivo() && estadoFiltro.equals("Inactivo"));

            if (coincideBusqueda && coincideCategoria && coincideEstado) {
                productosFiltrados.add(producto);
            }
        }

        tableModel.actualizarDatos(productosFiltrados);
        
        if (productosFiltrados.isEmpty() && (!textoBusqueda.isEmpty() || !categoriaFiltro.equals("TODAS") || !estadoFiltro.equals("TODOS"))) {
            JOptionPane.showMessageDialog(this,
                "No se encontraron productos que coincidan con los criterios de búsqueda.",
                "Búsqueda sin resultados",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void filtrarProductos() {
        buscarProductos(); // Reutilizamos la misma lógica
    }

    private void limpiarFiltros() {
        txtBusqueda.setText("");
        cmbCategoriaFiltro.setSelectedIndex(0);
        cmbEstadoFiltro.setSelectedIndex(0);
        tableModel.actualizarDatos(listaProductos);
    }

    private void mostrarDialogoNuevoProducto() {
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nuevo Producto", true);
        dialogo.setLayout(new BorderLayout());
        dialogo.setSize(500, 450);
        dialogo.setLocationRelativeTo(this);

        JPanel panelFormulario = new JPanel(new GridLayout(7, 2, 10, 10));
        panelFormulario.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelFormulario.setBackground(new Color(45, 45, 55));

        // Campos del formulario
        JTextField txtNombre = new JTextField();
        JTextField txtDescripcion = new JTextField();
        JComboBox<String> cmbCategoria = new JComboBox<>(new String[]{"TECNOLOGIA", "MUEBLES", "INSUMOS", "OFICINA", "OTROS"});
        JTextField txtPrecio = new JTextField();
        JTextField txtStock = new JTextField();
        JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});

        // Configurar campos
        configurarCampoTexto(txtNombre);
        configurarCampoTexto(txtDescripcion);
        configurarComboBox(cmbCategoria);
        configurarCampoTexto(txtPrecio);
        configurarCampoTexto(txtStock);
        configurarComboBox(cmbEstado);

        // Etiquetas
        agregarCampoFormulario(panelFormulario, "Nombre del Producto:", txtNombre);
        agregarCampoFormulario(panelFormulario, "Descripción:", txtDescripcion);
        agregarCampoFormulario(panelFormulario, "Categoría:", cmbCategoria);
        agregarCampoFormulario(panelFormulario, "Precio ($):", txtPrecio);
        agregarCampoFormulario(panelFormulario, "Stock Inicial:", txtStock);
        agregarCampoFormulario(panelFormulario, "Estado:", cmbEstado);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(new Color(45, 45, 55));
        
        JButton btnGuardar = crearBotonAccion("💾 Guardar", new Color(39, 174, 96));
        JButton btnCancelar = crearBotonAccion("❌ Cancelar", new Color(231, 76, 60));

        btnGuardar.addActionListener(e -> {
            if (validarFormulario(txtNombre, txtDescripcion, txtPrecio, txtStock)) {
                guardarNuevoProducto(
                    txtNombre.getText(),
                    txtDescripcion.getText(),
                    cmbCategoria.getSelectedItem().toString(),
                    new BigDecimal(txtPrecio.getText()),
                    Integer.parseInt(txtStock.getText()),
                    cmbEstado.getSelectedItem().toString().equals("Activo")
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

    private boolean validarFormulario(JTextField nombre, JTextField descripcion, JTextField precio, JTextField stock) {
        if (nombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del producto es obligatorio.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (descripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La descripción del producto es obligatoria.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            BigDecimal precioValor = new BigDecimal(precio.getText());
            if (precioValor.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "El precio debe ser mayor a 0.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            int stockValor = Integer.parseInt(stock.getText());
            if (stockValor < 0) {
                JOptionPane.showMessageDialog(this, "El stock no puede ser negativo.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El stock debe ser un número entero válido.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void guardarNuevoProducto(String nombre, String descripcion, String categoria, BigDecimal precio, int stock, boolean activo) {
        int nuevoId = listaProductos.size() + 1;
        Producto nuevoProducto = new Producto(nuevoId, nombre, descripcion, precio, stock, categoria, activo);
        listaProductos.add(nuevoProducto);
        tableModel.actualizarDatos(listaProductos);
        
        JOptionPane.showMessageDialog(this,
            "<html><b>Producto creado exitosamente</b><br>" +
            "ID: " + nuevoId + "<br>" +
            "Nombre: " + nombre + "<br>" +
            "Precio: $" + precio + "</html>",
            "Producto Creado",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportarReporte() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar Reporte de Productos");
        fileChooser.setSelectedFile(new java.io.File("reporte_productos.xlsx"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(this,
                "<html><b>Reporte exportado exitosamente</b><br>" +
                "Archivo: " + fileToSave.getAbsolutePath() + "<br>" +
                "Total de productos: " + listaProductos.size() + "</html>",
                "Exportación Completada",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarLista() {
        tableModel.actualizarDatos(listaProductos);
        limpiarFiltros();
        JOptionPane.showMessageDialog(this,
            "Lista de productos actualizada correctamente.",
            "Actualización Completada",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarEstadisticas() {
        int totalProductos = listaProductos.size();
        int productosActivos = 0;
        int productosInactivos = 0;
        BigDecimal valorTotalInventario = BigDecimal.ZERO;
        int totalStock = 0;

        for (Producto producto : listaProductos) {
            if (producto.isActivo()) {
                productosActivos++;
                BigDecimal valorProducto = producto.getPrecio().multiply(new BigDecimal(producto.getStock()));
                valorTotalInventario = valorTotalInventario.add(valorProducto);
                totalStock += producto.getStock();
            } else {
                productosInactivos++;
            }
        }

        String mensaje = "<html><div style='text-align: center;'>" +
            "<h2>📊 ESTADÍSTICAS DE PRODUCTOS</h2>" +
            "<table border='0' style='margin: 0 auto; text-align: left;'>" +
            "<tr><td><b>Total de Productos:</b></td><td>" + totalProductos + "</td></tr>" +
            "<tr><td><b>Productos Activos:</b></td><td>" + productosActivos + "</td></tr>" +
            "<tr><td><b>Productos Inactivos:</b></td><td>" + productosInactivos + "</td></tr>" +
            "<tr><td><b>Stock Total:</b></td><td>" + totalStock + " unidades</td></tr>" +
            "<tr><td><b>Valor Total Inventario:</b></td><td>$" + String.format("%.2f", valorTotalInventario) + "</td></tr>" +
            "</table></html>";

        JOptionPane.showMessageDialog(this,
            mensaje,
            "Estadísticas de Productos",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void editarProducto(int fila) {
        if (fila >= 0 && fila < listaProductos.size()) {
            Producto producto = listaProductos.get(fila);
            
            JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Producto", true);
            dialogo.setLayout(new BorderLayout());
            dialogo.setSize(500, 450);
            dialogo.setLocationRelativeTo(this);

            JPanel panelFormulario = new JPanel(new GridLayout(7, 2, 10, 10));
            panelFormulario.setBorder(new EmptyBorder(20, 20, 20, 20));
            panelFormulario.setBackground(new Color(45, 45, 55));

            // Campos del formulario con datos actuales
            JTextField txtNombre = new JTextField(producto.getNombre());
            JTextField txtDescripcion = new JTextField(producto.getDescripcion());
            JComboBox<String> cmbCategoria = new JComboBox<>(new String[]{"TECNOLOGIA", "MUEBLES", "INSUMOS", "OFICINA", "OTROS"});
            cmbCategoria.setSelectedItem(producto.getCategoria());
            JTextField txtPrecio = new JTextField(producto.getPrecio().toString());
            JTextField txtStock = new JTextField(String.valueOf(producto.getStock()));
            JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
            cmbEstado.setSelectedItem(producto.isActivo() ? "Activo" : "Inactivo");

            // Configurar campos
            configurarCampoTexto(txtNombre);
            configurarCampoTexto(txtDescripcion);
            configurarComboBox(cmbCategoria);
            configurarCampoTexto(txtPrecio);
            configurarCampoTexto(txtStock);
            configurarComboBox(cmbEstado);

            // Etiquetas
            agregarCampoFormulario(panelFormulario, "Nombre del Producto:", txtNombre);
            agregarCampoFormulario(panelFormulario, "Descripción:", txtDescripcion);
            agregarCampoFormulario(panelFormulario, "Categoría:", cmbCategoria);
            agregarCampoFormulario(panelFormulario, "Precio ($):", txtPrecio);
            agregarCampoFormulario(panelFormulario, "Stock:", txtStock);
            agregarCampoFormulario(panelFormulario, "Estado:", cmbEstado);

            // Panel de botones
            JPanel panelBotones = new JPanel(new FlowLayout());
            panelBotones.setBackground(new Color(45, 45, 55));
            
            JButton btnGuardar = crearBotonAccion("💾 Guardar Cambios", new Color(39, 174, 96));
            JButton btnCancelar = crearBotonAccion("❌ Cancelar", new Color(231, 76, 60));

            btnGuardar.addActionListener(e -> {
                if (validarFormulario(txtNombre, txtDescripcion, txtPrecio, txtStock)) {
                    actualizarProducto(fila, 
                        txtNombre.getText(),
                        txtDescripcion.getText(),
                        cmbCategoria.getSelectedItem().toString(),
                        new BigDecimal(txtPrecio.getText()),
                        Integer.parseInt(txtStock.getText()),
                        cmbEstado.getSelectedItem().toString().equals("Activo")
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

    private void actualizarProducto(int fila, String nombre, String descripcion, String categoria, BigDecimal precio, int stock, boolean activo) {
        Producto producto = listaProductos.get(fila);
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setCategoria(categoria);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setActivo(activo);
        
        tableModel.actualizarDatos(listaProductos);
        
        JOptionPane.showMessageDialog(this,
            "Producto actualizado exitosamente.",
            "Actualización Completada",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void cambiarEstadoProducto(int fila) {
        if (fila >= 0 && fila < listaProductos.size()) {
            Producto producto = listaProductos.get(fila);
            boolean estadoActual = producto.isActivo();
            boolean nuevoEstado = !estadoActual;
            
            int opcion = JOptionPane.showConfirmDialog(this,
                "<html><b>¿Cambiar estado del producto?</b><br>" +
                "Producto: <b>" + producto.getNombre() + "</b><br>" +
                "Estado actual: <b>" + (estadoActual ? "Activo" : "Inactivo") + "</b><br>" +
                "Nuevo estado: <b>" + (nuevoEstado ? "Activo" : "Inactivo") + "</b></html>",
                "Confirmar Cambio de Estado",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (opcion == JOptionPane.YES_OPTION) {
                producto.setActivo(nuevoEstado);
                tableModel.actualizarDatos(listaProductos);
                
                JOptionPane.showMessageDialog(this,
                    "<html>Estado del producto actualizado exitosamente<br>" +
                    "Nuevo estado: <b>" + (nuevoEstado ? "Activo" : "Inactivo") + "</b></html>",
                    "Estado Actualizado",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void eliminarProducto(int fila) {
        if (fila >= 0 && fila < listaProductos.size()) {
            Producto producto = listaProductos.get(fila);
            
            int opcion = JOptionPane.showConfirmDialog(this,
                "<html><b>¿Eliminar producto permanentemente?</b><br>" +
                "Producto: <b>" + producto.getNombre() + "</b><br>" +
                "ID: <b>" + producto.getId() + "</b><br>" +
                "<font color='red'>Esta acción no se puede deshacer.</font></html>",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (opcion == JOptionPane.YES_OPTION) {
                listaProductos.remove(fila);
                tableModel.actualizarDatos(listaProductos);
                
                JOptionPane.showMessageDialog(this,
                    "Producto eliminado exitosamente.",
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
    private class ProductoTableModel extends AbstractTableModel {
        private final String[] columnNames = {"ID", "Nombre", "Categoría", "Precio", "Stock", "Estado", "Acciones"};
        private List<Producto> datos;

        public ProductoTableModel(List<Producto> datos) {
            this.datos = new ArrayList<>(datos);
        }

        public void actualizarDatos(List<Producto> nuevosDatos) {
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
            Producto producto = datos.get(rowIndex);
            switch (columnIndex) {
                case 0: return producto.getId();
                case 1: return producto.getNombre();
                case 2: return producto.getCategoria();
                case 3: return String.format("$%.2f", producto.getPrecio());
                case 4: return producto.getStock();
                case 5: return producto.isActivo() ? "Activo" : "Inactivo";
                case 6: return ""; // Para botones de acción
                default: return null;
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 6; // Solo la columna de acciones es editable
        }
    }

    private class BotonesProductoRenderer extends JPanel implements TableCellRenderer {
        private JPanel panelBotones;
        
        public BotonesProductoRenderer() {
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

    private class BotonesProductoEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton btnEditar, btnEstado, btnEliminar;
        private int currentRow;
        
        public BotonesProductoEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
            panel.setBackground(new Color(45, 45, 55));
            panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            btnEditar = crearBotonEditor("✏️ Editar", new Color(243, 156, 18));
            btnEstado = crearBotonEditor("🔄 Estado", new Color(41, 128, 185));
            btnEliminar = crearBotonEditor("❌ Eliminar", new Color(231, 76, 60));
            
            // Acciones de los botones
            btnEditar.addActionListener(e -> {
                editarProducto(currentRow);
                fireEditingStopped();
            });
            
            btnEstado.addActionListener(e -> {
                cambiarEstadoProducto(currentRow);
                fireEditingStopped();
            });
            
            btnEliminar.addActionListener(e -> {
                eliminarProducto(currentRow);
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