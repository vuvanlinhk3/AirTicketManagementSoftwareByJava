package Database;

import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class DatabaseController  {
    // thêm thông tin khách hàng
    public static boolean addPassenger(String name, LocalDate birthday, String gender, String nationality,
                                String address, String email, String phoneNumber, String password) {
        String query = "INSERT INTO Passengers (name, birthday, gender, nationality, address, email, phone_number, password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseContection.getConnettion();
                     PreparedStatement preparedStatement = connection.prepareStatement(query)) {


            Date sqlDate = Date.valueOf(birthday);

            preparedStatement.setString(1, name);
            preparedStatement.setDate(2, sqlDate);
            preparedStatement.setString(3, gender);
            preparedStatement.setString(4, nationality);
            preparedStatement.setString(5, address);
            preparedStatement.setString(6, email);
            preparedStatement.setString(7, phoneNumber);
            preparedStatement.setString(8, password);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }





}
