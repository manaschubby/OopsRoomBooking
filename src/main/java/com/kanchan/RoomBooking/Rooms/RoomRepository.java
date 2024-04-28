package com.kanchan.RoomBooking.Rooms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<RoomModel, Integer> {
    // Find rooms greater than the given capacity
    RoomModel findByRoomCapacityGreaterThan(int capacity);
    List<RoomModel> findAllByRoomCapacityGreaterThanEqual(int capacity);

    // Find rooms by room name
    RoomModel findByRoomName(String roomName);
}
