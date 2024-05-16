package com.microservice.user.security;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
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

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final IUserService userService;
  
  private static final String TOKEN_SECRET = "$2a$10$aQLVfyMS/XCoxh.Mdc4yi.l840alP2ybDclEacfAeG3Jwx1VpjuOi";

  public AuthenticationFilter(AuthenticationManager authenticationManager,
      IUserService userService) {
    super(authenticationManager);
    this.userService = userService;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    LoginRequest credential;
    try {
      credential = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
      return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
          credential.getEmail(), credential.getPassword(), new ArrayList<>()));
    } catch (IOException ex) {
      throw new RuntimeException(ex.getMessage());
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
      Authentication auth) throws IOException, ServletException {
    String userName = ((User) auth.getPrincipal()).getUsername();
    UserResponse userDetail = userService.getUserDetailsByEmail(userName);
    byte[] secretKeyBytes = Base64.getEncoder().encode(TOKEN_SECRET.getBytes());
    SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
    Instant now = Instant.now();
    String token = Jwts.builder().subject(userDetail.getUserId())
        .expiration(Date.from(now.plusSeconds(60))).issuedAt(Date.from(now)).signWith(secretKey).compact();
    res.addHeader("token", token);
    res.addHeader("userId", userDetail.getUserId());
  }

}
