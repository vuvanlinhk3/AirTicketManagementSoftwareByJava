package Customer.Controller;

import Database.DatabaseController;
import com.sun.mail.imap.protocol.ID;
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

    public static int FlightIDForm;
    public static String ParAirportForm;
    public static String DesAirportForm;
    public static String TimeParForm;
    public static String typeSeatForm;
    public static String SeatNumberForm;
    public static String PriceForm;
    private static int IDPassenger;
    public static void getIdPassender(int id){
        IDPassenger = id;
        DatabaseController.getBooked(IDPassenger);
    }
    public static void GetBooked(){
        int FlightID = FlightIDForm;
        String ParAirport = ParAirportForm;
        String DesAirport = DesAirportForm;
        String TimePar = TimeParForm;
        String typeSeat = typeSeatForm;
        String SeatNumber = SeatNumberForm;
        String Price = typeSeatForm;
        DatabaseController data = new DatabaseController();
        data.FlightIDForm = FlightID;
        data.ParAirportForm = ParAirport;
        data.DesAirportForm = DesAirport;
        data.TimeParForm = TimePar;
        data.typeSeatForm = typeSeat;
        data.SeatNumberForm = SeatNumber;
        data.PriceForm = Price;

    }



    @FXML
    private void initialize(){

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
    @FXML
    private void logout_click(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
