package com.app.authentication.util;

import com.app.authentication.enums.RolName;
import com.app.authentication.models.RolModel;
import com.app.authentication.services.RolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CreateRoles implements CommandLineRunner {

  @Autowired
  RolService rolService;

  private final static Logger _log = LoggerFactory.getLogger(CreateRoles.class);


  @Override
  public void run(String... args) throws Exception {
    RolName[] rolNames = RolName.values();
    for (int i = 0; i < rolNames.length; i++) {
      if(!rolService.existsByRolName(rolNames[i])){
        _log.info("Rol to create: "+rolNames[i].toString());
        RolModel rolToSave = new RolModel(rolNames[i]);
        rolService.save(rolToSave);
      }
    }
  }

}