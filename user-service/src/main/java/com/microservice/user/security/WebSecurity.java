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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.microservice.user.service.IUserService;
import jakarta.ws.rs.HttpMethod;

@Configuration
@EnableWebSecurity
public class WebSecurity {

  private final IUserService userService;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public WebSecurity(IUserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userService = userService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Bean
  protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
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
            .requestMatchers(new AntPathRequestMatcher("/users/status-check", HttpMethod.GET)).authenticated()
            .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll())
        .addFilter(authenticationFilter).authenticationManager(authenticationManager)
        .sessionManagement(
            (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .headers((header) -> header.frameOptions((frameOptions) -> frameOptions.sameOrigin()));

    return http.build();
  }

}

