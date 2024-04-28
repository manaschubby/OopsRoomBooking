package com.kanchan.RoomBooking.Rooms;

import com.kanchan.RoomBooking.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Get Rooms by capacity
    @GetMapping("")
    public ResponseEntity<Object> getRoomsByCapacity(@RequestParam int capacity) {
        if (capacity < 0) {
            Map<String, Object> error = Error.errorResponse("Invalid capacity");
            return ResponseEntity.badRequest().body(error);
        }

        return roomService.getRoomsByCapacity(capacity);
    }

    @PostMapping("")
    public ResponseEntity<Object> addRoom(@RequestBody Map<String, String> roomDetails) {
        String roomName = roomDetails.get("roomName");
        if (roomDetails.get("roomCapacity") == null || roomName == null) {
            Map<String, Object> error = Error.errorResponse("Invalid parameters");
            return ResponseEntity.badRequest().body(error);
        }
        int roomCapacity = Integer.parseInt(roomDetails.get("roomCapacity"));
        if ( roomCapacity < 0) {
            Map<String, Object> error = Error.errorResponse("Invalid capacity");
            return ResponseEntity.badRequest().body(error);
        }
        return roomService.addRoom(roomName, roomCapacity);
    }
}
