package com.kanchan.RoomBooking.Bookings;

import com.kanchan.RoomBooking.Rooms.RoomModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingModel, Integer> {
    // Find bookings by user id
    List<BookingModel> findAllByUserId(int userId);

    // Find bookings by room id
    List<BookingModel> findAllByRoomId(int roomId);

    // Find bookings by date
    List<BookingModel> findByDateOfBookingAfter(Date date);

    // Find bookings by room id and time
    BookingModel findByRoomIdAndDateOfBookingAndTimeFromLessThanEqualAndTimeToGreaterThanEqual(int roomId,Date dateOfBooking, Date timeTo, Date timeFrom);

    BookingModel findByRoomIdAndDateOfBookingAndTimeFromGreaterThanEqualAndTimeToLessThanEqual(int roomId,Date dateOfBooking, Date timeFrom, Date timeTo);

    void deleteAllByRoom(RoomModel room);
}
