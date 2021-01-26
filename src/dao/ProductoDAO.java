package dao;

import model.Producto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductoDAO {

    void registrarProducto(Producto producto);

    Optional<Producto> consultarPorCodigoBarras(long codigoBarras);

    List<Producto> consultarProductos();

    void limpiarArchivo() throws IOException;

    void actualizarLista();
}
