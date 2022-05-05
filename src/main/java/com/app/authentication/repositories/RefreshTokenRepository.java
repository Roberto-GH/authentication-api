package com.app.authentication.repositories;

import com.app.authentication.models.RefreshTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenModel, Integer> {

  List<RefreshTokenModel> getByUserId(int userId);

  @Override
  void delete(RefreshTokenModel refreshTokenModel);

  void deleteById(int id);
}
