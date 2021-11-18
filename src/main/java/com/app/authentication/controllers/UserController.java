package com.app.authentication.controllers;

import com.app.authentication.dtos.response.AllUsersResponseDto;
import com.app.authentication.dtos.response.UserResponseDto;
import com.app.authentication.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(UserController.API)
@CrossOrigin(origins = "*")// TODO: 17/10/2021  Referenciar front
public class UserController {

  public static  final String API = "/api";
  public static  final String USERS = "/users";
  public static  final String ID_ID = "/{id}";

  @Autowired
  UserService userService;

  //@ApiIgnore Not endpoint into swagger
  @ApiOperation("List all users")
  @GetMapping(USERS)
  public ResponseEntity<?> findAllUsers(){
    List<AllUsersResponseDto> users = userService.findAllUsers();
    return ResponseEntity.status(HttpStatus.OK).body(users);
  }

  @ApiOperation("Get user by id")
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping(USERS+ID_ID)
  public  ResponseEntity<?> getById(@PathVariable int id){
    if (!userService.existsById(id)){
      Map<String, String> jsonResponse = new HashMap<>();
      jsonResponse.put("error", "El usuario no existe");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
    }
    UserResponseDto user = userService.getUserById(id);
    return ResponseEntity.status(HttpStatus.OK).body(user);
  }

}
