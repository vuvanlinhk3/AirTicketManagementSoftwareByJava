package Admin.Controller;

import Customer.MainCustomer;
import Database.DatabaseContection;
import javafx.application.Application;
import javafx.stage.Stage;

public class appLauncher  {
        public static void main(String[] args) {
            Application.launch(MainCustomer.class,args);
            DatabaseContection.closeConnettion();
        }

}
