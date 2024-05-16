package com.microservice.user.model;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Entity
@ToString
@Table(name = "CAT_USERS")
public class UserEntity implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = -4672545442267749196L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable=false, length=50, name = "FIRST_NAME")
  private String firstName;
  
  @Column(nullable=false, length=50, name = "LAST_NAME")
  private String lastName;
  
  @Column(nullable=false, length=120, unique=true, name = "EMAIL")
  private String email;
  
  @Column(nullable=false, unique=true, name = "USER_ID")
  private String userId;
  
  @Column(nullable=false, unique=true, name = "ENCRYPTED_PASSWORD")    
  private String encryptedPassword;

}
