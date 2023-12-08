package Customer.Controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController  {
    @FXML
    private TextField emailornumber;
    @FXML
    private void dangnhapbtn(){
        String user = emailornumber.getText().toString();
        if(user  == ""){

        }
    }

}
