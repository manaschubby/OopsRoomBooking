package com.kanchan.RoomBooking.Rooms;

import com.kanchan.RoomBooking.Bookings.BookingModel;
import com.kanchan.RoomBooking.Bookings.BookingRepository;
import com.kanchan.RoomBooking.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public RoomService(RoomRepository roomRepository, BookingRepository bookingRepository) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    public ResponseEntity<Object> getRoomsByCapacity(int capacity) {
        List<RoomModel> rooms =  roomRepository.findAllByRoomCapacityGreaterThanEqual(capacity);
        if (rooms.isEmpty()) {
            ArrayList<RoomModel> emptyList = new ArrayList<>();
            return ResponseEntity.ok(emptyList);
        }
        ArrayList<Map<String, Object>> roomsMap = new ArrayList<>();
        for (RoomModel room : rooms) {
            List<BookingModel> bookings = bookingRepository.findAllByRoomId(room.getId());
            ArrayList<Map<String, Object>> bookingsMap = new ArrayList<>();
            for (BookingModel booking : bookings) {
                Map<String, Object> bookingMap = new java.util.HashMap<>(UserService.convertBookingModelToMap(booking));
                bookingMap.remove("room");
                bookingMap.remove("roomID");
                bookingMap.put("userID", booking.getUser().getId());
                bookingsMap.add(bookingMap);
            }
            Map<String, Object> roomMap = Map.of("roomName", room.getRoomName(), "roomCapacity", room.getRoomCapacity(), "roomID", room.getId(), "bookings", bookingsMap);
            roomsMap.add(roomMap);
        }
        return ResponseEntity.ok(roomsMap);
    }

    public ResponseEntity<Object> addRoom(String roomName, int roomCapacity) {
        RoomModel existingRoom = roomRepository.findByRoomName(roomName);
        if (existingRoom != null) {
            return ResponseEntity.badRequest().body("Room already exists");
        }
        RoomModel room = new RoomModel();
        room.setRoomName(roomName);
        room.setRoomCapacity(roomCapacity);
        roomRepository.save(room);
        return ResponseEntity.ok("Room created successfully");
    }
}
