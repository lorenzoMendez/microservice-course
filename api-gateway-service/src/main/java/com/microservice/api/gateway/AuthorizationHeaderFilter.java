package com.microservice.api.gateway;

import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Class to validate token given a request configured in the gateway properties.
 * 
 * @author Lorenzo.
 *
 */
@Slf4j
@Component
public class AuthorizationHeaderFilter
    extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

  @Value(value = "${token.secret}")
  private String secret;

  public AuthorizationHeaderFilter() {
    super(Config.class);
  }

  public static class Config {
    // TODO
  }
  
  /**
   * Method to validate the given token.
   */
  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      log.info("Validating token....");
      ServerHttpRequest request = exchange.getRequest();
      if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
        log.error("User not authorized...");
        return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
      }
      String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
      String jwt = authorizationHeader.replace("Bearer", "").trim();
      Claims claims = isJwtValid(jwt);
      if (claims == null) {
        log.error("The provided token is not valid...");
        return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
      }
      log.info("Token is validated succesfully...");
      return chain.filter(exchange);
    };
  }
  
  /**
   * Method executed when token is not valid.
   * @param exchange
   * @param err
   * @param httpStatus
   * @return
   */
  private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
    log.error("An error was reported on token Authorization validation. '{}'", err);
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(httpStatus);
    return response.setComplete();
  }
  
  /**
   * Method to extract the token from the request and then is validated with the secret.
   * @param jwt is the token string.
   * @return Claims.
   */
  private Claims isJwtValid(String jwt) {
    Claims claims = null;
    String subject = null;
    byte[] secretKeyBytes = secret.getBytes();
    SecretKey signingKey = Keys.hmacShaKeyFor(secretKeyBytes);
    try {
      JwtParser jwtParser = Jwts.parser().verifyWith(signingKey).build();
      Jwt<JwsHeader, Claims> parsedToken = jwtParser.parseSignedClaims(jwt);
      claims = parsedToken.getPayload();
      subject = claims.getSubject();
    } catch (Exception ex) {
      log.error("Token is invalid.");
      return null;
    }
    if (subject == null || subject.isEmpty()) {
      log.error("Token is invalid.");
      return null;
    }
    log.info("Token validation is end....");
    return claims;
  }

}
