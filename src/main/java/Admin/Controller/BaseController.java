package Admin.Controller;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class BaseController  {
    // chung
    public static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static ButtonType result;
    public static void ShowConfirmationDialog(String khoaNgoai){
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Xác nhận");
        confirmationDialog.setHeaderText("Bạn đang bị ràng buộc khóa ngoại " + khoaNgoai);
        confirmationDialog.setContentText("This action will also delete associated records in other tables.");
        confirmationDialog.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        result = confirmationDialog.showAndWait().orElse(ButtonType.NO);
        System.out.println(result);
    }

}
