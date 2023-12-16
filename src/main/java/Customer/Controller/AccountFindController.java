package Customer.Controller;

import Database.DatabaseContection;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.text.TabableView;
import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class AccountFindController {
    @FXML
    private TextField phone_find_text;
    @FXML
    private TabableView account_find;

    private String phone_find;

    // chuyển form
    private Stage stage;
    private Scene scene;
    private Parent root;

//    lấy dữ liệu từ TextField phone_find
    private boolean isNullText(){
        phone_find = phone_find_text.getText();
        if(phone_find.isEmpty()){
            return true;
        }
        return false;
    }
    @FXML
    public static boolean getPassengerForPhone(String phone_find, String password) {
        String sql = "SELECT * FROM Passengers WHERE phone_number = ? AND password = ?";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, phone_find);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve customer information
                    int passengerId = resultSet.getInt("passenger_id");
                    String name = resultSet.getString("name");
                    // Add more fields as needed

                    // Display customer information (you can customize this part)
                    LoginController.displayCustomerInfo(passengerId, name);

                } else {
                    // No matching record found
//                    SignupPassController.showAlert("Lỗi","Invalid email or password.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }    @FXML
    private void back_click(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Home.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
