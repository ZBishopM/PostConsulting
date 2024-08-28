package com.carlosobispo.postconsulting.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carlosobispo.postconsulting.models.User;
import com.carlosobispo.postconsulting.models.dto.UserDTO;
import com.carlosobispo.postconsulting.security.JwtAuthenticationResponse;
import com.carlosobispo.postconsulting.security.JwtTokenProvider;
import com.carlosobispo.postconsulting.services.UserService;
import com.carlosobispo.postconsulting.validators.UserValidator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserValidator userValidator;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserService userService, UserValidator userValidator, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        Validator val = webDataBinder.getValidator();
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
            if (bindingResult.hasFieldErrors("email")) {
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
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody User loginRequest) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );
    
            SecurityContextHolder.getContext().setAuthentication(authentication);
    
            String jwt = jwtTokenProvider.generateToken(authentication);
            
            User user = userService.findByEmail(authentication.getName());
            UserDTO userDTO = new UserDTO(user);

            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, userDTO));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
