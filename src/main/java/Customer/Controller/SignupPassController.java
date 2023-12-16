package Customer.Controller;
import Database.DatabaseController;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
//import java.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Random;

public class SignupPassController  {
    private static String usernamedata;
    private static LocalDate  birthaydata;
    private static String genderdata;
    private static String nationalitydata;
    private static String languagedata;
    private static String addressdata;
    private static String countrydata;
    private static String provincesdata;
    private static String citydata;
    @FXML
    private TextField number_phone_TF;
    @FXML
    private TextField email_TF;
    @FXML
    private Label warning_input_number;
    @FXML
    private Label warning_input_email;
    @FXML
    private ComboBox <String> conflic_number_email;
    @FXML
    private TextField code_sent;
    @FXML
    private TextField password;
    @FXML
    private TextField cf_password;
    @FXML
    private Label warning_input_password;
    @FXML
    private Label warning_input_password_cf;
    @FXML
    private void initialize(){
        conflic_number_email.setValue("Email");
        ObservableList<String> genderOptions = FXCollections.observableArrayList("Email","Phone Number");
        conflic_number_email.setItems(genderOptions);



    }
    public static void setReceivedData(
            String nameuser,
            LocalDate birthday,
            String gender,
            String nationality,
            String language,
            String address,
            String country,
            String provinces,
            String city
    ) {
        usernamedata = nameuser;
        genderdata = gender;
        birthaydata = birthday;
        nationalitydata = nationality;
        languagedata = language;
        addressdata = address;
        countrydata = country;
        provincesdata = provinces;
        citydata = city;
        System.out.println(usernamedata);
        System.out.println(genderdata);
        System.out.println(languagedata);
        System.out.println(countrydata);
        System.out.println(citydata);
    }

    private String number_phone;
    private String email;
    private  String select_n_e;
    private String  verificationCode;
    private String passwordata;
    private String passwordcfdata;
    private  String specificAddress;
    @FXML
    private void send_code_click() {
        if (!validateFields()) {
            return;
        }

        select_n_e = conflic_number_email.getValue();
        email = email_TF.getText();
        number_phone = number_phone_TF.getText();

        if ("Email".equals(select_n_e)) {
            try {
                // Tạo mã xác nhận ngẫu nhiên
                verificationCode = generateRandomCode();

                // Gửi mã xác nhận qua email
                sendCodeByEmail(email, verificationCode);

                // Hiển thị thông báo khi gửi mã thành công
                showAlert("Success", "Mã của bạn đã được gửi !");

            } catch (MessagingException e) {
                e.printStackTrace();
                // Hiển thị thông báo khi gửi mã không thành công
                showAlert("Error", "Lỗi !, Hãy kiểm tra lại Email đã nhập.");
            }
        }
        if("Phone Number".equals(select_n_e)){
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

    private Boolean confirmed = false;

    @FXML
    private void comfirm_click(){
        if(!validateFields()){
            return;
        }
        String conSent = code_sent.getText();
//        String codeSend = generateRandomCode();
        System.out.println(verificationCode);
        System.out.println(conSent);
        if(conSent.equals(verificationCode)){
            confirmed = true;
            showAlert("Success", "Xác nhận thành công !");
        }
        else {
            showAlert("Faile", "Mã xác nhận không đúng !");
        }

    }


    @FXML
    private void signup_click(ActionEvent event) throws IOException{

        if(!validateFields()){
            return;
        }
        if(!validatePaswords()){
            return;
        }
        number_phone = number_phone_TF.getText();
        email = email_TF.getText();
        passwordata = password.getText();
        passwordcfdata = cf_password.getText();

        specificAddress = countrydata + ", " +provincesdata + ", " +citydata +", "+addressdata;
        if(confirmed && passwordata.equals(passwordcfdata)){
            System.out.println(confirmed.toString());
            // contecdatabase
            boolean addUser = DatabaseController.addPassenger(usernamedata,birthaydata , genderdata, nationalitydata , specificAddress , email, number_phone ,passwordata );
            if(addUser){
                showAlert("Đăng ký thành công", "Đăng ký thành công !");
                Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Login.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }else {
                showAlert("Thất bại","Đăng ký không thành công !");
            }

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
    public boolean validatePaswords() {
        boolean isTr = true;

        isTr &= validatePasword(password, warning_input_password);
        isTr &= validatePasword(cf_password, warning_input_password_cf);

        return isTr;
    }
    public boolean validateFields() {
        boolean isValid = true;

        isValid &= validateField(number_phone_TF, warning_input_number);
        isValid &= validateField(email_TF, warning_input_email);

        return isValid;
    }

    private boolean validatePasword(TextField field, Label warningLabel) {
        if (field.getText().isEmpty()) {
            setRedBorder(field);
            warningLabel.setText("Trường này không được bỏ trống");
            return false;
        } else {
            clearBorder(field);
            warningLabel.setText("");
            return true;
        }
    }
    private boolean validateField(TextField field, Label warningLabel) {
        if (field.getText().isEmpty()) {
            setRedBorder(field);
            warningLabel.setText("Trường này không được bỏ trống");
            return false;
        } else {
            clearBorder(field);
            warningLabel.setText("");
            return true;
        }
    }

    private void setRedBorder(TextField field) {
        field.setStyle("-fx-border-color: red;");
    }
    private void clearBorder(TextField field) {
        field.setStyle("-fx-border-color: null;");
    }

    public static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
