package com.kanchan.RoomBooking.Users;

import com.kanchan.RoomBooking.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class UserController {
    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<Object> fetchUserById(@RequestParam int userID){
        return userService.fetchUserById(userID);
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody UserModel user) {
        String email = user.getEmail();
        String password = user.getPassword();
        String name = user.getName();
        if (email == null || password == null || name == null) {
            Map<String, Object> error = Error.errorResponse("Invalid parameters");
            return ResponseEntity.badRequest().body(error);
        }
        return userService.signUp(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> loginRequest) {
        return userService.login(loginRequest);
    }

    @GetMapping("/history")
    public ResponseEntity<Object> getBookingsByUserId(@RequestParam int userId) {
        return userService.getBookingsHistoryByUserId(userId);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<Object> getUpcomingBookingsByUserId(@RequestParam int userId) {
        return userService.getUpcomingBookingsByUserId(userId);
    }


}
