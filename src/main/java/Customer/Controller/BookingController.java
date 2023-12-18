package Customer.Controller;

import Database.DatabaseContection;
import Database.DatabaseController;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class BookingController  {
    @FXML
    private Button show;
    @FXML
    private  Label sbDi;
    @FXML
    private  Label thoiGianDi;
    @FXML
    private  Label sbDen;
    @FXML
    private  Label diaDiemDi;
    @FXML
    private  Label diaDiemDen;
    @FXML
    private  Label giaVe;
    @FXML
    private ComboBox <String> hangKHoang;
    @FXML
    private ComboBox <String> chonMaGhe;
    public static String Hang_Khoang;
    public static String maGhe;
    public static int idPassenger;
    public static int idSeat;
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
        DatabaseController.getLocationAirport(flightIdData);

    }

    public static void displayLocation(String departureLocation,String destinationLocation){
        departureLocationData =departureLocation;
        destinationLocationData = destinationLocation;

    }

    public static void displayPrice(String Price){
        priceData = Price;
    }
    public static void getIdPassenderForFlight (int id){
        idPassenger = id;
    }
    public static void getIdSeat(int id){
        idSeat = id;
    }





    public static String TimeFlight;
    public static String AirportStartForForm;
    public static String AirportEndForForm;
    private void show_click(){
        String time = TimeFlight;
        String airdi = AirportStartForForm;
        String airden = AirportStartForForm;
        FlightFindController frmflight = new FlightFindController();
        frmflight.TimeFlight = time;
        frmflight.AirportStartForForm = airdi;
        frmflight.AirportEndForForm = airden;

        sbDi.setText(airdi);
        sbDen.setText(airden);
        thoiGianDi.setText(time);
//        diaDiemDi.setText(departureLocationData);
//        diaDiemDen.setText(destinationLocationData);
//        show.setVisible(false);
    }

    @FXML
    public void initialize() {
        show_click();
        ObservableList<String> genderOptions = FXCollections.observableArrayList("Thương Gia","Phổ Thông");
        hangKHoang.setItems(genderOptions);

            hangKHoang.valueProperty().addListener((observable, oldValue, newValue) -> {
                if(sbDi.getText() != null){
                    ObservableList<String> getSeatNumber = DatabaseController.getSeatNumber(newValue,flightIdData);
                    chonMaGhe.setItems(getSeatNumber);
                    hangKHoang.setValue(newValue);
                    Hang_Khoang = hangKHoang.getValue();
                    DatabaseController.getPriceTicket(flightIdData,Hang_Khoang);
                    giaVe.setText(priceData + " VND");
                }else {
                    SignupPassController.showAlert("Thông báo","Vui lòng lấy thông tin chuyến bay !");
                }

            });
            chonMaGhe.valueProperty().addListener((observable, oldValue, newValue) ->{
                chonMaGhe.setValue(newValue);
                maGhe = chonMaGhe.getValue();
            });
    }
    @FXML
    private void bookingClick(ActionEvent event)throws IOException{
        if(Hang_Khoang.isEmpty()){
            SignupPassController.showAlert("Lỗi" , "Chưa chọn hạng khoang");
            return;
        }
        if (maGhe.isEmpty()){
            SignupPassController.showAlert("Lỗi" , "Chưa chọn Mã ghế");
            return;
        }
        DatabaseController.getSeatNumberId(flightIdData,Hang_Khoang,maGhe);

        LocalDate currentDate = LocalDate.now();
        boolean BookingSuccess = DatabaseController.addBooking(idPassenger,flightIdData,currentDate,idSeat,"0");
        if(BookingSuccess){
            SignupPassController.showAlert("Thành công","Đặt vé thành công !");
            Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/FlightFind.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else {
            SignupPassController.showAlert("Lỗi","Đặt vé Thất bại!");
        }
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
