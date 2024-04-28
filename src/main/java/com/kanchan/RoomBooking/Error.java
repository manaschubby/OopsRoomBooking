package com.kanchan.RoomBooking;

import java.util.Map;
import java.util.Objects;

public class Error {
    public static Map<String, Object> errorResponse(String message) {
        return Map.of("Error", message);
    }
}
