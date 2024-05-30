package com.microservice.api.gateway;

import java.util.Base64;
import javax.crypto.SecretKey;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthorizationHeaderFilter
    extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

  private static final String TOKEN_SECRET =
      "QQkXrgx0YEH01PIqwAMMGRMfDEH2vyywbWxziaQzKQGS5SvEZrfHHGguKqQX8DxV";

  public AuthorizationHeaderFilter() {
    super(Config.class);
  }

  public static class Config {
    // TODO
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      log.info("Validating token....");
      ServerHttpRequest request = exchange.getRequest();
      if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
        log.error("User not authorized...");
        return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
      }
      String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
      String jwt = authorizationHeader.replace("Bearer", "").trim();
      if (!isJwtValid(jwt)) {
        log.error("The provided token is not valid...");
        return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
      }
      log.info("Token is validated succesfully...");
      return chain.filter(exchange)
          .doOnSuccess(
              aVoid -> log.info("################## -> Request processing completed successfully"))
          .doOnError(error -> log
              .error("################## -> Error occurred while processing request", error));
    };
  }

  private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
    log.error("An error was reported on token Authorization validation. '{}'", err);
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(httpStatus);
    return response.setComplete();
  }

  private boolean isJwtValid(String jwt) {
    boolean returnValue = true;
    String subject = null;
    byte[] secretKeyBytes = Base64.getEncoder().encode(TOKEN_SECRET.getBytes());
    // byte[] secretKeyBytes = Base64.getDecoder().decode(TOKEN_SECRET.getBytes());
    SecretKey signingKey = Keys.hmacShaKeyFor(secretKeyBytes);
    try {
      JwtParser jwtParser = Jwts.parser().verifyWith(signingKey).build();
      Jwt<JwsHeader, Claims> parsedToken = jwtParser.parseSignedClaims(jwt);
      subject = parsedToken.getPayload().getSubject();
    } catch (Exception ex) {
      log.error("Token is invalid.");
      return false;
    }
    if (subject == null || subject.isEmpty()) {
      log.error("Token is invalid.");
      return false;
    }
    log.info("Token validation is end....");
    return returnValue;
  }

}
