package Customer.Controller;

import Database.DatabaseContection;
import Database.DatabaseController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookedController {
    @FXML
    private TableView<Book> flightTableView;
    @FXML
    private TableColumn<Book, String> flightIdColumn;

    @FXML
    private TableColumn<Book, String> flightDestinationColumn;

    @FXML
    private TableColumn<Book, String> flightDepartureTimeColumn;

    @FXML
    private TableColumn<Book, String> flightSeatTypeColumn;

    @FXML
    private TableColumn<Book, String> flightSeatNumberColumn;

    @FXML
    private TableColumn<Book, String> flightPriceColumn;

    public static int IdPassenger;

    public static ObservableList<Book> ListBook = FXCollections.observableArrayList();


    @FXML
    private void initialize() {
        int IDpas = IdPassenger;
        System.out.println(IDpas);

        if (DatabaseController.getBooked(IDpas)) {
            // Assuming showCombo() is another method in your controller
            showCombo();

            // Assuming ListBook is a class variable
            flightTableView.setItems(ListBook);
            flightIdColumn.setCellValueFactory(new PropertyValueFactory<>("booking_id"));
            flightDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("departure_airport"));
            flightDepartureTimeColumn.setCellValueFactory(new PropertyValueFactory<>("departure_datetime"));
            flightSeatTypeColumn.setCellValueFactory(new PropertyValueFactory<>("seat_type_name"));
            flightSeatNumberColumn.setCellValueFactory(new PropertyValueFactory<>("seat_number"));
            flightPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        }

    }



    @FXML
    private ComboBox<String> IDBook;
    private boolean comboBoxOpened;
    private String selectedValue;
    private String StringValue;
    @FXML
    private TextField TxtD;

    private void showCombo() {
        ObservableList<String> ID = DatabaseController.getIDBook();
        ObservableList<String> genderOptions = FXCollections.observableArrayList(ID);
        FilteredList<String> filteredFlights = new FilteredList<>(genderOptions, s -> true);
        TxtD.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!comboBoxOpened) {
                IDBook.show();
                comboBoxOpened = true;
            }
        });
        IDBook.setOnMouseClicked(event -> {
            IDBook.show();
        });
        IDBook.setItems(filteredFlights);

        // gọi hàm lấy value cho textfield;
        getValueTextField(IDBook, TxtD);

        try {
            TxtD.textProperty().addListener((observable, oldValue, newValue) -> {

                if (newValue != null) {
                    StringValue = newValue.toString();
                    // DatabaseController.getdestination_airport(StringValue);
                    ObservableList<String> DestinationList = DatabaseController.getDestinationAirport(StringValue);
                    ObservableList<String> DestinationOptions = FXCollections.observableArrayList(DestinationList);
                    IDBook.setItems(DestinationOptions);
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
    private void HuyVeClick() {
        if (TxtD.getText().isEmpty()) {
            SignupPassController.showAlert("Trống", "");
        } else {
            System.out.println(TxtD.getText());
            int SelecID = Integer.parseInt(TxtD.getText());
            Boolean IsDelete = DatabaseController.DeleteBook(SelecID);
            if (IsDelete) {
                SignupPassController.showAlert("Thành công", "Hủy vé thành công !");
            } else {
                SignupPassController.showAlert("Lỗi", "Hủy vé thất bại !");
            }
        }

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
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/FlightFind.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void logout_click(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public static class Book {
        private final String booking_id;
        private final String departure_airport;
        private final String destination_airport;
        private final String departure_datetime;
        private final String seat_type_name;
        private final String seat_number;
        private final String price;

        public Book(String booking_id, String departure_airport, String destination_airport,
                    String departure_datetime, String seat_type_name, String seat_number, String price) {
            this.booking_id = booking_id;
            this.departure_airport = departure_airport;
            this.destination_airport = destination_airport;
            this.departure_datetime = departure_datetime;
            this.seat_type_name = seat_type_name;
            this.seat_number = seat_number;
            this.price = price;
        }

        public String getBooking_id() {
            return booking_id;
        }

        public String getDeparture_airport() {
            return departure_airport;
        }

        public String getDestination_airport() {
            return destination_airport;
        }

        public String getDeparture_datetime() {
            return departure_datetime;
        }

        public String getSeat_type_name() {
            return seat_type_name;
        }

        public String getSeat_number() {
            return seat_number;
        }

        public String getPrice() {
            return price;
        }
    }

}
