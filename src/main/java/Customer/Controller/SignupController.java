package Customer.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import javafx.util.converter.DefaultStringConverter;

import java.io.IOException;
import java.util.function.UnaryOperator;

public class SignupController {
    @FXML
    private TextField nameuser_textfield;
    @FXML
    private ComboBox<String> Gender_combobox;
    @FXML
    private DatePicker birthday_date;
    @FXML
    private ComboBox<String> nationality_combobox;
    @FXML
    private ComboBox<String> language_combobox;
    @FXML
    private TextField address_textfield;
    @FXML
    private ComboBox<String> country_combobox;
    @FXML
    private ComboBox<String> provinces_combobox;
    @FXML
    private TextField city_textfield;

    @FXML
    public void initialize() {
        ObservableList<String> genderOptions = FXCollections.observableArrayList("Nam", "Nữ", "Khác");
        Gender_combobox.setItems(genderOptions);

        // tạo chuỗi
        String[] ArrayCountries = { "Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua and Barbuda",
                "Argentina", "Armenia", "Australia", "Austria",
                "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin",
                "Bhutan",
                "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina Faso",
                "Burundi", "Cabo Verde", "Cambodia",
                "Cameroon", "Canada", "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros",
                "Congo (Congo-Brazzaville)", "Costa Rica",
                "Croatia", "Cuba", "Cyprus", "Czechia", "Denmark", "Djibouti", "Dominica", "Dominican Republic",
                "Ecuador", "Egypt",
                "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini", "Ethiopia", "Fiji", "Finland",
                "France", "Gabon",
                "Gambia", "Georgia", "Germany", "Ghana", "Greece", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau",
                "Guyana",
                "Haiti", "Honduras", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel",
                "Italy", "Ivory Coast", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kuwait",
                "Kyrgyzstan",
                "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg",
                "Madagascar",
                "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania", "Mauritius",
                "Mexico", "Micronesia",
                "Moldova", "Monaco", "Mongolia", "Montenegro", "Morocco", "Mozambique", "Myanmar (formerly Burma)",
                "Namibia", "Nauru", "Nepal",
                "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "North Korea",
                "North Macedonia (formerly Macedonia)", "Norway", "Oman", "Pakistan",
                "Palau", "Palestine State", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland",
                "Portugal", "Qatar",
                "Romania", "Russia", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia",
                "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe",
                "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia",
                "Solomon Islands", "Somalia",
                "South Africa", "South Korea", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Sweden",
                "Switzerland", "Syria",
                "Tajikistan", "Tanzania", "Thailand", "Timor-Leste", "Togo", "Tonga", "Trinidad and Tobago", "Tunisia",
                "Turkey", "Turkmenistan",
                "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States of America",
                "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City (Holy See)",
                "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe"
        };
        ObservableList<String> countries = FXCollections.observableArrayList(ArrayCountries);
        FXCollections.sort(countries);

        String[] provincesArray = {
                "An Giang", "Bà Rịa - Vũng Tàu", "Bắc Giang", "Bắc Kạn", "Bạc Liêu", "Bắc Ninh", "Bến Tre", "Bình Định",
                "Bình Dương", "Bình Phước",
                "Bình Thuận", "Cà Mau", "Cao Bằng", "Đắk Lắk", "Đắk Nông", "Điện Biên", "Đồng Nai", "Đồng Tháp",
                "Gia Lai", "Hà Giang",
                "Hà Nam", "Hà Tĩnh", "Hải Dương", "Hậu Giang", "Hòa Bình", "Hưng Yên", "Khánh Hòa", "Kiên Giang",
                "Kon Tum", "Lai Châu",
                "Lâm Đồng", "Lạng Sơn", "Lào Cai", "Long An", "Nam Định", "Nghệ An", "Ninh Bình", "Ninh Thuận",
                "Phú Thọ", "Quảng Bình",
                "Quảng Nam", "Quảng Ngãi", "Quảng Ninh", "Quảng Trị", "Sóc Trăng", "Sơn La", "Tây Ninh", "Thái Bình",
                "Thái Nguyên", "Thanh Hóa",
                "Thừa Thiên Huế", "Tiền Giang", "Trà Vinh", "Tuyên Quang", "Vĩnh Long", "Vĩnh Phúc", "Yên Bái",
                "Phú Yên", "Cần Thơ", "Đà Nẵng", "Hải Phòng", "Hà Nội", "Hồ Chí Minh"
        };
        ObservableList<String> provinces = FXCollections.observableArrayList(provincesArray);
        FXCollections.sort(provinces);
        // lấy sự kiện
        nationality_combobox.setItems(countries);
        language_combobox.setItems(countries);
        country_combobox.setItems(countries);
        country_combobox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Khi giá trị của countryComboBox thay đổi
                System.out.println("Selected Country: " + newValue);

                // Kiểm tra giá trị và cập nhật provincesComboBox
                if ("Vietnam".equals(newValue)) {
                    provinces_combobox.setItems(provinces);
                } else {
                    provinces_combobox.setItems(
                            FXCollections.observableArrayList("Không có thông tin về thành phố của quốc gia này"));
                }
            }
        });
        nameuser_textfield.addEventFilter(KeyEvent.KEY_TYPED, this::filterNameInput);

    };

    private void filterNameInput(KeyEvent event) {
        // Lấy ký tự được nhập
        String character = event.getCharacter();

        // Kiểm tra xem ký tự có phải là chữ cái không dấu và là chữ cái hoa không
        if (!character.matches("[a-zA-Z ]") && !character.isEmpty()) {
            // Nếu không phải là chữ cái không dấu hoặc khoảng trắng, hủy bỏ sự kiện
            event.consume();
        }

        // Tạo một TextFormatter với một UnaryOperator để chuyển đổi chữ in thường thành
        // chữ in hoa
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText().toUpperCase();
            change.setText(newText);
            change.setRange(0, change.getControlText().length()); // Đảm bảo đánh dấu toàn bộ văn bản
            return change;
        });

        // Gán TextFormatter cho TextField
        nameuser_textfield.setTextFormatter(formatter);
    }

    // xóa hết value trong tất cả
    @FXML
    private void delete_all_click() {
        // Xóa tất cả giá trị được chọn
        Gender_combobox.getSelectionModel().clearSelection();
        nationality_combobox.getSelectionModel().clearSelection();
        language_combobox.getSelectionModel().clearSelection();
        country_combobox.getSelectionModel().clearSelection();
        provinces_combobox.getSelectionModel().clearSelection();

        // Sử dụng Platform.runLater để đặt lại giá trị và PromptText cho ComboBox
        Platform.runLater(() -> {
            Gender_combobox.setValue("");
            Gender_combobox.layout();
            Gender_combobox.setPromptText("Chọn giới tính");

            nationality_combobox.setValue("");
            nationality_combobox.setPromptText("Chọn quốc tịch");

            language_combobox.setValue("");
            language_combobox.setPromptText("Chọn ngôn ngữ");

            country_combobox.setValue("");
            country_combobox.setPromptText("Chọn quốc gia/vùng");

            provinces_combobox.setValue("");
            provinces_combobox.setPromptText("Chọn tỉnh");
        });

        // Xóa hoặc đặt giá trị cho các TextField
        nameuser_textfield.setText("");
        address_textfield.setText("");
        city_textfield.setText("");
    }




    // lay gia tri value nhap vao
    private String nameuser;
    private String gender;
    private String nationality;
    private String language;
    private String address;
    private String country;
    private String provinces;
    private String city;

    // chuyển form
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private void next_click(ActionEvent event) throws IOException {
        // trước khi chuyển form kiểm tra
        if (!validateFields()) {
            return;
        }

        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/SignupPass.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        nameuser = nameuser_textfield.getText();
        gender = Gender_combobox.getValue();
        nationality = nationality_combobox.getValue();
        language = language_combobox.getValue();
        address = address_textfield.getText();
        country = country_combobox.getValue();
        provinces = provinces_combobox.getValue();
        city = city_textfield.getText();

        SignupPassController.setReceivedData(nameuser, gender, nationality, language,
                address, country, provinces, city);
    }
    // quay lại form đăng nhập
    @FXML
    private void back_login_click(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/CustomerView/Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Label warning_input_name;
    @FXML
    private Label warning_input_gender;
    @FXML
    private Label warning_input_nationality;
    @FXML
    private Label warning_input_language;
    @FXML
    private Label warning_input_address;
    @FXML
    private Label warning_input_country;
    @FXML
    private Label warning_input_provinces;
    @FXML
    private Label warning_input_city;

    public boolean validateFields() {
        boolean isValid = true;

        isValid &= validateField(nameuser_textfield, warning_input_name);
        isValid &= validateField(Gender_combobox, warning_input_gender);
        isValid &= validateField(nationality_combobox, warning_input_nationality);
        isValid &= validateField(language_combobox, warning_input_language);
        isValid &= validateField(address_textfield, warning_input_address);
        isValid &= validateField(country_combobox, warning_input_country);
        isValid &= validateField(provinces_combobox, warning_input_provinces);
        isValid &= validateField(city_textfield, warning_input_city);

        return isValid;
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

    private boolean validateField(ComboBox<String> comboBox, Label warningLabel) {
        if (comboBox.getValue() == null || comboBox.getValue().isEmpty()) {
            setRedBorder(comboBox);
            warningLabel.setText("Trường này không được bỏ trống");
            return false;
        } else {
            clearBorder(comboBox);
            warningLabel.setText("");
            return true;
        }
    }

    private void setRedBorder(TextField field) {
        field.setStyle("-fx-border-color: red;");
    }

    private void setRedBorder(ComboBox<String> comboBox) {
        comboBox.setStyle("-fx-border-color: red;");
    }

    private void clearBorder(TextField field) {
        field.setStyle("-fx-border-color: null;");
    }

    private void clearBorder(ComboBox<String> comboBox) {
        comboBox.setStyle("-fx-border-color: null;");
    }

}
