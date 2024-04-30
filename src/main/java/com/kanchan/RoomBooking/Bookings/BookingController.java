package com.kanchan.RoomBooking.Bookings;

import com.kanchan.RoomBooking.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
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
        // If timeFrom or timeTo start with values other than 0 - 23, return error
        if (timeFrom != null && timeTo != null) {

        }
        String purpose = bookingDetails.get("purpose");
        String invalidDateMessage = "Invalid date/time";
        Map<String, Object> error = Error.errorResponse(invalidDateMessage);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        boolean dateError = false;
        try {
            Date date = dateFormat.parse(bookingDetails.get("dateOfBooking"));
            // Validate time
            DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
            if (timeFrom == null || timeTo == null) {
                dateError = true;
            }
            assert timeFrom != null;
            if (Integer.parseInt(timeFrom.split(":")[0]) < 0 || Integer.parseInt(timeFrom.split(":")[0]) > 23) {
                dateError = true;
            }
            // If minutes are not between 0 and 59, return error
            if (Integer.parseInt(timeFrom.split(":")[1]) < 0 || Integer.parseInt(timeFrom.split(":")[1]) > 59) {
                dateError = true;
            }
            // If seconds are not between 0 and 59, return error
            if (Integer.parseInt(timeFrom.split(":")[2]) < 0 || Integer.parseInt(timeFrom.split(":")[2]) > 59) {
                dateError = true;
            }
            // Parse time
            try {
                Date timeFromDate = dateFormat1.parse(timeFrom);
                Date timeToDate = dateFormat1.parse(timeTo);
                if (timeFromDate.after(timeToDate)) {
                    return ResponseEntity.badRequest().body(error);
                }
                return bookingService.addBooking(userId, roomId, date, timeFromDate, timeToDate, purpose, dateError);
            } catch (Exception e) {
                return bookingService.addBooking(userId, roomId, date, new Date(), new Date(), purpose, true);
            }

        } catch (Exception e) {
            return bookingService.addBooking(userId, roomId, new Date(), new Date(), new Date(), purpose, true);
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
        boolean dateError = false;
        if (timeFrom == null || timeTo == null) {
            dateError = true;
        }
        assert timeFrom != null;
        if (Integer.parseInt(timeFrom.split(":")[0]) < 0 || Integer.parseInt(timeFrom.split(":")[0]) > 23) {
            dateError = true;
        }
        // If minutes are not between 0 and 59, return error
        if (Integer.parseInt(timeFrom.split(":")[1]) < 0 || Integer.parseInt(timeFrom.split(":")[1]) > 59) {
            dateError = true;
        }
        // If seconds are not between 0 and 59, return error
        if (Integer.parseInt(timeFrom.split(":")[2]) < 0 || Integer.parseInt(timeFrom.split(":")[2]) > 59) {
            dateError = true;
        }
        return bookingService.updateBooking(bookingId, bookingDetails, dateError);
    }

}
