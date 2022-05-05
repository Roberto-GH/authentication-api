package com.app.authentication.jwt;

import com.app.authentication.dtos.response.JwtResponseDto;
import com.app.authentication.models.RefreshTokenModel;
import com.app.authentication.models.UserModel;
import com.app.authentication.security.UserDetailsImpl;
import com.app.authentication.services.RefreshTokenService;
import com.app.authentication.services.UserService;
import com.nimbusds.jwt.JWTParser;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

  private final static Logger _log = LoggerFactory.getLogger(JwtProvider.class);

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.refresh-secret}")
  private String refreshSecret;

  @Value("${jwt.expiration}")
  private int expiration;

  @Autowired
  UserService userService;

  @Autowired
  RefreshTokenService refreshTokenService;


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
            .setExpiration(new Date(new Date().getTime() + expiration * 10))
            .signWith(SignatureAlgorithm.HS512, secret.getBytes())
            .compact();
  }


  public String generateTokenFromRefresh(String jwtRefreshToken) throws ParseException {
    if(validateToken(jwtRefreshToken, true)){
      String userEmailJwt = getUserEmailFromToken(jwtRefreshToken);
      List<String> roles = getRolesFromToken(jwtRefreshToken);

      return Jwts.builder()
                 .setSubject(userEmailJwt)
                 .claim("roles", roles).setIssuedAt(new Date())
                 .setExpiration(new Date(new Date().getTime() + expiration * 10))
                 .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                 .compact();
    }
    return null;
  }


  public String generateRefreshToken(String jwtToken) throws ParseException {
    if(validateToken(jwtToken,false)){
      String userEmailJwt = getUserEmailFromToken(jwtToken);
      List<String> roles = getRolesFromToken(jwtToken);
      return Jwts.builder()
                 .setSubject(userEmailJwt)
                 .claim("roles", roles)
                 .setIssuedAt(new Date())
                 .setExpiration(new Date(new Date().getTime() + expiration * 345600))
                 .signWith(SignatureAlgorithm.HS512, refreshSecret.getBytes())
                 .compact();
    }
    return null;
  }


  public void setRefreshTokenUser(String refreshToken, JwtResponseDto jwtResponseDto, boolean isLogin) throws ParseException {
    String userEmailJwt = getUserEmailFromToken(jwtResponseDto.getRefreshToken());
    Optional<UserModel> userOptional = userService.getByEmail(userEmailJwt);
    UserModel userModel = userOptional.get();

    List<RefreshTokenModel> refreshTokenUser = refreshTokenService.getByUserId(userModel.getId());
    refreshTokenUser.stream()
                    .filter(rt -> rt.getExpiredIn().isBefore(LocalDateTime.now()))
                    .forEach(rt -> {
                      refreshTokenService.delete(rt.getId());
                    });

    refreshTokenUser.removeIf(rt -> rt.getExpiredIn().isBefore(LocalDateTime.now()));
    //List<RefreshTokenModel> refreshTokenUser2 = refreshTokenService.getByUserId(userModel.getId());

    if(!isLogin){
      refreshTokenUser.stream()
                       .filter(rt -> jwtResponseDto.getRefreshToken().equals(rt.getRefreshToken()))
                       .forEach(rt -> {
                         if (!rt.isTokenUsed()) {
                           rt.setExpiredIn(LocalDateTime.now().plus(expiration * 10, ChronoField.MILLI_OF_DAY.getBaseUnit()));
                           rt.setTokenUsed(true);
                           userModel.addRefreshToken(rt);
                         }
                       });
    }
    RefreshTokenModel refreshTokenModel = new RefreshTokenModel();
    refreshTokenModel.setRefreshToken(refreshToken);
    refreshTokenModel.setCreatedAt(LocalDateTime.now());
    refreshTokenModel.setExpiredIn(LocalDateTime.now().plus(expiration * 345600, ChronoField.MILLI_OF_DAY.getBaseUnit()));
    userModel.addRefreshToken(refreshTokenModel);

    userService.updateUser(userModel);
  }


//  public String getUserEmailFromToken(String token){
//    return Jwts.parser()
//            .setSigningKey(secret.getBytes())
//            .parseClaimsJws(token)
//            .getBody()
//            .getSubject();
//  }

  public String getUserEmailFromToken(String token) throws ParseException {
    return JWTParser.parse(token)
                    .getJWTClaimsSet()
                    .getSubject();
  }

  @SuppressWarnings (value="unchecked")
  private List<String> getRolesFromToken(String token) throws ParseException {
    return (List<String>) JWTParser.parse(token)
                                   .getJWTClaimsSet()
                                   .getClaim("roles");
  }

  public boolean validateToken(String token, boolean isRefresh){
    try {
      if(isRefresh){
        Jwts.parser().setSigningKey(refreshSecret.getBytes()).parseClaimsJws(token);
        return true;
      }
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