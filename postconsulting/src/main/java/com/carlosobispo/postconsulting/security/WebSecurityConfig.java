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

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
        @Autowired
        private SecurityUserDetails securityUserDetails;

        @Autowired
        private JwtTokenProvider jwtTokenProvider;

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http,
                        BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);
                authenticationManagerBuilder.userDetailsService(securityUserDetails)
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
                                                .requestMatchers(HttpMethod.GET, "/posts/*", "/posts/**").authenticated()
                                                .requestMatchers(HttpMethod.GET, "/roles/*", "/roles/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.POST, "/posts/*", "/posts/**")
                                                .hasRole("USER")
                                                .requestMatchers(HttpMethod.POST, "/comments/*", "/comments/**")
                                                .authenticated()
                                                .anyRequest().denyAll())
                                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider, securityUserDetails),
                                                UsernamePasswordAuthenticationFilter.class)
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint((request, response, authException) -> {
                                                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                                                                        "Error: Unauthorized");
                                                }))
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
}
