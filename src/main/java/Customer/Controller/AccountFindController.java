package Customer.Controller;

import Database.DatabaseContection;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountFindController {

    @FXML
    private TextField phone_find_text;
    @FXML
    private TableView<Account> account_find;
    @FXML
    private TableColumn<Account, String> namecl;
    @FXML
    private TableColumn<Account, Button> confirmcl;
    @FXML
    private Button back_click;

    private String phone_find;
    //chuyển form
    private Stage stage;
    private Scene scene;
    private Parent root;

    private ObservableList<Account> accountList = FXCollections.observableArrayList();

    @FXML
    private void click_phone_find(ActionEvent event) {
        if (isNullText()) {
            showAlert("Lỗi!", "Vui lòng nhập số điện thoại.");
            return;
        }

        if (getNameForPhone(phone_find)) {
            // Nếu có khách hàng, hiển thị thông tin trong TableView
            displayCustomerInfoInTableView();
        } else {
            // Nếu không có khách hàng, hiển thị thông báo
            showAlert("Thông báo", "Không có tài khoản cho số điện thoại này!");
        }
    }

    private void displayCustomerInfoInTableView() {
        // Xóa dữ liệu hiện tại trong TableView
        accountList.clear();

        // Thêm dữ liệu mới từ truy vấn vào TableView
        account_find.setItems(accountList);

        namecl.setCellValueFactory(new PropertyValueFactory<>("namecl"));
        confirmcl.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getConfirmcl()));

        // Thêm dữ liệu từ truy vấn vào danh sách
        getNameForPhone(phone_find);

    }

    private boolean isNullText() {
        phone_find = phone_find_text.getText();
        return phone_find.isEmpty();
    }

    private boolean getNameForPhone(String phone_find) {
        String sql = "SELECT * FROM Passengers WHERE phone_number = ?";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, phone_find);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Lấy thông tin khách hàng và thêm vào danh sách
                    String namecl = resultSet.getString("name");

                    // Thêm thông tin vào danh sách
                    accountList.add(new Account(namecl));
                }
                return !accountList.isEmpty(); // Trả về true nếu có khách hàng
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static class Account {
        private final String namecl;
        private final Button confirmcl;

        public Account(String namecl) {
            this.namecl = namecl;
            this.confirmcl = new Button("Vui lòng xác nhận");
            setupButtonAction();
        }

        public String getNamecl() {
            return namecl;
        }

        public Button getConfirmcl() {
            return confirmcl;
        }

        private void setupButtonAction() {
            confirmcl.setOnAction(event -> {
                // Thực hiện hành động khi nút được click

                System.out.println("Button clicked for " + namecl);
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Customer/CustomerView/ChangePassword.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();

                    // Đóng cửa sổ hiện tại
                    ((Stage) confirmcl.getScene().getWindow()).close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML
    private void back_click(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
