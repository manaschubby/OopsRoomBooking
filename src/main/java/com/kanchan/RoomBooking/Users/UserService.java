package com.kanchan.RoomBooking.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<Object> signUp(UserModel user) {
        UserModel existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            return ResponseEntity.badRequest().body("Forbidden, Account already exists");
        }
        userRepository.save(user);
        return ResponseEntity.ok("Account Creation Successful");
    }

    public ResponseEntity<Object> login(Map<String, String> loginRequest) {
        UserModel emailUser = userRepository.findByEmail(loginRequest.get("email"));
        if (emailUser == null) {
            return ResponseEntity.badRequest().body("User does not exist");
        }
        UserModel existingUser = userRepository.findByEmailAndPassword(loginRequest.get("email"), loginRequest.get("password"));
        if (existingUser == null) {
            return ResponseEntity.badRequest().body("Username/Password Incorrect");
        }
        return ResponseEntity.ok("Login Successful");
    }

    public ResponseEntity<Object> fetchUserById(int userId) {
        UserModel user =  userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User does not exist");
        }
        Map<String, Object> userMap = Map.of("name", user.getName(), "email", user.getEmail(), "userID", user.getId());
        return ResponseEntity.ok(userMap);
    }
}
