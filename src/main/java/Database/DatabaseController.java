package Database;

import Admin.Controller.BaseController;
import Customer.Controller.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseController {

    // thêm thông tin khách hàng
    public static boolean addPassenger(String name, LocalDate birthday, String gender, String nationality,
            String address, String email, String phoneNumber, String password) {
        String query = "INSERT INTO Passengers (name, birthday, gender, nationality, address, email, phone_number, password) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseContection.getConnettion();
                PreparedStatement preparedStatement = connection.prepareStatement(query,
                        Statement.RETURN_GENERATED_KEYS)) {
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

            // Lấy giá trị tự tăng của passenger_id
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    System.out.println("Generated Passenger ID: " + generatedId);
                }
            }

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return airportList;
    }
    // hiển thị sân bay

    // tìm sân bay đến dựa vào sân bay đi
    public static ObservableList<String> getDestinationAirport(String airport) {
        ObservableList<String> airportList = FXCollections.observableArrayList();
        String sql = "SELECT A.destination_airport_id, B.airport_name AS destination_airport_name\n" +
                "FROM Flights A\n" +
                "JOIN Airports B ON A.destination_airport_id = B.airport_id\n" +
                "WHERE A.departure_airport_id = (SELECT airport_id FROM Airports WHERE airport_name = ?)";
        try (Connection connection = DatabaseContection.getConnettion();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, airport); // Gán giá trị cho tham số
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
                    // SignupPassController.showAlert("Lỗi","Invalid email or password.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    // dựa vào email và mật khẩu để tìm tài khoản hành khách

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
                    // SignupPassController.showAlert("Lỗi","Invalid email or password.");
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
        String sql = "SELECT " +
                "F.flight_id, " +
                "DA.airport_name AS departure_airport_name, " +
                "DA.location AS departure_location, " +
                "A.airport_name AS destination_airport_name, " +
                "A.location AS destination_location " +
                "FROM Flights F " +
                "INNER JOIN Airports DA ON F.departure_airport_id = DA.airport_id " +
                "INNER JOIN Airports A ON F.destination_airport_id = A.airport_id " +
                "WHERE F.flight_id = ?";
        try (Connection connection = DatabaseContection.getConnettion();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, flightId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Lấy thông tin địa điểm của sân bay
                    String departureLocation = resultSet.getString("departure_location");
                    String destinationLocation = resultSet.getString("destination_location");
                    // Hiển thị thông tin địa điểm của sân bay (Bạn có thể điều chỉnh phương thức
                    // này theo nhu cầu của bạn)
                    FlightFindController.displayLocation(departureLocation, destinationLocation);
                    Admin.Controller.HomeController.GetLocation(departureLocation, destinationLocation);
                    AllFlightController.GetLocation(departureLocation, destinationLocation);
                } else {
                    // Không tìm thấy bản ghi khớp
                    // Hiển thị cảnh báo hoặc thực hiện hành động phù hợp
                    SignupPassController.showAlert("Lỗi", "Không có dữ liệu đia điểm sân bay !");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // dựa vào id chuyến bay lấy ra địa điểm sân bay thông tin

    // lấy ra mã số ghế dựa vào loại ghế
    public static ObservableList<String> getSeatNumber(String seatType, int flightId) {
        ObservableList<String> seatNumberList = FXCollections.observableArrayList();
        String sql = "SELECT seat_numbers.seat_number " +
                "FROM seat_numbers " +
                "INNER JOIN seat_types ON seat_numbers.seat_type_id = seat_types.seat_type_id " +
                "WHERE seat_numbers.flight_id = ? AND seat_types.seat_type_name = ?";
        try (Connection connection = DatabaseContection.getConnettion();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, flightId);
            preparedStatement.setString(2, seatType);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String seatNumber = resultSet.getString("seat_number");
                    seatNumberList.add(seatNumber);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seatNumberList;
    }
    // lấy ra mã số ghế dựa vào loại ghế

    // lấy giá vé dựa vào id chuyến bay và khoang hạng
    public static void getPriceTicket(int flightId, String typeSeat) {
        String sql = "SELECT ticket_prices.price " +
                "FROM ticket_prices " +
                "INNER JOIN seat_types ON ticket_prices.seat_type_id = seat_types.seat_type_id " +
                "WHERE ticket_prices.flight_id = ? AND seat_types.seat_type_name = ?";
        try (Connection connection = DatabaseContection.getConnettion();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, flightId);
            preparedStatement.setString(2, typeSeat);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Lấy thông tin giá vé
                    String price = resultSet.getString("price");
                    // Hiển thị thông tin giá vé
                    BookingController.displayPrice(price);
                    Admin.Controller.HomeController.GetPrice(price);
                } else {
                    // Không tìm thấy bản ghi khớp
                    // Hiển thị cảnh báo hoặc thực hiện hành động phù hợp
                    SignupPassController.showAlert("Lỗi", "Không có dữ liệu giá vé!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // lấy giá vé dựa vào id chuyến bay và khoang hạng

    // lấy giá vé dựa vào id chuyến bay và khoang hạng
    public static void getPrices(int flightId, String typeSeat) {
        String sql = "SELECT ticket_prices.price " +
                "FROM ticket_prices " +
                "INNER JOIN seat_types ON ticket_prices.seat_type_id = seat_types.seat_type_id " +
                "WHERE ticket_prices.flight_id = ? AND seat_types.seat_type_name = ?";
        try (Connection connection = DatabaseContection.getConnettion();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, flightId);
            preparedStatement.setString(2, typeSeat);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Lấy thông tin giá vé
                    String price = resultSet.getString("price");
                    // Hiển thị thông tin giá vé
                    Admin.Controller.HomeController.GetPrices(price);
                } else {
                    // Không tìm thấy bản ghi khớp
                    // Hiển thị cảnh báo hoặc thực hiện hành động phù hợp
                    SignupPassController.showAlert("Lỗi", "Không có dữ liệu giá vé!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // lấy giá vé dựa vào id chuyến bay và khoang hạng

    // lấy ra chuyến bay dựa vào id khách hàng

//    public static ObservableList<Map<String, Object>> getBooked(int passengerId) {
//        ObservableList<Map<String, Object>> flights = FXCollections.observableArrayList();
//        String sql = "SELECT\n" +
//                "    b.booking_id,\n" +
//                "    departure_airport.airport_name AS departure_airport,\n" +
//                "    destination_airport.airport_name AS destination_airport,\n" +
//                "    f.departure_datetime,\n" +
//                "    st.seat_type_name,\n" +
//                "    sn.seat_number,\n" +
//                "    tp.price\n" +
//                "FROM\n" +
//                "    Bookings b\n" +
//                "JOIN Flights f ON b.flight_id = f.flight_id\n" +
//                "JOIN Seat_Numbers sn ON b.seat_id = sn.seat_id\n" +
//                "JOIN Seat_Types st ON b.seat_type_id = st.seat_type_id\n" +
//                "JOIN Ticket_Prices tp ON f.flight_id = tp.flight_id AND st.seat_type_id = tp.seat_type_id\n" +
//                "JOIN Airports departure_airport ON f.departure_airport_id = departure_airport.airport_id\n" +
//                "JOIN Airports destination_airport ON f.destination_airport_id = destination_airport.airport_id\n" +
//                "WHERE\n" +
//                "    b.passenger_id = ?;\n";
//        try (Connection connection = DatabaseContection.getConnettion();
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.setInt(1, passengerId);
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                while (resultSet.next()) {
//                    Map<String, Object> flight = new HashMap<>();
//                    flight.put("booking_id", String.valueOf(resultSet.getInt("booking_id")));
//                    flight.put("departure_airport", resultSet.getString("departure_airport"));
//                    flight.put("destination_airport", resultSet.getString("destination_airport"));
//                    flight.put("departure_datetime", resultSet.getString("departure_datetime"));
//                    flight.put("seat_type_name", resultSet.getString("seat_type_name"));
//                    flight.put("seat_number", resultSet.getString("seat_number"));
//                    flight.put("price", resultSet.getString("price"));
//                    flights.add(flight);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return flights;
//    }

    // - ------------------
    public static boolean getBooked(int passengerId) {

        String sql = "SELECT\n" +
                "    b.booking_id,\n" +
                "    departure_airport.airport_name AS departure_airport,\n" +
                "    destination_airport.airport_name AS destination_airport,\n" +
                "    f.departure_datetime,\n" +
                "    st.seat_type_name,\n" +
                "    sn.seat_number,\n" +
                "    tp.price\n" +
                "FROM\n" +
                "    Bookings b\n" +
                "JOIN Flights f ON b.flight_id = f.flight_id\n" +
                "JOIN Seat_Numbers sn ON b.seat_id = sn.seat_id\n" +
                "JOIN Seat_Types st ON b.seat_type_id = st.seat_type_id\n" +
                "JOIN Ticket_Prices tp ON f.flight_id = tp.flight_id AND st.seat_type_id = tp.seat_type_id\n" +
                "JOIN Airports departure_airport ON f.departure_airport_id = departure_airport.airport_id\n" +
                "JOIN Airports destination_airport ON f.destination_airport_id = destination_airport.airport_id\n" +
                "WHERE\n" +
                "    b.passenger_id = ?;\n";

        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, passengerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String booking_id = resultSet.getString("booking_id");
                    String departure_airport = resultSet.getString("departure_airport");
                    String destination_airport = resultSet.getString("destination_airport");
                    String departure_datetime = resultSet.getString("departure_datetime");
                    String seat_type_name = resultSet.getString("seat_type_name");
                    String seat_number = resultSet.getString("seat_number");
                    String price = resultSet.getString("price");
                    System.out.println(booking_id + departure_airport + destination_airport + departure_datetime
                            + seat_type_name + seat_number + price);
                    BookedController.ListBook.add(new BookedController.Book(booking_id, departure_airport, destination_airport, departure_datetime,
                            seat_type_name, seat_number, price));
                }
                return !BookedController.ListBook.isEmpty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    // - ------------------
    // lấy ra chuyến bay dựa vào id khách hàng

    // lấy ra id mã ghế dựa vào flightid và mã số ghế và khoang hạng
    public static void getSeatNumberId(int flightId, String typeSeat, String seatNumber) {
        String sql = "SELECT su.seat_id " +
                "FROM seat_numbers su " +
                "JOIN seat_types st ON su.seat_type_id = st.seat_type_id " +
                "WHERE su.flight_id = ?" +
                "  AND su.seat_number = ?" +
                "  AND st.seat_type_name = ?";
        try (Connection connection = DatabaseContection.getConnettion();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, flightId);
            preparedStatement.setString(3, typeSeat);
            preparedStatement.setString(2, seatNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Lấy thông tin giá vé
                    int seat_id = resultSet.getInt("seat_id");
                    // Hiển thị thông tin giá vé
                    BookingController.getIdSeat(seat_id);
                } else {
                    // Không tìm thấy bản ghi khớp
                    // Hiển thị cảnh báo hoặc thực hiện hành động phù hợp
                    SignupPassController.showAlert("Lỗi", "Không có dữ liệu ghế!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // lấy ra id mã ghế dựa vào flightid và mã số ghế và khoang hạng

    // đặt vé
    public static boolean addBooking(int passengerId, int flightId, LocalDate bookingDate,int Hk, int seatId,
            String bookingStatus) {
        String sql = "INSERT INTO Bookings (passenger_id, flight_id, booking_date, seat_id,seat_type_id, booking_status) VALUES (?, ?, ?, ?,?, ?)";
        try (Connection connection = DatabaseContection.getConnettion();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, passengerId);
            preparedStatement.setInt(2, flightId);
            preparedStatement.setObject(3, bookingDate);
            preparedStatement.setInt(5, Hk);
            preparedStatement.setInt(4, seatId);
            preparedStatement.setString(6, bookingStatus);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Booking added successfully.");
            } else {
                System.out.println("Failed to add booking.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        }
        return true;
    }
    // đặt vé

    // lấy ra ngày sinh dựa vào id khách hàng
    public static String getBir(int passengerId) {
        try (Connection connection = DatabaseContection.getConnettion()) {
            String query = "SELECT * FROM passengers WHERE passenger_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, passengerId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("birthday");
                    } else {
                        // No passenger found with the given ID
                        return ""; // Return an appropriate default value (e.g., empty string)
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception, maybe throw a custom exception or return a default value
            return ""; // Return an appropriate default value (e.g., empty string)
        }
    }
    // lấy ra ngày sinh dựa vào id khách hàng

    // lấy ra số điện thoại
    public static String getPhonePhone(int passengerId) {
        try (Connection connection = DatabaseContection.getConnettion()) {
            String query = "SELECT * FROM passengers WHERE passenger_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, passengerId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("phone_number");
                    } else {
                        // No passenger found with the given ID
                        return ""; // Return an appropriate default value (e.g., empty string)
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception, maybe throw a custom exception or return a default value
            return ""; // Return an appropriate default value (e.g., empty string)
        }
    }
    // lấy ra số điện thoại

    // lấy tên
    public static String getNamePassanger(int passengerId) {
        try (Connection connection = DatabaseContection.getConnettion()) {
            String query = "SELECT * FROM passengers WHERE passenger_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, passengerId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("name");
                    } else {
                        // No passenger found with the given ID
                        return "lỗi"; // Return an appropriate default value (e.g., empty string)
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception, maybe throw a custom exception or return a default value
            return "lỗi"; // Return an appropriate default value (e.g., empty string)
        }
    }
    // lấy tên

//    lấy email khách hàng
public static String getEmailPassanger(int passengerId) {
    try (Connection connection = DatabaseContection.getConnettion()) {
        String query = "SELECT * FROM passengers WHERE passenger_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, passengerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("email");
                } else {
                    // No passenger found with the given ID
                    return "lỗi"; // Return an appropriate default value (e.g., empty string)
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle the exception, maybe throw a custom exception or return a default value
        return "lỗi"; // Return an appropriate default value (e.g., empty string)
    }
}
//    lấy email khách hàng

    //đổi mật khẩu
    public static boolean updatePassword(int passengerId, String newPassword) {
        String sql = "UPDATE passengers\n" +
                "SET password = ?\n" +
                "WHERE passenger_id = ?;\n";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, passengerId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //đổi mật khẩu

    // xóa tài khoản
    public static boolean DeleteAcount(int passengerId) {
        String sql = "DELETE FROM passengers\n" +
                "WHERE passenger_id = ?";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, passengerId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // xóa tài khoản

//    xóa bảng bookinf dựa vào id khách hàng
public static boolean DeleteAcountForPasId(int passengerId) {
    String sql = "DELETE FROM bookings\n" +
            "WHERE passenger_id = ?";
    try (Connection connection = DatabaseContection.getConnettion();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setInt(1, passengerId);
        int rowsAffected = preparedStatement.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

//    xóa bảng bookinf dựa vào id khách hàng


    // lấy ra id đặt vé toàn bộ
// lấy ra id đặt vé toàn bộ
    public static ObservableList<String> getIDBook() {
        ObservableList<String> ID = FXCollections.observableArrayList();

        String sql = "SELECT booking_id FROM bookings";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String Id = resultSet.getString("booking_id");
                    ID.add(Id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ID;
    }

    // lấy ra id đặt vé toàn bộ

    // xóa đặt vé
    public static boolean DeleteBook(int ID) {
        boolean t =true;
        String sql = "DELETE FROM bookings\n" +
                "WHERE booking_id = ?";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, ID);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            t = false;
            e.printStackTrace();
            return t;
        }
    }
    // xóa đặt vé











    // Admin Login
    public static String ADMINNAME;
    public static boolean LoginAdmin(String user, String password) {
        String sql = "SELECT * FROM admin WHERE ad_user = ? AND ad_password = ?";
        try (Connection connection = DatabaseContection.getConnettion();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String ADMINNAME = resultSet.getString("ad_name");
                    Admin.Controller.HomeController frmhome = new Admin.Controller.HomeController();
                    frmhome.ADMINNAME = ADMINNAME;
                    System.out.println("successfully.");
                } else {
                    System.out.println("Failed");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    // Admin Login

    // lấy ra toàn bộ id chuyến bay
    public static List<Integer> getFlightIDs() {
        List<Integer> flightIds = new ArrayList<>();

        String sql = "SELECT flight_id FROM flights";

        try (Connection connection = DatabaseContection.getConnettion();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
    // lấy ra toàn bộ id chuyến bay

    // lấy thời gian dùng for cho id chuyến bay
    public static List<LocalDateTime> getTimesByFlightIds(int flightIds) {
        List<LocalDateTime> flightTimes = new ArrayList<>();

        String sql = "SELECT F.departure_datetime FROM Flights F WHERE F.flight_id = ?";

        try (Connection connection = DatabaseContection.getConnettion();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, flightIds);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    LocalDateTime departureTime = resultSet.getTimestamp("departure_datetime").toLocalDateTime();
                    flightTimes.add(departureTime);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flightTimes;
    }

    // lấy thời gian dùng for cho id chuyến bay

    // dựa vào id chuyến bay tìm sân bay đi
    public static List<String> getSBDiByFlightIds(int flightIds) {
        List<String> Sbdis = new ArrayList<>();

        String sql = "SELECT\n" +
                "    F.departure_airport_id,\n" +
                "    DA.airport_name AS departure_airport_name\n" +
                "FROM\n" +
                "    Flights F\n" +
                "JOIN Airports DA ON F.departure_airport_id = DA.airport_id\n" +
                "WHERE\n" +
                "    F.flight_id = ?;\n";
        try (Connection connection = DatabaseContection.getConnettion();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, flightIds);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String Sbdi = resultSet.getString("departure_airport_name");
                    Sbdis.add(Sbdi);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Sbdis;
    }

    // dựa vào id chuyến bay tìm sân bay đi

    // dựa vào id tìm sân bay đến
    public static List<String> getSBDenByFlightIds(int flightIds) {
        List<String> Sbdens = new ArrayList<>();

        String sql = "SELECT\n" +
                "    F.destination_airport_id ,\n" +
                "    DA.airport_name AS destination_airport_name\n" +
                "FROM\n" +
                "    Flights F\n" +
                "JOIN Airports DA ON F.destination_airport_id  = DA.airport_id\n" +
                "WHERE\n" +
                "    F.flight_id = ?;\n";
        try (Connection connection = DatabaseContection.getConnettion();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, flightIds);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String Sbden = resultSet.getString("destination_airport_name");
                    Sbdens.add(Sbden);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Sbdens;
    }
    // dựa vào id tìm sân bay đến

    // lấy id sân bay dựa vào tên
    public static int getAirportIdByName(String airportName) {
        int airportId = -1; // Giá trị mặc định nếu không tìm thấy

        String sql = "SELECT airport_id FROM Airports WHERE airport_name = ?";

        try (Connection connection = DatabaseContection.getConnettion();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, airportName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    airportId = resultSet.getInt("airport_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return airportId;
    }
    // lấy id sân bay dựa vào tên

    // lấy airline id dựa vào tên
    public static int getAirlineIdByName(String airlineName) {
        int airportId = -1; // Giá trị mặc định nếu không tìm thấy

        String sql = "SELECT airline_id FROM Airlines WHERE airline_name = ?";

        try (Connection connection = DatabaseContection.getConnettion();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, airlineName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    airportId = resultSet.getInt("airline_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return airportId;
    }
    // lấy airline id dựa vào tên

    // thêm chuyến bay
    public static boolean addFlight(int airlineId, int departureAirportId, int destinationAirportId,
            String departureDatetime, String arrivalDatetime,
            int availableSeats, int totalSeats) {
        try (Connection connection = DatabaseContection.getConnettion()) {
            String sql = "INSERT INTO Flights (airline_id, departure_airport_id, destination_airport_id, " +
                    "departure_datetime, arrival_datetime, available_seats, total_seats) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, airlineId);
                preparedStatement.setInt(2, departureAirportId);
                preparedStatement.setInt(3, destinationAirportId);

                LocalDateTime departureDateTime = LocalDateTime.parse(departureDatetime,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                preparedStatement.setTimestamp(4, Timestamp.valueOf(departureDateTime));

                LocalDate arrivalDate = LocalDate.parse(arrivalDatetime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                Date date = Date.valueOf(arrivalDate);
                preparedStatement.setDate(5, date);

                preparedStatement.setInt(6, availableSeats);
                preparedStatement.setInt(7, totalSeats);
                preparedStatement.executeUpdate();
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // thêm chuyến bay

    // xóa chuyến bay
    public static int deleteFlight(int flid) {
        try (Connection connection = DatabaseContection.getConnettion()) {
            // Kiểm tra ràng buộc khóa ngoại trong bảng Bookings
            String bookingCheckSql = "SELECT COUNT(*) FROM Bookings WHERE flight_id = ?";
            try (PreparedStatement bookingCheckStatement = connection.prepareStatement(bookingCheckSql)) {
                bookingCheckStatement.setInt(1, flid);
                try (ResultSet resultSet = bookingCheckStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        return -1; // Ràng buộc khóa ngoại trong bảng Bookings
                    }
                }
            }

            // Kiểm tra ràng buộc khóa ngoại trong bảng SeatNumbers
            String seatNumberCheckSql = "SELECT COUNT(*) FROM seat_numbers WHERE flight_id = ?";
            try (PreparedStatement seatNumberCheckStatement = connection.prepareStatement(seatNumberCheckSql)) {
                seatNumberCheckStatement.setInt(1, flid);
                try (ResultSet resultSet = seatNumberCheckStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        return -2; // Ràng buộc khóa ngoại trong bảng SeatNumbers
                    }
                }
            }

            // Kiểm tra ràng buộc khóa ngoại trong bảng TicketPrices
            String ticketPriceCheckSql = "SELECT COUNT(*) FROM ticket_prices WHERE flight_id = ?";
            try (PreparedStatement ticketPriceCheckStatement = connection.prepareStatement(ticketPriceCheckSql)) {
                ticketPriceCheckStatement.setInt(1, flid);
                try (ResultSet resultSet = ticketPriceCheckStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        return -3; // Ràng buộc khóa ngoại trong bảng TicketPrices
                    }
                }
            }

            // Nếu không có ràng buộc khóa ngoại, thực hiện xóa chuyến bay
            String deleteSql = "DELETE FROM Flights WHERE flight_id = ?";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
                deleteStatement.setInt(1, flid);
                int rowsAffected = deleteStatement.executeUpdate();
                return rowsAffected > 0 ? 1 : 0; // Trả về 1 nếu xóa thành công, ngược lại trả về 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -4; // Lỗi SQL
        }
    }

    // xóa chuyến bay

    // Xóa vé dựa vào id
    public static boolean deleteTicketPricesByFlightId(int flightId) {
        try (Connection connection = DatabaseContection.getConnettion()) {
            String deleteSql = "DELETE FROM ticket_prices WHERE flight_id = ?";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
                deleteStatement.setInt(1, flightId);
                int rowsAffected = deleteStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Xóa vé dựa vào id

    // Xóa ghế
    public static boolean deleteSeatNumbersByFlightId(int flightId) {
        try (Connection connection = DatabaseContection.getConnettion()) {
            String deleteSql = "DELETE FROM seat_numbers WHERE flight_id = ?";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
                deleteStatement.setInt(1, flightId);
                int rowsAffected = deleteStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa ghế

    // xóa đặt vé dựa vào id chuyến bay
    public static boolean deleteBookingsByFlightId(int flightId) {
        try (Connection connection = DatabaseContection.getConnettion()) {
            String deleteSql = "DELETE FROM Bookings WHERE flight_id = ?";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
                deleteStatement.setInt(1, flightId);
                int rowsAffected = deleteStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // xóa đặt vé dựa vào id chuyến bay

    // thêm sân bay
    public static boolean addAirport(String nameAirport, String location) {
        try (Connection connection = DatabaseContection.getConnettion()) {
            String sql = "INSERT INTO Airports (airport_name, location) VALUES (?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, nameAirport);
                preparedStatement.setString(2, location);

                // Thực hiện truy vấn INSERT
                int rowsAffected = preparedStatement.executeUpdate();

                // Kiểm tra xem có bản ghi nào bị ảnh hưởng không
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // thêm sân bay

    // xóa sân bay dựa vào tên
    public static boolean deleteAirportForName(String nameAirport) {
        try (Connection connection = DatabaseContection.getConnettion()) {
            // Trước hết, xóa dữ liệu liên quan trong các bảng khác
            deleteSeatNumbersAndTicketPricesAndBookingByAirportName(connection, nameAirport);
            deleteFlightsByAirportName(connection, nameAirport);

            // Sau đó, xóa dữ liệu trong bảng Airports
            String sql = "DELETE FROM Airports WHERE airport_name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, nameAirport);

                // Thực hiện truy vấn DELETE
                int rowsAffected = preparedStatement.executeUpdate();

                // Kiểm tra số dòng bị ảnh hưởng để xác định liệu xóa thành công hay không
                if (rowsAffected > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Hàm xóa chuyến bay có sân bay xuất phát hoặc đến
    private static void deleteFlightsByAirportName(Connection connection, String Name) throws SQLException {
        String deleteFlightsSql = "DELETE FROM Flights WHERE departure_airport_id = ? OR destination_airport_id = ?";
        try (PreparedStatement deleteFlightsStatement = connection.prepareStatement(deleteFlightsSql)) {
            deleteFlightsStatement.setString(1, Name);
            deleteFlightsStatement.setString(2, Name);
            deleteFlightsStatement.executeUpdate();
        }
    }

    // Hàm xóa mã số ghế và giá vé liên quan đến chuyến bay có sân bay xuất phát
    // hoặc đến
    private static void deleteSeatNumbersAndTicketPricesAndBookingByAirportName(Connection connection,
            String airportName) throws SQLException {
        String deleteTicketPricesSql = "DELETE FROM ticket_prices WHERE flight_id IN (SELECT flight_id FROM Flights WHERE departure_airport_id IN (SELECT airport_id FROM Airports WHERE airport_name = ?) OR destination_airport_id IN (SELECT airport_id FROM Airports WHERE airport_name = ?))";
        try (PreparedStatement deleteTicketPricesStatement = connection.prepareStatement(deleteTicketPricesSql)) {
            deleteTicketPricesStatement.setString(1, airportName);
            deleteTicketPricesStatement.setString(2, airportName);
            deleteTicketPricesStatement.executeUpdate();
        }
        String deleteBookingsSql = "DELETE FROM bookings WHERE flight_id IN (SELECT flight_id FROM Flights WHERE departure_airport_id IN (SELECT airport_id FROM Airports WHERE airport_name = ?) OR destination_airport_id IN (SELECT airport_id FROM Airports WHERE airport_name = ?))";
        try (PreparedStatement deleteBookingsStatement = connection.prepareStatement(deleteBookingsSql)) {
            deleteBookingsStatement.setString(1, airportName);
            deleteBookingsStatement.setString(2, airportName);
            deleteBookingsStatement.executeUpdate();
        }
        String deleteSeatNumbersSql = "DELETE FROM seat_numbers WHERE flight_id IN (SELECT flight_id FROM Flights WHERE departure_airport_id IN (SELECT airport_id FROM Airports WHERE airport_name = ?) OR destination_airport_id IN (SELECT airport_id FROM Airports WHERE airport_name = ?))";
        try (PreparedStatement deleteSeatNumbersStatement = connection.prepareStatement(deleteSeatNumbersSql)) {
            deleteSeatNumbersStatement.setString(1, airportName);
            deleteSeatNumbersStatement.setString(2, airportName);
            deleteSeatNumbersStatement.executeUpdate();
        }
    }

    // xóa sân bay dựa vào tên và địa điểm sân bay

    // xóa sân bay dựa vào id
    public static boolean deleteAirport(int airportId) {
        try (Connection connection = DatabaseContection.getConnettion()) {
            // Xóa chuyến bay có sân bay xuất phát hoặc đến
            deleteSeatNumbersAndTicketPricesAndBookingByAirportId(connection, airportId);
            deleteFlightsByAirportId(connection, airportId);

            // Xóa mã số ghế và giá vé liên quan đến chuyến bay có sân bay xuất phát hoặc
            // đến

            // Xóa sân bay
            String deleteAirportSql = "DELETE FROM Airports WHERE airport_id = ?";
            try (PreparedStatement deleteAirportStatement = connection.prepareStatement(deleteAirportSql)) {
                deleteAirportStatement.setInt(1, airportId);
                int rowsAffected = deleteAirportStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hàm xóa chuyến bay có sân bay xuất phát hoặc đến
    private static void deleteFlightsByAirportId(Connection connection, int airportId) throws SQLException {
        String deleteFlightsSql = "DELETE FROM Flights WHERE departure_airport_id = ? OR destination_airport_id = ?";
        try (PreparedStatement deleteFlightsStatement = connection.prepareStatement(deleteFlightsSql)) {
            deleteFlightsStatement.setInt(1, airportId);
            deleteFlightsStatement.setInt(2, airportId);
            deleteFlightsStatement.executeUpdate();
        }
    }

    // Hàm xóa mã số ghế và giá vé liên quan đến chuyến bay có sân bay xuất phát
    // hoặc đến
    private static void deleteSeatNumbersAndTicketPricesAndBookingByAirportId(Connection connection, int airportId)
            throws SQLException {
        String deleteTicketPricesSql = "DELETE FROM ticket_prices WHERE flight_id IN (SELECT flight_id FROM Flights WHERE departure_airport_id = ? OR destination_airport_id = ?)";
        try (PreparedStatement deleteTicketPricesStatement = connection.prepareStatement(deleteTicketPricesSql)) {
            deleteTicketPricesStatement.setInt(1, airportId);
            deleteTicketPricesStatement.setInt(2, airportId);
            deleteTicketPricesStatement.executeUpdate();
        }

        String deleteBookingsSql = "DELETE FROM bookings WHERE flight_id IN (SELECT flight_id FROM Flights WHERE departure_airport_id = ? OR destination_airport_id = ?)";
        try (PreparedStatement deleteBookingsStatement = connection.prepareStatement(deleteBookingsSql)) {
            deleteBookingsStatement.setInt(1, airportId);
            deleteBookingsStatement.setInt(2, airportId);
            deleteBookingsStatement.executeUpdate();
        }
        String deleteSeatNumbersSql = "DELETE FROM seat_numbers WHERE flight_id IN (SELECT flight_id FROM Flights WHERE departure_airport_id = ? OR destination_airport_id = ?)";
        try (PreparedStatement deleteSeatNumbersStatement = connection.prepareStatement(deleteSeatNumbersSql)) {
            deleteSeatNumbersStatement.setInt(1, airportId);
            deleteSeatNumbersStatement.setInt(2, airportId);
            deleteSeatNumbersStatement.executeUpdate();
        }

    }

    // thêm ghế
    public static boolean addSeatNumber(int flightId, String seatNumber, String seatStatus, int seatTypeId) {
        try (Connection connection = DatabaseContection.getConnettion()) {
            // Kiểm tra xem flight_id đã tồn tại trong bảng Flights hay chưa
            if (!isFlightExists(connection, flightId)) {
                BaseController.showAlert("Lỗi", "ID chuyến bay không tồn tại !");
                return false;
            }

            // Nếu flight_id tồn tại, thêm mã số ghế vào bảng seat_numbers
            String sql = "INSERT INTO seat_numbers (flight_id, seat_number, seat_status, seat_type_id) VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, flightId);
                preparedStatement.setString(2, seatNumber);
                preparedStatement.setString(3, seatStatus);
                preparedStatement.setInt(4, seatTypeId);

                // Thực hiện truy vấn INSERT
                int rowsAffected = preparedStatement.executeUpdate();

                // Kiểm tra xem có bản ghi nào bị ảnh hưởng không
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Phương thức kiểm tra xem flight_id có tồn tại trong bảng Flights hay không
    private static boolean isFlightExists(Connection connection, int flightId) throws SQLException {
        String sql = "SELECT 1 FROM Flights WHERE flight_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, flightId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    // thêm ghế
    // lấy id dựa vào hạng khoang
    public static int getSeatType(String seatTypeName) {
        try (Connection connection = DatabaseContection.getConnettion()) {
            String sql = "SELECT seat_type_id FROM seat_types WHERE seat_type_name = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, seatTypeName);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("seat_type_id");
                    } else {
                        // Nếu không tìm thấy loại ghế, có thể xử lý bằng cách in ra một thông báo hoặc
                        // trả về một giá trị đặc biệt.
                        System.out.println("Seat type with name " + seatTypeName + " not found.");
                        return -1; // Hoặc giá trị đặc biệt khác tùy theo yêu cầu của bạn.
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Xử lý lỗi nếu cần thiết.
        }
    }

    // Lấy id hãng dựa vào tên
    public static int IDAirlineForm;
    public static boolean GetAirlineID(String AirlineName) {
        String sql = "SELECT airline_id FROM airlines WHERE airline_name = ?";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, AirlineName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int IDAirline = resultSet.getInt("airline_id");
                    IDAirlineForm = IDAirline;
                    Admin.Controller.HomeController frmHomeAdmin = new Admin.Controller.HomeController();
                    frmHomeAdmin.IDAirlineForm = IDAirline;
                    System.out.println("successfully.");
                } else {
                    System.out.println("Failed");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    // Lấy id hãng dựa vào tên

    // lấy toàn bộ tên airline
    public static ObservableList<String> getAirlinesName() {
        ObservableList<String> airlineList = FXCollections.observableArrayList();
        String sql = "SELECT airline_name FROM airlines";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String airlinesName = resultSet.getString("airline_name");
                airlineList.add(airlinesName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return airlineList;
    }
    // lấy toàn bộ tên airline

    // lấy toàn bộ id sân bay
    public static ObservableList<String> getIDAirport() {
        ObservableList<String> IDS = FXCollections.observableArrayList();
        String sql = "SELECT airport_id FROM airports";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String ID = resultSet.getString("airport_id");
                IDS.add(ID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return IDS;
    }
    // lấy toàn bộ id sân bay


    // Lấy ra toàn bộ danh sách sân bay
    public static boolean getAirportName() {
        String sql = "SELECT airport_id, airport_name,location FROM airports";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String ID1 = resultSet.getString("airport_id");
                String ID2 = resultSet.getString("airport_name");
                String ID3 = resultSet.getString("location");
                Admin.Controller.HomeController.ListAirport.add(new Admin.Controller.HomeController.Airport(ID1,ID2,ID3));
            }
            return !Admin.Controller.HomeController.ListAirport.isEmpty();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Lấy ra toàn bộ danh sách sân bay

    // laays ra san bay dựa vào id
    public static boolean getAirportByID(int ID) {
        String sql = "SELECT airport_id, airport_name, location FROM airports WHERE airport_id = ?";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, ID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String ID1 = resultSet.getString("airport_id");
                    String ID2 = resultSet.getString("airport_name");
                    String ID3 = resultSet.getString("location");
                    Admin.Controller.HomeController.ListAirport.add(new Admin.Controller.HomeController.Airport(ID1, ID2, ID3));
                }
                return !Admin.Controller.HomeController.ListAirport.isEmpty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // laays ra san bay dựa vào id

    // xóa ghế dựa vào id
    // laays ra san bay dựa vào id
    public static boolean DeleteChair(int ID) {
        String sql = "DELETE FROM seat_numbers WHERE seat_id = ?";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, ID); // Đặt giá trị cho tham số 1
            int rowsAffected = preparedStatement.executeUpdate(); // Thực hiện xóa và trả về số hàng bị ảnh hưởng
            return rowsAffected > 0; // Trả về true nếu có ít nhất một hàng bị xóa
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // xóa ghế dựa vào id


    // tìm kiếm kahcsh hangd dựa vào số điện thoại và tên
    public static boolean getPassByID(int ID) {
        String sql = "SELECT passenger_id, name, birthday, gender, address, phone_number, email FROM passengers WHERE passenger_id = ?";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, ID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String passengerID = resultSet.getString("passenger_id");
                    String name = resultSet.getString("name");
                    String birthday = resultSet.getString("birthday");
                    String gender = resultSet.getString("gender");
                    String address = resultSet.getString("address");
                    String phoneNumber = resultSet.getString("phone_number");
                    String email = resultSet.getString("email");

                    Admin.Controller.HomeController.ListPassenger.add(new Admin.Controller.HomeController.Passenger(passengerID, name, birthday, gender, address, phoneNumber, email));
                }
                return !Admin.Controller.HomeController.ListPassenger.isEmpty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // tìm kiếm kahcsh hangd dựa vào số điện thoại và tên

    // Trạng thái thanh toán
    public static String GetStatusPay(int passengerID, int flightID) {
        String sql = "SELECT booking_status FROM bookings WHERE passenger_id = ? AND flight_id = ?";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, passengerID);
            preparedStatement.setInt(2, flightID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("booking_status");
                } else {
                    // Không tìm thấy bản ghi phù hợp
                    return null; // Hoặc có thể trả về một giá trị khác để biểu thị trạng thái không tìm thấy
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Hoặc xử lý theo cách khác tùy thuộc vào yêu cầu của bạn
        }
    }

    // Trạng thái thanh toán

    // cập nhật thanh toán
    public static boolean UpdateStatusPay(int passengerID, int flightID, String status) {
        String sql = "UPDATE bookings SET booking_status = ? WHERE passenger_id = ? AND flight_id = ?";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, passengerID);
            preparedStatement.setInt(3, flightID);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // cập nhật thanh toán



    //tìm khách hàng dựa vào id
    public static boolean getPassByNameAndPhone(String named , String phone) {
        String sql = "SELECT passenger_id, name, birthday, gender, address, phone_number, email FROM passengers WHERE name = ? AND phone_number =?";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, named);
            preparedStatement.setString(2, phone);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String passengerID = resultSet.getString("passenger_id");
                    String name = resultSet.getString("name");
                    String birthday = resultSet.getString("birthday");
                    String gender = resultSet.getString("gender");
                    String address = resultSet.getString("address");
                    String phoneNumber = resultSet.getString("phone_number");
                    String email = resultSet.getString("email");

                    Admin.Controller.HomeController.ListPassenger.add(new Admin.Controller.HomeController.Passenger(passengerID, name, birthday, gender, address, phoneNumber, email));
                }
                return !Admin.Controller.HomeController.ListPassenger.isEmpty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //tìm khách hàng dựa vào id


    // laays ra san bay dựa vào id
    public static boolean getAirportByName(String Name) {
        String sql = "SELECT airport_id, airport_name, location FROM airports WHERE airport_name = ?";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, Name); // Đặt giá trị cho tham số 1
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String ID1 = resultSet.getString("airport_id");
                    String ID2 = resultSet.getString("airport_name");
                    String ID3 = resultSet.getString("location");
                    Admin.Controller.HomeController.ListAirport.add(new Admin.Controller.HomeController.Airport(ID1, ID2, ID3));
                }
                return !Admin.Controller.HomeController.ListAirport.isEmpty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // laays ra san bay dựa vào id

    // lấy ra toàn bộ danh sách ghế
    public static boolean getSeatNumber() {
        String sql = "SELECT * FROM seat_numbers";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String ID1 = resultSet.getString("seat_id");
                String ID2 = resultSet.getString("flight_id");
                String ID4 = resultSet.getString("seat_number");
                String ID5 = resultSet.getString("seat_type_id");
                String ID6 = resultSet.getString("seat_status");
                Admin.Controller.HomeController.ListSeat.add(new Admin.Controller.HomeController.Seats(ID1,ID2,ID4,ID5,ID6));
            }
            return !Admin.Controller.HomeController.ListSeat.isEmpty();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // lấy ra toàn bộ danh sách ghế

    // lấy ra toàn bộ thông tin khách hàng
    public static boolean getPassengers() {
        String sql = "SELECT passenger_id , name ,birthday,gender,address,phone_number,email FROM passengers";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String ID1 = resultSet.getString("passenger_id");
                String ID2 = resultSet.getString("name");
                String ID4 = resultSet.getString("birthday");
                String ID5 = resultSet.getString("gender");
                String ID6 = resultSet.getString("address");
                String ID7 = resultSet.getString("phone_number");
                String ID8 = resultSet.getString("email");
                Admin.Controller.HomeController.ListPassenger.add(new Admin.Controller.HomeController.Passenger(ID1,ID2,ID4,ID5,ID6,ID7,ID8));
            }
            return !Admin.Controller.HomeController.ListAirport.isEmpty();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // lấy ra toàn bộ thông tin khách hàng








    // lấy id chuyến bay dựa và id khách hàng
    public static List<Integer> getFlightForIDPass(int passengerId) {
        List<Integer> flightIds = new ArrayList<>();
        String sql = "SELECT F.flight_id " +
                "FROM Flights F " +
                "JOIN Bookings B ON F.flight_id = B.flight_id " +
                "WHERE B.passenger_id = ?";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, passengerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int flightID = resultSet.getInt("flight_id");
                    flightIds.add(flightID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flightIds;
    }

    // lấy id chuyến bay dựa và id khách hàng


    // thêm airline
    public static boolean addAirline(String Name) {
        try (Connection connection = DatabaseContection.getConnettion()) {

            String sql = "INSERT INTO airlines (airline_name) VALUES (?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, Name);
                int rowsAffected = preparedStatement.executeUpdate();

                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // thêm airline

    // xóa airline
    public static boolean deleteAirline(String airlineName) {
        try (Connection connection = DatabaseContection.getConnettion()) {
            String sql = "DELETE FROM airlines WHERE airline_name = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, airlineName);
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // xóa airline

    // lấy ra toàn bộ airline
    public static boolean getAirlines() {
        String sql = "SELECT airline_id , airline_name FROM airlines";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String ID1 = resultSet.getString("airline_id");
                String ID2 = resultSet.getString("airline_name");
                Admin.Controller.HomeController.ListAirlines.add(new Admin.Controller.HomeController.Airlines(ID1,ID2));
            }
            return !Admin.Controller.HomeController.ListAirport.isEmpty();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // lấy ra toàn bộ airline

    // tim airline dua vao ten
    public static boolean FindAirline(String name) {
        String sql = "SELECT airline_id , airline_name FROM airlines WHERE airline_name =?";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String ID1 = resultSet.getString("airline_id");
                    String ID2 = resultSet.getString("airline_name");
                    Admin.Controller.HomeController.ListAirlines.add(
                            new Admin.Controller.HomeController.Airlines(ID1,ID2)
                    );
                }
                return !Admin.Controller.HomeController.ListAirlines.isEmpty();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
        }
    }
    // tim airline dua vao ten


    // lấy thời gian dự kiến dựa vào id chuyến bay
    // lấy thời gian dự kiến dựa vào id chuyến bay

    // lấy ra tổng chổ ngồi dựa vào id chuyến bay
    // lấy ra tổng chổ ngồi dựa vào id chuyến bay



    // doanh thu hôm nay

    public static double getRevenueToDay() {
        try (Connection connection = DatabaseContection.getConnettion()){
            String query = "SELECT SUM(tp.price) AS total_price " +
                    "FROM Bookings b " +
                    "JOIN Ticket_Prices tp ON b.flight_id = tp.flight_id AND b.seat_type_id = tp.seat_type_id " +
                    "WHERE DATE(b.booking_date) = CURDATE()";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    double totalPrice = resultSet.getDouble("total_price");
                    System.out.println("Total Price of Bookings Today: " + totalPrice);
                    return totalPrice;
                } else {
                    // No result found for today
                    System.out.println("No bookings today.");
                    return 0.0; // or throw a specific exception
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception, maybe throw a custom exception or return a default value
            return 0.0; // or throw a specific exception
        }
    }

    // doanh thu hôm nay

    // doanh thu tuần này
    public static double getRevenueBetween() {
        try (Connection connection = DatabaseContection.getConnettion()){
            String query = "SELECT SUM(tp.price) AS total_price " +
                    "FROM Bookings b " +
                    "JOIN Ticket_Prices tp ON b.flight_id = tp.flight_id AND b.seat_type_id = tp.seat_type_id " +
                    "WHERE b.booking_date BETWEEN CURDATE() - INTERVAL 6 DAY AND CURDATE()";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    double totalPrice = resultSet.getDouble("total_price");
                    System.out.println("Total Price of Bookings in the Last 7 Days: " + totalPrice);
                    return totalPrice;
                } else {
                    // No result found for the last 7 days
                    System.out.println("No bookings in the last 7 days.");
                    return 0.0; // or throw a specific exception
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception, maybe throw a custom exception or return a default value
            return 0.0; // or throw a specific exception
        }
    }
    // doanh thu tuần này

    // chuyến bay đã đặt trong ngày
    public static int getBookedFlights() {
        try (Connection connection = DatabaseContection.getConnettion()){
            String query = "SELECT COUNT(DISTINCT flight_id) AS total_booked_flights\n" +
                    "FROM bookings\n" +
                    "WHERE DATE(booking_date) = CURDATE();\n";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int totalBK = resultSet.getInt("total_booked_flights"); // Corrected column name
                    return totalBK;
                } else {
                    // No result found for the current date
                    return 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception, maybe throw a custom exception or return a default value
            return 0;
        }
    }
    // chuyến bay đã đặt trong ngày

    // tống số vé được đặt trong năm
    public static int getTiketsFlights() {
        try (Connection connection = DatabaseContection.getConnettion()){
            String query = "SELECT COUNT(*) AS total_booked_tickets\n" +
                    "FROM bookings\n" +
                    "WHERE YEAR(booking_date) = YEAR(CURDATE());";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int total = resultSet.getInt("total_booked_tickets");
                    return total;
                } else {
                    // No result found for the last 7 days
                    return 0; // or throw a specific exception
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception, maybe throw a custom exception or return a default value
            return 0; // or throw a specific exception
        }
    }
    // tống số vé được đặt trong năm

    // doanh thu tháng này
    public static double getTotalPriceForCurrentMonth() {
        try (Connection connection = DatabaseContection.getConnettion()) {
            String query = "SELECT SUM(tp.price) AS total_price\n" +
                    "FROM bookings b\n" +
                    "JOIN ticket_prices tp ON b.flight_id = tp.flight_id AND b.seat_type_id = tp.seat_type_id\n" +
                    "WHERE MONTH(b.booking_date) = MONTH(CURDATE()) AND YEAR(b.booking_date) = YEAR(CURDATE())";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("total_price");
                } else {
                    // No bookings found for the current month
                    return 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception, maybe throw a custom exception or return a default value
            return 0;
        }
    }
    // doanh thu tháng này

    // theo quý này
    public static double getTotalRevenueForCurrentQuarter() {
        try (Connection connection = DatabaseContection.getConnettion()) {
            String query = "SELECT SUM(tp.price) AS total_revenue\n" +
                    "FROM bookings b\n" +
                    "JOIN ticket_prices tp ON b.flight_id = tp.flight_id AND b.seat_type_id = tp.seat_type_id\n" +
                    "WHERE QUARTER(b.booking_date) = QUARTER(CURDATE()) AND YEAR(b.booking_date) = YEAR(CURDATE())";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("total_revenue");
                } else {
                    // No bookings found for the current quarter
                    return 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception, maybe throw a custom exception or return a default value
            return 0;
        }
    }
    // theo quý này
}
