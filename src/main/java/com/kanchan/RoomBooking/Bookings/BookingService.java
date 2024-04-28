package com.kanchan.RoomBooking.Bookings;

import com.kanchan.RoomBooking.Error;
import com.kanchan.RoomBooking.Rooms.RoomModel;
import com.kanchan.RoomBooking.Rooms.RoomRepository;
import com.kanchan.RoomBooking.Users.UserModel;
import com.kanchan.RoomBooking.Users.UserRepository;
import com.kanchan.RoomBooking.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<Object> addBooking(int userId, int roomId,Date dateOfBooking, Date timeFrom, Date timeTo, String purpose) {
        // Check if room exists
        RoomModel room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            return ResponseEntity.badRequest().body("Room does not exist");
        }
        // Check if user exists
        UserModel user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User does not exist");
        }

        // Check if room is available
        BookingModel existingBooking1 = bookingRepository.findByRoomIdAndDateOfBookingAndTimeFromLessThanEqualAndTimeToGreaterThanEqual(roomId,dateOfBooking, timeTo, timeTo);
        BookingModel existingBooking2 = bookingRepository.findByRoomIdAndDateOfBookingAndTimeFromGreaterThanEqualAndTimeToLessThanEqual(roomId, dateOfBooking,timeFrom, timeTo);
        BookingModel existingBooking3 = bookingRepository.findByRoomIdAndDateOfBookingAndTimeFromLessThanEqualAndTimeToGreaterThanEqual(roomId, dateOfBooking,timeFrom, timeFrom);
        if (existingBooking1 != null || existingBooking2 != null || existingBooking3 != null) {
            return ResponseEntity.badRequest().body("Room unavailable");
        }

        BookingModel booking = new BookingModel();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setTimeFrom(timeFrom);
        booking.setDateOfBooking(dateOfBooking);
        booking.setTimeTo(timeTo);
        booking.setPurpose(purpose);
        bookingRepository.save(booking);
        return ResponseEntity.ok("Booking created successfully");
    }

    public ResponseEntity<Object> getBookingsByUserId(int userId) {
        UserModel user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            Map<String, Object> error = Map.of("Error", "User does not exist");
            return ResponseEntity.badRequest().body(error);
        }
        List<BookingModel> allBookings = bookingRepository.findAllByUserId(userId);
        ArrayList<Map<String, Object>> allBookingsMap = new ArrayList<>();
        for (BookingModel booking : allBookings) {
            allBookingsMap.add(UserService.convertBookingModelToMap(booking));
        }
        return ResponseEntity.ok(allBookingsMap);
    }

    public ResponseEntity<Object> deleteBooking(int bookingId) {
        BookingModel booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            Map<String, Object> error = Error.errorResponse("Booking does not exist");
            return ResponseEntity.badRequest().body(error);
        }
        bookingRepository.delete(booking);
        return ResponseEntity.ok("Booking deleted successfully");
    }

    public ResponseEntity<Object> updateBooking(int bookingId, Map<String, String> bookingDetails) {
        BookingModel booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            Map<String, Object> error = Error.errorResponse("Booking does not exist");
            return ResponseEntity.badRequest().body(error
            );
        }
        String purpose = bookingDetails.get("purpose");
        if (purpose != null) {
            booking.setPurpose(purpose);
        }
        String timeFrom = bookingDetails.get("timeFrom");
        if (timeFrom != null) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            try {
                booking.setTimeFrom(timeFormat.parse(timeFrom));
            } catch (Exception e) {
                Map<String, Object> error = Error.errorResponse("Invalid time");
                return ResponseEntity.badRequest().body(error);
            }
        }
        if (bookingDetails.get("timeTo") != null) {
            String timeTo = bookingDetails.get("timeTo");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            try {
                booking.setTimeTo(timeFormat.parse(timeTo));
            } catch (Exception e) {
                Map<String, Object> error = Error.errorResponse("Invalid time");
                return ResponseEntity.badRequest().body(error);
            }
        }
        if (bookingDetails.get("dateOfBooking") != null) {
            String dateOfBooking = bookingDetails.get("dateOfBooking");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                booking.setDateOfBooking(dateFormat.parse(dateOfBooking));
            } catch (Exception e) {
                Map<String, Object> error = Error.errorResponse("Invalid date");
                return ResponseEntity.badRequest().body(error);
            }
        }
        if (bookingDetails.get("userID") != null) {
            int userId = Integer.parseInt(bookingDetails.get("userID"));
            UserModel user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                Map<String, Object> error = Error.errorResponse("User does not exist");
                return ResponseEntity.badRequest().body(error);
            }
            booking.setUser(user);
        }
        bookingRepository.save(booking);
        return ResponseEntity.ok("Booking updated successfully");
    }
}
