package com.carlosobispo.postconsulting.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.carlosobispo.postconsulting.models.User;
import com.carlosobispo.postconsulting.models.dto.UserDTO;
import com.carlosobispo.postconsulting.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
        @Autowired
        private SecurityUserDetails userDetailsService;

        @Autowired
        private UserService userService;

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http,
                        BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);
                authenticationManagerBuilder.userDetailsService(userDetailsService)
                                .passwordEncoder(bCryptPasswordEncoder);
                return authenticationManagerBuilder.build();
        }

        @Bean
        SecurityFilterChain defaultSecurityFilterChainProtected(HttpSecurity http,
                        AuthenticationManager authenticationManager)
                        throws Exception {
                http
                                .cors(cors -> cors
                                                .configurationSource(request -> {
                                                        CorsConfiguration config = new CorsConfiguration();
                                                        config.setAllowedOrigins(
                                                                        Arrays.asList("http://localhost:5173"));
                                                        config.setAllowedMethods(
                                                                        Arrays.asList("GET", "POST", "PUT", "DELETE"));
                                                        config.setAllowedHeaders(
                                                                        Arrays.asList("Content-Type", "Authorization"));
                                                        return config;
                                                }))
                                .csrf(csrf -> csrf.disable())
                                .exceptionHandling(exception -> exception.accessDeniedPage("/"))
                                .authorizeHttpRequests(auth -> auth
                                                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR)
                                                .permitAll()
                                                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                                                .requestMatchers("/email/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/", "/about", "/login").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/login", "/register").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/register").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/home/*", "/home/**").authenticated()
                                                .requestMatchers(HttpMethod.GET, "/users/*", "/users/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/users/*", "/users/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/posts/*", "/posts/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.POST, "/posts/*", "/posts/**").permitAll()
                                                .anyRequest().denyAll())
                                .addFilterBefore(jsonObjectAuthenticationFilter(authenticationManager),
                                                UsernamePasswordAuthenticationFilter.class)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                                .maximumSessions(1)
                                                .expiredUrl("/login"))
                                .logout(logout -> logout
                                                .logoutUrl("/api/logout")
                                                .logoutSuccessHandler((request, response, authentication) -> {
                                                        response.setStatus(HttpServletResponse.SC_OK);
                                                })
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .deleteCookies("JSESSIONID"));

                return http.build();
        }

        @Bean
        public JsonObjectAuthenticationFilter jsonObjectAuthenticationFilter(
                        AuthenticationManager authenticationManager) throws Exception {
                JsonObjectAuthenticationFilter filter = new JsonObjectAuthenticationFilter(authenticationManager);
                filter.setFilterProcessesUrl("/login");
                filter.setAuthenticationSuccessHandler((request, response, authentication) -> {
                        User user = userService.findByEmail(authentication.getName());
                        UserDTO userDTO = new UserDTO(user); // Create a DTO with non-sensitive user data
                        response.setContentType("application/json");
                        response.getWriter().write(new ObjectMapper().writeValueAsString(userDTO));
                });
                filter.setAuthenticationFailureHandler((request, response, exception) -> {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\": \"" + exception.getMessage() + "\"}");
                });
                return filter;
        }
}
