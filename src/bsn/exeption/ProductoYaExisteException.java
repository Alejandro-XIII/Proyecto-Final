package bsn.exeption;

public class ProductoYaExisteException extends Exception {
    public ProductoYaExisteException(){
        super("Ya existe un producto con el codigo de barras ingresado");
    }
}
