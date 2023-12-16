package Customer.Controller;

import Database.DatabaseContection;
import Database.DatabaseController;
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

import javax.xml.namespace.QName;
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
    private void login_click(ActionEvent event)throws IOException{

        if (isNullText()){
            SignupPassController.showAlert("Lỗi" ,"Vui lòng không để trống thông tin !");
            return;
        }
        if(!isValidANumber(tendangnhap) && !isValidEmail(tendangnhap)){
            SignupPassController.showAlert("Lỗi" ,"Vui lòng nhập đúng định dạng !");
            return;
        }
       else {
           if(isValidEmail(tendangnhap)){
               // email
               boolean successForEmail = DatabaseController.getPassengerForEmail(tendangnhap,matkhau);
               if(successForEmail){
                   SignupPassController.showAlert("Thành công" ,"Đăng nhập thành công !");

                    //chuyển forrm
                   Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Home.fxml"));
                   stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                   scene = new Scene(root);
                   stage.setScene(scene);
                   stage.show();
               }
               else {
                   SignupPassController.showAlert("Thất bại" ,"Tài khoản hoặc mật khẩu không đúng !");
               }
           }
           if(isValidANumber(tendangnhap)){
               boolean successForPhone = DatabaseController.getPassengerForPhone(tendangnhap,matkhau);

               if(successForPhone){
                   // phone
                   System.out.println(passengerIdData);
                   System.out.println(nameData);
                   SignupPassController.showAlert("Thành công" ,"Đăng nhập thành công !");
                   HomeController.getIdPassenderHome(passengerIdData);



                   //chuyển forrm
                   Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Home.fxml"));
                   stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                   scene = new Scene(root);
                   stage.setScene(scene);
                   stage.show();
               }else {
                   SignupPassController.showAlert("Thất bại" ,"Tài khoản hoặc mật khẩu không đúng !");
               }

           }

           return;
       }
   }
   private static int passengerIdData; // home lấy id từ đây  <------
   private static String nameData;
    public static void displayCustomerInfo(int passengerId,String name){
        passengerIdData = passengerId;
        nameData = name;
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


