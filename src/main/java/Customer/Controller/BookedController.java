package Customer.Controller;

import Database.DatabaseController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BookedController  {


    public static void getIdPassender(int id){

    }

    @FXML
    private void initialize(){
        DatabaseController.geBooked(1);
        SignupPassController.showAlert("sdfsf",departure_airportDATA);
    }

    public static int FLDATA;
    public static String departure_airportDATA;
    public static String destination_airportDATA;
    public static String departure_datetimeDATA;
    public static String seat_type_nameDATA;
    public static String seat_numberDATA;
    public static String priceDATA;

    public static void getFlightBooked(int fl , String departure_airport,String  destination_airport,String  departure_datetime,
                                      String  seat_type_name,String  seat_number,String  price){
        FLDATA = fl;
        departure_airportDATA = departure_airport;
        destination_airportDATA = destination_airport;
        departure_datetimeDATA = departure_datetime;
        seat_numberDATA = seat_number;
        seat_type_nameDATA = seat_type_name;
        priceDATA = price;

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
    private void booking_click(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Booking.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
