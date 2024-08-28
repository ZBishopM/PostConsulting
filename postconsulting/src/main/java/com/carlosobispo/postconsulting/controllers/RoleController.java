package com.carlosobispo.postconsulting.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carlosobispo.postconsulting.models.Role;
import com.carlosobispo.postconsulting.services.RoleService;


@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("")
    public ResponseEntity<List<Role>> getRoles() {
        List<Role> roles = roleService.findAll();
        if (roles != null && !roles.isEmpty()) {
            return ResponseEntity.ok(roles);
        }
        return ResponseEntity.notFound().build();
    }
    
}
