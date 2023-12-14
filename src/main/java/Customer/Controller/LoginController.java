package Customer.Controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.sql.SQLOutput;

public class LoginController {
    @FXML
    private TextField emailornumber;
    @FXML
    private TextField matkhaulogin;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    //chuyển form
    private Stage stage;
    private Scene scene;
    private Parent root;

    private void dangnhapbtn() {
        String user = emailornumber.getText().toString();
        String pass = matkhaulogin.getText().toString();
        if (user == "") {
            SignupController.setRedBorder(emailornumber);

        }
    }
    @FXML
    private void quenmkbtn(ActionEvent event ) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/AccountFind.fxml"));
        stage =(Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void dangkiLGbtn(ActionEvent event ) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Signup.fxml"));
        stage =(Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}


