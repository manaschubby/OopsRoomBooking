package com.kanchan.RoomBooking.Rooms;

import com.kanchan.RoomBooking.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

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
    public ResponseEntity<Object> getRoomsByCapacity(@RequestParam Optional<Integer> capacity) {
        if (!capacity.isPresent()) {
            return roomService.getRoomsByCapacity(0);
        } else {

        if (capacity.get() < 0) {
            Map<String, Object> error = Error.errorResponse("Invalid capacity");
            return ResponseEntity.badRequest().body(error);
        }

        return roomService.getRoomsByCapacity(capacity.get());}
    }

    @PostMapping("")
    public ResponseEntity<Object> addRoom(@RequestBody Map<String, String> roomDetails) {
        String roomName = roomDetails.get("roomName");
        if (roomDetails.get("roomCapacity") == null || roomName == null) {
            Map<String, Object> error = Error.errorResponse("Invalid parameters");
            return ResponseEntity.badRequest().body(error);
        }
        int roomCapacity = Integer.parseInt(roomDetails.get("roomCapacity"));
        boolean roomError = false;
        if ( roomCapacity < 0) {
            Map<String, Object> error = Error.errorResponse("Invalid capacity");
            roomError = true;
        }
        return roomService.addRoom(roomName, roomCapacity, roomError);
    }

    @DeleteMapping("")
    public ResponseEntity<Object> deleteRoom(@RequestParam int roomID) {
        return roomService.deleteRoom(roomID);
    }

    @PatchMapping("")
    public ResponseEntity<Object> updateRoom(@RequestBody Map<String, String> roomDetails) {
        String roomName = roomDetails.get("roomName");
        if (roomDetails.get("roomCapacity") == null || roomName == null) {
            Map<String, Object> error = Error.errorResponse("Invalid parameters");
            return ResponseEntity.badRequest().body(error);
        }
        boolean roomError = false;
        int roomCapacity = Integer.parseInt(roomDetails.get("roomCapacity"));
        if ( roomCapacity < 0) {
            Map<String, Object> error = Error.errorResponse("Invalid capacity");
            return ResponseEntity.badRequest().body(error);
        }
        int roomID = Integer.parseInt(roomDetails.get("roomID"));
        return roomService.updateRoom(roomID, roomDetails, roomError);
    }
}
