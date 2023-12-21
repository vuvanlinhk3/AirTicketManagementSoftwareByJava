package Customer.Controller;
import Database.DatabaseController;
import Database.DatabaseContection;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController  {
    @FXML
    private Button nameProfile;
    public static int IdPassenger;
    public static String tendangnhap;
    public static void getIdPassenderHome(int Id){
        IdPassenger = Id;
    }
    // tạo biến chung
    private String sanbaydi;
    @FXML
    private void initialize() {
        String namePas = tendangnhap;
        LoginController getName = new LoginController();
        getName.tendangnhap = namePas;
        nameProfile.setText(namePas);

    }
    @FXML
    private void ListAllFlightClick(ActionEvent event) throws IOException{
        AllFlightController.getIdPassender(IdPassenger);
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/ListAllFlight.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    // chuyển form
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    public void Bookedclick(ActionEvent event) throws IOException{
        BookedController.getIdPassender(IdPassenger);
        System.out.println(IdPassenger);
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Booked.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    public void assessclick(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Assess.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void flightfindclick(ActionEvent event) throws IOException{
        FlightFindController.getIdPassender(IdPassenger);
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/FlightFind.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void logout_click(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void profile_click(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Profiles.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void VIPClick(){
        SignupPassController.showAlert("None","Chức năng này chưa được hỗ trợ!");
    }

}
