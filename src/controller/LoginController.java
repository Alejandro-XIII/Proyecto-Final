package controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Administrador;
import model.Vendedor;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtContraseña;

    Administrador admin;
    Vendedor user;
    Vendedor user2;
    Vendedor user3;

    //crea los usuarios autorizados
    public LoginController() {
        admin = new Administrador("Admin", "9412daco");
        user = new Vendedor("User", "0000");
        user2 = new Vendedor("User2", "0002");
        user3 = new Vendedor("User3", "0003");
    }

    // comprueba los datos de usuario y contraseña, y abre la ventana indicada si es el caso
    public void btnEntrar_action() throws IOException {
        if(txtUsuario.getText().equals(admin.getUsuario()) && txtContraseña.getText().equals(admin.getContraseña())){
            abrirVentanaAdmin();
            return;
        }
        if(txtUsuario.getText().equals(user.getUsuario()) && txtContraseña.getText().equals(user.getContraseña())){
            VentasController.setUser(user);
            abrirVentanaVentas();
            return;
        }
        if(txtUsuario.getText().equals(user2.getUsuario()) && txtContraseña.getText().equals(user2.getContraseña())){
            VentasController.setUser(user2);
            abrirVentanaVentas();
            return;
        }
        if(txtUsuario.getText().equals(user3.getUsuario()) && txtContraseña.getText().equals(user3.getContraseña())){
            VentasController.setUser(user3);
            abrirVentanaVentas();
            return;
        }
        mostrarMensaje(Alert.AlertType.ERROR, "Login", "Resultado de la transacción", "El usuario o contraseña son incorrectos");
    }

    //cierra la aplicación
    public void btnSalir_action(){
        System.exit(0);
    }

    //abre la ventana del administrador
    private void abrirVentanaAdmin() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/ventana-admin.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Admin");
        stage.getIcons().add(new Image("images/Icon.png"));
        stage.show();
        AdminController.setStage(stage);
        txtUsuario.setText("User");
        txtContraseña.setText("0000");
    }

    // abre la ventana de ventas
    private void abrirVentanaVentas() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/ventana-ventas.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Ventas");
        stage.getIcons().add(new Image("images/Icon.png"));
        stage.show();
        VentasController.setStage(stage);
        txtUsuario.setText("User");
        txtContraseña.setText("0000");
    }

    // Muestra un mensaje
    private void mostrarMensaje(Alert.AlertType tipo, String titulo, String encabezado, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

