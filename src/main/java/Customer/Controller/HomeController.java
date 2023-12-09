package Customer.Controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class HomeController  {
    @FXML
    private TextField startpoin;

    @FXML
    private Button btnpupup;

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
    public void initialize() {
        // Thiết lập vai trò là combobox
        btnpupup.setAccessibleRoleDescription("combobox");

        // Mô phỏng dữ liệu trong combobox
        String[] options = {"Apple", "Banana", "Cherry", "Grapes", "Orange"};

        // Tạo Popup để hiển thị các mục phù hợp
        Popup popup = new Popup();
        for (String option : options) {
            Button optionButton = new Button(option);
            optionButton.setOnAction(e -> {
                startpoin.setText(option);
                popup.hide();
            });
            popup.getContent().add(optionButton);
        }

        // Thêm sự kiện khi người dùng nhấn nút
        btnpupup.setOnAction(e -> {
            popup.show(btnpupup.getScene().getWindow(), btnpupup.getScene().getWindow().getX() + btnpupup.localToScene(10, 10).getX(),
                    btnpupup.getScene().getWindow().getY() + btnpupup.localToScene(0, 0).getY() + btnpupup.getHeight());
        });
    }

}
