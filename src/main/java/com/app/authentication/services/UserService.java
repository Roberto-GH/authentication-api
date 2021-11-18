package com.app.authentication.services;

import com.app.authentication.dtos.response.AllUsersResponseDto;
import com.app.authentication.dtos.request.UserSignUpRequestDto;
import com.app.authentication.dtos.response.UserResponseDto;

import javax.validation.Valid;
import java.util.List;

public interface UserService {

  public List<AllUsersResponseDto> findAllUsers();
  public UserResponseDto getUserById(int id);
  public UserResponseDto getUserByEmail(String email);
  public UserResponseDto createUser(@Valid UserSignUpRequestDto userSignUpRequestDto);
  public void deleteUser(int id);
  public boolean existsById(int id);
  public boolean existsByEmail(String email);

}
