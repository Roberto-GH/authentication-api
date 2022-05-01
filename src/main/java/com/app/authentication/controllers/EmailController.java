package com.app.authentication.controllers;


import com.app.authentication.dtos.request.ChangePasswordDto;
import com.app.authentication.dtos.request.EmailValuesDto;
import com.app.authentication.models.UserModel;
import com.app.authentication.services.EmailService;
import com.app.authentication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/email-password")
@CrossOrigin
public class EmailController {

  @Autowired
  EmailService emailService;

  @Autowired
  UserService userService;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Value("${spring.mail.username}")
  private String mailFrom;

  private static final String SUBJECT = "Cambio de Contraseña";


  @PostMapping("/send-email")
  public ResponseEntity<?> sendEmail(@RequestBody EmailValuesDto emailValuesDto) {
    Map<String, String> jsonResponse = new HashMap<>();
    boolean userExist = userService.existsByEmail(emailValuesDto.getMailTo());
    if (!userExist) {
      jsonResponse.put("message", "No existe ningún usuario con esas credenciales");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
    }
    Optional<UserModel> userOptional = userService.getByEmail(emailValuesDto.getMailTo());
    UserModel user = userOptional.get();
    emailValuesDto.setMailFrom(mailFrom);
    emailValuesDto.setMailTo(user.getEmail());
    emailValuesDto.setSubject(SUBJECT);
    emailValuesDto.setUserName(user.getName());
    UUID uuid = UUID.randomUUID();
    String tokenPassword = uuid.toString();
    emailValuesDto.setTokenPassword(tokenPassword);
    user.setTokenPassword(tokenPassword);
    userService.updateUserPassword(user);
    emailService.sendEmail(emailValuesDto);
    jsonResponse.put("message", "Correo enviado con éxito");
    return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);
  }


  @PostMapping("/change-password")
  public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto, BindingResult bindingResult) {
    Map<String, String> jsonResponse = new HashMap<>();
    if (bindingResult.hasErrors()) {
      jsonResponse.put("message", "Campos mal puestos");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
    }
    if (!changePasswordDto.getPassword().equals(changePasswordDto.getConfirmPassword())) {
      jsonResponse.put("message", "Las contraseñas no coinciden");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
    }
    Optional<UserModel> userOptional = userService.getUserByTokenPassword(changePasswordDto.getTokenPassword());
    if (!userOptional.isPresent()) {
      jsonResponse.put("message", "No existe ningún usuario con esas credenciales");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
    }
    UserModel user = userOptional.get();
    String newPassword = passwordEncoder.encode(changePasswordDto.getPassword());
    user.setPassword(newPassword);
    user.setTokenPassword(null);
    userService.updateUserPassword(user);
    jsonResponse.put("message", "Contraseña actualizada");
    return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);
  }

}
