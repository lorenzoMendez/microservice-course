package com.microservice.user.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import com.microservice.user.model.request.UserRequest;
import com.microservice.user.model.response.UserResponse;

public interface IUserService extends UserDetailsService {
  
  /**
   * Method to create a new user.
   * @param request.
   */
  UserResponse createUser(UserRequest request);
  
  UserResponse getUserDetailsByEmail(String email);
  
  UserResponse getUserByUserId(String userId);
  
}
