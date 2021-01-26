package controller;

import bsn.ProductoBsn;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Factura;
import model.Producto;
import model.Vendedor;
import javax.swing.*;
import java.util.List;

public class VentasController {

    private static Stage newStage;
    private static Vendedor newUser;
    private List<Producto> productosList;
    @FXML
    private TableView<Producto> tblProductos;
    @FXML
    private TextField txtCodigo;
    @FXML
    private TableColumn<Producto,String> clmNombre;
    @FXML
    private TableColumn<Producto,String> clmCantidad;
    @FXML
    private TableColumn<Producto,String> clmPrecio;

    //conexion con el negocio
    private ProductoBsn productoBsn;

    public VentasController(){
        this.productoBsn = new ProductoBsn();
    }

    @FXML
    public void initialize(){
        productosList = productoBsn.consultarProductos();
    }

    //Agrega un producto a la tabla
    public void btnAgregarProducto_action(){
        newStage.setAlwaysOnTop(false);
        String codigoIngresado = txtCodigo.getText().trim();

        // se valida que el codigo de barras esté bien
        if (!validarCodigo(codigoIngresado)) {
            return;
        }

        // se busca el producto con el codigo de barras
        if(buscarProducto(Long.parseLong(codigoIngresado))){
            clmNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
            clmCantidad.setCellValueFactory(cellData -> new SimpleStringProperty(Byte.toString(cellData.getValue().getCantidad())));
            clmPrecio.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getPrecio())));
        } else {
            mostrarMensaje(Alert.AlertType.ERROR, "Agregar producto", "Resultado de la transacción", "El producto no está registrado");
        }
    }

    // retira un producto de la tabla
    public void btnRetirarProducto_action(){
        newStage.setAlwaysOnTop(false);
        String codigoIngresado = txtCodigo.getText().trim();

        // se valida que el codigo de barras esté bien
        if (!validarCodigo(codigoIngresado)) {
            return;
        }
        // busca el producto con el codigo de barra y si lo encuentra lo elimina
        eliminarProducto(Long.parseLong(codigoIngresado));
    }

    public void btnGenerarFactura_action() {
        Factura factura = new Factura(tblProductos, newUser);
        mostrarMensaje(Alert.AlertType.INFORMATION, "Generar factura", "Resultado de la transacción", "Factura generada con exito");
    }

    //Cierra la ventana
    public void btnRegresar_action(){
        newStage.close();
    }

    //Busca el producto del codigo de barras indicado y si lo encuentra lo agrega a la tabla
    private boolean buscarProducto(long codigo) {
        for(Producto producto:productosList) {
            if(producto.getCodigoBarras() == codigo) {
                producto.setCantidad(ingresarCantidad(producto));
                eliminarProducto(producto.getCodigoBarras());
                newStage.setAlwaysOnTop(true);
                tblProductos.getItems().add(producto);
                return true;
            }
        }
        return false;
    }

    //Elimina un producto de la tabla
    public void eliminarProducto(long codigo){
        for(int i = 0; i < tblProductos.getItems().size(); ++i){
            if(tblProductos.getItems().get(i).getCodigoBarras() == codigo){
                tblProductos.getItems().remove(i);
            }
        }
    }

    //Le pide al usuario la cantidad de productos
    private byte ingresarCantidad(Producto producto){
        newStage.setAlwaysOnTop(false);
        byte cantidad = 0;
        try {
            cantidad = Byte.parseByte(JOptionPane.showInputDialog(producto.getNombre() + ", por favor introduzca la cantidad:"));
        } catch (NumberFormatException numberFormatException) {
            mostrarMensaje(Alert.AlertType.ERROR, "Cantidad de productos", "Resultado de la transacción", "La cantidad debe ser un valor numérico");
            return ingresarCantidad(producto);
        }
        return cantidad;
    }

    //Muestra un mensaje
    private void mostrarMensaje(Alert.AlertType tipo, String titulo, String encabezado, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // se validan todas las condiciones del codigo de barras
    private boolean validarCodigo(String codigoIngresado){
        // se extrae el dato ingresados en el campo de texto y se eliminan los espacios a izquierda y derecha
        codigoIngresado = txtCodigo.getText().trim();
        // se valida que el codigo de barras contenga un valor
        if (codigoIngresado.isEmpty()) {
            mostrarMensaje(Alert.AlertType.ERROR, "Registro de producto", "Resultado de la transacción", "El codigo de barras es requerido");
            txtCodigo.requestFocus();
            return false;
        }

        // se valida que el codigo de barras sea un número
        try {
            Long.parseLong(codigoIngresado);
        } catch (NumberFormatException numberFormatException) {
            mostrarMensaje(Alert.AlertType.ERROR, "Registro de producto", "Resultado de la transacción", "El codigo de barras debe ser un valor numérico");
            txtCodigo.requestFocus();
            txtCodigo.clear();
            return false;
        }

        //se valida que el codigo de barras sea maximo de 13 caracteres
        if(codigoIngresado.length() > 13){
            mostrarMensaje(Alert.AlertType.ERROR, "Registro de producto", "Resultado de la transacción", "El codigo de barras debe tener máximo 13 caractéres");
            return false;
        }
        return true;
    }

    //Guarda el Stage actual para cuando toque cerrarlo
    public static void setStage(Stage stage) {
        newStage = stage;
    }

    //Guarda el usuario que abrio la ventana para imprimir la factura
    public static void setUser(Vendedor user) {
        newUser = user;
    }
}
