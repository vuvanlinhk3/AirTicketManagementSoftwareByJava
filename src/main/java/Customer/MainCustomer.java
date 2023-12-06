package Customer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainCustomer extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainCustomer.class.getResource("CustomerView/AccountFind.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1400, 650);
        // Set minimum width and height
        stage.setMinWidth(1400);
        stage.setMinHeight(650);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
