package com.app.authentication.services;

import com.app.authentication.dtos.response.AllUsersResponseDto;
import com.app.authentication.dtos.request.UserSignUpRequestDto;
import com.app.authentication.dtos.response.UserResponseDto;
import com.app.authentication.models.UserModel;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface UserService {

  List<AllUsersResponseDto> findAllUsers();
  UserResponseDto getUserById(int id);
  UserResponseDto getUserByEmail(String email);
  Optional<UserModel> getByEmail(String email);
  Optional<UserModel> getUserByTokenPassword(String tokenPassword);
  void updateUserPassword(UserModel userModel);
  void updateUser(UserModel userModel);
  UserResponseDto createUser(@Valid UserSignUpRequestDto userSignUpRequestDto);
  void deleteUser(int id);
  boolean existsById(int id);
  boolean existsByEmail(String email);
  boolean existsByTokenPassword(String tokenPassword);

}
