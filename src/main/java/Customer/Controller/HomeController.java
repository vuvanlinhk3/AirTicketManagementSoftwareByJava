package Customer.Controller;
import Database.DatabaseController;
import Database.DatabaseContection;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class HomeController  {
    @FXML
    private TextField startpoin;

    @FXML
    private Button btnpupup;

    // tạo biến chung
    private String sanbaydi;


//    @FXML
//    public void initialize() {
//        ComboBox<String> optionsComboBox = new ComboBox<>(FXCollections.observableArrayList("Option 1", "Option 2", "Option 3"));
//
//        // Xử lý sự kiện khi nút được nhấn
//        btnpupup.setOnAction(event -> {
//            // Hiển thị ComboBox như một popup
//            optionsComboBox.show();
//        });
//        // Xử lý sự kiện khi một mục được chọn trong ComboBox
//        optionsComboBox.setOnAction(event -> {
//            // Lấy giá trị từ ComboBox và đặt vào TextField
//            String selectedValue = optionsComboBox.getSelectionModel().getSelectedItem();
//            startpoin.setText(selectedValue);
//            optionsComboBox.hide();
//        });
//
//        // Tạo một layout HBox để đặt các thành phần
//        HBox root = new HBox(10);
//        root.getChildren().addAll(startpoin, btnpupup);
//
//    }





    public HomeController() {

    }

    @FXML
    public void bookingclick(){

    }
    @FXML
    public void Bookedclick(){

    }
    @FXML
    public void assessclick(){

    }
    @FXML
    public void flightfindclick(){

    }


    @FXML
    private void find_click(){

    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
