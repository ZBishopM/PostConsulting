package com.carlosobispo.postconsulting.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChainProtected(HttpSecurity http)
    throws Exception {
//TODO: Change the constraints of spring Security
    http
        .cors((cors) -> cors.disable())
        .csrf((csrf) -> csrf.disable())
        .exceptionHandling(exception -> exception.accessDeniedPage("/"))
        .authorizeHttpRequests(
            (auth) -> auth
                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR)
                .permitAll()
                .requestMatchers("/css/**","/js/**","/img/**")
                .permitAll()
                .requestMatchers("/email/**")
                .permitAll()
                .requestMatchers(
                    HttpMethod.GET,
                    "/") // "/landing"
                .permitAll()
                .requestMatchers(
                    HttpMethod.GET,
                    "/about") // "/about"
                .permitAll()
                .requestMatchers(
                    HttpMethod.GET,
                    "/login") // "/login"
                .permitAll()
                .requestMatchers(
                    HttpMethod.POST,
                    "/login") // "/login"
                .permitAll()
                .requestMatchers(
                    HttpMethod.GET,
                    "/register") // "/register"
                .permitAll()
                .requestMatchers(
                    HttpMethod.POST,
                    "/register")
                .permitAll()
                .requestMatchers(
                    HttpMethod.GET,
                    "/home/*","/home/**")
                .authenticated()
                .requestMatchers(
                    HttpMethod.GET,
                    "/users/*", "/users/**")
                .permitAll()
                .requestMatchers(
                    HttpMethod.POST,
                    "/users/*", "/users/**")
                .permitAll()
                .anyRequest()
                .denyAll())
        .formLogin((login -> login
            .usernameParameter("email")
            .loginPage("/login")
            .defaultSuccessUrl("/home", true)))
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies("JSESSIONID"))
        .sessionManagement(session -> session
            .maximumSessions(1)
            .expiredUrl("/login"));/* 
        .httpBasic(Customizer.withDefaults()); */

    return http.build();
    }
}

