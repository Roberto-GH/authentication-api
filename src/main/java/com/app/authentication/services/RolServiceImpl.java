package com.app.authentication.services;

import com.app.authentication.enums.RolName;
import com.app.authentication.models.RolModel;
import com.app.authentication.repositories.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RolServiceImpl implements RolService{

  @Autowired
  RolRepository rolRepository;

  @Override
  public RolModel getRoleByName(RolName name) {
    return rolRepository.getByRolName(name).get();
  }

}
