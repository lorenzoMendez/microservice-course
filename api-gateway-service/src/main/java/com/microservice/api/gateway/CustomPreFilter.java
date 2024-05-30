package com.microservice.api.gateway;

import java.util.Set;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomPreFilter implements GlobalFilter, Ordered {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    log.info("PRE FILTER WAS EXECUTED...");
    String requestPath = exchange.getRequest().getPath().toString();
    log.info("Request path {}", requestPath);
    HttpHeaders headers = exchange.getRequest().getHeaders();
    Set<String> headerNames = headers.keySet();
    headerNames.forEach(headerName -> {
      String value = headers.getFirst(headerName);
      log.info("{} : {}", headerName, value);
    });

    return chain.filter(exchange);
  }

  @Override
  public int getOrder() {
    return 0;
  }

}
