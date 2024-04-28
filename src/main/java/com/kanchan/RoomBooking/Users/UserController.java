package com.kanchan.RoomBooking.Users;

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
        return userService.signUp(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> loginRequest) {
        return userService.login(loginRequest);
    }


}
