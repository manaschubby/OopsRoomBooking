package com.kanchan.RoomBooking.Rooms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public ResponseEntity<Object> getRoomsByCapacity(int capacity) {
        List<RoomModel> rooms =  roomRepository.findAllByRoomCapacityGreaterThanEqual(capacity);
        if (rooms.isEmpty()) {
            ArrayList<RoomModel> emptyList = new ArrayList<>();
            return ResponseEntity.ok(emptyList);
        }
        return ResponseEntity.ok(rooms);
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
