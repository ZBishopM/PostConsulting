package com.carlosobispo.postconsulting.repositories;

import com.carlosobispo.postconsulting.models.User;

public interface UserRepository extends BaseRepository<User> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean subscribed(User user);
}
