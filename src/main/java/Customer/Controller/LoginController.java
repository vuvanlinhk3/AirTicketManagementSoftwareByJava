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
    private TextField tendangnhap_text;
   @FXML
    private TextField matkhau_text;

   private String tendangnhap;
   private String matkhau;

   @FXML
    private void login_click(){
        if (isNullText()){
            SignupPassController.showAlert("Lỗi" ,"Vui lòng không để trống thông tin !");
            return;
        }
        if(!isValidANumber(tendangnhap) && !isValidEmail(tendangnhap)){
            SignupPassController.showAlert("Lỗi" ,"Vui lòng nhập đúng định dạng !");
            return;
        }
       else {
           SignupPassController.showAlert("Lỗi" ,"đã đúng");
           return;
       }

   }
   private boolean isValidANumber(String Num){
       try {
           Double.parseDouble(Num);
           return true;
       } catch (NumberFormatException e) {
           return false;
       }
   }
   private boolean isValidEmail(String Ema){
       String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
       return Ema.matches(emailRegex);
   }


   private boolean isNullText(){
       tendangnhap = tendangnhap_text.getText();
       matkhau = matkhau_text.getText();
       if(tendangnhap.isEmpty() || matkhau.isEmpty()){
           return true;
       }
       return false;

   }


    // chuyển form
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private void forgot_click( ActionEvent event) throws  IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/AccountFind.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void signup_click(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Signup.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }




}


