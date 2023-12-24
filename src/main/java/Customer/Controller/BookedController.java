package Customer.Controller;

import Database.DatabaseController;
import com.sun.mail.imap.protocol.ID;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class BookedController {
    @FXML
    private TableView<Map<String, Object>> flightTableView;
    @FXML
    private TableColumn<DatabaseController, String> flightIdColumn;

    @FXML
    private TableColumn<DatabaseController, String> flightDestionColumn;
    @FXML
    private TableColumn<DatabaseController, String> flightDestinationColumn;

    @FXML
    private TableColumn<DatabaseController, String> flightDepartureTimeColumn;

    @FXML
    private TableColumn<DatabaseController, String> flightSeatTypeColumn;

    @FXML
    private TableColumn<DatabaseController, String> flightSeatNumberColumn;

    @FXML
    private TableColumn<DatabaseController, String> flightPriceColumn;

    public static int IdPassenger;

    @FXML
    private void initialize() {
        int IDpas = IdPassenger;
        HomeController frm = new HomeController();
        frm.IdPassenger = IDpas;
        showCombo();

//        flightIdColumn.setCellValueFactory(new PropertyValueFactory<>("flight_id"));
//        flightDestionColumn.setCellValueFactory(new PropertyValueFactory<>("departure_airport"));
//        flightDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination_airport"));
//        flightDepartureTimeColumn.setCellValueFactory(new PropertyValueFactory<>("departure_datetime"));
//        flightSeatTypeColumn.setCellValueFactory(new PropertyValueFactory<>("seat_type_name"));
//        flightSeatNumberColumn.setCellValueFactory(new PropertyValueFactory<>("seat_number"));
//        flightPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
//
//        ObservableList<Map<String, Object>> flights = DatabaseController.getBooked(IDpas);
//
//        flightTableView.setItems(flights);
    }

    @FXML
    private ComboBox<String> IDBook;
    private boolean comboBoxOpened;
    private String selectedValue;
    private String StringValue;
    @FXML
    private TextField TxtD;
    private void showCombo(){
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
                    // DatabaseController.getDestinationAirport(StringValue);
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
    private void HuyVeClick(){
        int SelecID = Integer.getInteger(TxtD.getText());
        Boolean IsDelete = DatabaseController.DeleteBook(SelecID);
        if (IsDelete){
            SignupPassController.showAlert("Thành công","Hủy vé thành công !");
        }else {
            SignupPassController.showAlert("Lỗi","Hủy vé thất bại !");
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
}
