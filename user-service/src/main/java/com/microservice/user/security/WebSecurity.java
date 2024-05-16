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
    // Configure AuthenticationManagerBuilder.
    AuthenticationManagerBuilder authenticationManagerBuilder =
        http.getSharedObject(AuthenticationManagerBuilder.class);

    // Aplicar configuraciones al AuthenticationManagerBuilder
    authenticationManagerBuilder.userDetailsService(userService)
        .passwordEncoder(bCryptPasswordEncoder);

    // Construir el AuthenticationManager
    AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

    // Create AuthenticationFilter
    AuthenticationFilter authenticationFilter =
        new AuthenticationFilter(authenticationManager, userService);
    authenticationFilter.setFilterProcessesUrl("/users/login");

    http.authorizeHttpRequests(
        requests -> requests.requestMatchers(new AntPathRequestMatcher("/users", "POST"))
            .permitAll().requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll())
        .csrf(csrf -> csrf.disable())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .headers((headers) -> headers.frameOptions((options) -> options.sameOrigin()))
        .addFilter(authenticationFilter).authenticationManager(authenticationManager);
    return http.build();
  }
}

