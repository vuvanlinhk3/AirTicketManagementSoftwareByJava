package Admin.Controller;

import Customer.Controller.BookingController;
import Customer.Controller.SignupPassController;
import Database.DatabaseContection;
import Database.DatabaseController;
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
    private void getRender(){
        List<Integer> FlightsIdAll = DatabaseController.getFlightIDs();

        for (Integer fl : FlightsIdAll){
            List<LocalDateTime> TimeAll = DatabaseController.getTimesByFlightIds(fl);
            List<String > Sbdis = DatabaseController.getSBDiByFlightIds(fl);
            List<String > Sbdens = DatabaseController.getSBDenByFlightIds(fl);
            for (LocalDateTime time : TimeAll){
                for ( String sbdi : Sbdis){
                    for ( String sbden : Sbdens){
                        CreateInfoFlight(sbdi,sbden,time,fl);
                        break;
                    }
                }
            }

        }
    }


    @FXML
    private TextField airportName;
    @FXML
    private ComboBox <String> airportCobo;
    @FXML
    private TextField locationAirport;
    @FXML
    private void initialize() {
        dialogInfo.setVisible(false);
        dialogInfo.setDisable(true);
        panelinfo.setPrefHeight(0);
        dialogInfo.setPrefHeight(0);

        getRender();
        classCompartment();

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
        SelectComboAirport(airportName,airportCobo);
    }


    // chung
    private void SelectComboAirport( TextField airportTextField , ComboBox <String> Comboboxx){
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
    public static void GetLocation(String ddsbdi ,String ddsbden){
        ddsb_di = ddsbdi;
        ddsb_den = ddsbden;
    }
    public static void GetPrice(String gia){
        giaPT =gia;
    }
    public static void GetPrices(String gia){
        giaTG = gia;
    }

    @FXML
    private Button DeleteFlight;
    private void viewDetailClick( int flightId , String time , String airport_start , String airport_end) throws IOException {
        DatabaseController.getLocationAirport(flightId);
        DatabaseController.getPriceTicket(flightId,"Phổ Thông");
        DatabaseController.getPrices(flightId,"Thương Gia");
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
    private void DeleteFlightClick(int flightid){

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
        public static void ShowConfirmationDialog(String khoaNgoai){
            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Xác nhận");
            confirmationDialog.setHeaderText("Bạn đang bị ràng buộc khóa ngoại " + khoaNgoai);
            confirmationDialog.setContentText("Có muốn xóa thông tin ID chuyến bay của Bảng " + khoaNgoai);
            confirmationDialog.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            result = confirmationDialog.showAndWait().orElse(ButtonType.NO);
            System.out.println(result);

    }







    @FXML
    private void thoatgialog(){
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

    private void CreateInfoFlight(String sbDi, String sbDen , LocalDateTime Time,int flightId ) {
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
        Label bayThangLabel = new Label("ID : " +idflight);

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
                viewDetailClick( flightId , time , sbDi, sbDen); // Truyền flightId khi click vào nút
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



            String VEPTData = VEPT.getText();  //6
            String VETGData = VETG.getText();//7
            int idsbdi= DatabaseController.getAirportIdByName(SbdiData);
            int idsbden= DatabaseController.getAirportIdByName(SbdenData);

            if(
                    SbdenData !=null && SbdiData !=null
                    && NgayGioxuatphatData !=null && NGAYDKData !=null
                    && SLGHEPT.getText() !=null && SLGHETG.getText() !=null && VEPTData !=null && VETGData !=null
            ){
                int SLGHEPTData = Integer.parseInt(SLGHEPT.getText());
                int SLGHETGData =Integer.parseInt( SLGHETG.getText());
                int SumDataGhe = SLGHEPTData + SLGHETGData; // 5
                if(SLGHEPTData != 0 && SLGHETGData != 0){
                    boolean IsAdd = DatabaseController.addFlight(1,idsbdi,idsbden,NgayGioxuatphatData,NGAYDKData,200,SumDataGhe);
                    if (IsAdd){
                        BaseController.showAlert("Thành công","Thêm chuyến bay thành công !");
                        Reset();
                    }
                    else {
                        BaseController.showAlert("Lỗi","Lỗi thêm chuyến bay, Vui lòng kiểm tra lại thông tin đã nhập !");
                    }
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


    // Sân bay
    // thêm sân bay
    private String newAirport;
    private String newLocation;
    @FXML
    private TextField airport_ID;
    private int IDAir;
    @FXML
    private void addAriportClick(){
         newAirport = airportName.getText();
         newLocation = locationAirport.getText();
        if(newAirport != null && newLocation != null){
            boolean isAddAirport = DatabaseController.addAirport(newAirport,newLocation);
            if(isAddAirport){
                BaseController.showAlert("Thành công","Thêm sân bay thành công !");
            }else {
                BaseController.showAlert("Lỗi","Lỗi Thêm sân bay,Hãy kiếm tra lại !");
            }
        }
        else {
            BaseController.showAlert("Trống","Vui lòng nhập đầy đủ thông tin !");
        }
    }

    // xóa sân bay


    private ButtonType warning;
    private void ShowWarning(){
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Xác nhận");
        confirmationDialog.setHeaderText("Bạn đang bị ràng buộc khóa ngoại ");
        confirmationDialog.setContentText("Khi xóa sẽ xóa luôn các thông tin của các bạn liên quan, Bạn có chắc chắn");
        confirmationDialog.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        warning = confirmationDialog.showAndWait().orElse(ButtonType.NO);
    }


    @FXML
    private void deleteAirport(){
        newAirport = airportName.getText();
        newLocation = locationAirport.getText();
        IDAir = Integer.parseInt(airport_ID.getText());
        if (true){
            ShowWarning();
                if(warning == ButtonType.YES){
                    if(newAirport.isEmpty() || newAirport != null && IDAir !=0){
                        boolean isDeleteAirport = DatabaseController.deleteAirport(IDAir);
                        if(isDeleteAirport){
                            BaseController.showAlert("Thành công","Xóa thành công sân bay !");
                        }else {
                            BaseController.showAlert("Lỗi","ID chuyến bay không tồn tại");
                        }
                    }
                    if (newAirport != null  && IDAir ==0){
                        boolean isDeleteAirport = DatabaseController.deleteAirportForName(newAirport);
                        if(isDeleteAirport){
                            BaseController.showAlert("Thành công","Xóa thành công sân bay !");
                        }else {
                            BaseController.showAlert("Lỗi","ID chuyến bay không tồn tại");
                        }
                    }
                }else {
                    return;
                }
        }
    }
    // Sân bay

    // Ghế
    @FXML
    private TextField chair_name;
    @FXML
    private TextField FL_id;
    @FXML
    private ComboBox <String> select_chair;
    @FXML
    private void addChairsClick(){
        String chairName = chair_name.getText();
        int FLId = Integer.parseInt(FL_id.getText());
        String Compart = select_chair.getValue();
        int IDCompart = DatabaseController.getSeatType(Compart);

        if(chairName !=null && FLId != 0 && Compart != null){
            boolean isAddSeat = DatabaseController.addSeatNumber(FLId,chairName,"0",IDCompart);
            if(isAddSeat){
                BaseController.showAlert("Thành công","Thêm thành công");
            }else {
                BaseController.showAlert("Lỗi","Thêm Thất bại");
            }
        }
    }

    // Ghế cobobox
    private void classCompartment(){
        ObservableList<String> Compart = FXCollections.observableArrayList("Phổ Thông","Thương Gia");
        select_chair.setItems(Compart);
    }




}
