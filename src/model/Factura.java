package model;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.WRITE;

public class Factura {
    @FXML
    private TableView<Producto> tblProductos;
    Vendedor user;
    private int total = 0;
    private final static String NOMBRE_ARCHIVO = "src/documents/factura.txt";
    private final static Path ARCHIVO = Paths.get(NOMBRE_ARCHIVO);

    public Factura(TableView<Producto> tblProductos, Vendedor user) {
        if(!Files.exists(ARCHIVO)){
            try{
                Files.createFile(ARCHIVO);
            } catch (IOException ioe){
                ioe.printStackTrace();
            }

        }
        this.tblProductos = tblProductos;
        this.user = user;
        imprimirFactura();
    }

    private void imprimirFactura() {
        String registro = parseProductosString();
        byte[] datosRegistro = registro.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.wrap(datosRegistro);
        try(FileChannel fc = (FileChannel.open(ARCHIVO, WRITE))){
            fc.write(byteBuffer);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    private String parseProductosString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vendedor: " + user.getUsuario() + "\n");
        for(int i = 0; i < tblProductos.getItems().size(); ++i){
            Producto producto = tblProductos.getItems().get(i);
            int totalProducto = producto.getCantidad() * producto.getPrecio();
            sb.append(producto.getNombre() + "   ")
                    .append(producto.getCantidad() + "   ")
                    .append(totalProducto + "   ")
                    .append("\n");
            total = total + totalProducto;
        }
        sb.append("Total ").append(total + "   ");
        return sb.toString();
    }
}
