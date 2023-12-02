module planeapp.paneapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.dlsc.formsfx;
    opens Customer to javafx.fxml;
    opens Admin to javafx.fxml;

    exports Customer.Controller;
    exports Admin;
    exports Customer;

}