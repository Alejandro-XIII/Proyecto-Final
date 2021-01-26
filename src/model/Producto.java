package model;

public class Producto {
    private long codigoBarras;
    private String nombre;
    private int fechaVencimiento, precio;
    private byte cantidad;

    public Producto(long codigoBarras, String nombre, int fechaVencimiento, int precio, byte cantidad) {
        this.codigoBarras = codigoBarras;
        this.nombre = nombre;
        this.fechaVencimiento = fechaVencimiento;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public Producto() {

    }

    public long getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(long codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(int fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public byte getCantidad() {
        return cantidad;
    }

    public void setCantidad(byte cantidad) {
        this.cantidad = cantidad;
    }
}
