package Customer.Controller;

import Database.DatabaseContection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    private TableColumn<Account, String> confirmcl;
    @FXML
    private Button back_button;

    private String phone_find;
    // chuyển form
    private Stage stage;
    private Scene scene;
    private Parent root;

    private ObservableList<Account> accountList = FXCollections.observableArrayList();

    @FXML
    private void click_phone_find(ActionEvent event) {
        if (isNullText()) {
            showAlert("Lỗi", "Vui lòng nhập số điện thoại.");
            return;
        }

        if (getPassengerForPhone(phone_find)) {
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
        confirmcl.setCellValueFactory(new PropertyValueFactory<>("confirmcl"));
    }

    private boolean isNullText() {
        phone_find = phone_find_text.getText();
        return phone_find.isEmpty();
    }

    private boolean getPassengerForPhone(String phone_find) {
        String sql = "SELECT * FROM Passengers WHERE phone_number = ?";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, phone_find);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Lấy thông tin khách hàng và thêm vào danh sách
                  //  int passengerId = resultSet.getInt("passenger_id");
                    String namecl = resultSet.getString("name");
                    // Thêm nhiều trường khác nếu cần

                    // Thêm thông tin vào danh sách
                    accountList.add(new Account(namecl, "Bấm vào đây để xác nhận"));
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
        private final String confirmcl;

        public Account(String namecl, String confirmcl) {
            this.namecl = namecl;
            this.confirmcl = confirmcl;
        }

        public String getNamecl() {
            return namecl;
        }

        public String getConfirmcl() {
            return confirmcl;
        }
    }

    @FXML
    private void back_click(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Home.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
