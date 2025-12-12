package com.example.taskmanager.service;

import com.example.taskmanager.entity.User;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    User save(User user);
}
