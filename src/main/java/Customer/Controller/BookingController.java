package Customer.Controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class BookingController {
    @FXML
    private TextField sbDi;
    @FXML
    private TextField sbDen;
    @FXML
    private TextField diaDiemDi;
    @FXML
    private  TextField diaDiemDen;
    @FXML
    private  TextField giaVe;
    @FXML
    private ComboBox hangKHoang;
    @FXML
    private ComboBox chonMaGhe;



    // quay lại
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private void back_click(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/FlightFind.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
