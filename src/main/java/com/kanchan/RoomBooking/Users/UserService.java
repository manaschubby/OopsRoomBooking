package com.kanchan.RoomBooking.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserModel saveUser(UserModel user) {
        return userRepository.save(user);
    }

    public UserModel fetchUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
