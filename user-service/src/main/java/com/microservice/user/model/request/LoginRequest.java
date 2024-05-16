package com.microservice.user.model.request;

import java.io.Serializable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest implements Serializable {

  private static final long serialVersionUID = 6169756077909914521L;
  
  @NotNull(message = "Email must not be null")
  @Email
  private String email;
  
  @NotNull(message = "Password must be not null")
  private String password;
  
}
