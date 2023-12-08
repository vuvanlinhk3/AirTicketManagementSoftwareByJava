package Database;

import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.locks.Condition;


public class DatabaseContection  {
    public static Connection getConnettion(){
        Connection databaselink =null;
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
        }
        return databaselink;
    }

}
