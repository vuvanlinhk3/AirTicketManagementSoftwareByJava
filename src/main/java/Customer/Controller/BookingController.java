package Customer.Controller;

import Database.DatabaseController;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class BookingController {
    @FXML
    private Label sbDi;
    @FXML
    private DatePicker thoiGianDi;
    @FXML
    private Label sbDen;
    @FXML
    private Label diaDiemDi;
    @FXML
    private  Label diaDiemDen;
    @FXML
    private  Label giaVe;
    @FXML
    private ComboBox <String> hangKHoang;
    @FXML
    private ComboBox <String> chonMaGhe;


    // Lấy id chuyến bay
    private static int flightIdData;
    private static String  flightTimeData;
    private static String  airportStartData;
    private static String  airportEndData;
    private static String  departureLocationData;
    private static String  destinationLocationData;

    // Constructor nhận tham số flightId
    public static void setFlightId(int flightId , String  time , String airport_start, String airport_end) {
        flightIdData = flightId;
        flightTimeData = time;
        airportStartData = airport_start;
        airportEndData = airport_end;
        System.out.println(flightIdData);
    }

    public static void displayLocation(String departureLocation,String destinationLocation){
        departureLocationData =departureLocation;
        destinationLocationData = destinationLocation;
    }
    @FXML
    private void initialize() {
        ObservableList<String> genderOptions = FXCollections.observableArrayList("Thương Gia","Phổ Thông");
        hangKHoang.setItems(genderOptions);
        DatabaseController.getLocationAirport(flightIdData);

        hangKHoang.valueProperty().addListener((observable, oldValue, newValue) -> {

            ObservableList<String> getSeatNumber = DatabaseController.getSeatNumber(newValue);


        });
    }











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
