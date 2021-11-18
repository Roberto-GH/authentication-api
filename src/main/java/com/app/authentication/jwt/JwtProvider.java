package com.app.authentication.jwt;

import com.app.authentication.security.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

  private final static Logger _log = LoggerFactory.getLogger(JwtProvider.class);

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private int expiration;


  public String generateToken(Authentication authentication) {
    UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetailsImpl.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

    return Jwts.builder()
            .setSubject(userDetailsImpl.getUsername())
            .claim("roles", roles)
            .setIssuedAt(new Date())
            .setExpiration(new Date(new Date().getTime() + expiration * 1000))
            .signWith(SignatureAlgorithm.HS512, secret.getBytes())
            .compact();
  }

  public String getUserEmailFromToken(String token){
    return Jwts.parser()
            .setSigningKey(secret.getBytes())
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
  }

  public boolean validateToken(String token){
    try {
      Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token);
      return true;
    }catch (MalformedJwtException e){
      _log.error("Token mal formado");
      _log.error(e.getMessage());
    }catch (UnsupportedJwtException e){
      _log.error("Token no soportado");
      _log.error(e.getMessage());
    }catch (ExpiredJwtException e){
      _log.error("Token expirado");
      _log.error(e.getMessage());
    }catch (IllegalArgumentException e){
      _log.error("Token vacio");
      _log.error(e.getMessage());
    }catch (SignatureException e){
      _log.error("Fallo en la firma");
      _log.error(e.getMessage());
    }
    return false;
  }


}
