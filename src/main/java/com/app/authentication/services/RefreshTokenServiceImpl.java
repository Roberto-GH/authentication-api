package com.app.authentication.services;

import com.app.authentication.models.RefreshTokenModel;
import com.app.authentication.repositories.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;


  @Override
  public List<RefreshTokenModel> getByUserId(int userId) {
    return refreshTokenRepository.getByUserId(userId);
  }

  @Override
  public void delete(RefreshTokenModel refreshTokenModel) {
    refreshTokenRepository.delete(refreshTokenModel);
  }

  @Override
  public void delete(int id) {
    refreshTokenRepository.deleteById(id);
  }

}
