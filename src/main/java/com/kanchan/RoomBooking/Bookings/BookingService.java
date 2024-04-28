package com.kanchan.RoomBooking.Bookings;

import com.kanchan.RoomBooking.Rooms.RoomModel;
import com.kanchan.RoomBooking.Rooms.RoomRepository;
import com.kanchan.RoomBooking.Users.UserModel;
import com.kanchan.RoomBooking.Users.UserRepository;
import com.kanchan.RoomBooking.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
}
