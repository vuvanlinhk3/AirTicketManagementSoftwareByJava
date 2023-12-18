package Admin.Controller;

import Customer.Controller.BookingController;
import Customer.Controller.SignupPassController;
import Database.DatabaseContection;
import Database.DatabaseController;
import javafx.application.Application;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomeController {
    @FXML
    private TextField start_TF;
    @FXML
    private DialogPane dialogInfo;
    @FXML
    private Pane panelinfo;
    @FXML
    private ComboBox<String> airport_start_combo;
    @FXML
    private TextField end_TF;
    @FXML
    private ComboBox<String> airport_end_combo;
    @FXML
    private ScrollPane pane_ngoai;
    // lấy dữ liệu
    private String airportStart;
    private String airportEnd;

    private boolean comboBoxOpened;
    private String selectedValue;
    private String StringValue;

    @FXML
    private void initialize() {
        dialogInfo.setVisible(false);
        dialogInfo.setDisable(true);
        panelinfo.setPrefHeight(0);
        dialogInfo.setPrefHeight(0);

        List<Integer> FlightsIdAll = DatabaseController.getFlightIDs();

        for (Integer fl : FlightsIdAll){
            List<LocalDateTime> TimeAll = DatabaseController.getTimesByFlightIds(fl);
            List<String > Sbdis = DatabaseController.getSBDiByFlightIds(fl);
            List<String > Sbden = DatabaseController.getSBDenByFlightIds(fl);
            for (LocalDateTime time : TimeAll){
                for ( String sbdi : Sbdis){
                    for ( String sbden : Sbdis){
                        CreateInfoFlight(sbdi,sbden,time,fl);
                        break;
                    }
                }
            }

        }


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
        SelectSB(airportList);
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
            mainVBox.getChildren().clear();
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


    private void viewDetailClick( int flightId , String time , String airport_start , String airport_end) throws IOException {
        mainVBox.getChildren().clear();
        dialogInfo.setVisible(true);
        dialogInfo.setDisable(false);
        panelinfo.setPrefHeight(400);
        dialogInfo.setPrefHeight(400);

    }
    @FXML
    private void thoatgialog(){
        dialogInfo.setVisible(false);
        dialogInfo.setDisable(true);
        panelinfo.setPrefHeight(0);
        dialogInfo.setPrefHeight(0);
        List<Integer> FlightsIdAll = DatabaseController.getFlightIDs();
        for (Integer fl : FlightsIdAll){
            List<LocalDateTime> TimeAll = DatabaseController.getTimesByFlightIds(fl);
            List<String > Sbdis = DatabaseController.getSBDiByFlightIds(fl);
            List<String > Sbden = DatabaseController.getSBDenByFlightIds(fl);
            for (LocalDateTime time : TimeAll){
                for ( String sbdi : Sbdis){
                    for ( String sbden : Sbdis){
                        CreateInfoFlight(sbdi,sbden,time,fl);
                        break;
                    }
                }
            }

        }

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
                viewDetailClick( flightId , time , airportStart, airportEnd); // Truyền flightId khi click vào nút
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
        mainPane.setStyle(" -fx-border-color: transparent transparent Black transparent;");

        // Đặt style cho mainPane
        mainPane.getStyleClass().add("node-with-shadow");
        mainPane.setPadding(new Insets(10));

        // Thêm các container vào mainPane
        mainPane.setLeft(vBox);
        mainPane.setCenter(bayThangLabel);
        mainPane.setRight(buttonPane);
        mainPane.getStylesheets().add(getClass().getResource("/Customer/CustomerAccess/Base.css").toExternalForm());
        mainVBox.getChildren().add(mainPane);
        VBox.setVgrow(pane_ngoai, Priority.ALWAYS);
    }

    // scene 2 thêm chuyến bay
    @FXML
    private ComboBox <String> COMBOSBDI;
    @FXML
    private ComboBox <String>COMBOSBDEN;
    @FXML
    private TextField SBDI;
    @FXML
    private TextField SBDEN;
    @FXML
    private TextField NGAYXP;
    @FXML
    private TextField GIOXP;
    @FXML
    private TextField NGAYDK;
    @FXML
    private TextField SLGHEPT;
    @FXML
    private TextField SLGHETG;
    @FXML
    private TextField VEPT;
    @FXML
    private TextField VETG;
    @FXML
    private ComboBox <String> HANGHK;

    private void SelectSB(ObservableList<String> airportList){
        ObservableList<String> genderOptions = FXCollections.observableArrayList(airportList);

        FilteredList<String> filteredFlights = new FilteredList<>(genderOptions, s -> true);
        SBDI.textProperty().addListener((observable, oldValue, newValue) -> {
            flightFind(newValue, filteredFlights);
            // Check if the ComboBox has been opened, if not, open it
            if (!comboBoxOpened) {
                COMBOSBDI.show();
                comboBoxOpened = true;
            }
        });
        SBDI.setOnMouseClicked(event -> {
            COMBOSBDI.show();
        });
        COMBOSBDI.setItems(filteredFlights);

        // gọi hàm lấy value cho textfield;
        getValueTextField(COMBOSBDI, SBDI);

        FilteredList<String> filtered_Flights = new FilteredList<>(genderOptions, s -> true);
        SBDEN.textProperty().addListener((observable, oldValue, newValue) -> {
            flightFind(newValue, filtered_Flights);
            // Check if the ComboBox has been opened, if not, open it
            if (!comboBoxOpened) {
                COMBOSBDEN.show();
                comboBoxOpened = true;
            }
        });
        SBDEN.setOnMouseClicked(event -> {
            COMBOSBDEN.show();
        });
        COMBOSBDEN.setItems(filtered_Flights);

        // gọi hàm lấy value cho textfield;
        getValueTextField(COMBOSBDEN, SBDEN);
    }

    @FXML
    private void AddCick(){
            String SbdiData = SBDI.getText(); //1/
            String SbdenData = SBDEN.getText(); //2
            String NgayGioxuatphatData = NGAYXP.getText() +"T"+ GIOXP.getText();
//            LocalDateTime localDateTimeXP = LocalDateTime.parse(NgayGioxuatphatData, DateTimeFormatter.ISO_DATE_TIME); //3

            String NGAYDKData = NGAYDK.getText();
//        LocalDate localDateDuK = LocalDate.parse(NgayGioxuatphatData, DateTimeFormatter.ISO_DATE_TIME);//4
            int SLGHEPTData = Integer.parseInt(SLGHEPT.getText());
            int SLGHETGData =Integer.parseInt( SLGHETG.getText());
            int SumDataGhe = SLGHEPTData + SLGHETGData; // 5
            String VEPTData = VEPT.getText();  //6
            String VETGData = VETG.getText();//7
            int idsbdi= DatabaseController.getAirportIdByName(SbdiData);
            int idsbden= DatabaseController.getAirportIdByName(SbdenData);

            if(
                    SbdenData !=null && SbdiData !=null
                    && NgayGioxuatphatData !=null && NGAYDKData !=null
                    && SumDataGhe !=0 && VEPTData !=null && VETGData !=null
            ){
               boolean IsAdd = DatabaseController.addFlight(1,idsbdi,idsbden,NgayGioxuatphatData,NGAYDKData,200,SumDataGhe);
                if (IsAdd){
                    BaseController.showAlert("Thành công","Thêm chuyến bay thành công !");
                    Reset();
                }
                else {
                    BaseController.showAlert("Lỗi","Lỗi thêm chuyến bay, Vui lòng kiểm tra lại thông tin đã nhập !");
                }

            }else {
                BaseController.showAlert("Trống thông tin","Bạn đã bỏ trống 1 thông tin !");
            }
    }

    private void Reset(){
        SBDI.setText("");
        SBDEN.setText("");
        NGAYXP.setText("");
        GIOXP.setText("");
        NGAYDK.setText("");
        SLGHEPT.setText("");
        SLGHETG.setText("");
        VEPT.setText("");
        VETG.setText("");
    }
}
