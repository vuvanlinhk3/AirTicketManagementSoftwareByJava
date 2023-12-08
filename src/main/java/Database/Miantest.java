package Database;

import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.Connection;

public class Miantest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Connection connection = DatabaseContection.getConnettion();
        System.out.println(connection);
    }
}
