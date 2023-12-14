package Customer.Controller;

import Database.DatabaseContection;
import Database.DatabaseController;
import com.mysql.cj.protocol.x.XMessage;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;

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
        airportStart = start_TF.getText();

        try {
            start_TF.textProperty().addListener((observable, oldValue, newValue) -> {

                if (newValue != null) {
                    StringValue = newValue.toString();
//                    System.out.println(StringValue);
                    // DatabaseController.getDestinationAirport(StringValue);

                    ObservableList<String> DestinationList = DatabaseController.getDestinationAirport(StringValue);
                    ObservableList<String> DestinationOptions = FXCollections.observableArrayList(DestinationList);
                    airport_end_combo.setItems(DestinationOptions);
                    getValueTextField(airport_end_combo, end_TF);
                    airportEnd = end_TF.getText();
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
                        System.out.println(StringValue);

                        ComboboxPoint.setValue(newValue);
                        AddressPoints.setText(StringValue);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            // System.out.println(selectedValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // lấy dữ liệu từ database
    private static int flightIdData;
    private static String departureAirportData;
    private static String destinationAirportData;
    private static String destinationLocationData;
    private static LocalDateTime departureDatetimeData;
    private static LocalDateTime arrivalDatetimeData;
    private static int availableSeatsData;

    public static void getValueFlight(
            int flightId, String departureAirport, String destinationAirport,
            String destinationLocation, LocalDateTime departureDatetime,
            LocalDateTime arrivalDatetime, int availableSeats) {
        flightIdData = flightId;
        departureAirportData = departureAirport;
        destinationAirportData = destinationAirport;
        destinationLocationData = destinationLocation;
        departureDatetimeData = departureDatetime;
        arrivalDatetimeData = arrivalDatetime;
        availableSeatsData = availableSeats;

    }

    @FXML
    private void findClick() {
        DatabaseController.loadDataFromDatabase(airportStart, airportEnd);
        CreateInfoFlight();
        System.out.println(flightIdData);
        System.out.println(departureAirportData);

    }

    private void viewDetailClick() {

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

    private void CreateInfoFlight() {
        Font labelFont = new Font(14);
        Font iconfont = new Font(30);

        // Tạo các Label
        Label departureAirportLabel = new Label("SB HANOI");
        Label destinationAirportLabel = new Label("SB VINH");
        Label flightIconLabel = new Label("🛫");
        Label timeLabel = new Label("Time: ");
        Label departureTimeLabel = new Label("8h:00:00");
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
