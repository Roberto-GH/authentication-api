package com.app.authentication.controllers;

import com.app.authentication.dtos.request.UserLoginRequestDto;
import com.app.authentication.dtos.request.UserSignUpRequestDto;
import com.app.authentication.dtos.response.JwtResponseDto;
import com.app.authentication.dtos.response.UserResponseDto;
import com.app.authentication.jwt.JwtProvider;
import com.app.authentication.models.RefreshTokenModel;
import com.app.authentication.models.UserModel;
import com.app.authentication.services.RefreshTokenService;
import com.app.authentication.services.RolService;
import com.app.authentication.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(AuthController.AUTH)
@CrossOrigin(origins = "${cross.origins}")
public class AuthController {


  public static final String SIGNUP = "/signup";
  public static final String AUTH = "/auth";
  public static final String LOGIN = "/login";
  public static final String REFRESH = "/refresh";

  @Autowired
  UserService userService;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  RolService rolService;

  @Autowired
  JwtProvider jwtProvider;

  @Autowired
  RefreshTokenService refreshTokenService;

  private final static Logger _log = LoggerFactory.getLogger(JwtProvider.class);


  @ApiOperation("Create new user")
  @PostMapping(SIGNUP)
  public ResponseEntity<?> signUp(@Valid @RequestBody UserSignUpRequestDto userSignUpRequestDto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      Map<String, String> jsonResponseError = new HashMap<>();
      jsonResponseError.put("message", "Campos mal puestos o email invalido");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponseError);
    }
    if (userService.existsByEmail(userSignUpRequestDto.getEmail())) {
      Map<String, String> jsonResponse = new HashMap<>();
      jsonResponse.put("message", "Ya existe un usuario con el email " + userSignUpRequestDto.getEmail());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
    }
    UserResponseDto userCreated = userService.createUser(userSignUpRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
  }


  @ApiOperation("Login, email and password")
  @PostMapping(LOGIN)
  public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto, BindingResult bindingResult) throws ParseException {
    if (bindingResult.hasErrors()) {
      Map<String, String> jsonResponseError = new HashMap<>();
      jsonResponseError.put("message", "Campos mal puestos o email invalido");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponseError);
    }
    Authentication authentication =
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginRequestDto.getEmail(), userLoginRequestDto.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtProvider.generateToken(authentication);
    String refreshJwt = jwtProvider.generateRefreshToken(jwt);
    //UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    JwtResponseDto jwtResponseDto = new JwtResponseDto(jwt);
    jwtResponseDto.setRefreshToken(refreshJwt);
    jwtProvider.setRefreshTokenUser(refreshJwt, jwtResponseDto, true);
    return ResponseEntity.status(HttpStatus.OK).body(jwtResponseDto);
  }

  @PostMapping(REFRESH)
  public ResponseEntity<?> refresh(@RequestBody JwtResponseDto jwtResponseDto) throws ParseException {

    String userEmailJwt = jwtProvider.getUserEmailFromToken(jwtResponseDto.getRefreshToken());
    Optional<UserModel> userOptional = userService.getByEmail(userEmailJwt);
    UserModel userModel = userOptional.get();
    RefreshTokenModel refreshTokenModel = refreshTokenService.getByUserId(userModel.getId()).stream()
                                       .filter(rt -> jwtResponseDto.getRefreshToken().equals(rt.getRefreshToken()))
                                       .findAny()
                                       .orElse(null);
    String token = jwtProvider.generateTokenFromRefresh(jwtResponseDto.getRefreshToken());
    String refreshToken = jwtProvider.generateRefreshToken(token);

    if (Objects.isNull(token) || Objects.isNull(refreshToken) || Objects.isNull(refreshTokenModel) ||refreshTokenModel.getExpiredIn().isBefore(LocalDateTime.now())) {
      Map<String, String> jsonResponse = new HashMap<>();
      jsonResponse.put("message", "No autorizado");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
    }

    jwtProvider.setRefreshTokenUser(refreshToken, jwtResponseDto, false);
    JwtResponseDto jwtRefresh = new JwtResponseDto(token);
    jwtRefresh.setRefreshToken(refreshToken);
    return ResponseEntity.status(HttpStatus.OK).body(jwtRefresh);
  }

}
