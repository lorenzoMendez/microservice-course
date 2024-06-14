package com.microservice.user.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.microservice.user.service.IUserService;
import jakarta.ws.rs.HttpMethod;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurity {

  private final IUserService userService;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  
  private final JwtAuthorizationFilter jwtAuthorizationFilter;

  public WebSecurity(IUserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, JwtAuthorizationFilter jwtAuthorizationFilter) {
    this.userService = userService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.jwtAuthorizationFilter = jwtAuthorizationFilter;
  }

  @Bean
  protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
    log.info("User service configuration init.....");
    // Configure AuthenticationManagerBuilder
    AuthenticationManagerBuilder authenticationManagerBuilder =
        http.getSharedObject(AuthenticationManagerBuilder.class);

    // Configure user detail authentication service.
    authenticationManagerBuilder.userDetailsService(userService)
        .passwordEncoder(bCryptPasswordEncoder);

    AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

    // Create AuthenticationFilter
    AuthenticationFilter authenticationFilter =
        new AuthenticationFilter(authenticationManager, userService);
    authenticationFilter.setFilterProcessesUrl("/users/login");

    http.csrf((csrf) -> csrf.disable())
        .authorizeHttpRequests((auth) -> auth
            .requestMatchers(new AntPathRequestMatcher("/users", HttpMethod.POST)).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/actuator/**", HttpMethod.GET)).permitAll()
            .anyRequest().authenticated())
        .addFilter(authenticationFilter)
        .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
        .authenticationManager(authenticationManager)
        .sessionManagement(
            (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .headers((header) -> header.frameOptions((frameOptions) -> frameOptions.sameOrigin()));

    return http.build();
  }

}

