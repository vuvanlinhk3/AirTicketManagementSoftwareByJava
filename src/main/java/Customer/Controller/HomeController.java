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
    private TextField startpoin;
    @FXML
    private TextField endpoint;
    @FXML
    private Button btnpupup;
    @FXML
    private ComboBox <String> start_combo;
    @FXML
    private ComboBox <String> end_combo;

    private boolean comboBoxOpened;
    private String StringValue;
    private String selectedValue;

    public static int IdPassenger;
    public static void getIdPassenderHome(int Id){
        IdPassenger = Id;
    }
    // tạo biến chung
    private String sanbaydi;
    @FXML
    private void initialize() {

    }

    private void flightFind(String searchText, FilteredList<String> filteredFlights) {
        // Thiết lập điều kiện lọc trong FilteredList
        filteredFlights.setPredicate(flight -> {
            // Nếu TextField trống, hiển thị tất cả các mục
            if (searchText == null || searchText.trim().isEmpty()) {
                return true;
            }

            String lowerCaseFlight = flight.toLowerCase();
            String lowerCaseFilter = searchText.toLowerCase();

            // Check if the flight name contains the search text
            return lowerCaseFlight.contains(lowerCaseFilter);
        });
    }

    private void getValueTextField(ComboBox<String> ComboboxPoint, TextField AddressPoints) {
        try {

            ComboboxPoint.setOnShown(event -> {
                // Lấy giá trị đã chọn từ ComboBox
                selectedValue = ComboboxPoint.getSelectionModel().getSelectedItem();
                ComboboxPoint.setValue(selectedValue);

            });
            ComboboxPoint.valueProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    if (newValue != null) {
                        StringValue = newValue.toString();

                        ComboboxPoint.setValue(newValue);
                        AddressPoints.setText(StringValue);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    // chuyển form
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    public void Bookedclick(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Booked.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        BookedController.getIdPassender(IdPassenger);
        System.out.println(IdPassenger);
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

}
