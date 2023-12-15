package Database;

import Customer.Controller.BookingController;
import Customer.Controller.FlightFindController;
import Customer.Controller.LoginController;
import Customer.Controller.SignupPassController;
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

    // dựa vào email và mật khẩu để tìm tài khoản hành khách
    public static boolean getPassengerForEmail(String email, String password) {
        String sql = "SELECT * FROM Passengers WHERE email = ? AND password = ?";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve customer information
                    int passengerId = resultSet.getInt("passenger_id");
                    String name = resultSet.getString("name");
                    // Add more fields as needed

                    // Display customer information (you can customize this part)
                    LoginController.displayCustomerInfo(passengerId, name);

                } else {
                    // No matching record found
//                    SignupPassController.showAlert("Lỗi","Invalid email or password.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    // dựa vào email và mật khẩu  để tìm tài khoản hành khách

    // dựa vào số điện thoại để tìm tài khoản hành khách
    public static boolean getPassengerForPhone(String phone, String password) {
        String sql = "SELECT * FROM Passengers WHERE phone_number = ? AND password = ?";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, phone);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve customer information
                    int passengerId = resultSet.getInt("passenger_id");
                    String name = resultSet.getString("name");
                    // Add more fields as needed

                    // Display customer information (you can customize this part)
                    LoginController.displayCustomerInfo(passengerId, name);

                } else {
                    // No matching record found
//                    SignupPassController.showAlert("Lỗi","Invalid email or password.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    // dựa vào số điện thoại để tìm tài khoản hành khách

    // dựa vào id chuyến bay lấy ra địa điểm sân bay thông tin
    public static void getLocationAirport(int flightId) {
        String sql = "SELECT departure.location AS departure_location, destination.location AS destination_location " +
                "FROM Flights " +
                "JOIN Airports departure ON Flights.departure_airport_id = departure.airport_id " +
                "JOIN Airports destination ON Flights.destination_airport_id = destination.airport_id " +
                "WHERE Flights.flight_id = ?";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, flightId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Lấy thông tin địa điểm của sân bay
                    String departureLocation = resultSet.getString("departure_location");
                    String destinationLocation = resultSet.getString("destination_location");
                    // Hiển thị thông tin địa điểm của sân bay (Bạn có thể điều chỉnh phương thức này theo nhu cầu của bạn)
                    BookingController.displayLocation(departureLocation, destinationLocation);
                } else {
                    // Không tìm thấy bản ghi khớp
                    // Hiển thị cảnh báo hoặc thực hiện hành động phù hợp
                    SignupPassController.showAlert("Lỗi","Lỗi gì đó :>");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // dựa vào id chuyến bay lấy ra địa điểm sân bay thông tin

    // lấy ra mã số ghế dựa vào loại ghế
    public static ObservableList<String> getSeatNumber(String typeSeat) {
        ObservableList<String> airportList = FXCollections.observableArrayList();
        String sql = "SELECT SeatNumbers.seat_number " +
                "FROM SeatNumbers " +
                "INNER JOIN SeatTypes ON SeatNumbers.seat_type_id = SeatTypes.seat_type_id " +
                "WHERE SeatTypes.seat_type_name = ?";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String seatNumber = resultSet.getString("seat_number");
                airportList.add(seatNumber);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return airportList;
    }
    // lấy ra mã số ghế dựa vào loại ghế










}
