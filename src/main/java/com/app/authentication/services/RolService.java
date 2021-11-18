package com.app.authentication.services;

import com.app.authentication.enums.RolName;
import com.app.authentication.models.RolModel;

public interface RolService {

  public RolModel getRoleByName(RolName name);

}
