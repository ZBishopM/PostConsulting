package com.carlosobispo.postconsulting.services;
import org.springframework.stereotype.Service;

import com.carlosobispo.postconsulting.models.User;
import com.carlosobispo.postconsulting.repositories.UserRepository;
import com.carlosobispo.postconsulting.security.PasswordEncoder;


@Service
public class UserService extends BaseService<User> {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public User save(User user) {
        encryptUserPassword(user);
        user = userRepository.save(user);
        return user;
    }
    
    public boolean subscribed(User user){
        return userRepository.subscribed(user);
    }

    private void encryptUserPassword(User user) {
        String hashedPassword = PasswordEncoder
            .bCryptPasswordEncoder()
            .encode(user.getPassword());
        user.setPassword(hashedPassword);
    }
}
