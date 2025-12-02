package models;

import java.math.BigDecimal;

public class Pedido {
    private int id;
    private String fecha;
    private String cliente;
    private String estado;
    private BigDecimal total;

    // Constructor
    public Pedido(int id, String fecha, String cliente, String estado, BigDecimal total) {
        this.id = id;
        this.fecha = fecha;
        this.cliente = cliente;
        this.estado = estado;
        this.total = total;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Pedido #" + id + " - " + cliente + " - $" + total + " - " + estado;
    }
}