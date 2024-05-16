package com.microservice.account.management.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountManagementController {
  
  @GetMapping
  public ResponseEntity<String> status() {
    return new ResponseEntity<String>("Working...", HttpStatus.OK);
  }
  
}
