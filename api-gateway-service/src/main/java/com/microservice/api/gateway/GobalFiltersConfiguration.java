package com.microservice.api.gateway;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GobalFiltersConfiguration {
  
  @Order(1)
  @Bean
  public GlobalFilter secondPrefilter() {
    
    return (exchange, chain) -> {
      
      log.info("SECOND GLOBAL PRE-FILTER WAS EXECUTED...");
      
      return chain.filter(exchange).then(Mono.fromRunnable(() -> {
        log.info("THIRD GLOBAL POST-FILTER WAS EXECUTED...");
      }));
    };
  }
  
  @Order(2)
  @Bean
  public GlobalFilter thirdPrefilter() {
    
    return (exchange, chain) -> {
      
      log.info("THIRD GLOBAL PRE-FILTER WAS EXECUTED...");
      
      return chain.filter(exchange).then(Mono.fromRunnable(() -> {
        log.info("SECOND GLOBAL POST-FILTER WAS EXECUTED...");
      }));
    };
  }
  
  @Order(3)
  @Bean
  public GlobalFilter fourPrefilter() {
    
    return (exchange, chain) -> {
      
      log.info("FOURTH GLOBAL PRE-FILTER WAS EXECUTED...");
      
      return chain.filter(exchange).then(Mono.fromRunnable(() -> {
        log.info("FIRST GLOBAL POST-FILTER WAS EXECUTED...");
      }));
    };
  }
  
}
