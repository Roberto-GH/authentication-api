package com.app.authentication.services;

import com.app.authentication.models.RefreshTokenModel;

import java.util.List;

public interface RefreshTokenService {

  List<RefreshTokenModel> getByUserId(int userId);
  void delete(RefreshTokenModel refreshTokenModel);
  void delete(int id);

}
