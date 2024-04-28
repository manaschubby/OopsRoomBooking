package com.kanchan.RoomBooking.Users;

import com.kanchan.RoomBooking.Bookings.BookingModel;
import com.kanchan.RoomBooking.Bookings.BookingRepository;
import com.kanchan.RoomBooking.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<Object> signUp(UserModel user) {
        UserModel existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            Map<String, Object> error = Error.errorResponse( "Forbidden, Account already exists");
            return ResponseEntity.badRequest().body(error);
        }
        userRepository.save(user);
        return ResponseEntity.ok("Account Creation Successful");
    }

    public ResponseEntity<Object> login(Map<String, String> loginRequest) {
        UserModel emailUser = userRepository.findByEmail(loginRequest.get("email"));
        if (emailUser == null) {
            Map<String, Object> error = Error.errorResponse("User does not exist");
            return ResponseEntity.badRequest().body(error);
        }
        UserModel existingUser = userRepository.findByEmailAndPassword(loginRequest.get("email"), loginRequest.get("password"));
        if (existingUser == null) {
            Map<String, Object> error = Error.errorResponse("Username/Password Incorrect");
            return ResponseEntity.badRequest().body(error);
        }
        return ResponseEntity.ok("Login Successful");
    }

    public ResponseEntity<Object> fetchUserById(int userId) {
        UserModel user =  userRepository.findById(userId).orElse(null);
        if (user == null) {
            Map<String, Object> error = Error.errorResponse("User does not exist");
            return ResponseEntity.badRequest().body(error);
        }
        Map<String, Object> userMap = Map.of("name", user.getName(), "email", user.getEmail(), "userID", user.getId());
        return ResponseEntity.ok(userMap);
    }

    public ResponseEntity<Object> getBookingsHistoryByUserId(int userId) {
        UserModel user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            Map<String, Object> error = Map.of("Error", "User does not exist");
            return ResponseEntity.badRequest().body(error);
        }
        Date date = new Date();

        // Array of bookings before current date
        BookingModel[] bookingsBeforeToday = bookingRepository.findAllByUserId(userId).stream().filter(booking -> booking.getDateOfBooking().before(date)).toArray(BookingModel[]::new);
        // Array of bookings with timeFrom today before current time
        BookingModel[] bookingsToday = bookingRepository.findAllByUserId(userId).stream().filter(booking -> booking.getDateOfBooking().equals(date) && booking.getTimeFrom().before(date)).toArray(BookingModel[]::new);

        // Combine both arrays
        BookingModel[] allBookings = new BookingModel[bookingsBeforeToday.length + bookingsToday.length];
        System.arraycopy(bookingsBeforeToday, 0, allBookings, 0, bookingsBeforeToday.length);
        System.arraycopy(bookingsToday, 0, allBookings, bookingsBeforeToday.length, bookingsToday.length);
        ArrayList<Map<String, Object>> allBookingsMap = new ArrayList<>();
        for (BookingModel booking : allBookings) {
            allBookingsMap.add(convertBookingModelToMap(booking));
        }
        return ResponseEntity.ok(allBookingsMap);
    }

    public ResponseEntity<Object> getUpcomingBookingsByUserId(int userId) {
        UserModel user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            Map<String, Object> error = Map.of("Error", "User does not exist");
            return ResponseEntity.badRequest().body(error);
        }
        Date date = new Date();
        // Array of bookings after current date
        BookingModel[] bookingsAfterToday = bookingRepository.findAllByUserId(userId).stream().filter(booking -> booking.getDateOfBooking().after(date)).toArray(BookingModel[]::new);
        // Array of bookings with timeFrom today after current time
        BookingModel[] bookingsToday = bookingRepository.findAllByUserId(userId).stream().filter(booking -> booking.getDateOfBooking().equals(date) && booking.getTimeFrom().after(date)).toArray(BookingModel[]::new);
        // Combine both arrays
        BookingModel[] allBookings = new BookingModel[bookingsAfterToday.length + bookingsToday.length];
        System.arraycopy(bookingsAfterToday, 0, allBookings, 0, bookingsAfterToday.length);
        System.arraycopy(bookingsToday, 0, allBookings, bookingsAfterToday.length, bookingsToday.length);
        // Transform all bookings to map
        ArrayList<Map<String, Object>> allBookingsMap = new ArrayList<>();
        for (BookingModel booking : allBookings) {
            allBookingsMap.add(convertBookingModelToMap(booking));
        }
        return ResponseEntity.ok(allBookingsMap);
    }

    public static Map<String, Object> convertBookingModelToMap(BookingModel booking) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return Map.of(
                "bookingID", booking.getId(),
                "roomID", booking.getRoom().getId(),
                "room", booking.getRoom().getRoomName(),
                "dateOfBooking", dateFormat.format(booking.getDateOfBooking()),
                "timeFrom", timeFormat.format(booking.getTimeFrom()),
                "timeTo", timeFormat.format(booking.getTimeTo()),
                "purpose", booking.getPurpose()
        );
    }

}
