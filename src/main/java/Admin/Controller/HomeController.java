package Admin.Controller;

import Customer.Controller.BookingController;
import Customer.Controller.SignupPassController;
import Database.DatabaseContection;
import Database.DatabaseController;
import com.sun.mail.imap.protocol.BODY;
import com.sun.mail.imap.protocol.ID;
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
import javafx.scene.control.cell.PropertyValueFactory;
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

    private void getRender() {
        List<Integer> FlightsIdAll = DatabaseController.getFlightIDs();

        for (Integer fl : FlightsIdAll) {
            List<LocalDateTime> TimeAll = DatabaseController.getTimesByFlightIds(fl);
            List<String> Sbdis = DatabaseController.getSBDiByFlightIds(fl);
            List<String> Sbdens = DatabaseController.getSBDenByFlightIds(fl);
            for (LocalDateTime time : TimeAll) {
                for (String sbdi : Sbdis) {
                    for (String sbden : Sbdens) {
                        CreateInfoFlight(sbdi, sbden, time, fl);
                        break;
                    }
                }
            }

        }
    }

    @FXML
    private TextField airportName;
    @FXML
    private ComboBox<String> airportCobo;
    @FXML
    private TextField locationAirport;
    @FXML
    private Label nameAdmin;
    public static String ADMINNAME;
    private void nameadmin(){
        String AdminName = ADMINNAME;
        nameAdmin.setText(AdminName);
    }
    @FXML
    private void initialize() {
        nameadmin();
        dialogInfo.setVisible(false);
        dialogInfo.setDisable(true);
        panelinfo.setPrefHeight(0);
        dialogInfo.setPrefHeight(0);

        getRender();
        classCompartment();
        getAirlines();
        SelectIDAirport(airport_ID, AirportList);
        initRevenue();
        initAllAirport();
        initPassengerList();
        initAllAirline();
        initSeat();
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
        SelectComboAirport(airportName, airportCobo);
    }

    // chung
    private void SelectComboAirport(TextField airportTextField, ComboBox<String> Comboboxx) {
        ObservableList<String> airportList = DatabaseController.getAirports();
        ObservableList<String> genderOptions = FXCollections.observableArrayList(airportList);
        FilteredList<String> filteredFlights = new FilteredList<>(genderOptions, s -> true);
        airportTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            flightFind(newValue, filteredFlights);
            // Check if the ComboBox has been opened, if not, open it
            if (!comboBoxOpened) {
                Comboboxx.show();
                comboBoxOpened = true;
            }
        });
        airportTextField.setOnMouseClicked(event -> {
            Comboboxx.show();
        });
        Comboboxx.setItems(filteredFlights);

        // gọi hàm lấy value cho textfield;
        getValueTextField(Comboboxx, airportTextField);
    }
    // chung

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

        List<Integer> flightIds = DatabaseController.getFlightIdsByAirports(airportStart, airportEnd);
        // System.out.println("Departure Airport: " + airportStart);
        // System.out.println("Destination Airport: " + airportEnd);

        // Kiểm tra và hiển thị danh sách flightIds
        if (airportStart.isEmpty() || airportEnd.isEmpty()) {
            SignupPassController.showAlert("Emty", "Sân bay đi và Sân bay đến không được trống !");
            return;
        }
        if (flightIds.isEmpty()) {
            // System.out.println("Không có chuyến bay nào từ " + airportStart + " đến " +
            // airportEnd);
            SignupPassController.showAlert("Not Fount",
                    "Không có chuyến bay nào từ " + airportStart + " đến " + airportEnd);
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
            // System.out.println(flightId);
            LocalDateTime flightTime = flightTimes.get(i);
            // System.out.println("Flight departure time: " + flightTime);
            CreateInfoFlight(airportStart, airportEnd, flightTime, flightId);
        }
    }

    // dia log
    @FXML
    private Label sbaydi;
    @FXML
    private Label sanbayden;
    @FXML
    private Label thoigiankhoihanh;
    @FXML
    private Label thoigiandendukien;
    @FXML
    private Label ddsbdi;
    @FXML
    private Label ddsbden;
    @FXML
    private Label cngoi;
    @FXML
    private Label giavept;
    @FXML
    private Label giavetg;

    public static String ddsb_di;
    public static String ddsb_den;
    public static String giaTG;
    public static String giaPT;

    public static void GetLocation(String ddsbdi, String ddsbden) {
        ddsb_di = ddsbdi;
        ddsb_den = ddsbden;
    }

    public static void GetPrice(String gia) {
        giaPT = gia;
    }

    public static void GetPrices(String gia) {
        giaTG = gia;
    }

    @FXML
    private Button DeleteFlight;

    private void viewDetailClick(int flightId, String time, String airport_start, String airport_end)
            throws IOException {
        DatabaseController.getLocationAirport(flightId);
        DatabaseController.getPriceTicket(flightId, "Phổ Thông");
        DatabaseController.getPrices(flightId, "Thương Gia");
        mainVBox.getChildren().clear();
        sbaydi.setText(airport_start);
        System.out.println(airport_end);
        sanbayden.setText(airport_end);
        thoigiankhoihanh.setText(time);
        ddsbdi.setText(ddsb_di);
        ddsbden.setText(ddsb_den);
        giavept.setText(giaPT);
        giavetg.setText(giaTG);

        dialogInfo.setVisible(true);
        dialogInfo.setDisable(false);
        panelinfo.setPrefHeight(400);
        dialogInfo.setPrefHeight(400);
        DeleteFlight.setOnAction(event -> {
            DeleteFlightClick(flightId); // Truyền flightId khi click vào nút
        });
    }

    private void DeleteFlightClick(int flightid) {

        int deleteStatus = -1;
        boolean continueDeletion = true;

        while (continueDeletion) {
            deleteStatus = DatabaseController.deleteFlight(flightid);

            if (deleteStatus == 1) {
                BaseController.showAlert("Thành công", "Xóa chuyến bay thành công !");
                continueDeletion = false;
            } else if (deleteStatus == -1) {
                ShowConfirmationDialog("Đặt vé");
                if (result == ButtonType.YES) {
                    DatabaseController.deleteBookingsByFlightId(flightid);
                } else {
                    continueDeletion = false;
                }
            } else if (deleteStatus == -2) {
                ShowConfirmationDialog("Bảng ghế");
                if (result == ButtonType.YES) {
                    DatabaseController.deleteSeatNumbersByFlightId(flightid);
                } else {
                    continueDeletion = false;
                }
            } else if (deleteStatus == -3) {
                ShowConfirmationDialog("Bảng vé");
                if (result == ButtonType.YES) {
                    DatabaseController.deleteTicketPricesByFlightId(flightid);
                } else {
                    continueDeletion = false;
                }
            } else if (deleteStatus == -4) {
                BaseController.showAlert("Lỗi", "Lỗi không xác định, xóa thất bại !");
                continueDeletion = false;
            }
        }
    }

    public static ButtonType result;

    public static void ShowConfirmationDialog(String khoaNgoai) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Xác nhận");
        confirmationDialog.setHeaderText("Bạn đang bị ràng buộc khóa ngoại " + khoaNgoai);
        confirmationDialog.setContentText("Có muốn xóa thông tin ID chuyến bay của Bảng " + khoaNgoai);
        confirmationDialog.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        result = confirmationDialog.showAndWait().orElse(ButtonType.NO);
        System.out.println(result);

    }

    @FXML
    private void thoatgialog() {
        dialogInfo.setVisible(false);
        dialogInfo.setDisable(true);
        panelinfo.setPrefHeight(0);
        dialogInfo.setPrefHeight(0);
        getRender();

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

    private void CreateInfoFlight(String sbDi, String sbDen, LocalDateTime Time, int flightId) {
        Font labelFont = new Font(14);
        Font iconfont = new Font(30);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd ' 'HH'h :' mm");
        String time = Time.format(formatter).toString();
        String idflight = Integer.toString(flightId);
        // Tạo các Label
        Label departureAirportLabel = new Label(sbDi);
        Label destinationAirportLabel = new Label(sbDen);
        Label flightIconLabel = new Label("🛫");
        Label timeLabel = new Label("Time: ");
        Label departureTimeLabel = new Label(time);
        Label bayThangLabel = new Label("ID : " + idflight);

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
                viewDetailClick(flightId, time, sbDi, sbDen); // Truyền flightId khi click vào nút
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
    private ComboBox<String> COMBOSBDI;
    @FXML
    private ComboBox<String> COMBOSBDEN;
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
    private ComboBox<String> HANGHK;

    private void SelectSB(ObservableList<String> airportList) {
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
    private void AddCick() {
        String SbdiData = SBDI.getText(); // 1/
        String SbdenData = SBDEN.getText(); // 2
        String NgayGioxuatphatData = NGAYXP.getText() + "T" + GIOXP.getText();
        // LocalDateTime localDateTimeXP = LocalDateTime.parse(NgayGioxuatphatData,
        // DateTimeFormatter.ISO_DATE_TIME); //3
        String NGAYDKData = NGAYDK.getText();
        // LocalDate localDateDuK = LocalDate.parse(NgayGioxuatphatData,
        // DateTimeFormatter.ISO_DATE_TIME);//4

        String VEPTData = VEPT.getText(); // 6
        String VETGData = VETG.getText();// 7
        int idsbdi = DatabaseController.getAirportIdByName(SbdiData);
        int idsbden = DatabaseController.getAirportIdByName(SbdenData);

        if (SbdenData != null && SbdiData != null
                && NgayGioxuatphatData != null && NGAYDKData != null
                && SLGHEPT.getText() != null && SLGHETG.getText() != null && VEPTData != null && VETGData != null) {
            int SLGHEPTData = Integer.parseInt(SLGHEPT.getText());
            int SLGHETGData = Integer.parseInt(SLGHETG.getText());
            int SumDataGhe = SLGHEPTData + SLGHETGData; // 5
            if (SLGHEPTData != 0 && SLGHETGData != 0) {
                boolean IsAdd = DatabaseController.addFlight(1, idsbdi, idsbden, NgayGioxuatphatData, NGAYDKData, 200,
                        SumDataGhe);
                if (IsAdd) {
                    BaseController.showAlert("Thành công", "Thêm chuyến bay thành công !");
                    Reset();
                } else {
                    BaseController.showAlert("Lỗi", "Lỗi thêm chuyến bay, Vui lòng kiểm tra lại thông tin đã nhập !");
                }
            }

        } else {
            BaseController.showAlert("Trống thông tin", "Bạn đã bỏ trống 1 thông tin !");
        }
    }

    public static int IDAirlineForm;
    private int IDAirline;

    private void getAirlines() {

        ObservableList<String> AirlinesDATA = DatabaseController.getAirlinesName();
        ObservableList<String> Airline = FXCollections.observableArrayList(AirlinesDATA);
        HANGHK.setItems(Airline);

        if (HANGHK.getValue() != null) {
            DatabaseController.GetAirlineID(HANGHK.getValue());
            IDAirline = IDAirlineForm;
            DatabaseController data = new DatabaseController();
            data.IDAirlineForm = IDAirline;
        }
    }

    private void Reset() {
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

    // Sân bay
    // thêm sân bay
    private String newAirport;
    private String newLocation;
    @FXML
    private TextField airport_ID;
    @FXML
    private ComboBox<String> AirportList;
    private int IDAir;

    @FXML
    private void addAriportClick() {
        newAirport = airportName.getText();
        newLocation = locationAirport.getText();
        if (newAirport.isEmpty()  || newLocation.isEmpty()) {
            BaseController.showAlert("Trống", "Vui lòng nhập đầy đủ thông tin !");

        } else {
            boolean isAddAirport = DatabaseController.addAirport(newAirport, newLocation);
            if (isAddAirport) {
                BaseController.showAlert("Thành công", "Thêm sân bay thành công !");
                initAllAirport();
            } else {
                BaseController.showAlert("Lỗi", "Lỗi Thêm sân bay,Hãy kiếm tra lại !");
            }
        }
    }

    // xóa sân bay

    private ButtonType warning;

    private void ShowWarning() {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Xác nhận");
        confirmationDialog.setHeaderText("Bạn đang bị ràng buộc khóa ngoại ");
        confirmationDialog.setContentText("Khi xóa sẽ xóa luôn các thông tin của các bạn liên quan, Bạn có chắc chắn");
        confirmationDialog.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        warning = confirmationDialog.showAndWait().orElse(ButtonType.NO);
    }

    @FXML
    private void deleteAirport() {
        newAirport = airportName.getText();
        newLocation = locationAirport.getText();

        if (true) {
            ShowWarning();
            if (warning == ButtonType.YES) {
                if (newAirport != null && airport_ID.getText().isEmpty()) {
                    boolean isDeleteAirport = DatabaseController.deleteAirportForName(newAirport);
                    if (isDeleteAirport) {
                        BaseController.showAlert("Thành công", "Xóa thành công sân bay !");
                        initAllAirport();
                    } else {
                        BaseController.showAlert("Lỗi", "Tên không tồn tại");
                    }
                }
                if (newAirport.isEmpty() || newAirport != null && airport_ID.getText() !=null) {
                    IDAir = Integer.parseInt(airport_ID.getText());
                    boolean isDeleteAirport = DatabaseController.deleteAirport(IDAir);
                    if (isDeleteAirport) {
                        BaseController.showAlert("Thành công", "Xóa thành công sân bay !");
                        initAllAirport();
                    } else {
                        BaseController.showAlert("Lỗi", "ID chuyến bay không tồn tại");
                    }
                }

            } else {
                return;
            }
        }
    }

    @FXML
    private void FindAirportClick() {
        String NameAirport = airportName.getText();

        if(NameAirport != null && airport_ID.getText().isEmpty()){
           boolean is =  DatabaseController.getAirportByName(NameAirport);
           if(is){
               ListAirport.clear();
               DatabaseController.getAirportByName(NameAirport);
               FindAirport();
           }
        }
        else {
            int AirportID = Integer.parseInt(airport_ID.getText());
            boolean is = DatabaseController.getAirportByID(AirportID);
            if(is){
                ListAirport.clear();
                DatabaseController.getAirportByID(AirportID);
                FindAirport();
            }
            else {
                BaseController.showAlert("Errorr","");
            }
        }
    }

    private void SelectIDAirport(TextField airportTextField, ComboBox<String> Comboboxx) {
        ObservableList<String> airportList = DatabaseController.getIDAirport();
        ObservableList<String> genderOptions = FXCollections.observableArrayList(airportList);
        FilteredList<String> filteredFlights = new FilteredList<>(genderOptions, s -> true);
        airportTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            flightFind(newValue, filteredFlights);
            // Check if the ComboBox has been opened, if not, open it
            if (!comboBoxOpened) {
                Comboboxx.show();
                comboBoxOpened = true;
            }
        });
        airportTextField.setOnMouseClicked(event -> {
            Comboboxx.show();
        });
        Comboboxx.setItems(filteredFlights);

        // gọi hàm lấy value cho textfield;
        getValueTextField(Comboboxx, airportTextField);
    }

    @FXML
    private TableView<Airport> TableViewAirport;
    @FXML
    private TableColumn<Airport, String> IDAirport;
    @FXML
    private TableColumn<Airport, String> NameAirport;
    @FXML
    private TableColumn<Airport, String> LocationForAirport;

    public static ObservableList<Airport> ListAirport = FXCollections.observableArrayList();

    private void initAllAirport() {
                     ListAirport.clear();
        if (DatabaseController.getAirportName()) {
            TableViewAirport.setItems(ListAirport);
            IDAirport.setCellValueFactory(new PropertyValueFactory<>("Airport_ID"));
            NameAirport.setCellValueFactory(new PropertyValueFactory<>("Airport_Name"));
            LocationForAirport.setCellValueFactory(new PropertyValueFactory<>("Airport_Location"));
        }

    }
    private void FindAirport(){
            TableViewAirport.setItems(ListAirport);
            IDAirport.setCellValueFactory(new PropertyValueFactory<>("Airport_ID"));
            NameAirport.setCellValueFactory(new PropertyValueFactory<>("Airport_Name"));
            LocationForAirport.setCellValueFactory(new PropertyValueFactory<>("Airport_Location"));

    }

    public static class Airport {
        private final String Airport_ID;
        private final String Airport_Name;
        private final String Airport_Location;

        public Airport(String Airport_ID, String Airport_Name, String Airport_Location) {
            this.Airport_ID = Airport_ID;
            this.Airport_Name = Airport_Name;
            this.Airport_Location = Airport_Location;
        }

        public String getAirport_ID() {
            return Airport_ID;
        }

        public String getAirport_Location() {
            return Airport_Location;
        }

        public String getAirport_Name() {
            return Airport_Name;
        }
    }

    // Sân bay

    // Ghế
    @FXML
    private TextField chair_name;
    @FXML
    private TextField FL_id;
    @FXML
    private ComboBox<String> select_chair;
    @FXML
    private TextField chairidtxt;
    @FXML
    private ComboBox<String > cobochairtxt;
    @FXML
    private void addChairsClick() {
        String chairName = chair_name.getText();
        String Compart = select_chair.getValue();
        int IDCompart = DatabaseController.getSeatType(Compart);

        if (chairName != null && FL_id.getText() != null && Compart != null) {
            int FLId = Integer.parseInt(FL_id.getText());
            boolean isAddSeat = DatabaseController.addSeatNumber(FLId, chairName, "0", IDCompart);
            if (isAddSeat) {
                BaseController.showAlert("Thành công", "Thêm thành công");
                initSeat();
            } else {
                BaseController.showAlert("Lỗi", "Thêm Thất bại");
            }
        }else{
            BaseController.showAlert("Lỗi" , "Không được để trống 1 vùng");
        }
    }
    @FXML
    private void deleteChair(){
        String txt = chairidtxt.getText();
        if(txt !=null){
            int txtid = Integer.parseInt(txt);
            boolean is = DatabaseController.DeleteChair(txtid);
            if(is){
                BaseController.showAlert("Success","Xóa thành công!")
                ;initSeat();
            }
            else {
                BaseController.showAlert("Failed","Xóa Lỗi!");
            }
        }else {
            BaseController.showAlert("Trống","Vui lòng nhập id ghế để xóa !");
        }
    }

    // Ghế cobobox
    private void classCompartment() {
        ObservableList<String> Compart = FXCollections.observableArrayList("Phổ Thông", "Thương Gia");
        select_chair.setItems(Compart);
    }

    // rendeer
    @FXML
    private TableView <Seats> TableViewSeat;
    @FXML
    private TableColumn <Seats , String > IDSeat;
        @FXML
    private TableColumn <Seats , String > SeatNumber;
        @FXML
    private TableColumn <Seats , String > TypeSeat;
        @FXML
    private TableColumn <Seats , String > IDFlight;
        @FXML
    private TableColumn <Seats , String > StatusSeat;

    public static ObservableList<Seats> ListSeat = FXCollections.observableArrayList();

    private void initSeat(){
        ListSeat.clear();
        if (DatabaseController.getSeatNumber()) {
            TableViewSeat.setItems(ListSeat);
            IDSeat.setCellValueFactory(new PropertyValueFactory<>("seatId"));
            SeatNumber.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
            TypeSeat.setCellValueFactory(new PropertyValueFactory<>("seatTypeId"));
            IDFlight.setCellValueFactory(new PropertyValueFactory<>("flightId"));
            StatusSeat.setCellValueFactory(new PropertyValueFactory<>("seatStatus"));
        }

    }
    public static class Seats {
        private final String seatId;
        private final String flightId;
        private final String seatNumber;
        private final String seatTypeId;
        private final String seatStatus;

        public Seats(String seatId, String flightId, String seatNumber, String seatTypeId, String seatStatus) {
            this.seatId = seatId;
            this.flightId = flightId;
            this.seatNumber = seatNumber;
            this.seatTypeId = seatTypeId;
            this.seatStatus = seatStatus;
        }

        public String getSeatId() {
            return seatId;
        }

        public String getFlightId() {
            return flightId;
        }

        public String getSeatNumber() {
            return seatNumber;
        }

        public String getSeatTypeId() {
            return seatTypeId;
        }

        public String getSeatStatus() {
            return seatStatus;
        }
    }


    // ghế

    // vé
    @FXML
    private TextField IDKH;
    @FXML
    private CheckBox Payed;

    private boolean IsBooo = false;
    @FXML
    private void FindBookClick() {


        if (IDKH.getText() != null) {
            int ID_KH = Integer.parseInt(IDKH.getText());
            IsBooo = true;

            List<Integer> IdFls = DatabaseController.getFlightForIDPass(ID_KH);
            for (Integer fl : IdFls) {
                List<String> sbdis = DatabaseController.getSBDiByFlightIds(fl);
                List<String> sbdens = DatabaseController.getSBDenByFlightIds(fl);
                List<LocalDateTime> times = DatabaseController.getTimesByFlightIds(fl);
                for (String sbdi : sbdis) {
                    for (String sbden : sbdens) {
                        for (LocalDateTime time : times) {
                            CreatFindAirport(sbdi, sbden, time, fl);
                            break;
                        }
                    }
                }
            }
        } else {
            BaseController.showAlert("Trống thông tin", "Vui lòng nhập ID khách hàng !");
        }
    }

    @FXML
    private Label sbaydi1;
    @FXML
    private Label sanbayden1;
    @FXML
    private Label thoigiankhoihanh1;
    private int ID_FLIGHT;
    private void initBooo(String di, String den , String time,int fl){
        if(IsBooo){
            ID_FLIGHT = fl;
            int ID_KH = Integer.parseInt(IDKH.getText());
            sbaydi1.setText(di);
            sanbayden1.setText(den);
            thoigiankhoihanh1.setText(time);

             String status =  DatabaseController.GetStatusPay(ID_KH,fl);
             if(status !="0"){
                 Payed.setSelected(true);
             }else {
                 Payed.setSelected(false);
             }

        }
    }

    @FXML
    private void SaveBookClick(){
        int ID_KH = Integer.parseInt(IDKH.getText());
        if(Payed.isSelected()){
            String status = "1";
            boolean is = DatabaseController.UpdateStatusPay(ID_KH,ID_FLIGHT,status);
            if(is){
                BaseController.showAlert("Success","Đã lưu");
            }else {
                BaseController.showAlert("Failed","Lỗi");
            }
        }
        else {
            String status = "0";
            boolean is = DatabaseController.UpdateStatusPay(ID_KH,ID_FLIGHT,status);
            if(is){
                BaseController.showAlert("Success","Đã lưu");
            }else {
                BaseController.showAlert("Failed","Lỗi");
            }
        }
    }
    @FXML
    private Tab InforSelect;
    @FXML
    private TabPane tabPane;

    private void ShowAirport(int fl, String time, String SBdi, String Sbden) {
        tabPane.getSelectionModel().select(InforSelect);
        initBooo(SBdi,Sbden,time,fl);
    }

    @FXML
    private VBox VBoxAirport;

    private void CreatFindAirport(String sbDi, String sbDen, LocalDateTime Time, int flightId) {
        Font labelFont = new Font(14);
        Font iconfont = new Font(30);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd ' 'HH'h :' mm");
        String time = Time.format(formatter).toString();
        String idflight = Integer.toString(flightId);
        // Tạo các Label
        Label departureAirportLabel = new Label(sbDi);
        Label destinationAirportLabel = new Label(sbDen);
        Label flightIconLabel = new Label("🛫");
        Label timeLabel = new Label("Time: ");
        Label departureTimeLabel = new Label(time);
        Label bayThangLabel = new Label("ID : " + idflight);

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
            ShowAirport(flightId, time, sbDi, sbDen); // Truyền flightId khi click vào nút
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
        VBoxAirport.getChildren().add(mainPane);
    }

    // vé

    // hãng hàng không
    @FXML
    private TextField TxtNameAirline;

    @FXML
    private void AddAirlineClick() {
        String NewNameAirline = TxtNameAirline.getText();

            if (NewNameAirline.isEmpty()) {
                BaseController.showAlert("Trống", "Vui lòng nhập !");

            } else {
                boolean IsAddAirline = DatabaseController.addAirline(NewNameAirline);
                if (IsAddAirline) {
                    BaseController.showAlert("Thành công", "Thêm thành công !");
                    initAllAirline();

                } else {
                    BaseController.showAlert("Lỗi", "Thêm thất bại !");

                }
            }
    }

    @FXML
    private void DeleteAirlineClick() {
        String NewNameAirline = TxtNameAirline.getText();
        if (NewNameAirline.isEmpty()) {
            BaseController.showAlert("Trống", "Vui lòng nhập !");
        } else {
            boolean IsAddAirline = DatabaseController.deleteAirline(NewNameAirline);
            if (IsAddAirline) {
                BaseController.showAlert("Thành công", "xóa thành công !");
                initAllAirline();
            } else {
                BaseController.showAlert("Lỗi", "xóa thất bại !");

            }

        }
    }


    @FXML
    private TableView<Airlines> TableViewAirline;
    @FXML
    private TableColumn<Airlines, String> AirlineID;
    @FXML
    private TableColumn<Airlines, String> NameAirline;

    public static ObservableList<Airlines> ListAirlines = FXCollections.observableArrayList();

    private void initAllAirline() {
        ListAirlines.clear();

        if (DatabaseController.getAirlines()) {
//            ListAirlines.clear();
            AirlineID.setCellValueFactory(new PropertyValueFactory<>("airline_id"));
            NameAirline.setCellValueFactory(new PropertyValueFactory<>("airline_name"));
            TableViewAirline.setItems(ListAirlines);

        }
    }
    @FXML
    private TextField AirlineText;
    @FXML
    private void FindAirlineClick(){
        String tx = AirlineText.getText();
        if (tx.isEmpty()) {

            BaseController.showAlert("Lỗi","Không được để trống !");

        }else {
            boolean is = DatabaseController.FindAirline(tx);
            if(is){

                ListAirlines.clear();
                DatabaseController.FindAirline(tx);
                FindAirline();
            }else {
                BaseController.showAlert("Null","");
            }
        }
    }

    private void FindAirline(){
        AirlineID.setCellValueFactory(new PropertyValueFactory<>("airline_id"));
        NameAirline.setCellValueFactory(new PropertyValueFactory<>("airline_name"));
        TableViewAirline.setItems(ListAirlines);
    }
    public static class Airlines {
        private final String airline_id;
        private final String airline_name;

        public Airlines(String airline_id ,String airline_name){
            this.airline_id = airline_id;
            this.airline_name = airline_name;
        }

        public String getAirline_id() {
            return airline_id;
        }

        public String getAirline_name() {
            return airline_name;
        }
    }

    // hãng hàng không

    // khách hàng
    @FXML
    private TextField namePass;
    @FXML
    private TextField phonePass;
    @FXML
    private TextField idPass;


    @FXML
    private void FindPassengerClick(){
        String Name = namePass.getText();
        String phone = phonePass.getText();
        String id = idPass.getText();

        if(Name !=null && phone != null && id.isEmpty()){
            boolean is = DatabaseController.getPassByNameAndPhone(Name,phone);
            if(is){
                ListPassenger.clear();
                DatabaseController.getPassByNameAndPhone(Name,phone);
                FindPassenger();
            }else {
                BaseController.showAlert("Lỗi","");
            }
        }if((Name !=null || phone != null)  && id !=null){
            int ID = Integer.parseInt(id);
            ListPassenger.clear();
            DatabaseController.getPassByID(ID);
            FindPassenger();
        }
        else {
            BaseController.showAlert("Lỗi","");
        }
    }
    private void FindPassenger(){
        IDPassenger.setCellValueFactory(new PropertyValueFactory<>("passenger_id"));
        NamePassenger.setCellValueFactory(new PropertyValueFactory<>("name"));
        GenderPassenger.setCellValueFactory(new PropertyValueFactory<>("gender"));
        BirPassenger.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        AddresssPassenger.setCellValueFactory(new PropertyValueFactory<>("address"));
        PhonePassenger.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
        EmailPassenger.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableViewPassenger.setItems(ListPassenger);
    }

    @FXML
    private TableView<Passenger> TableViewPassenger;
    @FXML
    private TableColumn<Passenger, String> IDPassenger;
    @FXML
    private TableColumn<Passenger, String> NamePassenger;
    @FXML
    private TableColumn<Passenger, String> GenderPassenger;
    @FXML
    private TableColumn<Passenger, String> BirPassenger;
    @FXML
    private TableColumn<Passenger, String> AddresssPassenger;
    @FXML
    private TableColumn<Passenger, String> PhonePassenger;
    @FXML
    private TableColumn<Passenger, String> EmailPassenger;

    public static ObservableList<Passenger> ListPassenger = FXCollections.observableArrayList();

    private void initPassengerList() {
        boolean isLoad = DatabaseController.getPassengers();
        if (isLoad) {
            IDPassenger.setCellValueFactory(new PropertyValueFactory<>("passenger_id"));
            NamePassenger.setCellValueFactory(new PropertyValueFactory<>("name"));
            GenderPassenger.setCellValueFactory(new PropertyValueFactory<>("gender"));
            BirPassenger.setCellValueFactory(new PropertyValueFactory<>("birthday"));
            AddresssPassenger.setCellValueFactory(new PropertyValueFactory<>("address"));
            PhonePassenger.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
            EmailPassenger.setCellValueFactory(new PropertyValueFactory<>("email"));
            TableViewPassenger.setItems(ListPassenger);
        }
    }

    public static class Passenger {
        private final String passenger_id;
        private final String name;
        private final String birthday;
        private final String gender;
        private final String address;
        private final String phone_number;
        private final String email;

        public Passenger(String passenger_id, String name, String birthday, String gender, String address,
                String phone_number, String email) {
            this.passenger_id = passenger_id;
            this.name = name;
            this.birthday = birthday;
            this.gender = gender;
            this.address = address;
            this.phone_number = phone_number;
            this.email = email;
        }

        public String getPassenger_id() {
            return passenger_id;
        }

        public String getName() {
            return name;
        }

        public String getBirthday() {
            return birthday;
        }

        public String getGender() {
            return gender;
        }

        public String getAddress() {
            return address;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public String getEmail() {
            return email;
        }

    }
    // khách hàng

    // doannh thu
    @FXML
    private Label RevenueToDay;
    @FXML
    private Label revenbetween;
    @FXML
    private Label flightbookedtoday;
    @FXML
    private Label ticketsforyear;
    @FXML
    private Label month;
    @FXML
    private Label QUY;

    @FXML
    private void initRevenue() {
        double RevenToday = DatabaseController.getRevenueToDay();
        double RevenBetween = DatabaseController.getRevenueBetween();
        double RevenMonth = DatabaseController.getTotalPriceForCurrentMonth();
        double RevenQuater = DatabaseController.getTotalRevenueForCurrentQuarter();
        int FlightsBooked = DatabaseController.getBookedFlights();
        int TicketsForYear = DatabaseController.getTiketsFlights();
        String RVTD = Double.toString(RevenToday);
        String RVBT = Double.toString(RevenBetween);
        String RVM = Double.toString(RevenMonth);
        String VRQ = Double.toString(RevenQuater);
        String FLBed = Integer.toString(FlightsBooked);
        String TKFY = Integer.toString(TicketsForYear);

        RevenueToDay.setText(RVTD + " VNĐ");
        revenbetween.setText(RVBT + " VNĐ");
        month.setText(RVM + " VNĐ");
        QUY.setText(VRQ + " VNĐ");
        flightbookedtoday.setText(FLBed);
        ticketsforyear.setText(TKFY);

    }
    // doannh thu
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private void logoutClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Admin/AdminView/Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
