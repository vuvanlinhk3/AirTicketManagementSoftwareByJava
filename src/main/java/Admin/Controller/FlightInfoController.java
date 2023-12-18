package Admin.Controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class FlightInfoController  {
    @FXML
    private Label sbaydi;
    private static String timer;
    public static String TenDangNhap;

    @FXML
    private void initialize(){

        Get();
    }
    private void Get(){
        String ten =TenDangNhap;
        LoginController login = new LoginController();
        login.TenDangNhap = ten;
        sbaydi.setText(ten);
        System.out.println(ten);
    }


    // chuyển form
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private void back_click(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Admin/AdminView/Home.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
