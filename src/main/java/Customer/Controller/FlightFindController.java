package Customer.Controller;

import Database.DatabaseController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FlightFindController {
    @FXML
    private TextField start_TF;
    @FXML
    private ComboBox<String> airport_start_combo;
    @FXML
    private TextField end_TF;
    @FXML
    private ComboBox<String> airport_end_combo;
    // lấy dữ liệu
    private String airportStart;
    private String airportEnd;

    private boolean comboBoxOpened;
    private String selectedValue;
    private String StringValue;
    private static int IdPassenger;

    public static void  getIdPassender(int id){
        IdPassenger = id;
    }
    @FXML
    private void initialize() {
        ObservableList<String> airportList = DatabaseController.getAirports();
        ObservableList<String> genderOptions = FXCollections.observableArrayList(airportList);

        FilteredList<String> filteredFlights = new FilteredList<>(genderOptions, s -> true);
        start_TF.textProperty().addListener((observable, oldValue, newValue) -> {
            flightFind(newValue, filteredFlights);
            // Check if the ComboBox has been opened, if not, open it
            if (!comboBoxOpened) {
                airport_start_combo.show();
                comboBoxOpened = true;
            }
        });
        start_TF.setOnMouseClicked(event -> {
            airport_start_combo.show();
        });
        airport_start_combo.setItems(filteredFlights);

        // gọi hàm lấy value cho textfield;
        getValueTextField(airport_start_combo, start_TF);

        try {
            start_TF.textProperty().addListener((observable, oldValue, newValue) -> {

                if (newValue != null) {
                    StringValue = newValue.toString();
                    // DatabaseController.getDestinationAirport(StringValue);
                    ObservableList<String> DestinationList = DatabaseController.getDestinationAirport(StringValue);
                    ObservableList<String> DestinationOptions = FXCollections.observableArrayList(DestinationList);
                    airport_end_combo.setItems(DestinationOptions);
                    getValueTextField(airport_end_combo, end_TF);
                } else {
                    return;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @FXML
    private void findClick() {
        mainVBox.getChildren().clear();
        airportStart = start_TF.getText();
        airportEnd = end_TF.getText();

        List<Integer> flightIds = DatabaseController.getFlightIdsByAirports(airportStart,airportEnd );
//        System.out.println("Departure Airport: " + airportStart);
//        System.out.println("Destination Airport: " + airportEnd);

//         Kiểm tra và hiển thị danh sách flightIds
        if(airportStart.isEmpty() || airportEnd.isEmpty()){
            SignupPassController.showAlert("Emty","Sân bay đi và Sân bay đến không được trống !");
            return;
        }
        if (flightIds.isEmpty()) {
//            System.out.println("Không có chuyến bay nào từ " + airportStart + " đến " + airportEnd);
            SignupPassController.showAlert("Not Fount","Không có chuyến bay nào từ " + airportStart + " đến " + airportEnd);
            return;
        } else {
            System.out.println("Danh sách flightIds:");
            for (Integer flightId : flightIds) {
                System.out.println(flightId);
            }
        }
        List<LocalDateTime> flightTimes = DatabaseController.getFlightTimesByFlightIds(flightIds);

        for (int i = 0; i < flightIds.size(); i++) {
            int flightId = flightIds.get(i);
//            System.out.println(flightId);
            LocalDateTime flightTime = flightTimes.get(i);
//            System.out.println("Flight departure time: " + flightTime);
            CreateInfoFlight(airportStart,airportEnd ,flightTime, flightId);
        }
    }


    // chuyển form
    private Stage stage;
    private Scene scene;
    private Parent root;
    private void viewDetailClick(ActionEvent event , int flightId , String time ,String airport_start , String airport_end) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Customer/CustomerView/Booking.fxml"));
        root = loader.load();
        BookingController.getIdPassenderForFlight(IdPassenger);
        BookingController.setFlightId(flightId, time , airport_start , airport_end);// đây là dữ liệu cần lấy sang  <------

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void back_click(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Home.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void logout_click(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/FlightFind.fxml"));
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

    @FXML
    private VBox mainVBox;

    private void CreateInfoFlight(String sbDi, String sbDen , LocalDateTime Time,int flightId ) {
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
                viewDetailClick(event, flightId , time , airportStart, airportEnd); // Truyền flightId khi click vào nút
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
