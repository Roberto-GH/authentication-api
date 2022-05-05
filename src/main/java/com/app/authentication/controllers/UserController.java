package com.app.authentication.controllers;

import com.app.authentication.dtos.request.UserUpdateRequestDto;
import com.app.authentication.dtos.response.AllUsersResponseDto;
import com.app.authentication.dtos.response.UserResponseDto;
import com.app.authentication.models.UserModel;
import com.app.authentication.services.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(UserController.API)
@CrossOrigin(origins = "*")// TODO: 17/10/2021  Referenciar front
public class UserController {

  public static final String API = "/api";
  public static final String USERS = "/users";
  public static final String UPDATE = "/update-user";
  public static final String DELETE = "/delete-user";
  public static final String ID_ID = "/{id}";

  @Autowired
  UserService userService;

  //@ApiIgnore  //Not endpoint into swagger
  @ApiOperation(response = UserResponseDto.class, value = "List all users")
  @GetMapping(USERS)
  public ResponseEntity<?> findAllUsers() {
    List<AllUsersResponseDto> users = userService.findAllUsers();
    return ResponseEntity.status(HttpStatus.OK).body(users);
  }


  @ApiOperation("Get user by id")
  @GetMapping(USERS + ID_ID)
  public ResponseEntity<?> getById(@PathVariable int id) {
    if (!userService.existsById(id)) {
      Map<String, String> jsonResponse = new HashMap<>();
      jsonResponse.put("message", "El usuario no existe");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
    }
    UserResponseDto user = userService.getUserById(id);
    return ResponseEntity.status(HttpStatus.OK).body(user);
  }


  @ApiOperation("Update user by id")
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping(UPDATE+ID_ID)
  public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto, @PathVariable int id, BindingResult bindingResult) {
    Map<String, String> jsonResponse = new HashMap<>();

    if (!userService.existsById(id)) {
      jsonResponse.put("message", "El usuario no existe");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
    }
    if (bindingResult.hasErrors()) {
      jsonResponse.put("message", "Email y nombre requeridos");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
    }
    Optional<UserModel> userOptional = userService.getByEmail(userUpdateRequestDto.getEmail());
    if (!userOptional.isPresent()) {
      jsonResponse.put("message", "Correo del usuario no coincide");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
    }
    UserModel user = userOptional.get();

    user.setBiography(Optional.ofNullable(userUpdateRequestDto.getBiography()).orElse(user.getBiography()));
    user.setUpdateAt(LocalDateTime.now().minusHours(5));
    user.setName(Optional.ofNullable(userUpdateRequestDto.getName()).orElse(user.getName()));
    user.setProfile_img(Optional.ofNullable(userUpdateRequestDto.getProfile_img()).orElse(user.getProfile_img()));
    user.setLastName(Optional.ofNullable(userUpdateRequestDto.getLastName()).orElse(user.getLastName()));
    user.setPhone(Optional.ofNullable(userUpdateRequestDto.getPhone()).orElse(user.getPhone()));

    userService.updateUser(user);
    jsonResponse.put("message", "Usuario actualizado");
    return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);
  }


  @ApiOperation("Delete user by id")
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping(DELETE+ID_ID)
  public ResponseEntity<?> deleteUser(@PathVariable int id) {
    Map<String, String> jsonResponse = new HashMap<>();
    userService.deleteUser(id);
    jsonResponse.put("message", "Usuario eliminado");
    return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);
  }

}
