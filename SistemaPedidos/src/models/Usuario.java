package models;

public class Usuario {
    private int id;
    private String usuario;
    private String nombre;
    private String rol;
    private String estado;
    
    public static final String ROL_ADMINISTRADOR = "Administrador";
    public static final String ROL_SUPERVISOR = "Supervisor";
    public static final String ROL_VENDEDOR = "Vendedor";
    public static final String ROL_OPERADOR = "Operador";
    
    public Usuario(int id, String usuario, String nombre, String rol, String estado) {
        this.id = id;
        this.usuario = usuario;
        this.nombre = nombre;
        this.rol = rol;
        this.estado = estado;
    }
    
    // Getters
    public int getId() { return id; }
    public String getUsuario() { return usuario; }
    public String getNombre() { return nombre; }
    public String getRol() { return rol; }
    public String getEstado() { return estado; }
    
    // Métodos de permisos
    public boolean puedeGestionarUsuarios() {
        return ROL_ADMINISTRADOR.equals(rol);
    }
    
    public boolean puedeGestionarProductos() {
        return ROL_ADMINISTRADOR.equals(rol) || ROL_SUPERVISOR.equals(rol);
    }
    
    public boolean puedeGestionarPedidos() {
        return ROL_ADMINISTRADOR.equals(rol) || ROL_SUPERVISOR.equals(rol) || ROL_VENDEDOR.equals(rol);
    }
    
    public boolean puedeVerReportes() {
        return ROL_ADMINISTRADOR.equals(rol) || ROL_SUPERVISOR.equals(rol);
    }
    
    public boolean puedeExportarDatos() {
        return ROL_ADMINISTRADOR.equals(rol);
    }
    
    public String getTextoRol() {
        return rol + " - " + nombre;
    }

    public boolean isActivo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Object getUsername() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Object getEmail() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}