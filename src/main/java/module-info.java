module planeapp.paneapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;
    requires com.dlsc.formsfx;
    requires javafx.base;
    requires java.mail;
//    requires jakarta.activation;
//    requires java.activation;



    opens Customer.Controller to javafx.fxml;
    opens Admin.Controller to javafx.fxml;

    exports Database;
    exports Admin;
    exports Customer;

}