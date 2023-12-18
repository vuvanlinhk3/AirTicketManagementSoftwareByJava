package Database;

import Customer.Controller.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseController  {

    // thêm thông tin khách hàng
    public static boolean addPassenger(String name, LocalDate birthday, String gender, String nationality,
                                       String address, String email, String phoneNumber, String password) {
        String query = "INSERT INTO Passengers (name, birthday, gender, nationality, address, email, phone_number, password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
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
    public static void getLocationAirport(int flightId ) {
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
                    // Hiển thị thông tin địa điểm của sân bay (Bạn có thể điều chỉnh phương thức này theo nhu cầu của bạn)
                    BookingController.displayLocation(departureLocation, destinationLocation);
                } else {
                    // Không tìm thấy bản ghi khớp
                    // Hiển thị cảnh báo hoặc thực hiện hành động phù hợp
                    SignupPassController.showAlert("Lỗi","Không có dữ liệu đia điểm sân bay !");
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
    public static void geBooked(int passengerId) {
        String sql = "SELECT F.flight_id, A1.airport_name AS departure_airport, A2.airport_name AS destination_airport, " +
                "F.departure_datetime, ST.seat_type_name, SN.seat_number, TP.price " +
                "FROM Flights F " +
                "JOIN Airports A1 ON F.departure_airport_id = A1.airport_id " +
                "JOIN Airports A2 ON F.destination_airport_id = A2.airport_id " +
                "JOIN seat_numbers SN ON F.flight_id = SN.flight_id " +
                "JOIN seat_types ST ON SN.seat_type_id = ST.seat_type_id " +
                "JOIN ticket_prices TP ON F.flight_id = TP.flight_id AND ST.seat_type_id = TP.seat_type_id " +
                "JOIN Bookings B ON F.flight_id = B.flight_id " +
                "WHERE B.passenger_id = ?";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, passengerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Lấy thông tin giá vé
                    int flightID = resultSet.getInt("flight_id");
                    String departure_airport = resultSet.getString("departure_airport");
                    String destination_airport = resultSet.getString("destination_airport");
                    String departure_datetime = resultSet.getString("departure_datetime");
                    String seat_type_name = resultSet.getString("seat_type_name");
                    String seat_number = resultSet.getString("seat_number");
                    String price = resultSet.getString("price");
                    // Hiển thị thông tin giá vé
                    BookedController.getFlightBooked(flightID,departure_airport,destination_airport,departure_datetime,
                            seat_type_name,seat_number,price
                            );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // lấy ra chuyến bay dựa vào id khách hàng

    // lấy ra id mã ghế dựa vào flightid và mã số ghế và  khoang hạng
    public static void getSeatNumberId(int flightId, String typeSeat ,String seatNumber) {
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
    // lấy ra id mã ghế dựa vào flightid và mã số ghế và  khoang hạng

    // đặt vé
    public static boolean addBooking(int passengerId, int flightId, LocalDate bookingDate, int seatId, String bookingStatus) {
        String sql = "INSERT INTO Bookings (passenger_id, flight_id, booking_date, seat_id, booking_status) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, passengerId);
            preparedStatement.setInt(2, flightId);
            preparedStatement.setObject(3, bookingDate);
            preparedStatement.setInt(4, seatId);
            preparedStatement.setString(5, bookingStatus);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Booking added successfully.");
            } else {
                System.out.println("Failed to add booking.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    // đặt vé












    //Admin Login
    public static boolean LoginAdmin(String user , String password) {
        String sql = "SELECT * FROM admin WHERE ad_user = ? AND ad_password = ?";
        try (Connection connection = DatabaseContection.getConnettion();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String passengerId = resultSet.getString("ad_user");
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
    //Admin Login

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


    //lấy thời gian dùng for cho id chuyến bay
    public static List<LocalDateTime> getTimesByFlightIds( int flightIds) {
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

    //lấy thời gian dùng for cho id chuyến bay


    // dựa vào id chuyến bay tìm sân bay đi
    public static List<String> getSBDiByFlightIds( int flightIds) {
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
                    String  Sbdi = resultSet.getString("departure_airport_name");
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
    public static List<String> getSBDenByFlightIds( int flightIds) {
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
                    String  Sbden = resultSet.getString("destination_airport_name");
                    Sbdens.add(Sbden);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Sbdens;
    }
    // dựa vào id tìm sân bay đến

    // lấy id sân bay dựa vào  tên
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
    // lấy id sân bay dựa vào  tên

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

                LocalDateTime departureDateTime = LocalDateTime.parse(departureDatetime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
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
}
