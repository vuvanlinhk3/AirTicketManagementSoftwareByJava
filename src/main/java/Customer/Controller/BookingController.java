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
    private Label thoiGianDi;
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
    private static String Hang_Khoang;
    private static String maGhe;

    // Lấy id chuyến bay
    public static int flightIdData;
    public static String  flightTimeData;
    public static String  airportStartData;
    public static String  airportEndData;
    public static String  departureLocationData;
    public static String  destinationLocationData;
    public static String  priceData;

    // Constructor nhận tham số flightId
    public static void setFlightId(int flightId , String  time , String airport_start, String airport_end) {
        flightIdData = flightId;
        flightTimeData = time;
        airportStartData = airport_start;
        airportEndData = airport_end;

//        getFlightId(flightId);
    }

    public static void displayLocation(String departureLocation,String destinationLocation){
        departureLocationData =departureLocation;
        destinationLocationData = destinationLocation;
    }

    public static void displayPrice(String Price){
        priceData = Price;
    }

    @FXML
    private void initialize() {
        /// -----
        sbDi.setText(airportStartData);
        sbDen.setText(airportEndData);
        thoiGianDi.setText(flightTimeData);
        diaDiemDi.setText(departureLocationData);
        diaDiemDen.setText(destinationLocationData);
        DatabaseController.getLocationAirport(flightIdData);
        ObservableList<String> genderOptions = FXCollections.observableArrayList("Thương Gia","Phổ Thông");
        hangKHoang.setItems(genderOptions);


        hangKHoang.valueProperty().addListener((observable, oldValue, newValue) -> {

            ObservableList<String> getSeatNumber = DatabaseController.getSeatNumber(newValue,flightIdData);
            chonMaGhe.setItems(getSeatNumber);
            hangKHoang.setValue(newValue);
            Hang_Khoang = hangKHoang.getValue();
            System.out.println(flightIdData);
            System.out.println(Hang_Khoang);
            DatabaseController.getPriceTicket(flightIdData,Hang_Khoang);
            SignupPassController.showAlert("hi",departureLocationData + destinationLocationData);
            giaVe.setText(priceData);

        });
        chonMaGhe.valueProperty().addListener((observable, oldValue, newValue) ->{
            chonMaGhe.setValue(newValue);
            maGhe = chonMaGhe.getValue();
        });
    }
    @FXML
    private void bookingClick(){
        if(Hang_Khoang.isEmpty()){
            SignupPassController.showAlert("Lỗi" , "Chưa chọn hạng khoang");
            return;
        }
        if (maGhe.isEmpty()){
            SignupPassController.showAlert("Lỗi" , "Chưa chọn Mã ghế");
            return;
        }



        SignupPassController.showAlert("Thành công" , "Đặt vé thành công");
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
