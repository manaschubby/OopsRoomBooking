package com.kanchan.RoomBooking.Bookings;

import com.kanchan.RoomBooking.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(path = "/book")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("")
    public ResponseEntity<Object> addBooking(@RequestBody Map<String, String> bookingDetails) {
        int userId = Integer.parseInt(bookingDetails.get("userID"));
        int roomId = Integer.parseInt(bookingDetails.get("roomID"));
        String timeFrom = bookingDetails.get("timeFrom");
        String timeTo = bookingDetails.get("timeTo");
        String purpose = bookingDetails.get("purpose");
        String invalidDateMessage = "Invalid date/time";
        Map<String, Object> error = Error.errorResponse(invalidDateMessage);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(bookingDetails.get("dateOfBooking"));
            // Validate time
            DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
            if (timeFrom == null || timeTo == null) {
                return ResponseEntity.badRequest().body(error);
            }
            // Parse time
            try {
                Date timeFromDate = dateFormat1.parse(timeFrom);
                Date timeToDate = dateFormat1.parse(timeTo);
                if (timeFromDate.after(timeToDate)) {
                    return ResponseEntity.badRequest().body(error);
                }
                return bookingService.addBooking(userId, roomId, date, timeFromDate, timeToDate, purpose);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(error);
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("")
    public ResponseEntity<Object> getBookingsByUserId(@RequestParam int userID) {
        return bookingService.getBookingsByUserId(userID);
    }

    @DeleteMapping("")
    public ResponseEntity<Object> deleteBooking(@RequestParam int bookingID) {
        return bookingService.deleteBooking(bookingID);
    }

    @PatchMapping("")
    public ResponseEntity<Object> updateBooking(@RequestBody Map<String, String> bookingDetails) {
        int bookingId = Integer.parseInt(bookingDetails.get("bookingID"));
        String purpose = bookingDetails.get("purpose");
        String timeFrom = bookingDetails.get("timeFrom");
        String timeTo = bookingDetails.get("timeTo");
        String dateOfBooking = bookingDetails.get("dateOfBooking");
        String userId = bookingDetails.get("userID");
        if (purpose == null && timeFrom == null && timeTo == null && dateOfBooking == null && userId == null) {
            Map<String, Object> error = Error.errorResponse("Invalid parameters");
            return ResponseEntity.badRequest().body(error);
        }
        return bookingService.updateBooking(bookingId, bookingDetails);
    }

}
