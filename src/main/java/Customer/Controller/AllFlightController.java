package Customer.Controller;

import Database.DatabaseController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AllFlightController{
    private static String ParLocation;
    private static String DesLocation;
    private static int IdPassenger;
    public static void getIdPassender(int ID){
        IdPassenger = ID;
    }
    public static void GetLocation(String di,String den){
        ParLocation =di;
        DesLocation = den;
    }
    private void getRender(){
        List<Integer> FlightsIdAll = DatabaseController.getFlightIDs();

        for (Integer fl : FlightsIdAll){
            List<LocalDateTime> TimeAll = DatabaseController.getTimesByFlightIds(fl);
            List<String > Sbdis = DatabaseController.getSBDiByFlightIds(fl);
            List<String > Sbdens = DatabaseController.getSBDenByFlightIds(fl);
            for (LocalDateTime time : TimeAll){
                for ( String sbdi : Sbdis){
                    for ( String sbden : Sbdens){
                        DatabaseController.getLocationAirport(fl);
                        CreateInfoFlight(sbdi,sbden,time,fl,ParLocation,DesLocation);
                        break;
                    }
                }
            }

        }
    }
    @FXML
    private void initialize(){
        getRender();
    }
    // chuyển form
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private void logout_click(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static String TimeFlight;
    public static String AirportStartForForm;
    public static String AirportEndForForm;
    public static String LocationPar;
    public static String LocationDes;
    private void viewDetailClick(ActionEvent event , int flightId , String time ,String airport_start , String airport_end , String loctiondi, String locationden) throws IOException {
        BookingController.getIdPassenderForFlight(IdPassenger);
        BookingController.setFlightId(flightId, time , airport_start , airport_end);// đây là dữ liệu cần lấy sang  <------
        TimeFlight = time;
        LocationPar = loctiondi;
        LocationDes = locationden;
        AirportStartForForm = airport_start;
        AirportEndForForm = airport_end;
        BookingController frmbooking = new BookingController();
        frmbooking.TimeFlight = time;
        frmbooking.AirportStartForForm = airport_start;
        frmbooking.AirportEndForForm = airport_end;
        frmbooking.LocationPar = loctiondi;
        frmbooking.LocationDes = locationden;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Customer/CustomerView/Booking.fxml"));
        root = loader.load();


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private VBox mainVBox;

    private void CreateInfoFlight(String sbDi, String sbDen , LocalDateTime Time, int flightId , String loctiondi, String locationden ) {

        Font labelFont = new Font(14);
        Font iconfont = new Font(30);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd ' 'HH'h :' mm");
        String time = Time.format(formatter).toString();
        // Tạo các Label
        Label departureAirportLabel = new Label(sbDi);
        Label destinationAirportLabel = new Label(sbDen);
        Label flightIconLabel = new Label("🛫");
        Label timeLabel = new Label("Time: ");
        Label departureTimeLabel = new Label(time);
        Label bayThangLabel = new Label("Bay thẳng");

        // Đặt Font cho các Label
        departureAirportLabel.setFont(labelFont);
        destinationAirportLabel.setFont(labelFont);
        destinationAirportLabel.setPadding(new Insets(0, 0, 10, 0));
        ;
        flightIconLabel.setFont(iconfont);
        timeLabel.setFont(labelFont);
        departureTimeLabel.setFont(labelFont);
        bayThangLabel.setFont(labelFont);
        Button viewDetailButton = new Button("Xem chi tiết");
        viewDetailButton.setOnAction(event -> {
            try {
                viewDetailClick(event, flightId , time , sbDi, sbDen , loctiondi,locationden); // Truyền flightId khi click vào nút
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Tạo các container
        BorderPane mainPane = new BorderPane();
        BorderPane flightDetailsPane = new BorderPane();
        flightDetailsPane.minWidth(200);
        flightDetailsPane.setPrefWidth(200);
        // BorderPane timePane = new BorderPane();
        BorderPane buttonPane = new BorderPane();
        buttonPane.minWidth(200);
        buttonPane.setPrefWidth(200);
        VBox vBox = new VBox(flightDetailsPane, bayThangLabel);
        vBox.setSpacing(50);
        BorderPane Topbr = new BorderPane();
        BorderPane Bottombr = new BorderPane();
        flightDetailsPane.setTop(Topbr);
        // Thêm các thành phần vào các container
        Bottombr.setLeft(timeLabel);
        Bottombr.setCenter(departureTimeLabel);

        Topbr.setLeft(departureAirportLabel);
        Topbr.setCenter(flightIconLabel);
        Topbr.setRight(destinationAirportLabel);
        flightDetailsPane.setBottom(Bottombr);
        buttonPane.setCenter(viewDetailButton);

        // Đặt background cho BorderPane chứa toàn bộ nội dung
        mainPane.setStyle("-fx-background-color: #fff;");

        // Đặt style cho mainPane
        mainPane.getStyleClass().add("node-with-shadow");
        mainPane.setPadding(new Insets(10));

        // Thêm các container vào mainPane
        mainPane.setLeft(vBox);
        mainPane.setCenter(bayThangLabel);
        mainPane.setRight(buttonPane);
        mainPane.getStylesheets().add(getClass().getResource("/Customer/CustomerAccess/Base.css").toExternalForm());
        mainVBox.getChildren().add(mainPane);
    }

}
