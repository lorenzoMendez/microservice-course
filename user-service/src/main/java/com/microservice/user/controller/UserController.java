package com.microservice.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.microservice.user.model.request.UserRequest;
import com.microservice.user.model.response.UserResponse;
import com.microservice.user.service.IUserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/users")
public class UserController {
  
  private final IUserService userService;
  
  public UserController(IUserService userService) {
    this.userService = userService;
  }
  
  @GetMapping("/status-check")
  public ResponseEntity<String> status() {
    return new ResponseEntity<String>("Working...", HttpStatus.OK);
  }
  
  @PostMapping
  public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request ) {
    return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
  }
  
  
}
