package com.microservice.user.model.response;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserResponse implements Serializable {
  
  private static final long serialVersionUID = 8355195123050850084L;
  
  private String firstName;
  
  private String lastName;
  
  private String email;
  
  private String userId;
  
  private Long id;
  
  private List<AlbumResponse> albums;}
