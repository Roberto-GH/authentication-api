package com.app.authentication.util;

import com.app.authentication.dtos.request.UserSignUpRequestDto;
import com.app.authentication.enums.RolName;
import com.app.authentication.models.RolModel;
import com.app.authentication.services.RolService;
import com.app.authentication.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CreateRoles implements CommandLineRunner {

  @Autowired
  RolService rolService;
  @Autowired
  UserService userService;

  private final static Logger _log = LoggerFactory.getLogger(CreateRoles.class);

  @Override
  public void run(String... args) {
    RolName[] rolNames = RolName.values();
    for (RolName rolName : rolNames) {
      if (!rolService.existsByRolName(rolName)) {
        _log.info("Rol to create: {}", rolName.toString());
        RolModel rolToSave = new RolModel(rolName);
        rolService.save(rolToSave);
      }
    }
    boolean existsByEmail = userService.existsByEmail("roberthdrums@gmail.com");
    if (!existsByEmail) {
      _log.info("User to create: roberthdrums@gmail.com");
      UserSignUpRequestDto userRequestDto = new UserSignUpRequestDto();
      Set<String> roles = new HashSet<>();
      roles.add("ADMIN");
      userRequestDto.setRoles(roles);
      userRequestDto.setPassword("123456");
      userRequestDto.setName("Roberto");
      userRequestDto.setPhone("12345678");
      userRequestDto.setLastName("Londo\u00f1o");
      userRequestDto.setEmail("roberthdrums@gmail.com");
      userService.createUser(userRequestDto);
    }

  }

}