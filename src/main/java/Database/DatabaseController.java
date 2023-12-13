package Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    // tìm chuyến bay dựa vào sân bay đi và sân bay đến
    private static int flightId;
    private static String departureAirport;
    private static String destinationAirport;
    private static String destinationLocation;
    private static LocalDateTime departureDatetime;
    private static LocalDateTime arrivalDatetime;
    private static int availableSeats;

    // Hàm này để lấy dữ liệu từ bảng Chuyến Bay và gán vào các biến
    public static void loadDataFromDatabase(String departureAirportName, String destinationAirportName) {
        String sql = "SELECT A.flight_id, B.airport_name AS departure_airport, C.airport_name AS destination_airport, C.location AS destination_location, " +
                "A.departure_datetime, A.arrival_datetime, A.available_seats " +
                "FROM Flights A " +
                "JOIN Airports B ON A.departure_airport_id = B.airport_id " +
                "JOIN Airports C ON A.destination_airport_id = C.airport_id " +
                "WHERE B.airport_name = ? AND C.airport_name = ?";

        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, departureAirportName);
            preparedStatement.setString(2, destinationAirportName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    flightId = resultSet.getInt("flight_id");
                    departureAirport = resultSet.getString("departure_airport");
                    destinationAirport = resultSet.getString("destination_airport");
                    destinationLocation = resultSet.getString("destination_location");
                    departureDatetime = resultSet.getTimestamp("departure_datetime").toLocalDateTime();
                    arrivalDatetime = resultSet.getTimestamp("arrival_datetime").toLocalDateTime();
                    availableSeats = resultSet.getInt("available_seats");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // tìm chuyến bay dựa vào sân bay đi và sân bay đến








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
