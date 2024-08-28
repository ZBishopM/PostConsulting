package com.carlosobispo.postconsulting.security;

import com.carlosobispo.postconsulting.models.dto.UserDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private UserDTO user;

    public JwtAuthenticationResponse(String accessToken, UserDTO user) {
        this.accessToken = accessToken;
        this.user = user;
    }
}
