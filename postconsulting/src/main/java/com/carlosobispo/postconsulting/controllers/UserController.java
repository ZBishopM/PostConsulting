package com.carlosobispo.postconsulting.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carlosobispo.postconsulting.models.User;
import com.carlosobispo.postconsulting.services.UserService;
import com.carlosobispo.postconsulting.validators.UserValidator;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserValidator userValidator;
    
    public UserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        Validator val =webDataBinder.getValidator();
        webDataBinder.setValidator(userValidator);
        webDataBinder.addValidators(val);
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
    public ResponseEntity<User> addUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            if(bindingResult.hasFieldErrors("email") && bindingResult.getFieldError("email").getCode().equals("USER_EMAIL_ALREADY_REGISTERED")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(user);
            }
            return ResponseEntity.badRequest().build();
        }
        User savedUser = userService.save(user);
        if (savedUser != null) {
            return ResponseEntity.ok(savedUser);
        }
        return ResponseEntity.notFound().build();
    }
    /* @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestParam String email, @RequestParam String password) {
        User user = userService.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    } */
    @GetMapping("/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
}
