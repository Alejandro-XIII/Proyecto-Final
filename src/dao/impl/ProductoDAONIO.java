package dao.impl;

import dao.ProductoDAO;
import model.Producto;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static java.nio.file.StandardOpenOption.APPEND;

public class ProductoDAONIO implements ProductoDAO {

    private final static String NOMBRE_ARCHIVO = "src/documents/productos.txt";
    private final static Path ARCHIVO = Paths.get(NOMBRE_ARCHIVO);

    private final static Integer LONGITUD_REGISTRO = 78;
    private final static Integer LONGITUD_CODIGO = 13;
    private final static Integer LONGITUD_NOMBRE = 50;
    private final static Integer LONGITUD_FECHA = 8;
    private final static Integer LONGITUD_PRECIO = 5;
    private final static Integer LONGITUD_CANTIDAD = 2;

    public static List<Producto> productos = new ArrayList<>();


    public ProductoDAONIO(){
        if(!Files.exists(ARCHIVO)){
            try{
                Files.createFile(ARCHIVO);
            } catch (IOException ioe){
                ioe.printStackTrace();
            }

        }
    }

    @Override
    public void registrarProducto(Producto producto) {
        String registro = parseProductoString(producto);
        byte[] datosRegistro = registro.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.wrap(datosRegistro);
        try(FileChannel fc = (FileChannel.open(ARCHIVO, APPEND))){
            fc.write(byteBuffer);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    private String parseProductoString(Producto producto){
        StringBuilder sb = new StringBuilder();
        sb.append(completarCampo(Long.toString(producto.getCodigoBarras()), LONGITUD_CODIGO))
                .append(completarCampo(producto.getNombre(), LONGITUD_NOMBRE))
                .append(completarCampo(Integer.toString(producto.getFechaVencimiento()), LONGITUD_FECHA))
                .append(completarCampo(Integer.toString(producto.getPrecio()), LONGITUD_PRECIO))
                .append(completarCampo(Byte.toString(producto.getCantidad()), LONGITUD_CANTIDAD));
        return sb.toString();
    }

    private String completarCampo(String valor, Integer longitudCampo) {
        if(valor.length() > longitudCampo){
            return valor.substring(0, longitudCampo);
        }
        return String.format("%1$" + longitudCampo + "s", valor);
    }

    @Override
    public Optional<Producto> consultarPorCodigoBarras(long codigoBarras) {
        for(Producto producto:consultarProductos()) {
            if(producto.getCodigoBarras() == codigoBarras) {
                return Optional.of(producto);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Producto> consultarProductos() {
        actualizarLista();
        try(SeekableByteChannel sbc = Files.newByteChannel(ARCHIVO)){
            ByteBuffer buffer = ByteBuffer.allocate(LONGITUD_REGISTRO);
            while (sbc.read(buffer) > 0){
                buffer.rewind();
                CharBuffer registro = Charset.defaultCharset().decode(buffer);
                Producto producto = parseBufferProducto(registro);
                productos.add(producto);
                buffer.flip();
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        return productos;
    }

    @Override
    public void limpiarArchivo() throws IOException {
        Files.delete(ARCHIVO);
        Files.createFile(ARCHIVO);
    }

    @Override
    public void actualizarLista(){
        this.productos = new ArrayList<>();
    }

    private Producto parseBufferProducto(CharBuffer registro) {
        Producto producto = new Producto();

        String codigo = registro.subSequence(0,LONGITUD_CODIGO).toString().trim();
        producto.setCodigoBarras(Long.parseLong(codigo));
        registro.position(LONGITUD_CODIGO);
        registro = registro.slice();

        String nombre = registro.subSequence(0,LONGITUD_NOMBRE).toString().trim();
        producto.setNombre(nombre);
        registro.position(LONGITUD_NOMBRE);
        registro = registro.slice();

        String fecha = registro.subSequence(0,LONGITUD_FECHA).toString().trim();
        producto.setFechaVencimiento(Integer.parseInt(fecha));
        registro.position(LONGITUD_FECHA);
        registro = registro.slice();

        String precio = registro.subSequence(0,LONGITUD_PRECIO).toString().trim();
        producto.setPrecio(Integer.parseInt(precio));
        registro.position(LONGITUD_PRECIO);
        registro = registro.slice();

        String cantidad = registro.subSequence(0,LONGITUD_CANTIDAD).toString().trim();
        producto.setCantidad(Byte.parseByte(cantidad));
        registro.position(LONGITUD_CANTIDAD);
        return producto;
    }
}
