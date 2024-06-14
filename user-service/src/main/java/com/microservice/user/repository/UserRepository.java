package com.microservice.user.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.microservice.user.model.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
  
  Optional<UserEntity> findByEmail(String email);
  
  Optional<UserEntity> findByUserId(String email);
}
