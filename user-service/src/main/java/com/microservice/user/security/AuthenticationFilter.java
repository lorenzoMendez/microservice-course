package com.microservice.user.security;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.user.model.request.LoginRequest;
import com.microservice.user.model.response.UserResponse;
import com.microservice.user.service.IUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final IUserService userService;

  @Value(value = "${token.secret}")
  private String secret;

  public AuthenticationFilter(AuthenticationManager authenticationManager,
      IUserService userService) {
    super(authenticationManager);
    this.userService = userService;
  }
  
  /**
   * Method to authenticate the given user from the request.
   * 
   * If user is not present in database an exception is thrown.
   */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    log.info("User service validating user.....");
    try {
      log.info("attemptAuthentication method....");
      LoginRequest credential = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
      return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
          credential.getEmail(), credential.getPassword(), new ArrayList<>()));
    } catch (IOException ex) {
      throw new RuntimeException(ex.getMessage());
    }
  }
  
  /**
   * When user is validated then this method will generate the token using the secret string.
   */
  @Override
  protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
      FilterChain chain, Authentication auth) throws IOException, ServletException {
    String userName = ((User) auth.getPrincipal()).getUsername();
    UserResponse userDetail = userService.getUserDetailsByEmail(userName);
    
    byte[] secretKeyBytes = secret.getBytes();
    SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
    Instant now = Instant.now();
    String token =
        Jwts.builder().subject(userDetail.getUserId()).expiration(Date.from(now.plusMillis(360000)))
            .issuedAt(Date.from(now)).signWith(secretKey).compact();
    log.info("Token generated: {}", token);
    res.addHeader("token", token);
    res.addHeader("userId", userDetail.getUserId());
    log.info("User was succesfully authenticated ... {}", LocalDateTime.now());
  }
  
}
