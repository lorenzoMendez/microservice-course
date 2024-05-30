package com.microservice.user.service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.microservice.user.model.UserEntity;
import com.microservice.user.model.request.UserRequest;
import com.microservice.user.model.response.UserResponse;
import com.microservice.user.repository.UserRepository;

@Service
public class UserService implements IUserService {

  private final UserRepository userRepository;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public UserResponse createUser(UserRequest request) {
    // TODO Auto-generated method stub
    ModelMapper modelMapper = new ModelMapper();
    request.setUserId(UUID.randomUUID().toString());
    request.setEncryptedPassword(bCryptPasswordEncoder.encode(request.getPassword()));
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    UserEntity userEntity = modelMapper.map(request, UserEntity.class);

    userEntity = userRepository.save(userEntity);
    UserResponse response = modelMapper.map(userEntity, UserResponse.class);
    return response;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<UserEntity> optUser = userRepository.findByEmail(username);
    if (!optUser.isPresent()) {
      throw new UsernameNotFoundException("User not found.");
    }
    UserEntity user = optUser.get();
    return new User(user.getEmail(), user.getEncryptedPassword(), true, true, true, true,
        new ArrayList<>());
  }

  @Override
  public UserResponse getUserDetailsByEmail(String email) {
    Optional<UserEntity> optUser = userRepository.findByEmail(email);
    if (!optUser.isPresent()) {
      throw new UsernameNotFoundException("User not found.");
    }
    return new ModelMapper().map(optUser.get(), UserResponse.class);
  }

}
