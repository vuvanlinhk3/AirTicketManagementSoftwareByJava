package Database;

import Customer.Controller.FlightFindController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    // thêm thông tin khách hàng ////



    // đăng nhập tài khoản




    // hiển thị sân bay .
    public static ObservableList<String> getAirports() {
        ObservableList<String> airportList = FXCollections.observableArrayList();
        String sql = "SELECT airport_name FROM Airports";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String airportName = resultSet.getString("airport_name");
                    airportList.add(airportName);
                }
            }
        catch (SQLException e) {
        e.printStackTrace();
    }
        return airportList;
}
    // hiển thị sân bay

    // tìm sân bay đến dựa vào sân bay đi
    public static ObservableList<String> getDestinationAirport(String airport ) {
        ObservableList<String> airportList = FXCollections.observableArrayList();
        String sql = "SELECT A.destination_airport_id, B.airport_name AS destination_airport_name\n" +
                "FROM Flights A\n" +
                "JOIN Airports B ON A.destination_airport_id = B.airport_id\n" +
                "WHERE A.departure_airport_id = (SELECT airport_id FROM Airports WHERE airport_name = ?)";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, airport);  // Gán giá trị cho tham số
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String airportName = resultSet.getString("destination_airport_name");
                airportList.add(airportName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return airportList;
    }
    // tìm sân bay dựa vào sân bay đến



    // tìm tất cả mã chuyến bay dựa vào sân bay đi và sân bay đến
    public static List<Integer> getFlightIdsByAirports(String departureAirportName, String destinationAirportName) {
        List<Integer> flightIds = new ArrayList<>();

        String sql = "SELECT F.flight_id FROM Flights F JOIN Airports DA ON F.departure_airport_id = DA.airport_id JOIN Airports AA ON F.destination_airport_id = AA.airport_id WHERE DA.airport_name = ? AND AA.airport_name = ?";

        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set parameters
            preparedStatement.setString(1, departureAirportName);
            preparedStatement.setString(2, destinationAirportName);

            // Debug print statements
            System.out.println("Before executing query");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("Số lượng ID chuyến bay tìm thấy: " + flightIds.size());

                while (resultSet.next()) {
                    int flightId = resultSet.getInt("flight_id");
                    flightIds.add(flightId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flightIds;
    }
    // tìm tất cả mã chuyến bay dựa vào sân bay đi và sân bay đến

    // tìm thời gian dựa vào mã chuyến bay
    public static List<LocalDateTime> getFlightTimesByFlightIds(List<Integer> flightIds) {
        List<LocalDateTime> flightTimes = new ArrayList<>();

        String sql = "SELECT F.departure_datetime FROM Flights F WHERE F.flight_id = ?";

        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (Integer flightId : flightIds) {
                preparedStatement.setInt(1, flightId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        LocalDateTime departureTime = resultSet.getTimestamp("departure_datetime").toLocalDateTime();
                        flightTimes.add(departureTime);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flightTimes;
    }
    // tìm thời gian dựa vào mã chuyến bay







    // test case
//    public static boolean testc (String value){
//        String query = "INSERT INTO test (value) VALUES (?)";
//        try(Connection connection = DatabaseContection.getConnettion();
//        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setString(1, value);
//            int rowsAffected = preparedStatement.executeUpdate();
//            return rowsAffected > 0;
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
    //tesc ///
}
