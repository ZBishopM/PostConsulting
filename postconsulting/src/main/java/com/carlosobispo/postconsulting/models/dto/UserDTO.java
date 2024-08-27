package com.carlosobispo.postconsulting.models.dto;

import com.carlosobispo.postconsulting.models.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private String role;
    private int postsNumber;
    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.postsNumber=user.getPosts().size();
        this.role = user.getRole().getName();
    }
}
