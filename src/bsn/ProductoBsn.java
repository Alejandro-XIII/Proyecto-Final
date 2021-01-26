package bsn;

import bsn.exeption.ProductoYaExisteException;
import dao.ProductoDAO;
import dao.impl.ProductoDAONIO;
import model.Producto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ProductoBsn {

    private ProductoDAO productoDAO;

    public ProductoBsn() {
        this.productoDAO = new ProductoDAONIO();
    }

    public void registrarProducto(Producto producto) throws ProductoYaExisteException {
        Optional<Producto> productoOptional = this.productoDAO.consultarPorCodigoBarras(producto.getCodigoBarras());
        //el producto no estaba
        if(productoOptional.isPresent()){
            throw new ProductoYaExisteException();
        } else {
            this.productoDAO.registrarProducto(producto);
        }
    }

    public List<Producto> consultarProductos(){
        return productoDAO.consultarProductos();
    }

    public void removerProducto(List<Producto> productosList) throws IOException {
        productoDAO.limpiarArchivo();
        productoDAO.actualizarLista();
        for(Producto producto:productosList){
            this.productoDAO.registrarProducto(producto);
            System.out.println(productosList.size());
            System.out.println(producto);
        }
    }
}
