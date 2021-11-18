package com.app.authentication.services;

import com.app.authentication.dtos.request.UserSignUpRequestDto;
import com.app.authentication.dtos.response.AllUsersResponseDto;
import com.app.authentication.dtos.response.UserResponseDto;
import com.app.authentication.enums.RolName;
import com.app.authentication.models.RolModel;
import com.app.authentication.models.UserModel;
import com.app.authentication.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@Validated
public class UserServiceImpl implements UserService {


  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RolService rolService;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private PasswordEncoder passwordEncoder;


  @Override
  public List<AllUsersResponseDto> findAllUsers() {
    List<UserModel> users = userRepository.findAll();
    List<AllUsersResponseDto> usersDto = modelMapper.map(users,
            new TypeToken<List<AllUsersResponseDto>>() {}.getType());
    return usersDto;
  }

  @Override
  public UserResponseDto getUserById(int id) {
    UserModel user = userRepository.getById(id);
    return modelMapper.map(user, UserResponseDto.class);
  }

  @Override
  public UserResponseDto getUserByEmail(String email) {
    Optional<UserModel> user = userRepository.getByEmail(email);
    return modelMapper.map(user.get(), UserResponseDto.class);
  }

  @Override
  public UserResponseDto createUser(UserSignUpRequestDto userSignUpRequestDto) {
    UserModel userModelToCreate = modelMapper.map(userSignUpRequestDto, UserModel.class);
    userModelToCreate.setCreatedAt(LocalDateTime.now().minusHours(5));// TODO: 18/10/2021 cambiar sistema de horarios
    userModelToCreate.setPassword(passwordEncoder.encode(userSignUpRequestDto.getPassword()));

    Set<RolModel> roles = new HashSet<>();
    roles.add(rolService.getRoleByName(RolName.ROLE_USER));
    if (userSignUpRequestDto.getRoles().contains("ADMIN")){
      roles.add(rolService.getRoleByName(RolName.ROLE_ADMIN));
    }

    userModelToCreate.setRoles(roles);
    UserModel userModel = userRepository.save(userModelToCreate);
    UserResponseDto userResponseDto = modelMapper.map(userModel, UserResponseDto.class);
    return userResponseDto;
  }

  @Override
  public void deleteUser(int id) {
    userRepository.deleteById(id);
  }

  @Override
  public boolean existsById(int id) {
    return userRepository.existsById(id);
  }

  @Override
  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

}
