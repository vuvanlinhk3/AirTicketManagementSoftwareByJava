package Customer.Controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLOutput;

public class LoginController  {
    @FXML
    private TextField emailornumber;
    @FXML
    private TextField matkhaulogin;
    @FXML
    private void dangnhapbtn(){
        String user = emailornumber.getText().toString();
        String pass = matkhaulogin.getText().toString();
        if(user  == "" ){
            SignupController.setRedBorder(emailornumber);
            .wariningLabel.setText("Trường này không đươcj bỏ trống ");
        }
    }


