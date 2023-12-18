package Customer.Controller;

import Database.DatabaseContection;
import Database.DatabaseController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Random;

import static Customer.Controller.SignupController.validateField;

public class ChangePasswordController {
    @FXML
    private TextField load_nameuser;
    @FXML
    private TextField load_phonenumber;
    @FXML
    private TextField load_email;
    @FXML
    private ComboBox <String> select;
    @FXML
    private void initialize(){
        load_email.setText("vanlinh30122003@gmail.com");
        load_nameuser.setText("vũ văn linh");
        load_phonenumber.setText("0365556145");
        select.setValue("Email");
        ObservableList<String> genderOptions = FXCollections.observableArrayList("Email","Phone Number");
        select.setItems(genderOptions);
    }
    //biến cục bộ value
    private String email;
    private String number_phone;
    private String selectvalue ;
    private String verificationCode;

    // ma xac nhan
    @FXML
    private void send_code_click() {
        email =load_email.getText();
        number_phone =load_phonenumber.getText();
        selectvalue = select.getValue();
        verificationCode = generateRandomCode();
        if ("Email".equals(selectvalue)) {
            try {
                // Gửi mã xác nhận qua email
                sendCodeByEmail(email, verificationCode);

                // Hiển thị thông báo khi gửi mã thành công
                showAlert("Success", "Mã của bạn đã được gửi !");
                System.out.print(verificationCode);

            } catch (MessagingException e) {
                e.printStackTrace();
                // Hiển thị thông báo khi gửi mã không thành công
                showAlert("Error", "Lỗi !, Hãy kiểm tra lại Email đã nhập.");
            }
        }
        if("Phone Number".equals(selectvalue)){
            showAlert("Error", "Chưa có hỗ trợ xác nhận qua số điện thoại");

        }
    }
    private String generateRandomCode() {
        // Tạo mã xác nhận ngẫu nhiên, có thể thay đổi theo nhu cầu
        int length = 6;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }

        return code.toString();
    }

    private void sendCodeByEmail(String to, String code) throws MessagingException {
        // Code gửi email sử dụng JavaMail
        // ...
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("shutupxpts@gmail.com", "kofl baju chvx ywqb");
            }
        });

        Message mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress("shutupxpts@gmail.com"));
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        mimeMessage.setSubject("Mã đã được gửi");
        mimeMessage.setText("Your verification code is: " + code);

        Transport.send(mimeMessage);
    }
//    @FXML
//    private void confirm_click





    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private void back_click(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/AccountFind.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private TextField code_mail;

    private String code ;
    private Boolean cofirmed = false;
    @FXML
    private void confirm_click (){
         String code = code_mail.getText();
        if( code.equals(verificationCode) ){
            System.out.println ("\nmã đã nhập :"+ code);
            cofirmed=true;
            showAlert("Thành Công ","Nhập mã xác nhận thành công!");
        }else{
            System.out.println ("\nmã đã nhập :"+ code);
            showAlert("Error", "Mã không chính xác");
        }
    }
    @FXML
    private PasswordField pass_new;
    @FXML
    private PasswordField pass_new_cofirm;

    @FXML
    private Label warning_label;

    @FXML
    private void cofirm_pass_click(){
        String newpass =pass_new.getText();
        String passcofirm= pass_new_cofirm.getText();
        if(newpass.equals(passcofirm)){
            warning_label.setVisible(false);
            showAlert("Thành công ","Đổi mật khẩu thành công!");
            // Update mật khẩu trong cơ sở dữ liệu
            updatePasswordInDatabase(newpass);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Customer/CustomerView/Login.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) pass_new.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            warning_label.setVisible(true);
        }
    }
    //hàm lấy dữ liệu PassengerID khi ô email = email trong cldl
    private int getPassengerIdFromEmail(String email) {
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT passenger_id FROM Passengers WHERE email = ?")) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("passenger_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Trả về giá trị không hợp lệ nếu không tìm thấy email
    }
    private void updatePasswordInDatabase(String newPassword) {
        String emailToUpdate = load_email.getText();
        int passengerIdToUpdate = getPassengerIdFromEmail(emailToUpdate);// Thay thế bằng ID thực tế của hành khách
        if (passengerIdToUpdate != -1) {
            try (Connection connection = DatabaseContection.getConnettion();
                 PreparedStatement preparedStatement = connection.prepareStatement(
                         "UPDATE Passengers SET password = ? WHERE passenger_id = ?")) {
                preparedStatement.setString(1, newPassword);
                preparedStatement.setInt(2, passengerIdToUpdate);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Cập nhật mật khẩu vào cơ sở dữ liệu thành công!");
                } else {
                    System.out.println("Không tìm thấy hành khách để cập nhật mật khẩu.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Không tìm thấy hành khách với email: " + emailToUpdate);
        }
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE Passengers SET password = ? WHERE passenger_id = ?")) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, passengerIdToUpdate);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cập nhật mật khẩu vào cơ sở dữ liệu thành công!");
            } else {
                System.out.println("Không tìm thấy hành khách để cập nhật mật khẩu.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
