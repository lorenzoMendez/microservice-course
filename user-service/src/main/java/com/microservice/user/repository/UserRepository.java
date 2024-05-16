package com.microservice.user.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.google.common.base.Optional;
import com.microservice.user.model.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
  
  Optional<UserEntity> findByEmail(String email);
}
