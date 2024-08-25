package com.carlosobispo.postconsulting.repositories;

import java.util.Optional;

import com.carlosobispo.postconsulting.models.Role;

public interface RoleRepository extends BaseRepository<Role> {
    Optional<Role> findByName(String name);
}
