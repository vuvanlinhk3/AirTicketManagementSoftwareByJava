package Database;

import Admin.Controller.BaseController;
import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.locks.Condition;


public class DatabaseContection  {
    private static Connection databaselink = null;
    public static Connection getConnettion(){

        try {
            // đăng ký mysql driver với drivermanager
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

            // các thông số
            String url ="jdbc:mySQL://localhost:3306/quanlybanvemaybay";
            String username = "root";
            String password ="";
            // tạo kết nối
            databaselink = DriverManager.getConnection(url,username,password);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
            BaseController.showAlert("lỖI","Không có kết nối mạng !");
        }
        return databaselink;
    }
    public static void closeConnettion(){
        try {
            if (databaselink !=null){
                databaselink.close();
            }

        }catch (Exception e){
        e.printStackTrace();
        }
    }
}
