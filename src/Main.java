import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;

    //Inicia el programa
    @Override
    public void start(Stage stage) throws Exception{
        this.primaryStage = stage;
        this.primaryStage.initStyle(StageStyle.UTILITY);
        this.primaryStage.setResizable(false);
        this.primaryStage.setTitle("Tienda verde");
        initRootLayout();
    }

    //crea y muestra la escena
    public void initRootLayout() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("view/login.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
