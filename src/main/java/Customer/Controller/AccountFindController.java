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
    private TableColumn<Account, String> nameclColumn;

    private String phone_find;

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
//        nameclColumn.setCellValueFactory(cellData -> cellData.getValue().nameclProperty());
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
                    int passengerId = resultSet.getInt("passenger_id");
                    String namecl = resultSet.getString("namecl");
                    // Thêm nhiều trường khác nếu cần

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

        public Account(String namecl) {
            this.namecl = namecl;
        }

        public String getNamecl() {
            return namecl;
        }
    }



    // chuyển form
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private void back_click(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
}
