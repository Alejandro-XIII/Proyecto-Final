package controller;

import bsn.ProductoBsn;
import bsn.exeption.ProductoYaExisteException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Producto;
import java.io.IOException;
import java.util.List;

public class AdminController {

    @FXML
    private TextField txtCodigoBarras;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtFechaVencimiento;
    @FXML
    private TextField txtPrecio;
    String codigoIngresado;
    String nombreIngresado;
    String fechaIngresada;
    String precioIngresado;
    private static Stage newStage;
    private List<Producto> productosList;

    // conexión con el negocio
    private ProductoBsn productoBsn;
    public AdminController() {
        this.productoBsn = new ProductoBsn();
    }

    //se validan los datos y se guardan si es el caso
    public void btnRegistrar_action(){
        if(!validarCodigo() || !validarNombre() || !validarFecha() || !validarPrecio()){
            return;
        }

        // se convierten los datos al tipo adecuado
        long codigo = Long.parseLong(codigoIngresado);
        int fecha = Integer.parseInt(fechaIngresada);
        int precio = Integer.parseInt(precioIngresado);

        Producto producto = new Producto(codigo, nombreIngresado, fecha, precio,(byte)0);
        try {
            this.productoBsn.registrarProducto(producto);
            mostrarMensaje(Alert.AlertType.INFORMATION, "Registro de producto", "Resultado de la transacción", "El producto ha sido registrado con éxito");
            limpiarCampos();
        } catch (ProductoYaExisteException e){
            mostrarMensaje(Alert.AlertType.ERROR, "Registro de producto", "Resultado de la transacción", e.getMessage());
        }
    }

    //se borra un producto y muestra sus datos para modificarlos y poderlos guardar
    public void btnActualizar_action() throws IOException {
        if(!validarCodigo()){
            return;
        }
        productosList = productoBsn.consultarProductos();
        if(buscarProducto(Long.parseLong(codigoIngresado))){
            mostrarMensaje(Alert.AlertType.INFORMATION, "Actualizar producto", "Resultado de la transacción", "Cambie los datos que desee y luego oprima Registrar");
        } else {
            mostrarMensaje(Alert.AlertType.ERROR, "Actualizar producto", "Resultado de la transacción", "El producto " + codigoIngresado +  " no se encuentra en la lista");
        }
    }

    //borra un producto si se encuentra en la lista
    public void btnBorrar_action() throws IOException {
        if(!validarCodigo()){
            return;
        }
        productosList = productoBsn.consultarProductos();
        if(buscarProducto(Long.parseLong(codigoIngresado))){
            mostrarMensaje(Alert.AlertType.INFORMATION, "Eliminar producto", "Resultado de la transacción", "El producto " + codigoIngresado +  " ha sido borrado con éxito");
            txtCodigoBarras.clear();
            txtNombre.clear();
            txtFechaVencimiento.clear();
            txtPrecio.clear();
        } else {
            mostrarMensaje(Alert.AlertType.ERROR, "Eliminar producto", "Resultado de la transacción", "El producto " + codigoIngresado +  " no se encuentra en la lista");
        }
    }

    // cierra la ventana actual
    public void btnRegresar_action(){
        newStage.close();
    }

    // limpia los campos donde se escriben los datos
    private void limpiarCampos() {
        this.txtCodigoBarras.clear();
        this.txtNombre.clear();
        this.txtFechaVencimiento.clear();
        this.txtPrecio.clear();
    }

    // muestra un mensaje
    private void mostrarMensaje(Alert.AlertType tipo, String titulo, String encabezado, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // se validan todas las condiciones del codigo de barras
    private boolean validarCodigo(){
        // se extrae el dato ingresados en el campo de texto y se eliminan los espacios a izquierda y derecha
        codigoIngresado = txtCodigoBarras.getText().trim();
        // se valida que el codigo de barras contenga un valor
        if (codigoIngresado.isEmpty()) {
            mostrarMensaje(Alert.AlertType.ERROR, "Registro de producto", "Resultado de la transacción", "El codigo de barras es requerido");
            txtCodigoBarras.requestFocus();
            return false;
        }

        // se valida que el codigo de barras sea un número
        try {
            Long.parseLong(codigoIngresado);
        } catch (NumberFormatException numberFormatException) {
            mostrarMensaje(Alert.AlertType.ERROR, "Registro de producto", "Resultado de la transacción", "El codigo de barras debe ser un valor numérico");
            txtCodigoBarras.requestFocus();
            txtCodigoBarras.clear();
            return false;
        }

        //se valida que el codigo de barras sea maximo de 13 caracteres
        if(codigoIngresado.length() > 13){
            mostrarMensaje(Alert.AlertType.ERROR, "Registro de producto", "Resultado de la transacción", "El codigo de barras debe tener máximo 13 caractéres");
            return false;
        }
        return true;
    }

    // se validan todas las condiciones del nombre
    private boolean validarNombre(){
        // se extrae el dato ingresados en el campo de texto y se eliminan los espacios a izquierda y derecha
        nombreIngresado = txtNombre.getText().trim();
        // se valida que el nombre contenga un valor
        if (nombreIngresado.isEmpty()) {
            mostrarMensaje(Alert.AlertType.ERROR, "Registro de producto", "Resultado de la transacción", "El nombre es requerido");
            txtNombre.requestFocus();
            return false;
        }

        //se valida que el nombre sea maximo de 50 caracteres
        if(nombreIngresado.length() > 50){
            mostrarMensaje(Alert.AlertType.ERROR, "Registro de producto", "Resultado de la transacción", "El nombre debe tener máximo 50 caractéres");
            return false;
        }
        return true;
    }

    // se validan todas las condiciones de la fecha
    private boolean validarFecha(){
        // se extrae el dato ingresados en el campo de texto y se eliminan los espacios a izquierda y derecha
        fechaIngresada = txtFechaVencimiento.getText().trim();
        // se valida que la fecha contenga un valor
        if (fechaIngresada.isEmpty()) {
            mostrarMensaje(Alert.AlertType.ERROR, "Registro de producto", "Resultado de la transacción", "La fecha es requerida");
            txtFechaVencimiento.requestFocus();
            return false;
        }

        // se valida que la fecha sea un número
        try {
            Long.parseLong(fechaIngresada);
        } catch (NumberFormatException numberFormatException) {
            mostrarMensaje(Alert.AlertType.ERROR, "Registro de producto", "Resultado de la transacción", "La fecha debe ser un valor numérico");
            txtFechaVencimiento.requestFocus();
            txtFechaVencimiento.clear();
            return false;
        }

        //se valida que la fecha sea maximo de 8 caracteres
        if(codigoIngresado.length() > 8){
            mostrarMensaje(Alert.AlertType.ERROR, "Registro de producto", "Resultado de la transacción", "La fecha debe tener máximo 8 caractéres");
            return false;
        }
        return true;
    }

    // se validan todas las condiciones del precio
    private boolean validarPrecio(){
        // se extrae el dato ingresados en el campo de texto y se eliminan los espacios a izquierda y derecha
        precioIngresado = txtPrecio.getText().trim();
        // se valida que el precio contenga un valor
        if (precioIngresado.isEmpty()) {
            mostrarMensaje(Alert.AlertType.ERROR, "Registro de producto", "Resultado de la transacción", "El precio es requerido");
            txtPrecio.requestFocus();
            return false;
        }

        // se valida que el precio sea un número
        try {
            Long.parseLong(precioIngresado);
        } catch (NumberFormatException numberFormatException) {
            mostrarMensaje(Alert.AlertType.ERROR, "Registro de producto", "Resultado de la transacción", "El precio debe ser un valor numérico");
            txtPrecio.requestFocus();
            txtPrecio.clear();
            return false;
        }

        //se valida que el precio sea maximo de 5 caracteres
        if(codigoIngresado.length() > 5){
            mostrarMensaje(Alert.AlertType.ERROR, "Registro de producto", "Resultado de la transacción", "El precio debe tener máximo 5 caractéres");
            return false;
        }
        return true;
    }

    //Busca el producto del codigo de barras indicado y actualiza la lista si lo encuentra
    private boolean buscarProducto(long codigo) throws IOException {
        for(Producto producto:productosList) {
            if(producto.getCodigoBarras() == codigo) {
                txtNombre.setText(producto.getNombre());
                txtFechaVencimiento.setText(Integer.toString(producto.getFechaVencimiento()));
                txtPrecio.setText(Integer.toString(producto.getPrecio()));
                productosList.remove(producto);
                productoBsn.removerProducto(productosList);
                return true;
            }
        }
        return false;
    }

    // cierra la ventana actual
    public static void setStage(Stage stage) {
        newStage = stage;
    }
}
