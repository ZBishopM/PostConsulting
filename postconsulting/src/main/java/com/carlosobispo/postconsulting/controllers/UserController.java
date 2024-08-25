package com.carlosobispo.postconsulting.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carlosobispo.postconsulting.models.User;
import com.carlosobispo.postconsulting.services.UserService;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.findAll();
        if (users != null && !users.isEmpty()) {
            return ResponseEntity.ok(users);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User savedUser = userService.save(user);
        if (savedUser != null) {
            return ResponseEntity.ok(savedUser);
        }
        return ResponseEntity.badRequest().build();
    }
}
