package com.carlosobispo.postconsulting.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.carlosobispo.postconsulting.models.Role;
import com.carlosobispo.postconsulting.repositories.RoleRepository;

@Service
public class RoleService extends BaseService<Role> {
    private final RoleRepository roleRepository;
    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    public RoleService(RoleRepository roleRepository) {
        super(roleRepository);
        this.roleRepository = roleRepository;
        try {
            if(roleRepository.count()==0){
                Role role = new Role();
                role.setName("ROLE_USER");
                roleRepository.save(role);
    
                role = new Role();
                role.setName("ROLE_ADMIN");
                roleRepository.save(role);
            }
        } catch (Exception e) {
            logger.error("initial roles already created", e);
        }
    }
    
    public Role findByName(String roleName) {
        Optional<Role> role = roleRepository.findByName(roleName);
        return role.isPresent() ? role.get() : null;
    }
}
