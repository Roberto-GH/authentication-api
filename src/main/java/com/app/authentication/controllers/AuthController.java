package com.app.authentication.controllers;

import com.app.authentication.dtos.request.UserLoginRequestDto;
import com.app.authentication.dtos.request.UserSignUpRequestDto;
import com.app.authentication.dtos.response.JwtResponseDto;
import com.app.authentication.dtos.response.UserResponseDto;
import com.app.authentication.jwt.JwtProvider;
import com.app.authentication.services.RolService;
import com.app.authentication.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(AuthController.AUTH)
@CrossOrigin(origins = "*")// TODO: 17/10/2021  Referenciar front
public class AuthController {


  public static final String SIGNUP = "/signup";
  public static final String AUTH = "/auth";
  public static final String LOGIN = "/login";

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
  public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto, BindingResult bindingResult) {
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
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    JwtResponseDto jwtResponseDto = new JwtResponseDto(jwt);

    return ResponseEntity.status(HttpStatus.OK).body(jwtResponseDto);
  }


}
