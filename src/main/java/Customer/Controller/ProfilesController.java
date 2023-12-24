package Customer.Controller;

import Admin.Controller.BaseController;
import Database.DatabaseController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfilesController  {
    @FXML
    private TextField name_main_txt;
    @FXML
    private TextField password_txt;
    @FXML
    private TextField birthday_txt;
    @FXML
    private TextField number_txt;
    @FXML
    private TextField id_pass_txt;
    private static int IdPassenger;
    @FXML
    private Button EditPassword;
    @FXML
    private TextField password_txt1;
    @FXML
    private Button Save;
    @FXML
    private Label label1;

    public static void getIdPassender(int Id){
        IdPassenger = Id;
    }
    @FXML
    private void initialize(){
        String birthday = DatabaseController.getBir(IdPassenger);
        String phoneNumber = DatabaseController.getPhonePhone(IdPassenger);
        String namePassanger = DatabaseController.getNamePassanger(IdPassenger);
        birthday_txt.setText(birthday);
        number_txt.setText(phoneNumber);
        name_main_txt.setText(namePassanger);
    }
    @FXML
    private void EditPasswordClick(){
        EditPassword.setVisible(false);
        password_txt1.setVisible(true);
        Save.setVisible(true);
        label1.setVisible(true);
        password_txt.setEditable(true);
    }
    @FXML
    private void SaveClick(){
        String Password = password_txt.getText();
        String Passwordcf = password_txt1.getText();
        if(Passwordcf.isEmpty() || Password.isEmpty()){
            SignupPassController.showAlert("Lỗi","Một mục chưa nhập !");
        }if(Passwordcf.isEmpty() && Password.isEmpty()){
            EditPassword.setVisible(true);
            password_txt1.setVisible(false);
            Save.setVisible(false);
            label1.setVisible(false);
            password_txt.setEditable(false);
        }else {
            boolean isChange = DatabaseController.updatePassword(IdPassenger,Password);
            if(isChange){
                SignupPassController.showAlert("Thành công","Đổi mật khẩu thành công !");
            }
            else {
                SignupPassController.showAlert("Lỗi","Đổi mật khẩu thất bại !");

            }
        }
        EditPassword.setVisible(true);
        password_txt1.setVisible(false);
        Save.setVisible(false);
        label1.setVisible(false);
        password_txt.setEditable(false);
    }

    @FXML
    private void ShowIDClick(){
        id_pass_txt.setText(Integer.toString(IdPassenger));
    }

    @FXML
    private void DeleteAcountClick(){
    ShowWarning();
    if(warning == ButtonType.YES){
        boolean isDelete = DatabaseController.DeleteAcount(IdPassenger);
        if (isDelete){
            SignupPassController.showAlert("Thống báo","Tài khoản của bạn sẽ xóa sau 24h !");
        }else {
            SignupPassController.showAlert("Thống báo","Lỗi !");
        }
    }else {
        return;
    }


    }
    private ButtonType warning;
    private void ShowWarning(){
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Xác nhận");
        confirmationDialog.setHeaderText("Bạn có chắc chắn muốn xóa toàn khoản!");
        confirmationDialog.setContentText("Tài khoản của bạn sẽ được xóa sau 24h sau khi xác nhận xóa tài khoản !");
        confirmationDialog.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        warning = confirmationDialog.showAndWait().orElse(ButtonType.NO);
    }


    // chuyển form
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private void back_click(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Home.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void LogoutClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
