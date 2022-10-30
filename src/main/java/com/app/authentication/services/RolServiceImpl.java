package com.app.authentication.services;

import com.app.authentication.enums.RolName;
import com.app.authentication.models.RolModel;
import com.app.authentication.repositories.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class RolServiceImpl implements RolService{

  @Autowired
  RolRepository rolRepository;

  @Override
  public RolModel getRoleByName(RolName name) {
    return rolRepository.getByRolName(name).get();
  }

  @Override
  public void save(RolModel rol) {
    rolRepository.save(rol);
  }

  @Override
  public boolean existsByRolName(RolName rolName) {
    Optional<RolModel> rol = rolRepository.getByRolName(rolName);
    return (rol.isPresent())?true:false;
  }

}
