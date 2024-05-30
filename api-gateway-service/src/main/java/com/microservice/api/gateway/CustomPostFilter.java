package com.microservice.api.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomPostFilter implements GlobalFilter, Ordered {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    
    log.info("{}", exchange.getRequest().getHeaders());
    
    return chain.filter(exchange).then(Mono.fromRunnable(() -> {
      log.info("POST FILTER WAS EXECUTED...");
    }));
  }

  @Override
  public int getOrder() {
    
    return 0;
  }

}
