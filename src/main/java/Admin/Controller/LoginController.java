package Admin.Controller;

import Database.DatabaseController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController  {
    @FXML
    private TextField tenDangNhap_txt;
    @FXML
    private TextField matKhau_txt;
    public static String TenDangNhap;
    private String passWord;

    // chuyển form
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private void login_click(ActionEvent event) throws IOException {
        TenDangNhap = tenDangNhap_txt.getText();
        passWord = matKhau_txt.getText();
        if(TenDangNhap != null   && passWord != null){
            boolean isLogin =  DatabaseController.LoginAdmin(TenDangNhap,passWord);
            if(isLogin){
                BaseController.showAlert("Thành công","Đăng nhập thành công !");
                Parent root = FXMLLoader.load(getClass().getResource("/Admin/AdminView/Home.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            else {
                BaseController.showAlert("Lỗi","Tài khoản không tồn tại !");
                return;
            }
        }else {
            BaseController.showAlert("Trống","Vui lòng nhập tên đăng nhập !");
        }
    }
    @FXML
    private void loginPass(ActionEvent event)throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
